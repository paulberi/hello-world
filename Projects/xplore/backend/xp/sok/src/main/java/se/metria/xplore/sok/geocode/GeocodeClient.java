package se.metria.xplore.sok.geocode;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import se.metria.xplore.sok.SokProperties;
import se.metria.xplore.sok.common.BaseClient;
import se.metria.xplore.sok.exceptions.GeofilterException;
import se.metria.xplore.sok.geocode.request.GeocodeRequest;
import se.metria.xplore.sok.geocode.model.MediumReplyExtent;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import se.metria.xplore.sok.geocode.request.GeocodeRequestCoordinate;
import se.metria.xplore.sok.geocode.request.GeocodeRequestFactory;
import se.metria.xplore.sok.geofilter.GeofilterClient;

import static java.lang.Math.min;

@Service
public class GeocodeClient extends BaseClient {
	private final GeofilterClient geofilterClient;
	private final GeocodeService geocodeService;

	public GeocodeClient(SokProperties properties, GeofilterClient geofilterClient, GeocodeService geocodeService) {
		super(properties);
		this.geofilterClient = geofilterClient;
		this.geocodeService = geocodeService;
	}

	/*
	 * Performs either a Geocode request or Reverse Geocode depending on the provided parameters.
	 *
	 * @param query The query to search for. NULL if reverse geocode
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param kommuner An array of kommuner. Used when calling the Geokodningstjänsten.
	 * @param maskUrl The URL to the geofiltering mask. Used both for removing unwanted hits from
	 *                the Geokodningstjänsten as well as ignoring requests outside 'kommuner'
	 * @return List<GeocodeRequest> one for each provided kommun
	 */
	public ResponseEntity<List<MediumReplyExtent>> request(GeocodeRequest request)
			throws RestClientException, GeofilterException {

		if (request.getMaskUrl() != null) {
			geofilterClient.addMask(request.getMaskUrl());
		}

		if (request instanceof GeocodeRequestCoordinate) {
			return reverseGeocode(request);
		} else {
			return geocode(request);
		}
	}

	/*
	 * Performs one or many async Geocoding requests, depending on the number of restricting kommuner that are used.
	 * The individual results are combined, sorted and cleaned for duplicates.
	 *
	 * @param requests A list of GeocodeRequest, one for each kommun
	 * @param maskUrl The URL to the geofiltering mask
	 * @return ResponseEntity containing a list of MediumReplyExtent
	 */
	private ResponseEntity<List<MediumReplyExtent>> geocode(GeocodeRequest request) throws RestClientException, GeofilterException {
		List<CompletableFuture<List<MediumReplyExtent>>> futuresList = new ArrayList<>();

		for (GeocodeRequest r : buildAsyncRequestList(request)) {
			futuresList.add(geocodeService.get(r, true, getAuth()));
		}

		CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0])).join();

		List<MediumReplyExtent> result = new ArrayList<>();

		for (CompletableFuture<List<MediumReplyExtent>> f : futuresList) {
			result.addAll(f.join());
		}

		return new ResponseEntity<>(purgeResults(result), HttpStatus.OK);
	}

	/*
	 * Performs one synchronous reverse Geocoding request.
	 * The results are then geofiltered using the provided mask.
	 *
	 * @param x The X coordinate
	 * @param y The y coordinate
	 * @param maskUrl The URL to the geofiltering mask
	 * @return ResponseEntity containing a list of MediumReplyExtent
	 */
	private ResponseEntity<List<MediumReplyExtent>> reverseGeocode(GeocodeRequest request) throws RestClientException, GeofilterException {
		// Geofiltering is intentionally left out, since we will filter the results
		// against the whole mask and not against each kommun separately
		List<MediumReplyExtent> replies = geocodeService.get(request, false, getAuth()).join();

		if (request.getMaskUrl() != null) {
			replies = this.geofilterClient.geofilter(replies, request.getMaskUrl(), null);
		}

		return new ResponseEntity<>(replies, HttpStatus.OK);
	}

	/*
	 * Creates a list of GeocodeRequests, one object for each kommun.
	 *
	 * @param query The query to search for
	 * @param x The x coordinate used for proximity
	 * @param y The y coordinate used for proximity
	 * @param kommuner An array of kommunnamn
	 * @return List<GeocodeRequest> one for each provided kommun
	 */
	private List<GeocodeRequest> buildAsyncRequestList(GeocodeRequest request) {
		List<GeocodeRequest> requests = new ArrayList<>();
		if (request.hasKommun()) {
			for (String kommun : request.getKommun()) {
				requests.add(GeocodeRequestFactory.getRequest(
						request.getQuery(),
						request.getX(),
						request.getY(),
						new String[]{kommun},
						request.getMaskUrl()));
			}
		} else {
			requests.add(request);
		}

		return requests;
	}

	/*
	 * Sorts and filters the provided list of replies. Sorting is by similarity and mismatch.
	 * All duplicates are removed and the number of replies is restricted to the 10 most significant.
	 *
	 * @param replies The unfiltered list of replies
	 * @return List<MediumReplyExtent> with filtered replies
	 */
	private List<MediumReplyExtent> purgeResults(List<MediumReplyExtent> replies) {
		// Filter mismatches and perform sorting
		replies = replies.stream()
				.filter(r -> r.getMismatch().equals("0"))
				.sorted(Comparator.comparingDouble(MediumReplyExtent::getSimilarity).reversed())
				.collect(Collectors.toList());

		// Removing duplicates
		Set<String> set = new HashSet<>(replies.size());
		replies.removeIf(p -> !set.add(p.getAddress()));

		// Returning only 10 first
		return replies.subList(0, min(replies.size(), 10));
	}
}
