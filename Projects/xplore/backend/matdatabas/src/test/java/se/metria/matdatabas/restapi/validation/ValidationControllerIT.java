package se.metria.matdatabas.restapi.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.analys.AnalysService;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjektgrupp.MatobjektgruppService;
import se.metria.matdatabas.service.matrunda.MatrundaService;
import se.metria.matdatabas.service.rapport.RapportService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = ValidationController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class ValidationControllerIT {

	@TestConfiguration
	static class TestContextConfiguration {
	}

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AnvandareService anvandareService;

	@MockBean
	private AnvandargruppService anvandargruppService;

	@MockBean
	private MatobjektService matobjektService;

	@MockBean
	private MatobjektgruppService matobjektgruppService;

	@MockBean
	private RapportService rapportService;

	@MockBean
	private DefinitionMatningstypService definitionMatningstypService;

	@MockBean
	private MatrundaService matrundaService;

	@MockBean
	private AnalysService analysService;

	@BeforeEach
	void setUp() {
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_kontrollera_tillgängliga_användarnamn() throws Exception {
		when(anvandareService.exists(eq("foo"))).thenReturn(false);
		mvc.perform(get("/api/validation/anvandare/exists?anvandarnamn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("result", equalTo(false)));
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_kontrollera_upptagna_användarnamn() throws Exception {
		when(anvandareService.exists(eq("foo"))).thenReturn(true);
		mvc.perform(get("/api/validation/anvandare/exists?anvandarnamn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("result", equalTo(true)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_kontrollera_om_användarnamn_är_tillgängligt() throws Exception {
		mvc.perform(get("/api/validation/anvandare/exists?anvandarnamn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void en_administrator_ska_kunna_kontrollera_om_användargrupp_existerar() throws Exception {
		when(anvandargruppService.exists("foo")).thenReturn(true);
		mvc.perform(get("/api/validation/anvandargrupp/exists?namn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("result", equalTo(true)));

		mvc.perform(get("/api/validation/anvandargrupp/exists?namn=bar")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("result", equalTo(false)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_kontrollera_om_användargrupp_existerar() throws Exception {
		mvc.perform(get("/api/validation/anvandargrupp/exists?namn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_kontrollera_tillgängliga_mätobjektnamn() throws Exception {
		when(matobjektService.exists(eq("foo"))).thenReturn(false);
		mvc.perform(get("/api/validation/matobjekt/exists?namn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("result", equalTo(false)));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_kontrollera_upptagna_mätobjektnamn() throws Exception {
		when(matobjektService.exists(eq("foo"))).thenReturn(true);
		mvc.perform(get("/api/validation/matobjekt/exists?namn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("result", equalTo(true)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_kontrollera_mätobjektnamn() throws Exception {
		mvc.perform(get("/api/validation/matobjekt/exists?namn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}


	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_kontrollera_tillgängliga_mätobjektgruppnamn() throws Exception {
		when(matobjektgruppService.exists(eq("foo"))).thenReturn(false);
		mvc.perform(get("/api/validation/matobjektgrupp/exists?namn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("result", equalTo(false)));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_kontrollera_upptagna_mätobjektgruppnamn() throws Exception {
		when(matobjektgruppService.exists(eq("foo"))).thenReturn(true);
		mvc.perform(get("/api/validation/matobjektgrupp/exists?namn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("result", equalTo(true)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_kontrollera_mätobjektgruppnamn() throws Exception {
		mvc.perform(get("/api/validation/matobjektgrupp/exists?namn=foo")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

}
