package se.metria.xplore.sok.geocode;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import se.metria.xplore.sok.SokProperties;
import se.metria.xplore.sok.exceptions.GeofilterException;
import se.metria.xplore.sok.geocode.model.MediumReplyExtent;
import se.metria.xplore.sok.geocode.request.GeocodeRequest;
import se.metria.xplore.sok.geofilter.GeofilterClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class GeocodeService {
	private final SokProperties properties;
	private final RestTemplate restTemplate;
	private final GeofilterClient geofilterClient;

	public GeocodeService(SokProperties properties, GeofilterClient geofilterClient) {
		this.properties = properties;
		this.restTemplate = new RestTemplate();
		this.geofilterClient = geofilterClient;
	}

	@Async("asyncExecutor")
	public CompletableFuture<List<MediumReplyExtent>> get(GeocodeRequest request, boolean geofilter, String auth) throws RestClientException, GeofilterException {
		List<MediumReplyExtent> replies = new ArrayList<>();
		try {
			ResponseEntity<MediumReplyExtent[]> responseEntity = this.restTemplate.exchange(
					this.properties.getGeocodeEndpoint() + "?" + request.getQueryTemplate(),
					HttpMethod.GET,
					createHttpEntity(auth),
					MediumReplyExtent[].class,
					request.getQueryParameters());

			if (responseEntity.hasBody()) {
				replies = Arrays.asList(responseEntity.getBody());
			}
		} catch (HttpClientErrorException e) {
			// Known bug in Geokodningstjänsten, we simply suppress the error
			if (!e.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)){
				throw e;
			}
		}

		if (geofilter && request.getMaskUrl() != null && !replies.isEmpty()) {
			replies = this.geofilterClient.geofilter(
					replies,
					request.getMaskUrl(),
					request.hasKommun() ? request.getKommun().get(0) : null);
		}

		return CompletableFuture.completedFuture(replies);
	}

	private HttpEntity createHttpEntity(String auth) {
		HttpHeaders headers = new HttpHeaders();
		if (auth != null) {
			headers.set("Authorization", auth);
		}
		return new HttpEntity<>(headers);
	}
}
