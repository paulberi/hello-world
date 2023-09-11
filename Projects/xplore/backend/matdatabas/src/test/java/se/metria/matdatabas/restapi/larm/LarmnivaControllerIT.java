package se.metria.matdatabas.restapi.larm;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import se.metria.matdatabas.openapi.model.LarmnivaDto;
import se.metria.matdatabas.openapi.model.SaveLarmnivaDto;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.dto.Larmniva;
import se.metria.matdatabas.service.larm.exception.LarmnivaNamnConflictException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = LarmnivaController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class LarmnivaControllerIT {

	private Larmniva larmniva0;
	private Larmniva larmniva1;
	private Larmniva larmniva2;
	private Larmniva larmniva3;
	private SaveLarmnivaDto saveLarmnivaDto;
	private SaveLarmnivaDto saveLarmnivaDtoFail;

	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.createTypeMap(Larmniva.class, LarmnivaDto.class);
			modelMapper.validate();
			return modelMapper;
		}
	}

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private LarmService larmService;

	@MockBean
	private GransvardeService gransvardeService;

	@BeforeEach
	void setUp() {
		larmniva0 = Larmniva.builder()
				.id(0)
				.namn("Varningsnivå")
				.beskrivning("Gränsvärde för varningsnivå")
				.build();
		larmniva1 = Larmniva.builder()
				.id(0)
				.namn("Åtgärdsnivå 1")
				.beskrivning("Gränsvärde för Åtgärdsnivå 1")
				.build();
		larmniva2 = Larmniva.builder()
				.id(0)
				.namn("Åtgärdsnivå 2")
				.beskrivning("Gränsvärde för Åtgärdsnivå 2")
				.build();
		larmniva3 = Larmniva.builder()
				.id(0)
				.namn("Annan åtgärd")
				.beskrivning("Gränsvärde för annan åtgärd")
				.build();

		saveLarmnivaDto = new SaveLarmnivaDto()
				.beskrivning("Testar")
				.namn("test");
		saveLarmnivaDtoFail = new SaveLarmnivaDto()
				.beskrivning("Varningsnivå")
				.namn("testar fail");
	}


	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandlaggare_ska_kunna_hämta_en_lista_med_larmnivåer() throws Exception {
		List<Larmniva> larmniva = new ArrayList<>();
		larmniva.add(larmniva0);
		larmniva.add(larmniva1);
		larmniva.add(larmniva2);
		larmniva.add(larmniva3);

		when(larmService.getLarmnivaer()).thenReturn(larmniva);
		mvc.perform(get("/api/larmniva")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_lista_med_larmnivåer() throws Exception {
		List<Larmniva> larmniva = new ArrayList<>();
		larmniva.add(larmniva0);
		larmniva.add(larmniva1);
		larmniva.add(larmniva2);
		larmniva.add(larmniva3);

		when(larmService.getLarmnivaer()).thenReturn(larmniva);
		mvc.perform(get("/api/larmniva")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_uppdatera_en_larmnivå() throws Exception {
		mvc.perform(put("/api/larmniva/0")
				.content(mapper.writeValueAsString(saveLarmnivaDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_skapa_en_larmnivå() throws Exception {
		mvc.perform(post("/api/larmniva")
				.content(mapper.writeValueAsString(saveLarmnivaDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_inte_kunna_uppdatera_en_larmnivå() throws Exception {
		mvc.perform(put("/api/larmniva/0")
				.content(mapper.writeValueAsString(saveLarmnivaDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_inte_kunna_skapa_en_larmnivå() throws Exception {
		mvc.perform(post("/api/larmniva")
				.content(mapper.writeValueAsString(saveLarmnivaDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_uppdatera_en_existerande_användare() throws Exception {
		mvc.perform(put("/api/larmniva/0")
				.content(mapper.writeValueAsString(saveLarmnivaDto))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUserAdministrator
	void en_administrör_ska_inte_kunna_skapa_en_larmnivå_med_existerande_namn() throws Exception {
		when(larmService.createLarmniva(any())).thenThrow(new LarmnivaNamnConflictException());
		mvc.perform(post("/api/larmniva")
				.content(mapper.writeValueAsString(saveLarmnivaDtoFail))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isConflict());
	}
}
