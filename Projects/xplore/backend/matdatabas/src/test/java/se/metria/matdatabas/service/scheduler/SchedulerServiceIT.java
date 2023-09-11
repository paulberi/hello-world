package se.metria.matdatabas.service.scheduler;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.QueueDispatcher;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.service.inrapportering.ImportVattenstandService;
import se.metria.matdatabas.service.scheduler.entity.SchedulerJobEntity;
import se.metria.matdatabas.service.statusrapport.StatusService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@DisplayName("Givet scheduler service tjänst")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class SchedulerServiceIT {

	private static MockWebServer mockBackEnd;

	@MockBean
	private MatdatabasUser user;

	@MockBean
	private SchedulerStartUpHandler schedulerStartUpHandler;

	@SpyBean
	private SchedulerService schedulerService;

	@SpyBean
	private ImportVattenstandService importVattenstandService;

	@SpyBean
	private StatusService statusService;


	@BeforeAll
	static void setup(@Value( "${importservice.stockholmnshamnar.url}" ) String fileUrl) throws IOException {
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

	@Test
	@Sql("/data/test_create_matningstyper_vattenstand.sql")
	void när_vattenstånd_service_scheduleras_ska_den_köras() throws InterruptedException, ExecutionException {

		mockBackEnd.enqueue(new MockResponse()
				.addHeader("Content-Type", "text/plain; charset=UTF-8")
				.setResponseCode(HttpStatus.OK.value())
				.setBody("200322, 0030,        27.26,       10.66,"));

		SchedulerJobEntity jobInfo = new SchedulerJobEntity();
		jobInfo.setCronJob(false);
		jobInfo.setJobClass(JobServicetyper.IMPORT_VATTENSTAND);
		jobInfo.setJobName("ImportHamnarJobTest");

		schedulerService.startJobNow(jobInfo);

		System.out.println("**** Wait for job start.");
		verify(importVattenstandService, timeout(10000)).importVattenstand(any(),  any());
		System.out.println("**** Job started");
		verify(schedulerService, timeout((10000))).jobDone(ArgumentMatchers.eq(JobServicetyper.IMPORT_VATTENSTAND),any());
		System.out.println("**** Job done.");

		Thread.sleep(500); // Wait so the save in the background job has time to execute

		SchedulerJobEntity schedulerJobEntity = schedulerService.getJobInfo(JobServicetyper.IMPORT_VATTENSTAND);
		assertEquals(Jobstatus.OK, schedulerJobEntity.getLatestStatus());

		LocalDateTime timeLastRun = schedulerJobEntity.getLatestOk();
		LocalDateTime timeNow = LocalDateTime.now();
		assertTrue(ChronoUnit.SECONDS.between(timeLastRun, timeNow) < 60);
	}

	@Test
	void när_ny_statusrapport_scheduleras_ska_den_köras() throws InterruptedException, ExecutionException {
		SchedulerJobEntity jobInfo = new SchedulerJobEntity();
		jobInfo.setCronJob(false);
		jobInfo.setJobClass(JobServicetyper.STATUS_MAIL);
		jobInfo.setJobName("StatusService");

		schedulerService.startJobNow(jobInfo);

		System.out.println("**** Wait for job start.");
		verify(statusService, timeout(10000)).skickaStatusmailAllaAnvandare();
		System.out.println("**** Job started");
		verify(schedulerService, timeout((10000))).jobDone(ArgumentMatchers.eq(JobServicetyper.STATUS_MAIL),any());
		System.out.println("**** Job done.");

		Thread.sleep(500); // Wait so the save in the background job has time to execute

		LocalDateTime timeLastRun = schedulerService.getLastRunOkDate(JobServicetyper.STATUS_MAIL);
		LocalDateTime timeNow = LocalDateTime.now();
		assertTrue(ChronoUnit.SECONDS.between(timeLastRun, timeNow) < 60);
	}
}
