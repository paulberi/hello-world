package se.metria.matdatabas.restapi.bifogadfil;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import se.metria.matdatabas.openapi.model.BifogadfilDto;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.service.bifogadfil.BifogadfilService;
import se.metria.matdatabas.service.bifogadfil.dto.Bifogadfil;
import se.metria.matdatabas.service.bifogadfil.dto.BifogadfilInfo;
import se.metria.matdatabas.service.bifogadfil.dto.SaveBifogadfil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = BifogadFilController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class BifogadFilControllerIT {

	private UUID bifogadfilId;
	private UUID bifogadfilId2;
	private BifogadfilInfo bifogadfilInfo;
	private BifogadfilInfo bifogadfilInfo2;
	private Bifogadfil bifogadfil;
	private SaveBifogadfil saveBifogadfil;

	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.createTypeMap(Bifogadfil.class, BifogadfilDto.class);
			modelMapper.validate();
			return modelMapper;
		}
	}

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BifogadfilService bifogadfilService;

	@BeforeEach
	void setUp() {
		bifogadfilId = UUID.randomUUID();
		bifogadfilId2 = UUID.randomUUID();

		bifogadfilInfo = BifogadfilInfo.builder()
				.id(bifogadfilId)
				.filnamn("filnamn")
				.mimeTyp("image/jpeg")
				.skapadDatum(LocalDateTime.now())
				.build();

		bifogadfilInfo2 = BifogadfilInfo.builder()
				.id(bifogadfilId2)
				.filnamn("filnamn")
				.mimeTyp("image/jpeg")
				.skapadDatum(LocalDateTime.now())
				.build();

		bifogadfil = Bifogadfil.builder()
				.id(bifogadfilId)
				.filnamn("filnamn.jpg")
				.mimeTyp("image/jpeg")
				.skapadDatum(LocalDateTime.now())
				.fil(new byte[]{1,2,3})
				.thumbnail(new byte[]{1})
				.build();

		saveBifogadfil = SaveBifogadfil.builder()
				.filnamn("filnamn")
				.mimeTyp("image/jpeg")
				.fil(new byte[]{1,2,3})
				.build();
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_bifogad_fil() throws Exception {
		when(bifogadfilService.getBifogadfil(bifogadfilId)).thenReturn(Optional.of(bifogadfil));
		mvc.perform(get("/api/bifogadfil/" + bifogadfilId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(bifogadfil.getId().toString())))
				.andExpect(jsonPath("filnamn", equalTo(bifogadfil.getFilnamn())))
				.andExpect(jsonPath("mimeTyp", equalTo(bifogadfil.getMimeTyp())))
				.andExpect(jsonPath("skapad_datum", startsWith(bifogadfil.getSkapadDatum().toString().substring(0,18))));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_lista_med_bifogad_filer() throws Exception {
		Set<UUID> ids = Stream.of(bifogadfilId, bifogadfilId2).collect(Collectors.toSet());
		Set<BifogadfilInfo> bifogadeFiler = Stream.of(bifogadfilInfo, bifogadfilInfo2).collect(Collectors.toSet());
		when(bifogadfilService.getBifogadfilInfos(eq(ids))).thenReturn(bifogadeFiler);
		mvc.perform(get("/api/bifogadfil?ids=" + bifogadfilId + "," + bifogadfilId2)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_bifogad_fils_innehåll() throws Exception {
		when(bifogadfilService.getBifogadfil(bifogadfilId)).thenReturn(Optional.of(bifogadfil));
		mvc.perform(get("/api/bifogadfil/" + bifogadfilId + "/data")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + bifogadfil.getFilnamn()))
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/octet-stream"))
				.andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(bifogadfil.getFil().length)))
				.andExpect(content().bytes(bifogadfil.getFil()));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_hämta_en_bifogad_fils_thumbnail() throws Exception {
		when(bifogadfilService.getBifogadfil(bifogadfilId)).thenReturn(Optional.of(bifogadfil));
		mvc.perform(get("/api/bifogadfil/" + bifogadfilId + "/thumbnail")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filnamn_thumbnail.jpg"))
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/octet-stream"))
				.andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(bifogadfil.getThumbnail().length)))
				.andExpect(content().bytes(bifogadfil.getThumbnail()));
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_kunna_skicka_in_en_bifogad_fil() throws Exception {
		when(bifogadfilService.createBifogadfil(saveBifogadfil)).thenReturn(bifogadfil);
		MockMultipartFile file = new MockMultipartFile(
				"file",
				saveBifogadfil.getFilnamn(),
				saveBifogadfil.getMimeTyp(),
				saveBifogadfil.getFil());
		mvc.perform(multipart("/api/bifogadfil").file(file).with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id", equalTo(bifogadfil.getId().toString())))
				.andExpect(jsonPath("filnamn", equalTo(bifogadfil.getFilnamn())))
				.andExpect(jsonPath("mimeTyp", equalTo(bifogadfil.getMimeTyp())))
				.andExpect(jsonPath("skapad_datum", startsWith(bifogadfil.getSkapadDatum().toString().substring(0,18))));
	}
}
