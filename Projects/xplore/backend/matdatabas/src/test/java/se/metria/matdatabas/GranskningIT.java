package se.metria.matdatabas;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import se.metria.matdatabas.openapi.model.MatningDataSeriesDto;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Import(TestConfig.class)
public class GranskningIT {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private Requests requests;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	MatdatabasUser user;

	@BeforeAll
	static void setup(@Autowired DataSource dataSource, @Autowired Requests requests) throws Exception {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_matningstyper.sql"));
		}

		requests.addMatningar(3, 3, LocalDateTime.now().minusYears(5), 10, 2.0);
		requests.addMatningar(3, 3, LocalDateTime.now(), 10, 3.0);
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämtning_av_komplett_dataserie() throws Exception {
		Integer matningstypId = 3;
		mvc.perform(get("/api/matningstyper/{matningstypId}/matningDataSeries", matningstypId)
				.with(csrf()))
				.andExpect(status().isOk())
				// The test data is arranged such that the following two IDs are the same.
				.andExpect(jsonPath("matobjektId", equalTo(matningstypId)))
				.andExpect(jsonPath("matningstypId", equalTo(matningstypId)))
				.andExpect(jsonPath("data", hasSize(equalTo(20))));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämtning_av_dataserie_med_startdatum() throws Exception {
		Integer matningstypId = 3;
		LocalDate date = LocalDateTime.now().minusYears(3).toLocalDate();
		mvc.perform(get("/api/matningstyper/{matningstypId}/matningDataSeries?fromDatum={date}", matningstypId, date)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("data", hasSize(equalTo(10))));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämtning_av_dataserie_med_startdatum_och_referensdatum_på_sättning() throws Exception {
		// Testa sättning med referensdatum innan dataserien vi hämtar börjar.

		Integer matningstypId = 3;
		LocalDate date = LocalDateTime.now().minusYears(3).toLocalDate();
		LocalDate referensDate = LocalDateTime.now().minusYears(4).toLocalDate();
		mvc.perform(get("/api/matningstyper/{matningstypId}/matningDataSeries?fromDatum={date}&sattningReferensdatum={referensDate}", matningstypId, date, referensDate)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("data", hasSize(equalTo(10))))
				.andExpect(jsonPath("data[0].varde", equalTo(1.0)));
	}


	@Test
	@WithMockUserTillstandshandlaggare
	void hämtning_av_dataserier_med_bara_godkända_mätvärden() throws Exception {
		Integer matningstypId = 3;
		LocalDate date = LocalDateTime.now().minusYears(3).toLocalDate();
		mvc.perform(get("/api/matningstyper/{matningstypId}/matningDataSeries?fromDatum={date}&filterGodkanda=true", matningstypId, date)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("data", hasSize(equalTo(0))));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	@Disabled("Takes a long time to complete...")
	void godkännande_av_många_värden() throws Exception {
		Integer matningstypId = 1;
		requests.addMatningar(1, 1, LocalDateTime.now().minusYears(2), 5000, 3.0);
		String contentAsString = mvc.perform(get("/api/matningstyper/{matningstypId}/matningDataSeries", matningstypId)
				.with(csrf()))
				.andExpect(status().isOk())
				// The test data is arranged such that the following two IDs are the same.
				.andExpect(jsonPath("matobjektId", equalTo(matningstypId)))
				.andExpect(jsonPath("matningstypId", equalTo(matningstypId)))
				.andExpect(jsonPath("data", hasSize(equalTo(5000))))
				.andReturn().getResponse().getContentAsString();

		MatningDataSeriesDto series = mapper.readValue(contentAsString, MatningDataSeriesDto.class);
		List<String> ids = series.getData().stream().map(d -> d.getId().toString()).collect(Collectors.toList());
		mvc.perform(post("/api/matningar/godkann")
				.content("id=" + String.join(",", ids))
				.contentType("application/x-www-form-urlencoded")
				.with(csrf()))
				.andExpect(status().isNoContent());
	}
}
