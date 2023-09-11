package se.metria.matdatabas.restapi.user;

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
import se.metria.matdatabas.restapi.UserController;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.paminnelse.PaminnelseService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = UserController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class UserControllerIT {

	@TestConfiguration
	static class TestContextConfiguration {
	}

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MatdatabasUser matdatabasUser;

	@MockBean
	private LarmService larmService;

	@MockBean
	private MatobjektService matobjektService;

	@MockBean
	private PaminnelseService paminnelseService;

	@Test
	@WithMockUserMatrapportor
	void en_anvandare_ska_kunna_hamta_ut_antalet_larm() throws Exception {
		when(larmService.getLarmCountForAnvandare(any())).thenReturn(11);
		mvc.perform(get("/api/user/larm")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("count", equalTo(11)));
	}

	@Test
	void en_oinloggad_anvandare_ska_inte_kunna_hamta_ut_antalet_larm() throws Exception {
		mvc.perform(get("/api/user/larm")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUserMatrapportor
	void en_anvandare_ska_kunna_hamta_ut_antalet_ogranskade_mätvärden() throws Exception {
		when(matobjektService.getOgranskadeMatningarCountForAnvandare(any())).thenReturn(22);
		mvc.perform(get("/api/user/matvarden/ogranskade")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("count", equalTo(22)));
	}

	@Test
	void en_oinloggad_anvandare_ska_inte_kunna_hamta_ut_antalet_ogranskade_mätvärden() throws Exception {
		mvc.perform(get("/api/user/matvarden/ogranskade")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUserMatrapportor
	void en_anvandare_ska_kunna_hamta_ut_antalet_påminnelser() throws Exception {
		when(paminnelseService.getForsenadeMatningarCountForAnvandare(any())).thenReturn(33);
		mvc.perform(get("/api/user/paminnelser")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("count", equalTo(33)));
	}

	@Test
	void en_oinloggad_anvandare_ska_inte_kunna_hamta_ut_antalet_påminnelser() throws Exception {
		mvc.perform(get("/api/user/paminnelser")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
}
