package se.metria.markkoll.restapi.avtal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.openapi.model.AvtalsjobbProgressDto;
import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;
import se.metria.markkoll.openapi.model.FastighetsfilterDto;
import se.metria.markkoll.restapi.AvtalController;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.projekt.ProjektService;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.util.ResponseBodyMatchers.responseBody;

@DisplayName("Givet AvtalController")
@MarkkollMVCTest(controllers = AvtalController.class)
public class AvtalControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AvtalService avtalService;

    @MockBean
    private DokumentGeneratorService dokumentGeneratorService;

    @MockBean
    private ElnatVarderingsprotokollService varderingsprotokollService;

    @MockBean
    private FiberVarderingsprotokollService fiberVarderingsprotokollService;

    @MockBean
    private ProjektService projektService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandläggare_kunna_hämta_progressen_för_ett_avtalsjobb() throws Exception {
        // Given
        var avtalsjobbId = mockUUID(0);
        var progress = new AvtalsjobbProgressDto()
                .total(13)
                .generated(2)
                .status(AvtalsjobbStatusDto.DONE);

        when(avtalService.getAvtalsjobbProgress(any())).thenReturn(progress);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/avtal/" + avtalsjobbId + "/progress")
                .with(csrf())
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(progress, progress.getClass()));

        verify(avtalService).getAvtalsjobbProgress(eq(avtalsjobbId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandläggare_kunna_hämta_ett_avtal() throws Exception {
        // Given
        var projektId = mockUUID(1);
        var avtalsjobbId = mockUUID(0);
        var path = "src/test/resources/testData/oneco-skanova-owner.zip";
        var dataExpect = new ByteArrayResource(Files.readAllBytes(Paths.get(path)));

        when(avtalService.getAvtalZipAvtalsjobb(any(), (UUID)any())).thenReturn(Optional.of(dataExpect));

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/projekt/"+projektId+"/avtal/" + avtalsjobbId)
                .with(csrf())
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().bytes(dataExpect.getByteArray()))
                .andExpect(header().exists("Content-Disposition"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM.toString()));

        verify(avtalService).getAvtalZipAvtalsjobb(eq(projektId), eq(avtalsjobbId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_sätta_att_ett_avtal_är_för_en_skogsfastighet() throws Exception {
        // Given
        var avtalId = mockUUID(0);
        var projektId = mockUUID(1);
        final var skogsfastighet = true;

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .patch("/api/projekt/"+projektId+"/avtal/"+avtalId+"/skogsfastighet?skogsfastighet="+skogsfastighet)
                .with(csrf())
        );

        // Then
        result.andExpect(status().isOk());

        verify(avtalService).setSkogsfastighet(eq(avtalId), eq(skogsfastighet));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_exportera_en_skogsmarksvärdering() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var avtalId = mockUUID(1);
        var data = "en fil med data".getBytes();

        when(dokumentGeneratorService.getVarderingSkogsmark(eq(avtalId))).thenReturn(new ByteArrayResource(data));
        when(avtalService.getFastighetsBeteckning(avtalId)).thenReturn("HÖLJES 1:129");

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/projekt/"+projektId+"/avtal/"+avtalId+"/skogsfastighet")
                .with(csrf())
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().bytes(data))
                .andExpect(header().exists("Content-Disposition"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM.toString()));

        verify(dokumentGeneratorService).getVarderingSkogsmark(eq(avtalId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_ett_avtals_värderingsprotokoll() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var avtalId = mockUUID(1);
        var vp = new ElnatVarderingsprotokollDto().id(mockUUID(2));

        when(varderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(Optional.of(vp));

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/elnat/{projektId}/avtal/{avtalId}/varderingsprotokoll", projektId, avtalId)
            .with(csrf())
        );

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(vp, ElnatVarderingsprotokollDto.class));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_204_returneras_om_ett_avtal_saknar_värderingsprotokoll() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var avtalId = mockUUID(1);

        when(varderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(Optional.empty());

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/elnat/{projektId}/avtal/{avtalId}/varderingsprotokoll", projektId, avtalId)
            .with(csrf())
        );

        // Then
        result.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_erhålla_antalet_avtal_i_ett_projekt() throws Exception {
        // Given
        var filter = new FastighetsfilterDto();
        var count = 11;
        var projektId = mockUUID(0);

        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
        Map<String, String> fieldMap = objectMapper.convertValue(filter, new TypeReference<Map<String, String>>() {});
        valueMap.setAll(fieldMap);

        when(avtalService.avtalCount(eq(projektId), eq(filter))).thenReturn(count);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/{projektId}/avtal/count/", projektId)
            .queryParams(valueMap)
            .with(csrf())
        );

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(count, Integer.class));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_ett_avtal() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var pdf = new FileNameAwareByteArrayResource("pdf".getBytes(), "filnamn");

        when(avtalService.getAvtalZipFastighet(eq(projektId), eq(fastighetId))).thenReturn(pdf);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/{projektId}/fastighet/{fastighet}/avtal", projektId, fastighetId)
            .with(csrf())
        );

        // Then
        result.andExpect(status().isOk())
            .andExpect(content().bytes(pdf.getByteArray()))
            .andExpect(header().string("Content-Disposition", "inline;filename=" + "filnamn"))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM.toString()));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_avbryta_ett_avtalsjobb() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var avtalsjobbId = mockUUID(1);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .delete("/api/projekt/{projektId}/avtal/{avtalsjobbId}", projektId, avtalsjobbId)
            .with(csrf())
        );

        // Then
        result.andExpect(status().isOk());

        verify(avtalService).cancelAvtalsjobb(eq(avtalsjobbId));
    }
}
