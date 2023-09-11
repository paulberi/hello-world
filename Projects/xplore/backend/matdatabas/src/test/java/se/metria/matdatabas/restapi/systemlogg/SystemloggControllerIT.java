package se.metria.matdatabas.restapi.systemlogg;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import se.metria.matdatabas.openapi.model.SystemloggDto;
import se.metria.matdatabas.openapi.model.SystemloggPageDto;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.systemlogg.SystemloggService;
import se.metria.matdatabas.service.systemlogg.dto.Systemlogg;
import se.metria.matdatabas.service.systemlogg.handelse.HandelseTyp;

@DisplayName("Givet systemlogg API med loggposter")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
		controllers = SystemloggController.class,
		includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
) 
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class SystemloggControllerIT {
	
	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.createTypeMap(Systemlogg.class, SystemloggDto.class);
			modelMapper.createTypeMap(PageImpl.class, SystemloggPageDto.class);
			modelMapper.validate();
			return modelMapper;
		}
	}
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private SystemloggService service;
	
	private Systemlogg.SystemloggBuilder builder;

	private static String ANVANDARNAMN = "Test namn";
	private static String BESKRIVNING = "Test beskrivning";
	private static LocalDateTime DATUM = LocalDateTime.now();

	@BeforeEach
	void setUp() throws Exception {
		builder = Systemlogg.builder()
				.anvandarnamn(ANVANDARNAMN)
				.beskrivning(BESKRIVNING)
				.datum(DATUM)
				.handelse(HandelseTyp.MATNINGSTYP.getId());
	}

	@Test()
	@WithMockUserAdministrator
	void när_användare_hämtar_systemlogg_skall_sidans_meddelanden_returneras() throws Exception {		
		when(service.getSystemlogg(anyInt(), anyInt(), anyString(), any(Sort.Direction.class), isNull(), isNull(), isNull())).thenAnswer(inv -> {
			var logs = Collections.nCopies(inv.getArgument(1), builder.build());
			return new PageImpl<>(logs);
		});
		mvc.perform(get("/api/systemlogg?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(50)))
				.andExpect(jsonPath("numberOfElements", equalTo(50)))
				.andExpect(jsonPath("content", hasSize(50)))
				.andExpect(jsonPath("content[0].anvandarnamn", equalTo(ANVANDARNAMN)))
				.andExpect(jsonPath("content[0].beskrivning", equalTo(BESKRIVNING)))
				.andExpect(jsonPath("content[0].datum", equalTo(DATUM.format(DateTimeFormatter.ISO_DATE_TIME))))
				.andExpect(jsonPath("content[0].handelse", equalTo((int)HandelseTyp.MATNINGSTYP.getId())));
	}

	@Test()
	@WithMockUserTillstandshandlaggare
	void när_ej_behörig_användare_hämtar_systemlogg_skall_forbidden_returneras() throws Exception {		
		mvc.perform(get("/api/systemlogg?page=0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

}
