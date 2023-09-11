package se.metria.matdatabas.restapi.matrunda;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matrunda.MatrundaRapporteringService;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matrunda.exception.MatrundaConflictException;
import se.metria.matdatabas.service.matrunda.MatrundaService;
import se.metria.matdatabas.service.matrunda.dto.EditMatrunda;
import se.metria.matdatabas.service.matrunda.dto.ListMatrunda;
import se.metria.matdatabas.service.matrunda.dto.Matrunda;
import se.metria.matdatabas.service.matrunda.dto.MatrundaMatningstyp;
import se.metria.matdatabas.service.matrunda.exception.MatrundaNotFoundException;

@DisplayName("Givet mätrundor API med mätrundor")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = MatrundaController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MockWebSecurityConfig.class})
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class MatrundaControllerIT {

	private List<MatrundaMatningstyp> matrundaMatningstyper = List.of(
			MatrundaMatningstyp.builder().matningstypId(1).ordning((short)1).build(),
			MatrundaMatningstyp.builder().matningstypId(2).ordning((short)2).build(),
			MatrundaMatningstyp.builder().matningstypId(3).ordning((short)3).build());

	private final Matrunda matrunda =  Matrunda.builder()
			.id(1)
			.namn("namn")
			.aktiv(true)
			.beskrivning("beskrivning")
			.matningstyper(matrundaMatningstyper)
			.build();

	private final EditMatrunda editMatrunda = EditMatrunda.builder()
			.namn("nytt namn")
			.aktiv(false)
			.beskrivning("ny beskrivning")
			.matningstyper(matrundaMatningstyper)
			.build();

	private Matrunda updatedMatrunda;

	@Autowired
	private ObjectMapper mapper;

	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.createTypeMap(ListMatrunda.class, ListMatrundaDto.class);
			modelMapper.createTypeMap(PageImpl.class, MatrundaPageDto.class);
			modelMapper.createTypeMap(Matrunda.class, MatrundaDto.class);
			modelMapper.createTypeMap(MatrundaMatningstyp.class, MatrundaMatningstypDto.class);
			modelMapper.createTypeMap(EditMatrunda.class, EditMatrundaDto.class);
			modelMapper.validate();
			return modelMapper;
		}
	}

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MatrundaService matrundaService;

	@MockBean
	private MatningstypService matningstypService;

	@MockBean
	private MatrundaRapporteringService matrundaRapporteringService;

	@MockBean
	private MatningService matningService;

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_sida_med_mätrundor() throws Exception {
		var listMatrunda = ListMatrunda.builder()
				.id(1)
				.beskrivning("beskrivning")
				.namn("namn")
				.aktiv(true)
				.antalMatobjekt(42)
				.build();
		var matrundaPage = new PageImpl<>(List.of(listMatrunda));

		when(matrundaService.getMatrundor(any(), any(), any(), any(), anyBoolean())).thenReturn(matrundaPage);

		mvc.perform(get("/api/matrunda?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(1)))
				.andExpect(jsonPath("numberOfElements", equalTo(1)));
	}
	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_hämta_en_mätrunda() throws Exception {
		when(matrundaService.getMatrunda(eq(0))).thenReturn(matrunda);
		mvc.perform(get("/api/matrunda/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(matrunda.getId())))
				.andExpect(jsonPath("namn", equalTo(matrunda.getNamn())))
				.andExpect(jsonPath("aktiv", equalTo(matrunda.getAktiv())))
				.andExpect(jsonPath("beskrivning", equalTo(matrunda.getBeskrivning())))
				.andExpect(jsonPath("matningstyper.length()", equalTo(3)));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_en_icke_existerande_mätrunda_ger_404() throws Exception {
		when(matrundaService.getMatrunda(eq(0))).thenThrow(new MatrundaNotFoundException());
		mvc.perform(get("/api/matrunda/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_mätrunda() throws Exception {
		when(matrundaService.getMatrunda(eq(0))).thenReturn(matrunda);
		mvc.perform(get("/api/matrunda/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_skapa_en_mätrunda() throws Exception {
		when(matrundaService.createMatrunda(editMatrunda)).thenReturn(matrunda);
		mvc.perform(post("/api/matrunda")
				.content(mapper.writeValueAsString(editMatrunda))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id", equalTo(matrunda.getId())))
				.andExpect(jsonPath("namn", equalTo(matrunda.getNamn())))
				.andExpect(jsonPath("aktiv", equalTo(matrunda.getAktiv())))
				.andExpect(jsonPath("beskrivning", equalTo(matrunda.getBeskrivning())))
				.andExpect(jsonPath("matningstyper.length()", equalTo(3)));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_inte_kunna_skapa_en_mätrunda_med_existerande_namn() throws Exception {
		when(matrundaService.createMatrunda(any())).thenThrow(new MatrundaConflictException(editMatrunda.getNamn() + " already exists."));
		mvc.perform(post("/api/matrunda")
				.content(mapper.writeValueAsString(editMatrunda))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isConflict());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_skapa_en_mätrunda() throws Exception {
		mvc.perform(post("/api/matrunda")
				.content(mapper.writeValueAsString(editMatrunda))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_uppdatera_en_existerande_mätrunda() throws Exception {
		when(matrundaService.updateMatrunda(eq(0), any())).thenReturn(matrunda);
		mvc.perform(put("/api/matrunda/0")
				.content(mapper.writeValueAsString(editMatrunda))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(matrunda.getId())))
				.andExpect(jsonPath("namn", equalTo(matrunda.getNamn())))
				.andExpect(jsonPath("aktiv", equalTo(matrunda.getAktiv())))
				.andExpect(jsonPath("beskrivning", equalTo(matrunda.getBeskrivning())))
				.andExpect(jsonPath("matningstyper.length()", equalTo(3)));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void uppdatering_utan_mandatory_parameter_returnerar_400() throws Exception {
		EditMatrunda editMatrunda = EditMatrunda.builder()
				.namn(null)
				.aktiv(false)
				.beskrivning("ny beskrivning")
				.matningstyper(matrundaMatningstyper)
				.build();


		mvc.perform(put("/api/matrunda/0")
				.content(mapper.writeValueAsString(editMatrunda))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_ett_icke_existerande_mätrunda_uppdateras_returneras_404() throws Exception {
		when(matrundaService.updateMatrunda(eq(0), any())).thenThrow(new MatrundaNotFoundException());
		mvc.perform(put("/api/matrunda/0")
				.content(mapper.writeValueAsString(editMatrunda))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_uppdatera_en_mätrunda() throws Exception {
		mvc.perform(put("/api/matrunda/0")
				.content(mapper.writeValueAsString(editMatrunda))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_ta_bort_en_mätrunda() throws Exception {
		doNothing().when(matrundaService).deleteMatrunda(eq(0));
		mvc.perform(delete("/api/matrunda/0")
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_få_404_vid_delete_om_en_mätrunda_inte_finns() throws Exception {
		doThrow(new MatrundaNotFoundException()).when(matrundaService).deleteMatrunda(eq(0));
		mvc.perform(delete("/api/matrunda/0")
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_ta_bort_en_mätrunda() throws Exception {
		mvc.perform(delete("/api/matrunda/0")
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

}
