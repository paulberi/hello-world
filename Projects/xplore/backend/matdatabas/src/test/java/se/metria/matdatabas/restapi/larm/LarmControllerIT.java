package se.metria.matdatabas.restapi.larm;

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
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.dto.Larm;
import se.metria.matdatabas.service.larm.query.LarmSearchFilter;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
		controllers = LarmController.class,
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class LarmControllerIT {


	@TestConfiguration
	static class TestContextConfiguration {
		@Bean
		public ModelMapper modelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.validate();
			return modelMapper;
		}
	}

	@Autowired
	private MockMvc mvc;

	@MockBean
	private LarmService larmService;

	@MockBean
	private MatdatabasUser user;

	@MockBean
	private AnvandareService anvandareService;

	@Test
	@WithMockUserTillstandshandlaggare
	void när_tillståndshandläggare_hämtar_mätobjektets_larm_så_ska_larmen_returneras() throws Exception {
		List<Larm> larmdetaljerList = new ArrayList<>();
		LocalDateTime date = LocalDateTime.now();

		for (int i = 0; i < 3; i++) {
			larmdetaljerList.add((Larm) new Larm()
					.id((long) i)
					.matobjektNamn("matobjektnamn" + i)
					.matningstypNamn("matningstypnamn" + i)
					.matobjektTyp("matobjektTyp" + i)
					.matobjektFastighet("fastighet" + i)
					.avlastDatum(date.plusDays(i))
					.varde(10d)
					.larmnivaId(i)
					.typAvKontroll(1)
					.gransvarde(12d)
					.anvandargruppNamn("anvandargruppnamn" + i)
					.status(0)
					.typAvKontroll(0));
		}

		var larmdetaljerPage = new PageImpl<>(larmdetaljerList);

		MatningstypSearchFilter filter = MatningstypSearchFilter.builder().includeIds(List.of(0, 1, 2)).build();
		LarmSearchFilter larmSearchFilter = LarmSearchFilter.builder().build();
		when(larmService.getLarmListPage(eq(larmSearchFilter) ,eq(filter), any())).thenReturn(larmdetaljerPage);

		// HTTP POST
		mvc.perform(post("/api/larm?egnaLarm=false")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"includeIds\": [0,1,2]}")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("totalPages", equalTo(1)))
				.andExpect(jsonPath("totalElements", equalTo(3)))
				.andExpect(jsonPath("numberOfElements", equalTo(3)));
	}
}
