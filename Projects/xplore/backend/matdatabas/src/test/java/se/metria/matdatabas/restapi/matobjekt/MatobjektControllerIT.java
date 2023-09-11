package se.metria.matdatabas.restapi.matobjekt;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import se.metria.matdatabas.openapi.model.DefinitionMatningstypDto;
import se.metria.matdatabas.openapi.model.EditMatobjektDto;
import se.metria.matdatabas.openapi.model.ListMatobjektDto;
import se.metria.matdatabas.openapi.model.MatningstypDto;
import se.metria.matdatabas.openapi.model.MatobjektPageDto;
import se.metria.matdatabas.restapi.bifogadfil.BifogadFilHelper;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserObservator;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.analys.AnalysService;
import se.metria.matdatabas.service.analys.dto.Analys;
import se.metria.matdatabas.service.analys.exception.AnalysSaveMatningException;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.definitionmatningstyp.dto.DefinitionMatningstyp;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.handelse.HandelseService;
import se.metria.matdatabas.service.handelse.dto.Handelse;
import se.metria.matdatabas.service.handelse.dto.SaveHandelse;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.Matningsfelkod;
import se.metria.matdatabas.service.matning.Matningstatus;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matning.dto.MatningSaveResult;
import se.metria.matdatabas.service.matning.dto.ReviewMatning;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matningstyp.dto.MatningstypMatobjekt;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjekt.dto.EditMatobjekt;
import se.metria.matdatabas.service.matobjekt.dto.ListMatobjekt;
import se.metria.matdatabas.service.matobjekt.dto.MatningstypMatrunda;
import se.metria.matdatabas.service.matobjekt.dto.Matobjekt;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektConflictException;
import se.metria.matdatabas.service.matningstyp.exception.MatningstypHasMatningarException;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektNotFoundException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = MatobjektController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MockWebSecurityConfig.class})
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class MatobjektControllerIT {

	private final Matobjekt matobjekt = Matobjekt.builder()
			.id(1)
			.typ((short) 0)
			.namn("namn")
			.aktiv(true)
			.kontrollprogram(true)
			.posN(new BigDecimal("123.456"))
			.posE(new BigDecimal("987.654"))
			.fastighet("fastighet")
			.lage("läge")
			.bifogadBildId(UUID.randomUUID())
			.matobjektgrupper(List.of(1, 2))
			.matrundor(List.of(new MatningstypMatrunda(1,2)))
			.dokument(List.of(UUID.randomUUID(), UUID.randomUUID()))
			.build();
	private final EditMatobjekt editMatobjekt = EditMatobjekt.builder()
			.typ((short) 1)
			.namn("namn2")
			.aktiv(false)
			.kontrollprogram(false)
			.posN(new BigDecimal("654.321"))
			.posE(new BigDecimal("456.789"))
			.fastighet("fastighet2")
			.lage("läge2")
			.bifogadBildId(UUID.randomUUID())
			.matobjektgrupper(List.of(1, 2, 3))
			.dokument(List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()))
			.build();
	private final SaveHandelse editHandelse = SaveHandelse.builder()
			.benamning("Benämning")
			.beskrivning("Beskrivning")
			.bifogadeBilderIds(Collections.emptySet())
			.datum(LocalDateTime.now())
			.build();
	private final Handelse handelse = Handelse.builder()
			.benamning("Benämning")
			.beskrivning("Beskrivning")
			.bifogadebilder(Collections.emptyList())
			.datum(LocalDateTime.now())
			.foretag("Företag")
			.id(0)
			.build();	
	private final SaveMatning saveMatning = SaveMatning.builder()
			.avlastDatum(LocalDateTime.now())
			.avlastVarde(23.456d)
			.felkod(Matningsfelkod.OK)
			.kommentar("En kommentar")
			.rapportor("Mätbolaget")
			.build();
	private final Matning matning = Matning.builder()
			.avlastDatum(LocalDateTime.now())
			.avlastVarde(23.456d)
			.felkod(Matningsfelkod.OK)
			.kommentar("En kommentar")
			.rapportor("Mätbolaget")
			.build();
	private final ReviewMatning reviewMatning = ReviewMatning.builder()
			.status(Matningstatus.GODKANT)
			.build();

	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.createTypeMap(PageImpl.class, MatobjektPageDto.class);
			modelMapper.createTypeMap(Matningstyp.class, MatningstypDto.class);
			modelMapper.createTypeMap(DefinitionMatningstyp.class, DefinitionMatningstypDto.class);
			modelMapper.createTypeMap(EditMatobjekt.class, EditMatobjektDto.class);
			modelMapper.createTypeMap(ListMatobjekt.class, ListMatobjektDto.class);
			modelMapper.validate();
			return modelMapper;
		}
	}

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MatobjektService matobjektService;

	@MockBean
	private MatningstypService matningstypService;

	@MockBean
	private DefinitionMatningstypService definitionMatningstypService;

	@MockBean
	private HandelseService handelseService;

	@MockBean
	private MatningService matningService;

	@MockBean
	private AnalysService analysService;

	@MockBean
	private GransvardeService gransvardeService;

	@BeforeEach
	void setUp() throws Exception {

	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_sida_med_matobjekt() throws Exception {
		ListMatobjekt listMatobjekt = ListMatobjekt.builder()
				.id(1)
				.typ((short) 0)
				.namn("namn")
				.aktiv(true)
				.fastighet("fastighet")
				.lage("läge")
				.build();
		Page<ListMatobjekt> matobjektPage = new PageImpl<>(List.of(listMatobjekt, listMatobjekt, listMatobjekt));

		when(matobjektService.getMatobjekt(any(), any())).thenReturn(matobjektPage);

		// HTTP GET
		mvc.perform(get("/api/matobjekt?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(3)))
				.andExpect(jsonPath("numberOfElements", equalTo(3)));

		// HTTP POST
		mvc.perform(post("/api/matobjekt/page?page=0&size=10&sortProperty=id&sortDirection=asc")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(3)))
				.andExpect(jsonPath("numberOfElements", equalTo(3)));
	}

	@Test
	@WithMockUserObservator
	void en_observatör_ska_kunna_hämta_ett_mätobjekt() throws Exception {
		when(matobjektService.getMatobjekt(eq(0))).thenReturn(matobjekt);
		mvc.perform(get("/api/matobjekt/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(matobjekt.getId())))
				.andExpect(jsonPath("typ", equalTo(matobjekt.getTyp().intValue())))
				.andExpect(jsonPath("namn", equalTo(matobjekt.getNamn())))
				.andExpect(jsonPath("aktiv", equalTo(matobjekt.getAktiv())))
				.andExpect(jsonPath("kontrollprogram", equalTo(matobjekt.getKontrollprogram())))
				.andExpect(jsonPath("fastighet", equalTo(matobjekt.getFastighet())))
				.andExpect(jsonPath("lage", equalTo(matobjekt.getLage())))
				.andExpect(jsonPath("bifogadBildId", equalTo(matobjekt.getBifogadBildId().toString())))
				.andExpect(jsonPath("bifogadBildLink", equalTo(BifogadFilHelper.getFilLink(matobjekt.getBifogadBildId()))))
				.andExpect(jsonPath("bifogadBildThumbnail", equalTo(BifogadFilHelper.getThumbnailLink(matobjekt.getBifogadBildId()))))
				.andExpect(jsonPath("matobjektgrupper", equalTo(matobjekt.getMatobjektgrupper())))
				.andExpect(jsonPath("matrundor[0].matningstypId", equalTo(matobjekt.getMatrundor().get(0).getMatningstypId())))
				.andExpect(jsonPath("matrundor[0].matrundaId", equalTo(matobjekt.getMatrundor().get(0).getMatrundaId())))
				.andExpect(jsonPath("dokument", equalTo(matobjekt.getDokument().stream().map(UUID::toString).collect(Collectors.toList()))));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void ett_icke_existerande_matobjekt_ger_404() throws Exception {
		when(matobjektService.getMatobjekt(eq(0))).thenThrow(new MatobjektNotFoundException());
		mvc.perform(get("/api/matobjekt/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_skapa_ett_mätobjekt() throws Exception {
		when(matobjektService.createMatobjekt(editMatobjekt)).thenReturn(matobjekt);
		mvc.perform(post("/api/matobjekt")
				.content(mapper.writeValueAsString(editMatobjekt))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id", equalTo(matobjekt.getId())))
				.andExpect(jsonPath("typ", equalTo(matobjekt.getTyp().intValue())))
				.andExpect(jsonPath("namn", equalTo(matobjekt.getNamn())))
				.andExpect(jsonPath("aktiv", equalTo(matobjekt.getAktiv())))
				.andExpect(jsonPath("kontrollprogram", equalTo(matobjekt.getKontrollprogram())))
				.andExpect(jsonPath("fastighet", equalTo(matobjekt.getFastighet())))
				.andExpect(jsonPath("lage", equalTo(matobjekt.getLage())))
				.andExpect(jsonPath("bifogadBildId", equalTo(matobjekt.getBifogadBildId().toString())))
				.andExpect(jsonPath("matobjektgrupper", equalTo(matobjekt.getMatobjektgrupper())))
				.andExpect(jsonPath("matrundor[0].matningstypId", equalTo(matobjekt.getMatrundor().get(0).getMatningstypId())))
				.andExpect(jsonPath("matrundor[0].matrundaId", equalTo(matobjekt.getMatrundor().get(0).getMatrundaId())))
				.andExpect(jsonPath("dokument", equalTo(matobjekt.getDokument().stream().map(UUID::toString).collect(Collectors.toList()))));
	}

 	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_inte_kunna_skapa_ett_mätobjekt_med_existerande_namn() throws Exception {
		when(matobjektService.createMatobjekt(any())).thenThrow(new MatobjektConflictException());
		mvc.perform(post("/api/matobjekt")
				.content(mapper.writeValueAsString(editMatobjekt))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isConflict());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_skapa_ett_mätobjekt() throws Exception {
		mvc.perform(post("/api/matobjekt")
				.content(mapper.writeValueAsString(editMatobjekt))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_uppdatera_ett_existerande_mätobjekt() throws Exception {
		when(matobjektService.updateMatobjekt(eq(0), any())).thenReturn(matobjekt);
		mvc.perform(put("/api/matobjekt/0")
				.content(mapper.writeValueAsString(editMatobjekt))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(matobjekt.getId())))
				.andExpect(jsonPath("typ", equalTo(matobjekt.getTyp().intValue())))
				.andExpect(jsonPath("namn", equalTo(matobjekt.getNamn())))
				.andExpect(jsonPath("aktiv", equalTo(matobjekt.getAktiv())))
				.andExpect(jsonPath("kontrollprogram", equalTo(matobjekt.getKontrollprogram())))
				.andExpect(jsonPath("fastighet", equalTo(matobjekt.getFastighet())))
				.andExpect(jsonPath("lage", equalTo(matobjekt.getLage())))
				.andExpect(jsonPath("bifogadBildId", equalTo(matobjekt.getBifogadBildId().toString())))
				.andExpect(jsonPath("matobjektgrupper", equalTo(matobjekt.getMatobjektgrupper())))
				.andExpect(jsonPath("matrundor[0].matningstypId", equalTo(matobjekt.getMatrundor().get(0).getMatningstypId())))
				.andExpect(jsonPath("matrundor[0].matrundaId", equalTo(matobjekt.getMatrundor().get(0).getMatrundaId())))
				.andExpect(jsonPath("dokument", equalTo(matobjekt.getDokument().stream().map(UUID::toString).collect(Collectors.toList()))));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void utan_mandatory_parameter_returneras_400() throws Exception {
		EditMatobjekt editMatobjekt = EditMatobjekt.builder()
				.typ((short) 1)
				.namn(null)
				.aktiv(false)
				.kontrollprogram(false)
				.posN(new BigDecimal("654.321"))
				.posE(new BigDecimal("456.789"))
				.fastighet("fastighet2")
				.lage("läge2")
				.bifogadBildId(UUID.randomUUID())
				.matobjektgrupper(List.of(1, 2, 3))
				.dokument(List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()))
				.build();

		mvc.perform(put("/api/matobjekt/0")
				.content(mapper.writeValueAsString(editMatobjekt))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isBadRequest());
	}


	@Test
	@WithMockUserTillstandshandlaggare
	void när_ett_icke_existerande_mätobjekt_uppdateras_returneras_404() throws Exception {
		when(matobjektService.updateMatobjekt(eq(0), any())).thenThrow(new MatobjektNotFoundException());
		mvc.perform(put("/api/matobjekt/0")
				.content(mapper.writeValueAsString(editMatobjekt))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_uppdatera_ett_mätobjekt() throws Exception {
		mvc.perform(put("/api/matobjekt/0")
				.content(mapper.writeValueAsString(editMatobjekt))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_ta_bort_ett_mätobjekt_utan_mätningar() throws Exception {
		doNothing().when(matobjektService).deleteMatobjekt(eq(0));
		mvc.perform(delete("/api/matobjekt/0")
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_inte_kunna_ta_bort_ett_mätobjekt_med_mätningar() throws Exception {
		doThrow(new MatningstypHasMatningarException()).when(matobjektService).deleteMatobjekt(eq(0));
		mvc.perform(delete("/api/matobjekt/0")
				.with(csrf()))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_få_404_vid_delete_om_en_mätobjektet_inte_finns() throws Exception {
		doThrow(new MatobjektNotFoundException()).when(matobjektService).deleteMatobjekt(eq(0));
		mvc.perform(delete("/api/matobjekt/0")
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_ta_bort_mätobjekt() throws Exception {
		mvc.perform(delete("/api/matobjekt/0")
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_fråga_om_ett_mätobjekt_utan_mätningar_får_tas_bort() throws Exception {
		when(matobjektService.canDelete(eq(0))).thenReturn(true);
		mvc.perform(options("/api/matobjekt/0")
				.with(csrf()))
				.andExpect(status().isNoContent())
				.andExpect(header().string("Allow", "GET,PUT,DELETE,OPTIONS"));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_fråga_om_ett_mätobjekt_med_mätningar_får_tas_bort() throws Exception {
		when(matobjektService.canDelete(eq(0))).thenReturn(false);
		mvc.perform(options("/api/matobjekt/0")
				.with(csrf()))
				.andExpect(status().isNoContent())
				.andExpect(header().string("Allow", "GET,PUT,OPTIONS"));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_som_frågar_om_ett_icke_existerande_mätobjekt_får_tas_bort_ska_få_404() throws Exception {
		doThrow(new MatobjektNotFoundException()).when(matobjektService).canDelete(eq(0));
		mvc.perform(options("/api/matobjekt/0")
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_fråga_om_ett_mätobjekt_får_tas_bort_men_ska_inte_få_göra_det() throws Exception {
		mvc.perform(options("/api/matobjekt/0")
				.with(csrf()))
				.andExpect(status().isNoContent())
				.andExpect(header().string("Allow", "GET,OPTIONS"));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_lista_med_mätningstyper() throws Exception {
		Matningstyp matningstyp = Matningstyp.builder().id(2).typ("Infiltration (momentant flöde)").build();
		when(matningstypService.getMatningstyperForMatobjekt(1)).thenReturn(Collections.singletonList(matningstyp));
		mvc.perform(get("/api/matobjekt/1/matningstyper")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_alla_matobjektnamn() throws Exception {
		when(matobjektService.getMatobjektNamn(any(), any())).thenReturn(List.of("namn1", "namn2", "namn3"));
		mvc.perform(get("/api/matobjekt/namn?q=abc")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", equalTo(3)))
				.andExpect(jsonPath("$", everyItem(anyOf(
						containsString("namn1"), containsString("namn2"), containsString("namn3")))));
	}

	@Test
	@WithMockUserObservator
	void en_observatör_ska_kunna_hämta_alla_fastigheter() throws Exception {
		when(matobjektService.getMatobjektFastigheter(any())).thenReturn(List.of("fastighet1", "fastighet2", "fastighet3"));
		mvc.perform(get("/api/matobjekt/fastigheter?q=abc")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", equalTo(3)))
				.andExpect(jsonPath("$", everyItem(anyOf(
						containsString("fastighet1"), containsString("fastighet2"), containsString("fastighet3")))));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_hämta_en_sida_med_händelser() throws Exception {
		var pageHandelser = new PageImpl<Handelse>(Collections.emptyList());
		when(matobjektService.exists(anyInt())).thenReturn(true);
		when(handelseService.getHandelseForMatobjekt(any(), any(), any(), any(), any())).thenReturn(pageHandelser);
		mvc.perform(get("/api/matobjekt/1/handelser")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(0)))
				.andExpect(jsonPath("numberOfElements", equalTo(0)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_sida_med_händelser() throws Exception {
		var pageHandelser = new PageImpl<Handelse>(Collections.emptyList());
		when(matobjektService.exists(anyInt())).thenReturn(true);
		when(handelseService.getHandelseForMatobjekt(any(), any(), any(), any(), any())).thenReturn(pageHandelser);
		mvc.perform(get("/api/matobjekt/1/handelser")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_en_sida_med_händelser_hämtas_med_felaktigt_id_skall_not_found_returneras() throws Exception {
		when(matobjektService.exists(anyInt())).thenReturn(false);
		mvc.perform(get("/api/matobjekt/1/handelser")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_hämta_en_händelse() throws Exception {
		when(handelseService.getHandelse(any(), any())).thenReturn(Optional.of(handelse));
		mvc.perform(get("/api/matobjekt/1/handelser/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_händelse() throws Exception {
		when(handelseService.getHandelse(any(), any())).thenReturn(Optional.of(handelse));
		mvc.perform(get("/api/matobjekt/1/handelser/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_en_händelse_hämtas_med_felaktigt_id_skallnot_found_returneras() throws Exception {
		when(handelseService.getHandelse(any(), any())).thenReturn(Optional.empty());
		mvc.perform(get("/api/matobjekt/1/handelser/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_skapa_en_ny_händelse_på_ett_mätobjekt() throws Exception {
		when(matobjektService.exists(anyInt())).thenReturn(true);
		when(handelseService.create(any(), any())).thenReturn(handelse);
		mvc.perform(post("/api/matobjekt/1/handelser")
				.content(mapper.writeValueAsString(editHandelse))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isCreated());
	}
	
	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_skapa_en_ny_händelse_på_ett_mätobjekt() throws Exception {
		when(matobjektService.exists(anyInt())).thenReturn(true);
		when(handelseService.create(any(), any())).thenReturn(handelse);
		mvc.perform(post("/api/matobjekt/1/handelser")
				.content(mapper.writeValueAsString(editHandelse))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_en_ny_händelse_skapas_på_ett_mätobjekt_med_felaktigt_id_skall_not_found_returneras() throws Exception {
		when(matobjektService.exists(anyInt())).thenReturn(false);
		mvc.perform(post("/api/matobjekt/1/handelser")
				.content(mapper.writeValueAsString(editHandelse))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_uppdatera_en_händelse_på_ett_mätobjekt() throws Exception {
		when(handelseService.save(any(), any(), any())).thenReturn(handelse);
		mvc.perform(put("/api/matobjekt/1/handelser/1")
				.content(mapper.writeValueAsString(editHandelse))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_uppdatera_en_händelse_på_ett_mätobjekt() throws Exception {
		when(handelseService.save(any(), any(), any())).thenReturn(handelse);
		mvc.perform(put("/api/matobjekt/1/handelser/1")
				.content(mapper.writeValueAsString(editHandelse))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_en_händelse_uppdateras_på_ett_mätobjekt_med_felaktigt_id_skall_not_found_returneras() throws Exception {
		when(handelseService.save(any(), any(), any())).thenThrow(new EntityNotFoundException());
		mvc.perform(put("/api/matobjekt/1/handelser/1")
				.content(mapper.writeValueAsString(editHandelse))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_ta_bort_en_händelse_på_ett_mätobjekt() throws Exception {
		mvc.perform(delete("/api/matobjekt/1/handelser/1")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_kunna_ta_bort_en_händelse_på_ett_mätobjekt() throws Exception {
		mvc.perform(delete("/api/matobjekt/1/handelser/1")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUserTillstandshandlaggare
	void när_en_händelse_tas_bort_på_ett_mätobjekt_med_felaktigt_id_skall_not_found_returneras() throws Exception {
		doThrow(new EntityNotFoundException()).when(handelseService).delete(any(), any());
		mvc.perform(delete("/api/matobjekt/1/handelser/1")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_skapa_en_ny_mätning_på_ett_mätobjekt() throws Exception {
		when(matningstypService.existsInMatobjekt(anyInt(), anyInt())).thenReturn(true);
		when(matningstypService.findById(any())).thenReturn(Optional.of(new Matningstyp()));
		when(matningService.create(any(), any(), anyBoolean(), eq(StandardKallsystem.MiljöKoll.getNamn()))).thenReturn(new MatningSaveResult(matning,null));
		mvc.perform(post("/api/matobjekt/1/matningstyper/2/matningar")
				.content(mapper.writeValueAsString(saveMatning))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUserMatrapportor
	void när_en_ny_mätning_skapas_på_ett_mätobjekt_med_felaktigt_id_skall_not_found_returneras() throws Exception {
		when(matningstypService.existsInMatobjekt(anyInt(), anyInt())).thenReturn(false);
		when(matningstypService.findById(any())).thenReturn(Optional.of(new Matningstyp()));
		when(matningService.create(any(), any(), anyBoolean(), eq(StandardKallsystem.MiljöKoll.getNamn()))).thenReturn(new MatningSaveResult(matning,null));
		mvc.perform(post("/api/matobjekt/1/matningstyper/1/matningar")
				.content(mapper.writeValueAsString(saveMatning))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUserMatrapportor
	void när_en_ny_mätning_skapas_på_ett_mätobjekt_med_ogiltiga_mätvärden_skall_bad_request_returneras() throws Exception {
		when(matningstypService.existsInMatobjekt(anyInt(), anyInt())).thenReturn(true);
		when(matningstypService.findById(any())).thenReturn(Optional.of(new Matningstyp()));
		doThrow(MatningIllegalMatvarde.class).when(matningService).create(any(), any(), anyBoolean(), eq(StandardKallsystem.MiljöKoll.getNamn()));
		mvc.perform(post("/api/matobjekt/1/matningstyper/1/matningar")
				.content(mapper.writeValueAsString(saveMatning))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_tillståndshandläggare_hämtar_mätobjektets_mätningstyper_så_ska_mätningstyperna_returneras() throws Exception {
		List<MatningstypMatobjekt> matningstypMatobjekt = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			matningstypMatobjekt.add(MatningstypMatobjekt.builder()
					.matningstypId(i)
					.matningstypNamn("matningstypnamn" + i)
					.matningstypAktiv(i % 2 == 0)
					.matobjektId(i)
					.matobjektNamn("matobjektnamn" + i)
					.matobjektFastighet("fastighet" + i)
					.matobjektLage("lage" + i)
					.matobjektAktiv(true)
					.aktiv(i % 2 == 0).build());
		}

		var matningstypMatobjektPage = new PageImpl<>(matningstypMatobjekt);

		MatningstypSearchFilter filter = MatningstypSearchFilter.builder().includeIds(List.of(0, 1, 2)).build();

		when(matningstypService.getMatningstypMatobjektPage(eq(filter), any())).thenReturn(matningstypMatobjektPage);

		// HTTP GET
		mvc.perform(get("/api/matobjekt/matningstyper?includeIds=0,1,2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(3)))
				.andExpect(jsonPath("numberOfElements", equalTo(3)));

		when(matningstypService.getMatningstypMatobjektPage(eq(filter), any())).thenReturn(matningstypMatobjektPage);

		// HTTP POST
		mvc.perform(post("/api/matobjekt/matningstyper")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"includeIds\": [0,1,2]}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(3)))
				.andExpect(jsonPath("numberOfElements", equalTo(3)));
	}
	
	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_sida_med_mätningar() throws Exception {
		var pageMatningar = new PageImpl<Matning>(Collections.emptyList());
		when(matningstypService.existsInMatobjekt(anyInt(), anyInt())).thenReturn(true);
		when(matningService.getMatningarForMatningstyp(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(pageMatningar);
		mvc.perform(get("/api/matobjekt/1/matningstyper/2/matningar")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(0)))
				.andExpect(jsonPath("numberOfElements", equalTo(0)));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_granska_en_mätning_på_ett_mätobjekt() throws Exception {
		when(matningstypService.existsInMatobjekt(anyInt(), anyInt())).thenReturn(true);
		when(matningstypService.findById(any())).thenReturn(Optional.of(new Matningstyp()));
		when(matningService.review(anyLong(), any(), any())).thenReturn(matning);
		mvc.perform(put("/api/matobjekt/1/matningstyper/2/matningar/1/review")
				.content(mapper.writeValueAsString(reviewMatning))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUserTillstandshandlaggare
	void när_en_analys_med_felaktig_matning_sparas_returneras_400() throws Exception {
		when(matobjektService.exists(anyInt())).thenReturn(true);
		when(analysService.createAnalys(any())).thenThrow(new AnalysSaveMatningException());
		mvc.perform(post("/api/matobjekt/0/vattenkemi")
				.content(mapper.writeValueAsString(editMatobjekt))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_sida_med_analyser() throws Exception {
		var pageAnalys = new PageImpl<Analys>(Collections.emptyList());
		when(matobjektService.exists(anyInt())).thenReturn(true);
		when(analysService.getAnalyser(anyInt(), any(), any(), any(), any())).thenReturn(pageAnalys);
		mvc.perform(get("/api/matobjekt/1/vattenkemi")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(0)))
				.andExpect(jsonPath("numberOfElements", equalTo(0)));
	}
}
