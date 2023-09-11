package se.metria.matdatabas.restapi.anvandare;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.anvandare.dto.Anvandare;
import se.metria.matdatabas.service.anvandare.dto.EditAnvandare;
import se.metria.matdatabas.service.anvandargrupp.dto.Anvandargrupp;
import se.metria.matdatabas.service.anvandare.exception.*;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = AnvandareController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class AnvandareControllerIT {

	private Anvandare anvandare;
	private Page<Anvandare> anvandarePage;
	private EditAnvandareDto editAnvandareDto;

	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.createTypeMap(Anvandare.class, AnvandareDto.class);
			modelMapper.createTypeMap(Anvandargrupp.class, AnvandargruppDto.class);
			modelMapper.createTypeMap(PageImpl.class, AnvandarePageDto.class);
			modelMapper.createTypeMap(EditAnvandareDto.class, EditAnvandare.class);
			modelMapper.validate();
			return modelMapper;
		}
	}

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AnvandareService anvandareService;

	@BeforeEach
	void setUp() throws AnvandareNotFoundException {
		anvandare = Anvandare.builder()
				.id(0)
				.namn("namn")
				.foretag("foretag")
				.aktiv(true)
				.inloggningsnamn("inloggningsnamn")
				.behorighet((short) 1)
				.defaultKartlagerId(3)
				.skickaEpost(true)
				.epost("epost")
				.build();

		editAnvandareDto = new EditAnvandareDto()
				.namn("test")
				.inloggningsnamn("test")
				.aktiv(false)
				.behorighet(2)
				.skickaEpost(false)
				.anvandargrupper(List.of(1,2));

		anvandarePage = new PageImpl<>(List.of(anvandare, anvandare, anvandare));

		when(anvandareService.getAnvandare(eq(0))).thenReturn(anvandare);
		when(anvandareService.getAnvandare(eq(1))).thenThrow(new AnvandareNotFoundException());
		when(anvandareService.updateAnvandare(eq(0), any())).thenReturn(anvandare);
		when(anvandareService.updateAnvandare(eq(1), any())).thenThrow(new AnvandareNotFoundException());
		when(anvandareService.getAnvandare(any(), any(), any(), any(), any())).thenReturn(anvandarePage);
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_hämta_en_användare() throws Exception {
		mvc.perform(get("/api/anvandare/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(anvandare.getId())))
				.andExpect(jsonPath("namn", equalTo(anvandare.getNamn())))
				.andExpect(jsonPath("foretag", equalTo(anvandare.getForetag())))
				.andExpect(jsonPath("aktiv", equalTo(anvandare.getAktiv())))
				.andExpect(jsonPath("inloggningsnamn", equalTo(anvandare.getInloggningsnamn())))
				.andExpect(jsonPath("behorighet", equalTo(anvandare.getBehorighet().intValue())))
				.andExpect(jsonPath("defaultKartlagerId", equalTo(anvandare.getDefaultKartlagerId())))
				.andExpect(jsonPath("skickaEpost", equalTo(anvandare.getSkickaEpost())))
				.andExpect(jsonPath("epost", equalTo(anvandare.getEpost())));
	}

	@Test
	@WithMockUserAdministrator
	void en_icke_existerande_användare_ger_404() throws Exception {
		mvc.perform(get("/api/anvandare/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_hämta_en_användare() throws Exception {
		mvc.perform(get("/api/anvandare/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());

	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_uppdatera_en_existerande_användare() throws Exception {
		mvc.perform(put("/api/anvandare/0")
				.content(mapper.writeValueAsString(editAnvandareDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserAdministrator
	void utan_mandatory_parameter_returneras_400() throws Exception {
		editAnvandareDto.inloggningsnamn(null);

		mvc.perform(put("/api/anvandare/0")
				.content(mapper.writeValueAsString(editAnvandareDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUserAdministrator
	void när_en_icke_existerande_användare_uppdateras_returneras_404() throws Exception {
		mvc.perform(put("/api/anvandare/1")
				.content(mapper.writeValueAsString(editAnvandareDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_uppdatera_en_användare() throws Exception {
		mvc.perform(put("/api/anvandare/0")
				.content(mapper.writeValueAsString(editAnvandareDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_skapa_en_användare() throws Exception {
		mvc.perform(post("/api/anvandare")
				.content(mapper.writeValueAsString(editAnvandareDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_hämta_en_sida_med_användare() throws Exception {
		mvc.perform(get("/api/anvandare?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(3)))
				.andExpect(jsonPath("numberOfElements", equalTo(3)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_hämta_en_sida_med_användare() throws Exception {
		mvc.perform(get("/api/anvandare?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_ta_bort_en_oinloggad_användare() throws Exception {
		doNothing().when(anvandareService).deleteAnvandare(eq(0));
		mvc.perform(delete("/api/anvandare/0")
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_inte_kunna_ta_bort_en_inloggad_användare() throws Exception {
		doThrow(new AnvandareHasLoggedInException()).when(anvandareService).deleteAnvandare(eq(0));
		mvc.perform(delete("/api/anvandare/0")
				.with(csrf()))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_få_404_om_en_användaren_inte_finns() throws Exception {
		doThrow(new AnvandareNotFoundException()).when(anvandareService).deleteAnvandare(eq(123));
		mvc.perform(delete("/api/anvandare/123")
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_ta_bort_användare() throws Exception {
		mvc.perform(delete("/api/anvandare/0")
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_anonymisera_en_användare() throws Exception {
		when(anvandareService.anonymizeAnvandare(eq(0))).thenReturn(anvandare);
		mvc.perform(put("/api/anvandare/0/anonymisera")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(anvandare.getId())));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_anonymisera_en_användare() throws Exception {
		mvc.perform(put("/api/anvandare/0/anonymisera")
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_inte_kunna_anonymisera_en_användare_som_inte_finns() throws Exception {
		when(anvandareService.anonymizeAnvandare(eq(1))).thenThrow(new AnvandareNotFoundException());
		mvc.perform(put("/api/anvandare/1/anonymisera")
				.with(csrf()))
				.andExpect(status().isNotFound());
	}
}
