package se.metria.xplore.sok;

import org.locationtech.jts.io.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.metria.xplore.sok.exceptions.GeoJsonMergeException;
import se.metria.xplore.sok.exceptions.GeofilterException;
import se.metria.xplore.sok.exceptions.GmlConversionException;
import se.metria.xplore.sok.fastighet.FastighetClient;
import se.metria.xplore.sok.fastighet.model.GeometryOperation;
import se.metria.xplore.sok.fastighet.model.GeometryRequest;
import se.metria.xplore.sok.fastighet.request.FastighetRequestFactory;
import se.metria.xplore.sok.geocode.GeocodeClient;
import se.metria.xplore.sok.geocode.model.MediumReplyExtent;
import se.metria.xplore.sok.geocode.request.GeocodeRequestFactory;
import se.metria.xplore.sok.kommun.KommunClient;
import se.metria.xplore.sok.kommun.model.KommunRequest;
import se.metria.xplore.sok.ort.OrtClient;
import se.metria.xplore.sok.ort.model.OrtRequest;
import se.metria.xplore.sok.ort.model.OrtSokResultat;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SokController {
	private final GeocodeClient geocodeClient;
	private final FastighetClient fastighetClient;
	private final KommunClient kommunClient;
	private final OrtClient ortClient;

	public SokController(GeocodeClient geocodeClient, FastighetClient fastighetClient, OrtClient ortClient, KommunClient kommunClient) {
		this.geocodeClient = geocodeClient;
		this.fastighetClient = fastighetClient;
		this.ortClient = ortClient;
		this.kommunClient = kommunClient;
	}

	@GetMapping("/geocode")
	public ResponseEntity<List<MediumReplyExtent>> getGeocode(@RequestParam(value = "q") String query,
															  @RequestParam(value = "x", required = false) Double x,
															  @RequestParam(value = "y", required = false) Double y,
															  @RequestParam(value = "k", required = false) String[] kommuner,
															  @RequestParam(value = "m", required = false) String maskUrl) throws GeofilterException {
		return this.geocodeClient.request(
				GeocodeRequestFactory.getRequest(query, x, y, kommuner, maskUrl));
	}

	@GetMapping("/reverse-geocode")
	public ResponseEntity<List<MediumReplyExtent>> getReverseGeocode(@RequestParam(value = "x") Double x,
																	 @RequestParam(value = "y") Double y,
																	 @RequestParam(value = "k", required = false) String[] kommuner,
																	 @RequestParam(value = "m", required = false) String maskUrl) throws GeofilterException {
		return this.geocodeClient.request(
				GeocodeRequestFactory.getRequest(null, x, y, kommuner, maskUrl));
	}

	@GetMapping("/fastighet")
	public ResponseEntity<String> getFastighet(@RequestParam(value = "q") String query,
									   @RequestParam(value = "k", required = false) String[] kommuner) {
		return this.fastighetClient.request(
				FastighetRequestFactory.getRequest(query, kommuner, "Fastighetsytor_WFS"));
	}

	@PostMapping("/fastighet/geometry")
	public ResponseEntity<String> getFastigheterByGeometry(@RequestParam(value = "q") String query,
														   @RequestParam(value = "o") GeometryOperation operation,
														   @RequestParam(value = "b", required = false) Double bufferDistance,
														   @RequestParam(value = "k", required = false) String[] kommuner,
														   @RequestParam(value = "maxFeatures", required = false, defaultValue = "500") int maxFeatures,
														   @RequestParam(value = "startIndex", required = false, defaultValue = "0") int startIndex) throws ParseException {
		return this.fastighetClient.request(
				FastighetRequestFactory.getGeometryRequest(new GeometryRequest(query, operation, bufferDistance), kommuner, "Fastighetsytor_WFS", maxFeatures, startIndex));
	}

	@GetMapping("/samf-mittlinje")
	public ResponseEntity<String> getSamfMittlinje(@RequestParam(value = "q") String query,
												   @RequestParam(value = "k", required = false) String[] kommuner) {
		return this.fastighetClient.request(
				FastighetRequestFactory.getRequest(query, kommuner, "Mittlinjeredovisadesamfalligheter"));
	}

	@PostMapping("/samf-mittlinje/geometry")
	public ResponseEntity<String> getSamfMittlinjeByGeometry(@RequestParam(value = "q") String query,
															 @RequestParam(value = "o") GeometryOperation operation,
															 @RequestParam(value = "b", required = false) Double bufferDistance,
															 @RequestParam(value = "k", required = false) String[] kommuner,
															 @RequestParam(value = "maxFeatures", required = false, defaultValue = "500") int maxFeatures,
															 @RequestParam(value = "startIndex", required = false, defaultValue = "0") int startIndex) throws ParseException {
		return this.fastighetClient.request(
				FastighetRequestFactory.getGeometryRequest(new GeometryRequest(query, operation, bufferDistance), kommuner, "Mittlinjeredovisadesamfalligheter", maxFeatures, startIndex));
	}

	@GetMapping("/fastighet-punkt")
	public ResponseEntity<String> getFastighetPunkt(@RequestParam(value = "q") String query,
													@RequestParam(value = "k", required = false) String[] kommuner) {
		return this.fastighetClient.request(
				FastighetRequestFactory.getRequest(query, kommuner, "registerenhet_punkt"));
	}

	@PostMapping("/fastighet-punkt/geometry")
	public ResponseEntity<String> getFastighetPunktByGeometry(@RequestParam(value = "q") String query,
															  @RequestParam(value = "o") GeometryOperation operation,
															  @RequestParam(value = "b", required = false) Double bufferDistance,
															  @RequestParam(value = "k", required = false) String[] kommuner,
															  @RequestParam(value = "maxFeatures", required = false, defaultValue = "500") int maxFeatures,
															  @RequestParam(value = "startIndex", required = false, defaultValue = "0") int startIndex) throws ParseException {
		return this.fastighetClient.request(
				FastighetRequestFactory.getGeometryRequest(new GeometryRequest(query, operation, bufferDistance), kommuner, "registerenhet_punkt", maxFeatures, startIndex));
	}

	@GetMapping("/ort")
	public ResponseEntity<List<OrtSokResultat>> getOrt(@RequestParam(value = "q") String query,
													   @RequestParam(value = "k", required = false) String kommun,
													   @RequestParam(value = "l", required = false) String lan,
													   @RequestParam(value = "x", required = false) Double x,
													   @RequestParam(value = "y", required = false) Double y) throws GmlConversionException {
		return this.ortClient.request(new OrtRequest(query, kommun, lan, x, y));
	}

	@GetMapping("/tatort")
	public ResponseEntity<List<OrtSokResultat>> getTatort(@RequestParam(value = "q") String query,
									@RequestParam(value = "k", required = false) String kommun,
									@RequestParam(value = "l", required = false) String lan,
									@RequestParam(value = "x", required = false) Double x,
									@RequestParam(value = "y", required = false) Double y) throws GmlConversionException {
		return this.ortClient.request(new OrtRequest(query, kommun, lan, x, y, Arrays.asList("BEBTÄTTX")));
	}

	@GetMapping("/kommun")
	public ResponseEntity<String> getKommun(@RequestParam(value = "q") String query,
									@RequestParam(value = "l", required = false) String lan) throws GeoJsonMergeException {
		return this.kommunClient.request(new KommunRequest(query, lan));
	}
}
