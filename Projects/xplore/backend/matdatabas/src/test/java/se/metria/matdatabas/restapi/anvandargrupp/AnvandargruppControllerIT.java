package se.metria.matdatabas.restapi.anvandargrupp;

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
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.anvandargrupp.dto.Anvandargrupp;
import se.metria.matdatabas.service.anvandargrupp.dto.EditAnvandargrupp;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.dto.Larm;
import se.metria.matdatabas.service.matningstyp.MatningstypService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = AnvandargruppController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class AnvandargruppControllerIT {

	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.createTypeMap(Anvandargrupp.class, AnvandargruppDto.class);
			modelMapper.createTypeMap(PageImpl.class, AnvandargruppPageDto.class);
			modelMapper.createTypeMap(EditAnvandargruppDto.class, EditAnvandargrupp.class);
			modelMapper.createTypeMap(PageImpl.class, MatansvarPageDto.class);
			modelMapper.createTypeMap(PageImpl.class, LarmPageDto.class);
			modelMapper.createTypeMap(PageImpl.class, AnvandarePageDto.class);
			modelMapper.validate();
			return modelMapper;
		}
	}

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AnvandargruppService anvandargruppService;

	@MockBean
	private MatningstypService matningstypService;

	@MockBean
	private LarmService larmService;

	@MockBean
	private AnvandareService anvandareService;

	@BeforeEach
	void setUp() {
		Anvandargrupp existing = Anvandargrupp.builder()
				.id(0)
				.namn("grupp")
				.beskrivning("beskrivning")
				.antalAnvandare(0)
				.build();

		when(anvandargruppService.getAnvandargrupp(eq(0))).thenReturn(Optional.of(existing));
		when(anvandargruppService.editAnvandargrupp(eq(0), any())).thenReturn(Optional.of(existing));
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_hämta_en_grupp() throws Exception {
		mvc.perform(get("/api/anvandargrupp/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(0)))
				.andExpect(jsonPath("namn", equalTo("grupp")))
				.andExpect(jsonPath("beskrivning", equalTo("beskrivning")))
				.andExpect(jsonPath("antalAnvandare", equalTo(0)));
	}

	@Test
	@WithMockUserAdministrator
	void en_icke_existerande_grupp_ger_404() throws Exception {
		mvc.perform(get("/api/anvandargrupp/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_hämta_en_grupp() throws Exception {
		mvc.perform(get("/api/anvandargrupp/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void en_administratör_ska_kunna_uppdatera_en_existerande_grupp() throws Exception {
		var edit = new EditAnvandargruppDto().namn("test").beskrivning("beskrivning");
		mvc.perform(put("/api/anvandargrupp/0")
				.content(mapper.writeValueAsString(edit))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserAdministrator
	void utan_namn_och_beskrivning_returneras_400() throws Exception {
		var edit = new EditAnvandargruppDto();
		mvc.perform(put("/api/anvandargrupp/0")
				.content(mapper.writeValueAsString(edit))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUserAdministrator
	void när_en_icke_existerande_grupp_uppdateras_returneras_404() throws Exception {
		var edit = new EditAnvandargruppDto().namn("test").beskrivning("beskrivning");
		mvc.perform(put("/api/anvandargrupp/1")
				.content(mapper.writeValueAsString(edit))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_ändra_en_grupp() throws Exception {
		var edit = new EditAnvandargruppDto().namn("test").beskrivning("beskrivning");
		mvc.perform(put("/api/anvandargrupp/0")
				.content(mapper.writeValueAsString(edit))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_skapa_en_grupp() throws Exception {
		var edit = new EditAnvandargruppDto().namn("test").beskrivning("beskrivning");
		mvc.perform(post("/api/anvandargrupp")
				.content(mapper.writeValueAsString(edit))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportor_ska_inte_kunna_hämta_larm_för_en_grupp() throws Exception {
		when(larmService.getLarmForAnvandargrupp(any(), any(), any(), any(), any())).thenReturn(Page.empty());
		mvc.perform(get("/api/anvandargrupp/1/larm"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void en_administrator_ska_kunna_hämta_larm_för_en_grupp() throws Exception {
		List<Larm> larm = List.of((Larm) new Larm()
				.matobjektNamn("aaa")
				.matningstypNamn("bbb")
				.matobjektFastighet("ccc")
				.larmnivaId(1)
				.gransvarde(12d));
		when(larmService.getLarmForAnvandargrupp(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(larm));
		mvc.perform(get("/api/anvandargrupp/1/larm"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(1)))
				.andExpect(jsonPath("numberOfElements", equalTo(1)))
				.andExpect(jsonPath("content", hasSize(1)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportor_ska_inte_kunna_hämta_anvandare_för_en_grupp() throws Exception {
		when(anvandareService.getAnvandareForAnvandargrupp(any(), any(), any(), any(), any())).thenReturn(Page.empty());
		mvc.perform(get("/api/anvandargrupp/1/anvandare"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void en_administrator_ska_kunna_hämta_anvandare_för_en_grupp() throws Exception {
		List<Anvandare> anvandare = List.of(Anvandare.builder()
				.id(1)
				.build());
		when(anvandareService.getAnvandareForAnvandargrupp(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(anvandare));
		mvc.perform(get("/api/anvandargrupp/1/anvandare"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(1)))
				.andExpect(jsonPath("numberOfElements", equalTo(1)))
				.andExpect(jsonPath("content", hasSize(1)));
	}
}
