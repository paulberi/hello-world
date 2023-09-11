package se.metria.matdatabas;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integrationstester relaterade till export-funktionaliteten.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ExportIT {
	@Autowired
	private MockMvc mvc;

	@MockBean
	MatdatabasUser user;

	@BeforeAll
	static void setup(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_matningstyper.sql"));
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 5, 10})
	@WithMockUserTillstandshandlaggare
	void matobjektExportListing(int pageSize) throws Exception {
		mvc.perform(post("/api/export/matningstyper?size={pageSize}", pageSize)
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("numberOfElements", equalTo(pageSize)));
	}

	@Test
	void matobjektExportListing_not_authorized() throws Exception {
		mvc.perform(post("/api/export/matningstyper")
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void matobjektExport() throws Exception {
		mvc.perform(post("/api/export/export?rapportTyp=MATOBJEKT")
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/csv"));
	}

	@Test
	void matobjektExport_not_authorized() throws Exception {
		mvc.perform(post("/api/export/export?rapportTyp=MATOBJEKT")
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@ParameterizedTest
	@ValueSource(strings = {"MATDATA", "INLACKAGE", "VATTENKEMI"})
	@WithMockUserTillstandshandlaggare
	void matdataExport(String rapportTyp) throws Exception {
		mvc.perform(post("/api/export/export?rapportTyp={rapportTyp}", rapportTyp)
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/csv"));
	}
}
