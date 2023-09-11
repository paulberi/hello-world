package se.metria.markkoll.service.map;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.WKTWriter2;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import se.metria.markkoll.service.KundConfigService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.util.HttpUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

import static se.metria.markkoll.service.map.MapService.HighResolutionDpi;

@Service
@RequiredArgsConstructor
@Slf4j
public class LayerService {
    @Value("${markkoll.metria-maps.url:}")
    private String metriaMapsUrl;

    @Value("${geoserver.url:}markkoll/wms")
    private String geoserverUrl;

    @NonNull
    private final KundConfigService kundConfigService;

    @NonNull
    private final UserService userService;

    private final WebClient webClient = WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(10 * 1024 * 1024)) //10MB
                    .build())
            .defaultHeader("x-xplore-principal", "markkoll")
            .build();

    public BufferedImage
    getLayerIntrangProjekt(UUID projektId, Integer width, Integer height, Envelope bbox, String styles, String cqlFilter) throws IOException {
        String cql = String.format("projekt_id='%s'", projektId);
        if (cqlFilter != null) {
            cql = cql.concat(" AND " + cqlFilter);
        }
        cql = URLEncoder.encode(cql, StandardCharsets.UTF_8);
        var query = mapRequestParams(Layer.INTRANG_PROJEKT.toString(), width, height, Format.PNG8, cql, bbox, styles, HighResolutionDpi);
        return getImage(geoserverUrl + query, MediaType.IMAGE_PNG);
    }

    private BufferedImage
    getLayerIntrangFastighet(UUID projektId,
                             Integer width,
                             Integer height,
                             Envelope bbox) throws IOException {
        var cql = String.format("projekt_id='%s'", projektId);
        var query = mapRequestParams(Layer.INTRANG_FASTIGHET.toString(), width, height, Format.PNG8, cql, bbox, null, HighResolutionDpi);
        return getImage(geoserverUrl + query, MediaType.IMAGE_PNG);
    }

    public BufferedImage
    getLayerIntrangProjektOversikt(UUID projektId,
                                   Integer width,
                                   Integer height,
                                   Envelope bbox,
                                   boolean showProjektomrade) throws IOException {
        var cql = String.format("projekt_id='%s'", projektId);
        var style = showProjektomrade ? LayerStyle.INTRANG_PROJEKT_PROJEKTOMRADE : LayerStyle.INTRANG_PROJEKT;
        var query = mapRequestParams(Layer.INTRANG_PROJEKT_OVERSIKT.toString(), width, height, Format.PNG8, cql, bbox, style.toString(), HighResolutionDpi);

        return getImage(geoserverUrl + query, MediaType.IMAGE_PNG);
    }

    public BufferedImage
    getLayerFastighetExtent(UUID projektId,
                            UUID fastighetId,
                            Integer width,
                            Integer height,
                            Envelope bbox) throws IOException {
        var cql = String.format("fastighet_id='%s' AND projekt_id='%s'", fastighetId, projektId);
        cql = URLEncoder.encode(cql, StandardCharsets.UTF_8);
        var query = mapRequestParams(Layer.FASTIGHET_EXTENT.toString(), width, height, Format.PNG8, cql, bbox, null, HighResolutionDpi);

        return getImage(geoserverUrl + query, MediaType.IMAGE_PNG);
    }

    public BufferedImage
    getLayerIntrangExtent(UUID projektId,
                          UUID fastighetId,
                          Long omradeNr,
                          Integer width,
                          Integer height,
                          Envelope bbox) throws IOException {
        var cql = String.format("fastighet_id='%s' AND projekt_id='%s' AND omrade_nr='%s'", fastighetId, projektId, omradeNr);
        cql = URLEncoder.encode(cql, StandardCharsets.UTF_8);
        var query = mapRequestParams(Layer.INTRANG_EXTENT.toString(), width, height, Format.PNG8, cql, bbox, null, HighResolutionDpi);

        return getImage(geoserverUrl + query, MediaType.IMAGE_PNG);
    }

    public BufferedImage
    getLayerFastighetYta(UUID projektId,
                         UUID fastighetId,
                         Integer width,
                         Integer height,
                         Envelope bbox) throws IOException {
        var cql = URLEncoder.encode(String.format("fastighet_id='%s' AND projekt_id='%s'", fastighetId, projektId), StandardCharsets.UTF_8);
        var query = mapRequestParams(Layer.FASTIGHET_YTA.toString(), width, height, Format.PNG8, cql, bbox, null, HighResolutionDpi);
        return getImage(geoserverUrl + query, MediaType.IMAGE_PNG);
    }

    /**
     * Fastighetsytor från Metria Maps. Läser in lagret två gånger med olika stilmallar:
     * 1) Tonade fastighetsytor för omkringliggande fastigheter. I dagsläget döljs lagret för skalor > 17000.
     * 2) Ett urval av fastighetsytor som ritar ut gränser och beteckningar. OBS, av prestandaskäl får lagret
     * endast anropas med ett WKT-filter.
     */
    public BufferedImage
    getLayerFastighetsytorMetriaMaps(Integer width,
                                     Integer height,
                                     Envelope bbox,
                                     UUID fastighetId,
                                     Geometry extentFilter) throws IOException {
        var layers = String.format("%s,%s", Layer.FASTIGHETSYTOR, Layer.FASTIGHETSYTOR);
        var styles = String.format("%s,%s", LayerStyle.FASTIGHETSYTOR_TONAD, LayerStyle.FASTIGHETER_URVAL);
        var extentFilterWkt = new WKTWriter2().write(extentFilter);
        var cql = String.format("objekt_id NOT IN('%s');INTERSECTS(geom, %s)", fastighetId, extentFilterWkt);
        var query = mapRequestParams(layers, width, height, Format.PNG8, cql, bbox, styles, HighResolutionDpi);
        return getImageAuth(metriaMapsUrl + query, MediaType.IMAGE_PNG);
    }

    public BufferedImage
    getLayerBackgroundSkarp(Integer width, Integer height, Envelope bbox) throws IOException {
        String query;
        query = mapRequestParams(Layer.BAKGRUNDSKARTA_SKARP.toString(), width, height, Format.JPEG, null, bbox, null, HighResolutionDpi);
        return getImageAuth(metriaMapsUrl + query, MediaType.IMAGE_JPEG);
    }

    public BufferedImage
    getLayerBackground(Integer width, Integer height, Envelope bbox) throws IOException {
        String query = mapRequestParams(Layer.BAKGRUNDSKARTA.toString(), width, height, Format.JPEG, null, bbox, null, HighResolutionDpi);

        return getImageAuth(metriaMapsUrl + query, MediaType.IMAGE_JPEG);
    }

    public BufferedImage getLegendProjekt(UUID projektId, Integer width, Integer height, Integer dpi, Integer srcWidth, Integer srcHeight, Envelope bbox, double scaleRatio, String styles, String cqlFilter) throws IOException {
        var cql = String.format("projekt_id='%s'", projektId);
        if (cqlFilter != null) {
            cql = cql.concat(" AND " + cqlFilter);
        }
        cql = URLEncoder.encode(cql, StandardCharsets.UTF_8);
        var query = legendRequestParamsWithExtent(Layer.INTRANG_PROJEKT, width, height, srcWidth, srcHeight, bbox, scaleRatio, Format.PNG, cql, styles, dpi, true);

        return getImage(geoserverUrl + query, MediaType.IMAGE_PNG);
    }

    public BufferedImage getLegendIntrangFastighet(UUID projektId, UUID fastighetId, Integer width, Integer height, Integer dpi, Integer srcWidth, Integer srcHeight, Envelope bbox, double scaleRatio) throws IOException {
        var cql = String.format("fastighet_id='%s' AND projekt_id='%s'", fastighetId, projektId);
        cql = URLEncoder.encode(cql, StandardCharsets.UTF_8);
        var query = legendRequestParamsWithExtent(Layer.INTRANG_FASTIGHET, width, height, srcWidth, srcHeight, bbox, scaleRatio, Format.PNG, cql, null, dpi, true);

        return getImage(geoserverUrl + query, MediaType.IMAGE_PNG);
    }

    public BufferedImage getLegendProjektOversikt(UUID projektId, Integer width, Integer height, Integer dpi, boolean showProjektomrade) throws IOException {
        var cql = String.format("projekt_id='%s'", projektId);
        var style = showProjektomrade ? LayerStyle.INTRANG_PROJEKT_PROJEKTOMRADE : LayerStyle.INTRANG_PROJEKT;
        var query = legendRequestParams(Layer.INTRANG_PROJEKT_OVERSIKT, width, height, Format.PNG, cql, style.toString(), dpi, false);

        return getImage(geoserverUrl + query, MediaType.IMAGE_PNG);
    }

    private String
    mapRequestParams(String layer, Integer width, Integer height, Format format, String cqlFilter, Envelope bbox, String styles, Integer dpi) {
        return "?SERVICE=WMS" +
                "&VERSION=1.3.0" +
                "&REQUEST=GetMap" +
                "&FORMAT=" + format +
                "&TRANSPARENT=true" +
                "&LAYERS=" + layer +
                (cqlFilter == null ? "" : String.format("&CQL_FILTER=%s", cqlFilter)) +
                "&CRS=EPSG:3006" +
                (styles == null ? "" : String.format("&STYLES=%s", styles)) +
                "&WIDTH=" + width +
                "&HEIGHT=" + height +
                String.format(Locale.ROOT, "&BBOX=%f,%f,%f,%f", bbox.getMinY(), bbox.getMinX(),
                        bbox.getMaxY(), bbox.getMaxX()) +
                String.format("&FORMAT_OPTIONS=dpi:%s", dpi);
    }

    private String
    legendRequestParams(Layer layer, Integer width, Integer height, Format format, String cqlFilter, String styles, Integer dpi, Boolean hideEmptyRules) {
        return "?SERVICE=WMS" +
                "&VERSION=1.3.0" +
                "&REQUEST=GetLegendGraphic" +
                "&FORMAT=" + format +
                "&TRANSPARENT=true" +
                "&LAYER=" + layer +
                "&SCALE=0" +
                (cqlFilter == null ? "" : String.format("&CQL_FILTER=%s", cqlFilter)) +
                (styles == null ? "" : String.format("&STYLE=%s", styles)) +
                "&HEIGHT=" + height +
                "&WIDTH=" + width +
                String.format("&LEGEND_OPTIONS=fontName:Arial;fontSize:10;fontAntiAliasing:true;dpi:%s;forceLabels:on;forceTitles:off;hideEmptyRules:%s", dpi, hideEmptyRules);
    }

    private String
    legendRequestParamsWithExtent(Layer layer, Integer width, Integer height, Integer srcWidth, Integer srcHeight, Envelope bbox, double scaleRatio, Format format, String cqlFilter, String styles, Integer dpi, Boolean hideEmptyRules) {
        return "?SERVICE=WMS" +
                "&VERSION=1.3.0" +
                "&REQUEST=GetLegendGraphic" +
                "&FORMAT=" + format +
                "&TRANSPARENT=true" +
                "&LAYER=" + layer +
                "&CRS=EPSG:3006" +
                "&SCALE=" + scaleRatio +
                (cqlFilter == null ? "" : String.format("&CQL_FILTER=%s", cqlFilter)) +
                (styles == null ? "" : String.format("&STYLE=%s", styles)) +
                "&HEIGHT=" + height +
                "&WIDTH=" + width +
                "&SRCHEIGHT=" + srcHeight +
                "&SRCWIDTH=" + srcWidth +
                String.format(Locale.ROOT, "&BBOX=%f,%f,%f,%f", bbox.getMinY(), bbox.getMinX(),
                        bbox.getMaxY(), bbox.getMaxX()) +
                String.format("&LEGEND_OPTIONS=fontName:Arial;fontSize:10;fontAntiAliasing:true;minSymbolSize:10;labelMargin:5;dpi:%s;forceLabels:on;forceTitles:off;hideEmptyRules:%s", dpi, hideEmptyRules);
    }

    private BufferedImage getImage(String url, MediaType mediaType) throws IOException {
        var geoserverRole = userService.getCurrentUser().getKundId();

        var bytes = webClient.get()
                .uri(url)
                .header("x-xplore-roles", geoserverRole)
                .accept(mediaType)
                .retrieve()
                .bodyToMono(byte[].class)
                .map(ByteArrayInputStream::new)
                .block();

        var img = ImageIO.read(bytes);
        if (img == null) {
            throw new IOException("Error reading image data: " + new String(bytes.readAllBytes()));
        }
        return img;
    }

    private BufferedImage getImageAuth(String url, MediaType mediaType) throws IOException {
        var auth = kundConfigService.getMetriaMapsAuth();
        var authHeader = HttpUtil.basicAuth(auth.getUsername(), auth.getPassword());
        var geoserverRole = userService.getCurrentUser().getKundId();

        var bytes = webClient.get()
                .uri(url)
                .header("Authorization", authHeader)
                .header("x-xplore-roles", geoserverRole)
                .accept(mediaType)
                .retrieve()
                .bodyToMono(byte[].class)
                .map(ByteArrayInputStream::new)
                .block();

        var img = ImageIO.read(bytes);
        if (img == null) {
            throw new IOException("Error reading image data: " + new String(bytes.readAllBytes()));
        }
        return img;
    }

    private enum Format {
        PNG8("image/png8"),
        PNG("image/png"),
        JPEG("image/jpeg");

        private String value;

        Format(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private enum Layer {
        BAKGRUNDSKARTA("metria:Europa,MetriaBakgrundEnkel"),
        BAKGRUNDSKARTA_SKARP("metria:Europa,MetriaFastighetSkarp"),
        FASTIGHETSYTOR("metria:Fastighetsytor"),
        INTRANG_PROJEKT("markkoll:intrang_projekt"),
        INTRANG_PROJEKT_OVERSIKT("markkoll:intrang_projekt_oversikt"),
        INTRANG_FASTIGHET("markkoll:intrang_fastighet"),
        INTRANG_EXTENT("markkoll:intrang_extent"),
        FASTIGHET_YTA("markkoll:fastighet_yta"),
        FASTIGHET_EXTENT("markkoll:fastighet_extent");

        private String value;

        Layer(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
