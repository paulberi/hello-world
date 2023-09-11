package se.metria.markkoll.service.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.DetaljtypDto;
import se.metria.markkoll.service.KundConfigService;
import se.metria.markkoll.service.fastighet.FastighetsOmrInfo;
import se.metria.xplore.maputils.GeometryUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static se.metria.markkoll.service.common.XpSearchServiceProperty.*;

/**
 * Interface to the search service in xplore.
 */
@Service
@Slf4j
public class XpSearchService {

    @Value("${sok.api.url}")
    private String searchUrl;
    private final WebClient webClient;
    private final KundConfigService kundConfigService;

    public XpSearchService(KundConfigService kundConfigService, WebClient.Builder webClientBuilder) {
        this.kundConfigService = kundConfigService;

        // Increase the size of in memory buffer to 10 MB when fetching large objects.
        // Otherwise we can get org.springframework.core.io.buffer.DataBufferLimitException: Exceeded limit on max bytes to buffer
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)).build();
        this.webClient = webClientBuilder.exchangeStrategies(exchangeStrategies).build();
    }

    public List<FastighetsOmrInfo> getFastigheterByWkt(String wkt) throws IOException {
        var fastighetUUIDArray = getDelomradenByWkt(wkt).stream()
                .map(fastighetsOmrInfo -> fastighetsOmrInfo.getFastighet().getId())
                .distinct()
                .collect(Collectors.toList());

        return Lists.partition(fastighetUUIDArray, 100).stream()
                .map(this::getFastigheterByUuidArray)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<FastighetsOmrInfo> getDelomradenByWkt(String wkt) throws IOException {
        // Perform all requests in parallel.
        var fastigheter = doWktSearchRequest(searchUrl + "/fastighet/geometry", wkt);
        var samfMittlinje = doWktSearchRequest(searchUrl + "/samf-mittlinje/geometry", wkt);

        List<FastighetsOmrInfo> result = new ArrayList<>();
        result.addAll(fastigheter);
        result.addAll(samfMittlinje);

        return result;
    }

    public List<FastighetsOmrInfo> getFastighetByUuid(UUID fastighetId) {
        return getFastigheterByQuery(fastighetId.toString());
    }

    public List<FastighetsOmrInfo> getFastigheterByUuidArray(List<UUID> fastighetIds) {
        return getFastigheterByQuery(fastighetIds.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(",")));
    }

    private List<FastighetsOmrInfo> getFastigheterByQuery(String query) {
        // Perform all requests in parallel.
        var fastigheter = buildUuidSearchRequest(
                searchUrl + "/fastighet", query);
        var samfMittlinje = buildUuidSearchRequest(
                searchUrl + "/samf-mittlinje", query);
        return combineRequests(fastigheter, samfMittlinje);
    }

    private List<FastighetsOmrInfo> combineRequests(Mono<GeoJsonResult> r1, Mono<GeoJsonResult> r2) {

        var tries = 0;

        while(true) {
            try {
                var response = Mono.zip(r1, r2).map(tuple -> {
                    List<FastighetsOmrInfo> result = new ArrayList<>();

                    if (tuple.getT1().numberMatched > tuple.getT1().numberReturned) {
                        log.error("WktSearchRequest requestResult.numberMatched ({}) > requestResult.numberReturned ({})", tuple.getT1().numberMatched, tuple.getT1().numberReturned);
                        throw new MarkkollException(MarkkollError.FASTIGHET_ERROR);
                    }

                    if (tuple.getT2().numberMatched > tuple.getT2().numberReturned) {
                        log.error("WktSearchRequest requestResult.numberMatched ({}) > requestResult.numberReturned ({})", tuple.getT2().numberMatched, tuple.getT2().numberReturned);
                        throw new MarkkollException(MarkkollError.FASTIGHET_ERROR);
                    }


                    result.addAll(tuple.getT1().fastighetsOmrInfoList);
                    result.addAll(tuple.getT2().fastighetsOmrInfoList);
                    return result;
                }).block();

                return response;
            } catch (WebClientRequestException e) {
                tries++;
                if (tries == 5) {
                    throw e;
                }
            }
        }
    }

    private List<FastighetsOmrInfo> doWktSearchRequest(String url, String wkt) {
        GeoJsonResult result = null;
        var startIndex = 0;

        while(true) {
            var auth = kundConfigService.getMetriaMapsAuth();

            var requestResult = webClient.post()
                    .uri(url)
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .headers(headers -> headers.setBasicAuth(encodeCredentials(auth.getUsername(), auth.getPassword())))
                    .body(BodyInserters.fromValue("q=" +
                            URLEncoder.encode(wkt, StandardCharsets.UTF_8) +
                            "&maxFeatures=1000" +
                            "&startIndex=" + startIndex +
                            "&o=INTERSECTS"))
                    .retrieve()
                    .toEntity(String.class)
                    .map(response -> parseGeoJsonResponse(response.getBody()))
                    .block();

            if (requestResult == null) {
                log.error("Problem calling search, url: {}", url);
                throw new MarkkollException(MarkkollError.FASTIGHET_ERROR);
            }

            if (result == null) {
                result = requestResult;
            } else {
                if (result.numberMatched != requestResult.numberMatched) {
                    log.error("WktSearchRequest ({}) result.numberMatched ({}) != requestResult.numberMatched ({})", url, result.numberMatched, requestResult.numberMatched);
                    throw new MarkkollException(MarkkollError.FASTIGHET_ERROR);
                }

                result.numberReturned += requestResult.numberReturned;

                if (requestResult.numberReturned > 0) {
                    result.fastighetsOmrInfoList.addAll(requestResult.fastighetsOmrInfoList);
                }
            }

            if (requestResult.numberReturned == 0) {
                break;
            } else {
                startIndex += requestResult.numberReturned;
            }

            if (result.numberReturned >= result.numberMatched) {
                break;
            }
        }

        if (result.numberMatched > result.numberReturned) {
            log.error("WktSearchRequest ({}) requestResult.numberMatched ({}) > requestResult.numberReturned ({})", url, result.numberMatched, result.numberReturned);
            throw new MarkkollException(MarkkollError.FASTIGHET_ERROR);
        }

        return result.fastighetsOmrInfoList;
    }

    private Mono<GeoJsonResult> buildUuidSearchRequest(String url, String query) {
        var auth = kundConfigService.getMetriaMapsAuth();

        return webClient.get()
                .uri(url + "?q=" + query)
                .header(HttpHeaders.ACCEPT, "application/json")
                .headers(headers -> headers.setBasicAuth(encodeCredentials(auth.getUsername(), auth.getPassword())))
                .retrieve()
                .toEntity(String.class)
                .map(response -> parseGeoJsonResponse(response.getBody()));
    }

    private GeoJsonResult parseGeoJsonResponse(String response) {
        List<FastighetsOmrInfo> resultList = new ArrayList<>();
        GeoJsonResult result = new GeoJsonResult();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            result.numberMatched=jsonNode.get("numberMatched").asInt();
            result.numberReturned=jsonNode.get("numberReturned").asInt();

            var features = GeometryUtil.getFeaturesFromGeoJSON(response);
            log.debug("Hämtade {} geometrier från söktjänst. Skapar fastigheter från geometrier...", features.size());

            for (SimpleFeature feature : features) {
                log.debug("Skapar ny FastighetEntity för geometri {}...", feature.getID());
                var newFastighet = new FastighetEntity();
                long omrnr = feature.getProperty(OMRNR) == null ? 0 : (long) feature.getProperty(OMRNR).getValue();
                String detaljtyp = parseStringProperty(feature, DETALJTYP);
                String externid = parseStringProperty(feature, EXTERNID);
                String objekt_id = parseStringProperty(feature, OBJEKT_ID);
                String trakt = parseStringProperty(feature, TRAKT);
                String blockenhet = parseStringProperty(feature, BLOCKENHET);
                String kommunnamn = parseStringProperty(feature, KOMMUNNAMN);
                String lan = lanFromKommunkod(parseStringProperty(feature, KOMMUNKOD));
                String beteckning;
                if (!trakt.isEmpty() && !blockenhet.isEmpty()) {
                    beteckning = trakt + " " + blockenhet;
                } else {
                    beteckning = externid;
                }

                // TODO: Implementera korrekt hantering av Fiskesamfälligheter
                //       https://jira.metria.se/browse/MHLG-520
                if (detaljtyp.equals("FISKESAMF")) {
                    log.warn("Ytan med omrnr {} för registerenheten {} kodas om från FISKESAMF till SAMF", omrnr, beteckning);
                    detaljtyp = DetaljtypDto.SAMF.getValue();
                }

                UUID id;
                if (detaljtyp.equals(DetaljtypDto.SAMFO.toString()) || detaljtyp.equals(DetaljtypDto.FASTO.toString())) {
                    // Outredd fastighet, saknar objekt_id
                    id = UUID.nameUUIDFromBytes(externid.getBytes(StandardCharsets.UTF_8));
                    // Eftersom vi i vissa edge cases kan få outredda fastigheter som ändå har trakt och blockenhet så
                    // sätter vi beteckning, trakt och blockenhet så som vi förväntar oss att en outredd fastighet ska bete sig.
                    beteckning = externid;
                    trakt = "";
                    blockenhet = "";
                    log.info("Registerenheten {} ({}) saknar objekt_id; ett har genererats ({})",
                            externid, detaljtyp, id);
                } else {
                    id = UUID.fromString(objekt_id);
                }

                try {
                    newFastighet.setDetaljtyp(DetaljtypDto.valueOf(detaljtyp));
                } catch (IllegalArgumentException iae) {
                    log.error("Okänd detaljtyp {} för omrnr {} på registerenheten {}, ignorerar denna yta", detaljtyp, omrnr, beteckning);
                    continue;
                }
                newFastighet.setFastighetsbeteckning(beteckning);
                newFastighet.setId(id);
                newFastighet.setExternid(externid);
                newFastighet.setBlockenhet(blockenhet);
                newFastighet.setTrakt(trakt);
                newFastighet.setKommunnamn(kommunnamn);
                newFastighet.setLan(lan);

                var geom = GeometryUtil.getFeatureGeometry(feature);

                resultList.add(new FastighetsOmrInfo(newFastighet, omrnr, geom));
            }

        } catch (IOException e) {
            throw new MarkkollException(MarkkollError.FASTIGHET_ERROR, e);
        }

        result.fastighetsOmrInfoList = resultList;
        return result;
    }

    private String parseStringProperty(SimpleFeature feature, String propertyName) {
        return feature.getProperty(propertyName).getValue() == null ? "" : feature.getProperty(propertyName).getValue().toString();
    }

    private String encodeCredentials(String username, String password) {
        var authString = username + ":" + password;
        return Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
    }

    private String lanFromKommunkod(String kommunkod) {
        if (kommunkod == null || kommunkod.length() != 4) {
            log.warn("Okänd kommunkod: " + kommunkod);
            return "Okänd";
        }
        // Verkar vara standardrespons från Fsök för en fastighet på okänd ort
        if (kommunkod.equals("9999")) {
            return "Okänd";
        }
        else switch (kommunkod.substring(0, 2)) {
            case "01":
                return "STOCKHOLM";
            case "03":
                return "UPPSALA";
            case "04":
                return "SÖDERMANLAND";
            case "05":
                return "ÖSTERGÖTLAND";
            case "06":
                return "JÖNKÖPING";
            case "07":
                return "KRONOBERG";
            case "08":
                return "KALMAR";
            case "09":
                return "GOTLAND";
            case "10":
                return "BLEKING";
            case "12":
                return "SKÅNE";
            case "13":
                return "HALLAND";
            case "14":
                return "VÄSTRA GÖTALAND";
            case "17":
                return "VÄRMLAND";
            case "18":
                return "ÖREBRO";
            case "19":
                return "VÄSTMANLAND";
            case "20":
                return "DALARNA";
            case "21":
                return "GÄVLEBORG";
            case "22":
                return "VÄSTERNORRLAND";
            case "23":
                return "JÄMTLAND";
            case "24":
                return "VÄSTERBOTTEN";
            case "25":
                return "NORRBOTTEN";
            default:
                log.warn("Okänd kommunkod: " + kommunkod);
                return "Okänd";
        }
    }

    private static class GeoJsonResult {
        public int numberReturned;
        private int numberMatched;
        List<FastighetsOmrInfo> fastighetsOmrInfoList;
    }
}
