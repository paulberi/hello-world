package se.metria.matdatabas.service.inrapportering;

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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.service.matning.MatningRepository;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matningslogg.MatningsloggRepository;
import se.metria.matdatabas.service.matningstyp.MatningstypRepository;
import se.metria.matdatabas.service.scheduler.SchedulerService;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.systemlogg.SystemloggService;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.matdatabas.service.inrapportering.ImportFel.*;

@DisplayName("Importera vattenstånd")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class ImportVattenstandServiceIT {

	private final static Integer MATNINGSTYP_MALAREN = 10001;
	private final static Integer MATNINGSTYP_SALTSJON = 10002;

	private static MockWebServer mockBackEnd;
	private static String csv;

	@Autowired
	private ImportVattenstandService importVattenstandService;

	@MockBean
	private MatdatabasUser user;

	@MockBean
	private SystemloggService systemloggService;

	@Autowired
	private MatningService matningService;

	@MockBean
	private SchedulerService schedulerService;

	@Autowired
	private MatningstypRepository matningstypRepository;

	@Autowired
	private MatningRepository matningRepository;

	@Autowired
	private MatningsloggRepository matningsloggRepository;

	@BeforeAll
	static void setup(@Autowired DataSource dataSource,
					  @Value( "${importservice.stockholmnshamnar.url}" ) String fileUrl) throws SQLException, IOException {
		InputStream inputStream = ImportVattenstandServiceIT.class.getResourceAsStream("/csv/level_malaren_saltsjon.csv");
		csv = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		mockBackEnd = new MockWebServer();
		QueueDispatcher dispatcher = new QueueDispatcher();
		dispatcher.setFailFast(true);
		mockBackEnd.setDispatcher(dispatcher);
		URL url = new URL(fileUrl);
		mockBackEnd.start(url.getPort());

	}

	@AfterAll
	static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}

	@AfterEach
	public void afterEach() {
		matningsloggRepository.deleteAll();
		matningRepository.deleteAll();
		matningstypRepository.deleteAll();
	}

	@Test
	@WithMockUserAdministrator
	@Sql({"/data/test_create_matningstyper_vattenstand.sql", "/data/test_update_scheduled_jobs.sql"})
	void när_vattenstånd_import_misslyckades_ska_fel_loggas() {
		when(systemloggService.addHandelseImportFailed(any(), any())).thenReturn(new SystemloggEntry());

		LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
		LocalDateTime toDate = LocalDateTime.now();

		mockBackEnd.enqueue(new MockResponse()
				.addHeader("Content-Type", "text/plain; charset=UTF-8")
				.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.setBody("200402,0030,sdfsdf,gwss"));

		importVattenstandService.importVattenstand(fromDate, toDate);
		verify(systemloggService).addHandelseImportFailed(JobServicetyper.IMPORT_VATTENSTAND, IMPORT_FEL_INLASNING);

		mockBackEnd.enqueue(new MockResponse()
				.addHeader("Content-Type", "text/plain; charset=UTF-8")
				.setResponseCode(HttpStatus.OK.value())
				.setBody("200402,0030,sdfsdf,gwss"));

		importVattenstandService.importVattenstand(fromDate, toDate);
		verify(systemloggService).addHandelseImportFailed(JobServicetyper.IMPORT_VATTENSTAND, IMPORT_FEL_FORMAT);
	}

	@Test
	@WithMockUserAdministrator
	@Sql({"/data/test_create_matningstyper_vattenstand.sql", "/data/test_update_scheduled_jobs.sql"})
	void när_vattenstånd_import_lyckas_ska_matningar_skapas() {
		when(systemloggService.addHandelseImportDone(any(), any())).thenReturn(new SystemloggEntry());

		mockBackEnd.enqueue(new MockResponse()
				.addHeader("Content-Type", "text/plain; charset=UTF-8")
				.setResponseCode(HttpStatus.OK.value())
				.setBody(csv));

		LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
		LocalDateTime toDate = LocalDateTime.now();
		importVattenstandService.importVattenstand(fromDate, toDate);
		verify(systemloggService).addHandelseImportDone(JobServicetyper.IMPORT_VATTENSTAND, 94); // Två tomma värden, därav två mindre än antalet mätrader

		assertEquals(true, matningService.hasMatningar(MATNINGSTYP_MALAREN));
		var page0 = matningService.getMatningarForMatningstyp(MATNINGSTYP_MALAREN, 0, 50,
				"avlastDatum",  Sort.Direction.DESC, null, null, null);
		assertEquals(1, page0.getTotalPages());
		assertEquals(47, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(47, page0.getContent().size());
	}
}
