package se.metria.matdatabas.restapi.matobjektgrupp;

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
import se.metria.matdatabas.openapi.model.EditMatobjektgruppDto;
import se.metria.matdatabas.openapi.model.ListMatobjektgruppDto;
import se.metria.matdatabas.openapi.model.MatobjektgruppDto;
import se.metria.matdatabas.openapi.model.MatobjektgruppPageDto;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.matobjektgrupp.MatobjektgruppService;
import se.metria.matdatabas.service.matobjektgrupp.dto.EditMatobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.dto.ListMatobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.dto.Matobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.exception.MatobjektgruppConflictException;
import se.metria.matdatabas.service.matobjektgrupp.exception.MatobjektgruppNotFoundException;

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
		controllers = MatobjektgruppController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class MatobjektgruppControllerIT {

	private final Matobjektgrupp matobjektgrupp = Matobjektgrupp.builder()
			.id(1)
			.kategori((short)0)
			.namn("namn")
			.kartsymbol((short)0)
			.beskrivning("beskrivning")
			.matobjekt(List.of(1,2,3))
			.build();
	private final EditMatobjektgrupp editMatobjektgrupp = EditMatobjektgrupp.builder()
			.kategori((short)1)
			.namn("nytt namn")
			.kartsymbol((short)2)
			.beskrivning("ny beskrivning")
			.matobjekt(List.of(1,5))
			.build();

	private Matobjektgrupp updatedMatobjektgrupp;

	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.createTypeMap(ListMatobjektgrupp.class, ListMatobjektgruppDto.class);
			modelMapper.createTypeMap(Matobjektgrupp.class, MatobjektgruppDto.class);
			modelMapper.createTypeMap(EditMatobjektgrupp.class, EditMatobjektgruppDto.class);
			modelMapper.createTypeMap(PageImpl.class, MatobjektgruppPageDto.class);
			modelMapper.validate();
			return modelMapper;
		}
	}

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MatobjektgruppService matobjektgruppService;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_hämta_en_sida_med_matobjektgrupper() throws Exception {
		ListMatobjektgrupp listMatobjektgrupp = ListMatobjektgrupp.builder()
				.id(1)
				.kategori((short)0)
				.namn("namn")
				.beskrivning("beskrivning")
				.antalMatobjekt(22)
				.build();

		Page<ListMatobjektgrupp> matobjektgruppPage = new PageImpl<>(List.of(listMatobjektgrupp, listMatobjektgrupp, listMatobjektgrupp));

		when(matobjektgruppService.getMatobjektgrupper(any(), any(), any(), any(), any())).thenReturn(matobjektgruppPage);

		mvc.perform(get("/api/matobjektgrupp?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(3)))
				.andExpect(jsonPath("numberOfElements", equalTo(3)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_sida_med_matobjektgrupper() throws Exception {
		ListMatobjektgrupp listMatobjektgrupp = ListMatobjektgrupp.builder()
				.id(1)
				.kategori((short)0)
				.namn("namn")
				.beskrivning("beskrivning")
				.antalMatobjekt(22)
				.build();

		Page<ListMatobjektgrupp> matobjektgruppPage = new PageImpl<>(List.of(listMatobjektgrupp, listMatobjektgrupp, listMatobjektgrupp));

		when(matobjektgruppService.getMatobjektgrupper(any(), any(), any(), any(), any())).thenReturn(matobjektgruppPage);

		mvc.perform(get("/api/matobjektgrupp?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_hämta_en_mätobjektgrupp() throws Exception {
		when(matobjektgruppService.getMatobjektgrupp(eq(0))).thenReturn(matobjektgrupp);
		mvc.perform(get("/api/matobjektgrupp/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(matobjektgrupp.getId())))
				.andExpect(jsonPath("kategori", equalTo(matobjektgrupp.getKategori().intValue())))
				.andExpect(jsonPath("namn", equalTo(matobjektgrupp.getNamn())))
				.andExpect(jsonPath("kartsymbol", equalTo(matobjektgrupp.getKartsymbol().intValue())))
				.andExpect(jsonPath("beskrivning", equalTo(matobjektgrupp.getBeskrivning())));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_en_icke_existerande_mätobjektgrupp_ger_404() throws Exception {
		when(matobjektgruppService.getMatobjektgrupp(eq(0))).thenThrow(new MatobjektgruppNotFoundException());
		mvc.perform(get("/api/matobjektgrupp/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_mätobjektgrupp() throws Exception {
		when(matobjektgruppService.getMatobjektgrupp(eq(0))).thenReturn(matobjektgrupp);
		mvc.perform(get("/api/matobjektgrupp/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_skapa_en_mätobjektgrupp() throws Exception {
		when(matobjektgruppService.createMatobjektgrupp(editMatobjektgrupp)).thenReturn(matobjektgrupp);
		mvc.perform(post("/api/matobjektgrupp")
				.content(mapper.writeValueAsString(editMatobjektgrupp))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id", equalTo(matobjektgrupp.getId())))
				.andExpect(jsonPath("kategori", equalTo(matobjektgrupp.getKategori().intValue())))
				.andExpect(jsonPath("namn", equalTo(matobjektgrupp.getNamn())))
				.andExpect(jsonPath("kartsymbol", equalTo(matobjektgrupp.getKartsymbol().intValue())))
				.andExpect(jsonPath("beskrivning", equalTo(matobjektgrupp.getBeskrivning())));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_inte_kunna_skapa_en_mätobjektgrupp_med_existerande_namn() throws Exception {
		when(matobjektgruppService.createMatobjektgrupp(any())).thenThrow(new MatobjektgruppConflictException());
		mvc.perform(post("/api/matobjektgrupp")
				.content(mapper.writeValueAsString(editMatobjektgrupp))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isConflict());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_skapa_en_mätobjektgrupp() throws Exception {
		mvc.perform(post("/api/matobjektgrupp")
				.content(mapper.writeValueAsString(editMatobjektgrupp))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_uppdatera_en_existerande_mätobjektgrupp() throws Exception {
		when(matobjektgruppService.updateMatobjektgrupp(eq(0), any())).thenReturn(matobjektgrupp);
		mvc.perform(put("/api/matobjektgrupp/0")
				.content(mapper.writeValueAsString(editMatobjektgrupp))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(matobjektgrupp.getId())))
				.andExpect(jsonPath("kategori", equalTo(matobjektgrupp.getKategori().intValue())))
				.andExpect(jsonPath("namn", equalTo(matobjektgrupp.getNamn())))
				.andExpect(jsonPath("kartsymbol", equalTo(matobjektgrupp.getKartsymbol().intValue())))
				.andExpect(jsonPath("beskrivning", equalTo(matobjektgrupp.getBeskrivning())));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void uppdatering_utan_mandatory_parameter_returnerar_400() throws Exception {
		EditMatobjektgrupp editMatobjektgrupp = EditMatobjektgrupp.builder()
				.kategori((short)0)
				.namn(null)
				.kartsymbol((short)0)
				.beskrivning("beskrivning")
				.matobjekt(List.of(1,2))
				.build();

		mvc.perform(put("/api/matobjektgrupp/0")
				.content(mapper.writeValueAsString(editMatobjektgrupp))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_ett_icke_existerande_mätobjektgrupp_uppdateras_returneras_404() throws Exception {
		when(matobjektgruppService.updateMatobjektgrupp(eq(0), any())).thenThrow(new MatobjektgruppNotFoundException());
		mvc.perform(put("/api/matobjektgrupp/0")
				.content(mapper.writeValueAsString(editMatobjektgrupp))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_uppdatera_en_mätobjektgrupp() throws Exception {
		mvc.perform(put("/api/matobjektgrupp/0")
				.content(mapper.writeValueAsString(editMatobjektgrupp))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_ta_bort_en_mätobjektgrupp() throws Exception {
		doNothing().when(matobjektgruppService).deleteMatobjektgrupp(eq(0));
		mvc.perform(delete("/api/matobjektgrupp/0")
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_få_404_vid_delete_om_en_mätobjektgrupp_inte_finns() throws Exception {
		doThrow(new MatobjektgruppNotFoundException()).when(matobjektgruppService).deleteMatobjektgrupp(eq(0));
		mvc.perform(delete("/api/matobjektgrupp/0")
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_ta_bort_en_mätobjektgrupp() throws Exception {
		mvc.perform(delete("/api/matobjektgrupp/0")
				.with(csrf()))
				.andExpect(status().isForbidden());
	}
}
