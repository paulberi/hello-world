package se.metria.matdatabas;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Import(TestConfig.class)
public class PaminnelseIT {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private Requests requests;

	@MockBean
	MatdatabasUser user;

	private static final Integer NEDMATNING = 1;
	private static final Integer PORTTRYCK = 2;
	private static final Integer SATTNING = 3;
	private static final Integer FLODE = 5;

	@BeforeAll
	static void setup(@Autowired DataSource dataSource) throws Exception {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_matningstyper.sql"));
		}
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void kontrollera_att_det_går_filtrera_påminnelser() throws Exception {
		mvc.perform(post("/api/paminnelser?onlyForsenade=false")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"matansvarigAnvandargruppIds\": [1]}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(4)))
				.andExpect(jsonPath("numberOfElements", equalTo(4)));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void kontrollera_påminnelser_med_olika_matningar() throws Exception {

		mvc.perform(post("/api/paminnelser")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("numberOfElements", equalTo(10)));

		requests.addMatningar(NEDMATNING, NEDMATNING, LocalDateTime.now(), 1, 3.0);
		mvc.perform(post("/api/paminnelser?onlyForsenade=true")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("numberOfElements", equalTo(9)));

		// Mätintervall = 1 vecka
		// Påminnelse = 1 dag
		requests.addMatningar(PORTTRYCK, PORTTRYCK, LocalDateTime.now().minusWeeks(1).minusDays(2), 1, 3.0);
		mvc.perform(post("/api/paminnelser?onlyForsenade=true")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("numberOfElements", equalTo(9)));
		requests.addMatningar(PORTTRYCK, PORTTRYCK, LocalDateTime.now().minusWeeks(1), 1, 3.0);
		mvc.perform(post("/api/paminnelser?onlyForsenade=true")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("numberOfElements", equalTo(8)));

		// Mätintervall = 1 månad
		// Påminnelse = 3 dagar
		requests.addMatningar(SATTNING, SATTNING, LocalDateTime.now().minusMonths(1).minusDays(6), 1, 3.0);
		mvc.perform(post("/api/paminnelser?onlyForsenade=true")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("numberOfElements", equalTo(8)));
		requests.addMatningar(SATTNING, SATTNING, LocalDateTime.now().minusMonths(1), 1, 3.0);
		mvc.perform(post("/api/paminnelser?onlyForsenade=true")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("numberOfElements", equalTo(7)));

	}

	@Test
	@WithMockUserTillstandshandlaggare
	void kontrollera_att_det_går_sortera_påminnelser() throws Exception {
		mvc.perform(post("/api/paminnelser?page=0&size=10&sortProperty=matobjektNamn&sortDirection=desc")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"matansvarigAnvandargruppIds\": [1]}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].matobjektNamn", equalTo("Mätobjekt5")))
				.andExpect(jsonPath("$.content[1].matobjektNamn", equalTo("Mätobjekt3")))
				.andExpect(jsonPath("$.content[2].matobjektNamn", equalTo("Mätobjekt2")))
				.andExpect(jsonPath("$.content[3].matobjektNamn", equalTo("Mätobjekt1")))
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(4)))
				.andExpect(jsonPath("numberOfElements", equalTo(4)));

		requests.addMatningar(NEDMATNING, NEDMATNING, LocalDateTime.now().minusDays(1), 1, 3.0);
		requests.addMatningar(PORTTRYCK, PORTTRYCK, LocalDateTime.now().minusDays(2), 1, 3.0);
		requests.addMatningar(SATTNING, SATTNING, LocalDateTime.now().minusDays(3), 1, 3.0);
		requests.addMatningar(FLODE, FLODE, LocalDateTime.now().minusDays(4), 1, 3.0);


		mvc.perform(post("/api/paminnelser?page=0&size=10&sortProperty=avlastDatum&sortDirection=desc")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"matansvarigAnvandargruppIds\": [1]}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].matobjektNamn", equalTo("Mätobjekt1")))
				.andExpect(jsonPath("$.content[1].matobjektNamn", equalTo("Mätobjekt2")))
				.andExpect(jsonPath("$.content[2].matobjektNamn", equalTo("Mätobjekt3")))
				.andExpect(jsonPath("$.content[3].matobjektNamn", equalTo("Mätobjekt5")))
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(4)))
				.andExpect(jsonPath("numberOfElements", equalTo(4)));
	}
}
