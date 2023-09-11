package se.metria.matdatabas.service.statusrapport;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.service.scheduler.SchedulerService;
import se.metria.matdatabas.service.systemlogg.SystemloggRepository;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Givet status service tjänst")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@TestPropertySource(properties = {"management.health.mail.enabled=false"}) // Slå av healthchecks så att testen funkar
class StatusServiceIT {

	private static final String TEST_BESKRIVNING = "Status mail skickad till användare #1, adress: test@metria.se";
	private static final String ANV_NAMN_0 = "SYSTEM";

	@MockBean
	private SchedulerService schedulerService;

	@Autowired
	private StatusService statusService;

	@MockBean
	private MatdatabasUser user;

	@MockBean
	private JavaMailSender javaMailSender;

	@Autowired
	private SystemloggService systemloggService;

	@Autowired
	private SystemloggRepository systemloggRepository;

	@MockBean
	private MimeMessageHelper mimeMessageHelper;

	@BeforeAll
	static void setup(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_matningstyper.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_gransvarden.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_matning.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_larm.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_anvandare.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_anvandargrupp_map.sql"));
		}
	}

	@BeforeEach
	public void beforeEach() {
		when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session)null));
		systemloggRepository.deleteAll();
	}

	@Test
	@WithMockUserAdministrator
	void när_statusmail_skickas_ska_det_loggas() throws JobExecutionException {

		statusService.skickaStatusmailAllaAnvandare();
		verify(javaMailSender).send(any(MimeMessage.class));
		var page = systemloggService.getSystemlogg(0, 3, "datum", Sort.Direction.ASC, null, null, null);
		assertEquals(1, page.getTotalPages());
		assertEquals(1, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(1, loggPoster.size());
		assertEquals(TEST_BESKRIVNING, loggPoster.get(0).getBeskrivning());
		assertEquals(ANV_NAMN_0, loggPoster.get(0).getAnvandarnamn());
	}
}
