package se.metria.matdatabas.restapi.meddelande;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import se.metria.matdatabas.openapi.model.EditMeddelandeDto;
import se.metria.matdatabas.openapi.model.MeddelandeDto;
import se.metria.matdatabas.openapi.model.MeddelandePageDto;
import se.metria.matdatabas.security.mock.*;
import se.metria.matdatabas.service.meddelande.MeddelandeService;
import se.metria.matdatabas.service.meddelande.dto.EditMeddelande;
import se.metria.matdatabas.service.meddelande.dto.Meddelande;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Givet meddelande API med fem meddelanden")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = MeddelandeController.class,
		includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
) 
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class MeddelandeControllerIT {

	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.createTypeMap(Meddelande.class, MeddelandeDto.class);
			modelMapper.createTypeMap(PageImpl.class, MeddelandePageDto.class);
			modelMapper.createTypeMap(EditMeddelandeDto.class, EditMeddelande.class);
			modelMapper.validate();
			return modelMapper;
		}
	}
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private MeddelandeService meddelandeService;

	@Autowired
	private ObjectMapper mapper;

	private Meddelande.MeddelandeBuilder meddelandeBuilder;
	private EditMeddelande.EditMeddelandeBuilder editMeddelandeBuilder;

	private static String RUBRIK = "TEST_RUBRIK";
	private static String MEDDELANDE = "TEST_MEDDELANDE";
	private static String URL = "http://TEST_URL";
	private static LocalDate NOW = LocalDate.now();

	@BeforeEach
	void setUp() {
		meddelandeBuilder = Meddelande.builder().id(42).datum(NOW).rubrik(RUBRIK).meddelande(MEDDELANDE).url(URL);
		editMeddelandeBuilder = EditMeddelande.builder().datum(NOW).rubrik(RUBRIK).meddelande(MEDDELANDE).url(URL);
    }

	@Test
	@WithMockUserMatrapportor
	void När_användare_hämtar_sida_med_meddelanden_skall_sidans_meddelanden_returneras() throws Exception {
		when(meddelandeService.getMeddelanden(anyInt(), anyInt())).thenAnswer(inv -> {
			List<Meddelande> meddelanden = Collections.nCopies(inv.getArgument(1), meddelandeBuilder.build());
			return new PageImpl<>(meddelanden);
		});
		mvc.perform(get("/api/meddelande?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(3)))
				.andExpect(jsonPath("numberOfElements", equalTo(3)))
				.andExpect(jsonPath("content", hasSize(3)))
				.andExpect(jsonPath("content[0].rubrik", equalTo(RUBRIK)))
				.andExpect(jsonPath("content[0].meddelande", equalTo(MEDDELANDE)))
				.andExpect(jsonPath("content[0].url", equalTo(URL)))
				.andExpect(jsonPath("content[0].datum", equalTo(NOW.format(DateTimeFormatter.ISO_LOCAL_DATE))));
	}
	
	@Test()
	@WithMockUserNoRoles
	void När_ej_behörig_användare_hämtar_meddelanden_skall_forbidden_returneras() throws Exception {		
		mvc.perform(get("/api/meddelande?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserMatrapportor
	void När_användare_hämtar_ett_meddelanden_skall_meddelandet_returneras() throws Exception {
		when(meddelandeService.getMeddelande(anyInt())).thenReturn(Optional.of(meddelandeBuilder.build()));
		mvc.perform(get("/api/meddelande/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("rubrik", equalTo(RUBRIK)))
				.andExpect(jsonPath("meddelande", equalTo(MEDDELANDE)))
				.andExpect(jsonPath("url", equalTo(URL)))
				.andExpect(jsonPath("datum", equalTo(NOW.format(DateTimeFormatter.ISO_LOCAL_DATE))));
	}

	@Test
	@WithMockUserNoRoles
	void När_ej_behörig_användare_hämtar_ett_meddelanden_skall_forbidden_returneras() throws Exception {
		mvc.perform(get("/api/meddelande/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void När_användare_skapar_ett_nytt_meddelanden_skall_meddelandet_skapas() throws Exception {
		when(meddelandeService.createMeddelande(any(EditMeddelande.class))).thenReturn(meddelandeBuilder.build());
		var meddelande = editMeddelandeBuilder.build();
		mvc.perform(post("/api/meddelande")
				.content(mapper.writeValueAsString(meddelande))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("rubrik", equalTo(RUBRIK)))
				.andExpect(jsonPath("meddelande", equalTo(MEDDELANDE)))
				.andExpect(jsonPath("url", equalTo(URL)))
				.andExpect(jsonPath("datum", equalTo(NOW.format(DateTimeFormatter.ISO_LOCAL_DATE))));
	}	

	@Test
	@WithMockUserMatrapportor
	void När_ej_behörig_användare_skapar_ett_nytt_meddelanden_skall_forbidden_returneras() throws Exception {
		var meddelande = meddelandeBuilder.build();
		mvc.perform(post("/api/meddelande")
				.content(mapper.writeValueAsString(meddelande))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}	

	@Test
	@WithMockUserAdministrator
	void När_användare_uppdaterar_ett_meddelanden_skall_meddelandet_uppdateras() throws Exception {
		when(meddelandeService.editMeddelande(eq(1), any(EditMeddelande.class))).thenReturn(Optional.of(meddelandeBuilder.build()));
		var meddelande = editMeddelandeBuilder.build();

		mvc.perform(put("/api/meddelande/1")
				.content(mapper.writeValueAsString(meddelande))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("rubrik", equalTo(RUBRIK)))
				.andExpect(jsonPath("meddelande", equalTo(MEDDELANDE)))
				.andExpect(jsonPath("url", equalTo(URL)))
				.andExpect(jsonPath("datum", equalTo(NOW.format(DateTimeFormatter.ISO_LOCAL_DATE))));
	}	

	@Test
	@WithMockUserAdministrator
	void När_användare_uppdaterar_ett_meddelanden_med_ogiltigt_id_skall_not_found_returneras() throws Exception {
		when(meddelandeService.editMeddelande(eq(2), any(EditMeddelande.class))).thenReturn(Optional.empty());
		var meddelande = editMeddelandeBuilder.build();
		mvc.perform(put("/api/meddelande/2")
				.content(mapper.writeValueAsString(meddelande))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void När_ej_behörig_användare_uppdaterar_ett_meddelanden_skall_forbidden_returneras() throws Exception {
		var meddelande = editMeddelandeBuilder.build();
		mvc.perform(put("/api/meddelande/1")
				.content(mapper.writeValueAsString(meddelande))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	void När_användare_tar_bort_ett_meddelanden_skall_meddelandet_tas_bort() throws Exception {
		when(meddelandeService.deleteMeddelande(eq(1))).thenReturn(true);
		mvc.perform(delete("/api/meddelande/1")
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUserAdministrator
	void När_användare_försöker_ta_bort_meddelande_med_ogiltigt_id_skall_not_found_returneras() throws Exception {
		when(meddelandeService.editMeddelande(eq(2), any(EditMeddelande.class))).thenReturn(Optional.empty());
		mvc.perform(delete("/api/meddelande/2")
				.with(csrf()))
				.andExpect(status().isNotFound());
	}


	@Test
	@WithMockUserMatrapportor
	void När_ej_behörig_användare_tar_bort_ett_meddelanden_skall_forbidden_returneras() throws Exception {
		mvc.perform(delete("/api/meddelande/1")
				.with(csrf()))
				.andExpect(status().isForbidden());
	}	
}
