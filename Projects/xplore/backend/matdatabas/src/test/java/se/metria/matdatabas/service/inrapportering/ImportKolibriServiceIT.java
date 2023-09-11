package se.metria.matdatabas.service.inrapportering;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.QueueDispatcher;
import org.apache.commons.io.IOUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.kolibri.api.MeasurementsApi;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.scheduler.SchedulerStartUpHandler;
import se.metria.matdatabas.service.systemlogg.SystemloggService;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.matdatabas.service.inrapportering.ImportFel.IMPORT_FEL_INLASNING;
import static se.metria.matdatabas.service.inrapportering.ImportFel.IMPORT_FEL_GODKAND;

@DisplayName("Importera kolibri data")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class ImportKolibriServiceIT {

	private final static Integer MATNINGSTYP_NIVA = 10001;
	private final static Integer MATNINGSTYP_LUFTTRYCK = 10002;
	private final static Integer MEASUREMENT_DEFINITION_P1 = 2;
	private final static Integer MEASUREMENT_DEFINITION_PBARO = 7;

	private static MockWebServer mockBackEnd;
	private static String measurementDefinitionP1;
	private static String measurementDefinitionPBaro;
	private static String measurementDefinitionGodkann;
	private static String measurementDefinitionGodkannOverwrite;

	@Autowired
	private ImportKolibriService importKolibriService;

	@MockBean
	private MatdatabasUser user;

	@MockBean
	private SystemloggService systemloggService;

	@Autowired
	private MatningService matningService;

	@MockBean
	private SchedulerStartUpHandler schedulerStartUpHandler;

	static void addParam(List<ConversionTestParam> params, Double avlastVarde, Double convertedVarde) {
		params.add(new ConversionTestParam(avlastVarde, convertedVarde));
	}

	// Bar to hPa
	static List<ConversionTestParam> lufttryckConversionTestParams() {
		List<ConversionTestParam> params = new ArrayList<>();

		addParam(params, 1., 1000.);
		addParam(params, 1.03, 1030.);
		addParam(params, 0.97, 970.);

		return params;
	}

	// Bar to cmvp
	static List<ConversionTestParam> vattennivåConversionTestParams() {
		List<ConversionTestParam> params = new ArrayList<>();

		addParam(params, 1.16, 163.1725750647882);
		addParam(params, 1.19, 193.76743288943615);
		addParam(params, 1.14, 142.77600318168965);

		return params;
	}

	static List<KolibriMeasurement> measurementsFromParams(List<ConversionTestParam> testParams) {
		List<KolibriMeasurement> measurements = new ArrayList<>();
		LocalDateTime dateTime = LocalDateTime.now();
		for (int i = 0; i < testParams.size(); i++) {
			KolibriMeasurement kolibriMeasurement = new KolibriMeasurement(
					dateTime.plusSeconds(i).toString(), testParams.get(i).avlastVarde);

			measurements.add(kolibriMeasurement);
		}

		return measurements;
	}

	static String importResource(String path) throws IOException {
		InputStream inputStream = ImportKolibriServiceIT.class.getResourceAsStream(path);
		return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
	}

	static void mockBackendEnqueue(String body) {
		mockBackEnd.enqueue(new MockResponse()
				.addHeader("content-type", "application/json; charset=UTF-8")
				.setResponseCode(HttpStatus.OK.value())
				.setBody(body));
	}

	static void mockBackendEnqueueTestParams(Integer measurementDefinitionId, List<ConversionTestParam> params)
	throws IOException {
		final var response = new KolibriMeasurementResponse(measurementDefinitionId, 1879,
				measurementsFromParams(params), 101);
		ObjectMapper obj = new ObjectMapper();
		final var body = obj.writeValueAsString(response);

		mockBackendEnqueue(body);
	}

	void assertCorrectConversions(List<ConversionTestParam> testParams, Integer matningstyp) {
		var matningar = matningService.getMatningarForMatningstyp(
				matningstyp, 0, testParams.size(),
				"avlastDatum", Sort.Direction.DESC,
				null, null, null
				).getContent();
		assertEquals(matningar.size(), testParams.size());

		// The list is sorted by time in descending order. Reverse it to get things in the right order.
		// I think the list is immutable, so I have to do this in order for Collections.reverse to not throw an exception in my face.
		matningar = new ArrayList<>(matningar);
		Collections.reverse(matningar);

		for (int i = 0; i < matningar.size(); i++) {
			assertEquals(matningar.get(i).getAvlastVarde(), testParams.get(i).convertedVarde, 1e-3);
		}
	}

	@BeforeAll
	static void setup(@Autowired DataSource dataSource,
					  @Autowired MeasurementsApi measurementsApi,
					  @Value( "${importservice.stockholmnshamnar.url}" ) String fileUrl) throws IOException, SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_matningstyper_kolibri.sql"));
		}
		measurementDefinitionP1 = importResource("/json/measurementDefinitionId2.json");
		measurementDefinitionPBaro = importResource("/json/measurementDefinitionId7.json");
		measurementDefinitionGodkann = importResource("/json/measurementDefinitionGodkann.json");
		measurementDefinitionGodkannOverwrite = importResource("/json/measurementDefinitionGodkannOverwrite.json");
		mockBackEnd = new MockWebServer();
		QueueDispatcher dispatcher = new QueueDispatcher();
		dispatcher.setFailFast(true);
		mockBackEnd.setDispatcher(dispatcher);
		URL url = new URL(fileUrl);
		mockBackEnd.start(url.getPort());
		measurementsApi.getApiClient().setBasePath(url.toString());
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}

	@Test
	@WithMockUserAdministrator
	void när_kolibri_import_misslyckades_ska_fel_loggas() {
		when(systemloggService.addHandelseImportFailed(any(), any())).thenReturn(new SystemloggEntry());

		mockBackEnd.enqueue(new MockResponse()
				.addHeader("content-type", "application/json; charset=UTF-8")
				.setResponseCode(HttpStatus.UNAUTHORIZED.value()));
		LocalDateTime dateTime = LocalDateTime.now();
		importKolibriService.importKolibri(dateTime.minusDays(1), dateTime);
		verify(systemloggService).addHandelseImportFailed(JobServicetyper.IMPORT_KOLIBRI, IMPORT_FEL_INLASNING);
	}

	@Test
	@WithMockUserAdministrator
	void när_kolibri_import_lyckas_ska_mätvärden_konverteras_korrekt() throws IOException {
		// Given
		final var lufttryckTestParams = lufttryckConversionTestParams();
		final var vattennivåTestParams = vattennivåConversionTestParams();

		mockBackendEnqueueTestParams(MEASUREMENT_DEFINITION_PBARO, lufttryckTestParams);
		mockBackendEnqueueTestParams(MEASUREMENT_DEFINITION_P1, vattennivåTestParams);

		// When
		LocalDateTime dateTime = LocalDateTime.now();
		importKolibriService.importKolibri(dateTime.minusDays(1), dateTime);

		// Then
		assertCorrectConversions(lufttryckTestParams, MATNINGSTYP_LUFTTRYCK);
		assertCorrectConversions(vattennivåTestParams, MATNINGSTYP_NIVA);
	}

	@Test
	@WithMockUserAdministrator
	void när_kolibri_import_lyckas_ska_matningar_skapas() {
		when(systemloggService.addHandelseImportDone(any(), any())).thenReturn(new SystemloggEntry());
		mockBackendEnqueue(measurementDefinitionPBaro);
		mockBackendEnqueue(measurementDefinitionP1);

		LocalDateTime dateTime = LocalDateTime.now();
		importKolibriService.importKolibri(dateTime.minusDays(1), dateTime);
		verify(systemloggService).addHandelseImportDone(JobServicetyper.IMPORT_KOLIBRI, 48);

		LocalDateTime timeFrom = LocalDateTime.parse("2020-03-01T00:00:00");
		LocalDateTime timeTo = LocalDateTime.parse("2020-03-02T23:59:59");
		assertTrue(matningService.hasMatningar(MATNINGSTYP_NIVA));
		var page0 = matningService.getMatningarForMatningstyp(MATNINGSTYP_NIVA, 0, 50,
				"avlastDatum",  Sort.Direction.DESC, timeFrom, timeTo, null);
		assertEquals(1, page0.getTotalPages());
		assertEquals(24, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(24, page0.getContent().size());
		assertNotNull(page0.getContent().get(0).getBeraknatVarde());
	}

	@Test
	@WithMockUserAdministrator
	void när_redan_godkänt_mätning_skrivs_över_ska_fel_loggas() {
		when(systemloggService.addHandelseImportFailed(any(), any())).thenReturn(new SystemloggEntry());

		mockBackendEnqueue(measurementDefinitionGodkann);
		mockBackendEnqueue(measurementDefinitionGodkann);
		mockBackendEnqueue(measurementDefinitionGodkannOverwrite);

		LocalDateTime dateTime = LocalDateTime.now();
		importKolibriService.importKolibri(dateTime.minusDays(1), dateTime);

		var godkannList = new ArrayList<Long>();
		godkannList.add((long)1);
		matningService.godkann(godkannList);

		importKolibriService.importKolibri(dateTime.minusDays(1), dateTime);

		verify(systemloggService).addHandelseImportFailed(JobServicetyper.IMPORT_KOLIBRI, IMPORT_FEL_GODKAND);
	}
}
