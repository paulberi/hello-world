package se.metria.matdatabas.restapi.rapport;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
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
import org.springframework.test.web.servlet.ResultActions;
import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.rapport.RapportConstants;
import se.metria.matdatabas.service.rapport.RapportFetcher;
import se.metria.matdatabas.service.rapport.RapportService;
import se.metria.matdatabas.service.rapport.Tidsintervall;
import se.metria.matdatabas.service.rapport.dto.RapportGrafSettings;
import se.metria.matdatabas.service.rapport.dto.RapportMottagare;
import se.metria.matdatabas.service.rapport.dto.RapportSettings;
import se.metria.matdatabas.service.rapport.exception.RapportSettingsErrorException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = RapportController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MockWebSecurityConfig.class })
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RapportControllerIT {
    private RapportSettings rapportSettings;

    @Autowired
    private MockMvc mvc;

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public ModelMapper mapper() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.addConverter(uuidConverter);
            modelMapper.createTypeMap(RapportSettings.class, RapportSettingsDto.class);
            modelMapper.validate();
            return modelMapper;
        }
    }

    @MockBean
    private RapportService rapportService;

    @MockBean
    private RapportFetcher rapportFetcher;

    @Autowired
    private ObjectMapper objectMapper;

    static Converter<String, UUID> uuidConverter = new AbstractConverter<String, UUID>() {
        protected UUID convert(String source) {
            if (source == null) {
                return null;
            }
            return UUID.fromString(source);
        }
    };

    @BeforeEach
    void setUp() throws RapportSettingsErrorException {
        rapportSettings = mockRapportSetting();

        when(rapportService.getRapportSettings(eq(1))).thenReturn(rapportSettings);
        when(rapportService.getRapportSettings(eq(0))).thenThrow(new EntityNotFoundException());
        when(rapportService.createRapportSettings(rapportSettings)).thenReturn(rapportSettings);
        when(rapportService.updateRapportSettings(1, rapportSettings)).thenReturn(rapportSettings);
        when(rapportService.updateRapportSettings(0, rapportSettings)).thenThrow(new EntityNotFoundException());
        when(rapportService.deleteRapportSettings(eq(1))).thenReturn(true);
        when(rapportService.deleteRapportSettings(eq(0))).thenReturn(false);
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void en_tillståndshandläggare_ska_kunna_hämta_en_rapport() throws Exception {
        final var resultMatcher = mvc.perform(get("/api/rapport/settings/1")
           .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());

        verifyRapportSettings(resultMatcher, rapportSettings);
    }

    @Test
    @WithMockUserAdministrator
    void en_icke_existerande_rapport_ger_404() throws Exception {
        mvc.perform(get("/api/rapport/settings/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void en_tillståndshandläggare_ska_kunna_skapa_en_rapport() throws Exception {
        final var resultMatcher = mvc.perform(post("/api/rapport/settings/")
           .content(objectMapper.writeValueAsString(rapportSettings))
           .contentType(MediaType.APPLICATION_JSON)
           .with(csrf()))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyRapportSettings(resultMatcher, rapportSettings);
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void en_tillståndshandläggare_ska_kunna_uppdatera_en_rapport() throws Exception {
        final var resultMatcher = mvc.perform(put("/api/rapport/settings/1")
                .content(objectMapper.writeValueAsString(rapportSettings))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
           .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyRapportSettings(resultMatcher, rapportSettings);
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void att_uppdatera_en_icke_existerande_rapport_ger_404() throws Exception {
        mvc.perform(put("/api/rapport/settings/0")
           .content(objectMapper.writeValueAsString(rapportSettings))
           .contentType(MediaType.APPLICATION_JSON)
           .with(csrf()))
           .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void en_tillståndshandläggare_ska_kunna_radera_en_rapport() throws Exception {
        mvc.perform(delete("/api/rapport/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void en_tillståndshandläggare_ska_få_404_om_man_raderar_en_rapport_som_inte_finns() throws Exception {
        mvc.perform(delete("/api/rapport/0")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    void verifyRapportSettings(ResultActions resultActions, RapportSettings rapport) throws Exception {
        resultActions
                .andExpect(jsonPath("id", equalTo(rapport.getId())))
                .andExpect(jsonPath("namn", equalTo(rapport.getNamn())))
                .andExpect(jsonPath("aktiv", equalTo(rapport.getAktiv())))
                .andExpect(jsonPath("mejlmeddelande", equalTo(rapport.getMejlmeddelande())))
                .andExpect(jsonPath("beskrivning", equalTo(rapport.getBeskrivning())))
                .andExpect(jsonPath("dataperiodFrom", equalTo(rapport.getDataperiodFrom())))
                .andExpect(jsonPath("rorelsereferensdatum", startsWith(rapport.getRorelsereferensdatum().toString())))
                .andExpect(jsonPath("tidsintervall", equalTo(rapport.getTidsintervall())))
                .andExpect(jsonPath("startDatum", startsWith(rapport.getStartDatum().toString())))
                .andExpect(jsonPath("inledningRubrik", equalTo(rapport.getInledningRubrik())))
                .andExpect(jsonPath("inledningInformation", equalTo(rapport.getInledningInformation())))
                .andExpect(jsonPath("lagesbild", equalTo(rapport.getLagesbild())))
                .andExpect(jsonPath("senastSkickad", startsWith(rapport.getSenastSkickad().toString())))
                .andExpect(jsonPath("skapadDatum", startsWith(rapport.getSkapadDatum().toString())))
                .andExpect(jsonPath("andradDatum", startsWith(rapport.getAndradDatum().toString())))
                .andExpect(jsonPath("andradAvId", equalTo(rapport.getAndradAvId())))

                .andExpect(jsonPath("rapportMottagare", hasSize(rapport.getRapportMottagare().size())))
                .andExpect(jsonPath("rapportMottagare[0].namn", equalTo(rapport.getRapportMottagare().get(0).getNamn())))
                .andExpect(jsonPath("rapportMottagare[0].epost", equalTo(rapport.getRapportMottagare().get(0).getEpost())))

                .andExpect(jsonPath("rapportGraf", hasSize(rapport.getRapportGraf().size())))
                .andExpect(jsonPath("rapportGraf[0].id", equalTo(rapport.getRapportGraf().get(0).getId())))
                .andExpect(jsonPath("rapportGraf[0].rubrik", equalTo(rapport.getRapportGraf().get(0).getRubrik())))
                .andExpect(jsonPath("rapportGraf[0].info", equalTo(rapport.getRapportGraf().get(0).getInfo())))
                .andExpect(jsonPath("rapportGraf[0].matningstyper", containsInAnyOrder(rapport.getRapportGraf().get(0).getMatningstyper().toArray())))
                .andExpect(jsonPath("rapportGraf[0].gransvarden", containsInAnyOrder(rapport.getRapportGraf().get(0).getGransvarden().toArray())))

                .andExpect(jsonPath("rapportGraf", hasSize(rapport.getRapportGraf().size())))
                .andExpect(jsonPath("rapportGraf[1].id", equalTo(rapport.getRapportGraf().get(1).getId())))
                .andExpect(jsonPath("rapportGraf[1].rubrik", equalTo(rapport.getRapportGraf().get(1).getRubrik())))
                .andExpect(jsonPath("rapportGraf[1].info", equalTo(rapport.getRapportGraf().get(1).getInfo())))
                .andExpect(jsonPath("rapportGraf[1].matningstyper", containsInAnyOrder(rapport.getRapportGraf().get(1).getMatningstyper().toArray())))
                .andExpect(jsonPath("rapportGraf[1].gransvarden", containsInAnyOrder(rapport.getRapportGraf().get(1).getGransvarden().toArray())));
    }

    RapportSettings mockRapportSetting() {
        RapportMottagare rapportMottagare = RapportMottagare.builder()
                .namn("Anonym")
                .epost("test@email.se")
                .build();

        RapportGrafSettings graf1 = RapportGrafSettings.builder()
                .rubrik("Min graf")
                .id(1)
                .info("som kan allt")
                .matningstyper(Arrays.asList(new Integer[]{2350, 2343}))
                .gransvarden(new ArrayList<>())
                .build();

        RapportGrafSettings graf2 = RapportGrafSettings.builder()
                .rubrik("Min andra graf")
                .id(2)
                .info("som kan lite mer")
                .matningstyper(Arrays.asList(new Integer[]{2343}))
                .gransvarden(Arrays.asList(new Integer[]{303}))
                .build();

        List<RapportGrafSettings> grafer = Arrays.asList(new RapportGrafSettings[]{graf1, graf2});

        return RapportSettings.builder()
                .id(1)
                .namn("testrapport")
                .aktiv(true)
                .mejlmeddelande("meddelande")
                .beskrivning("Cool rapport")
                .dataperiodFrom(RapportConstants.MONTHS_6)
                .rorelsereferensdatum(LocalDateTime.parse("2016-05-27T11:52"))
                .tidsintervall(Tidsintervall.MANADSVIS)
                .startDatum(LocalDateTime.parse("2016-05-29T12:34"))
                .inledningRubrik("rubrik")
                .inledningInformation("och information")
                .lagesbild("0561d0f7-2332-4247-84e2-2a539d999827")
                .senastSkickad(LocalDateTime.parse("2020-06-29T00:00"))
                .skapadDatum(LocalDateTime.parse("2020-06-29T00:00"))
                .andradDatum(LocalDateTime.parse("2020-06-29T00:00"))
                .andradAvId(1)
                .rapportMottagare(Arrays.asList(rapportMottagare))
                .rapportGraf(grafer)
                .build();
    }
}
