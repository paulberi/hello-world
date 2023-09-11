package se.metria.finfo.service.agare;

import com.google.common.collect.Iterables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import se.metria.finfo.openapi.model.AgareRequestDto;
import se.metria.finfo.service.agare.model.Agarforteckning;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgarforteckningService {
	@Value("${finfo.fsok-url}/ws/forteckningar/v2.0")
	private String fsokUrl;

	@Value("${finfo.batchSize:500}")
	private int batchSize;

	@Value("${finfo.fallback-adress-from-taxering}")
	private boolean fallbackAdressFromTaxering;

	private final WebClient webClient = WebClient.builder()
			.exchangeStrategies(ExchangeStrategies.builder()
					.codecs(configurer -> configurer
							.defaultCodecs()
							.maxInMemorySize(10 * 1024 * 1024)) //10MB
					.build())
			.build();

	public List<Agarforteckning> fetch(AgareRequestDto finfoAgareRequestDto)
		throws InterruptedException {

		var result = new LinkedList<Agarforteckning>();
		int batchNum = 0;

		for (var batch : Iterables.partition(finfoAgareRequestDto.getFastighetIds(), batchSize)) {
			log.info("Hämtar batch {}", ++batchNum);
			var batchRequest = new AgareRequestDto()
				.fastighetIds(batch)
				.username(finfoAgareRequestDto.getUsername())
				.password(finfoAgareRequestDto.getPassword())
				.kundmarke(finfoAgareRequestDto.getKundmarke());

			var agarforteckning = fetchBatch(batchRequest)
					.orElseThrow(() -> new IllegalArgumentException());
			result.add(agarforteckning);
		}
		return result;
	}


	/**
	 * @return Optional.empty om inget svar gick att få från tjänsten
	 */
	private Optional<Agarforteckning> fetchBatch(AgareRequestDto agareRequestDto) throws InterruptedException {
		log.info("Hämtar ägarförteckning för {} fastigheter från Fastighetssök ({}) med användare {}...",
				agareRequestDto.getFastighetIds().size(), fsokUrl, agareRequestDto.getUsername());

		Optional<Agarforteckning> agarforteckning = agarforteckningForFastigheter(agareRequestDto);
		log.info("Hämtning av ägarförteckning klar.");
		return agarforteckning;

	}

	private Optional<Agarforteckning>
	agarforteckningForFastigheter(AgareRequestDto agareRequestDto)
			throws InterruptedException {

		Optional<Agarforteckning> agarforteckning = agarforteckningjobbPost(agareRequestDto);

		if(agarforteckning.isEmpty() || agarforteckning.get().getAgarforteckningJob() == null) {
			log.info("Ägarförteckningsjobbet kunde inte skapas.");
			return Optional.empty();
		}

		var agarforteckningJob = agarforteckning.get().getAgarforteckningJob();

		if(agarforteckningJob.getStatus().equals("KLAR")) {
			return agarforteckning;
		}

		int jobbId = agarforteckningJob.getId();

        /* Om jobbet inte är klart så testar vi att hämta resultatet med 500 ms delay och amx 10 gånger. */
		for (int iter = 0; iter < 10; iter++) {
			//noinspection BusyWait
			Thread.sleep(500);

			agarforteckning = agarforteckningGet(jobbId, fsokUrl, agareRequestDto.getUsername(),
				agareRequestDto.getPassword());

			if(agarforteckning.isEmpty() || agarforteckning.get().getAgarforteckningJob() == null) {
				log.info("Resultatet av ägarförteckningsjobbet kunde inte hämtas.");
				return Optional.empty();
			}

			agarforteckningJob = agarforteckning.get().getAgarforteckningJob();

			if(agarforteckningJob.getStatus().equals("KLAR")) {
				return agarforteckning;
			}
		}

		log.info("Resultatet av ägarförteckningsjobbet kunde inte hämtas efter 10 försök.");
		return Optional.empty();
	}

	private Optional<Agarforteckning>
	agarforteckningGet(Integer agarforteckningsjobbId, String fsokUrl, String username, String password) {
		var url = fsokUrl + "/" + agarforteckningsjobbId;

		log.info("Hämtar ägarförteckning från Fastighetssök för ägarförteckningsjobb {}", agarforteckningsjobbId);
		return webClient
				.get()
				.uri(url)
				.headers(headers -> headers.setBasicAuth(username, password))
				.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.exchangeToMono(this::agarforteckningMono)
				.block();
	}

	private Optional<Agarforteckning>
	agarforteckningjobbPost(AgareRequestDto agareRequestDto) {
		var kundmarke = agareRequestDto.getKundmarke();
		var fastighetIds = agareRequestDto.getFastighetIds();

		Optional<String> kundmarkeOptional = kundmarke == null ? Optional.empty() : Optional.of(kundmarke);
		var body = fastighetIdsJson(fastighetIds, kundmarkeOptional);

		var queryParams = "?mode=direct" + "&fallbackAdressFromTaxering=" + fallbackAdressFromTaxering;
		log.info("Skapar ägarförteckningsjobb i Fastighetssök");

		return webClient
				.post()
				.uri(fsokUrl + queryParams)
				.headers(headers -> headers.setBasicAuth(agareRequestDto.getUsername(), agareRequestDto.getPassword()))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.body(BodyInserters.fromValue(body.toString()))
				.exchangeToMono(this::agarforteckningMono)
				.block();
	}

	private Mono<Optional<Agarforteckning>> agarforteckningMono(ClientResponse response) {
		var status = response.statusCode();
		if (status.value() == HttpStatus.OK.value()) {
			return response.bodyToMono(Agarforteckning.class).map(Optional::of);
		}
		// Fås om vi har gjort det här anropet innan systemet har hunnit skapa ägarförteckningen
		else if (status.value() == HttpStatus.NO_CONTENT.value()) {
			log.warn("Ingen ägarförteckningsdata tillgänglig.");
			return Mono.just(Optional.empty());
		} else {
			throw new ResponseStatusException(status);
		}
	}

	@SuppressWarnings("unchecked") // JOOQ har dåligt generic-stöd...
	private JSONObject fastighetIdsJson(List<UUID> fastighetIds, Optional<String> kundmarkeOptional) {
		var jsonBody = new JSONObject();
		var fastigheter = new JSONArray();
		fastigheter.addAll(fastighetIds);
		jsonBody.put("fastigheter", fastigheter);
		kundmarkeOptional.ifPresent(kundmarke -> jsonBody.put("kundmarke", kundmarke));

		return jsonBody;
	}
}
