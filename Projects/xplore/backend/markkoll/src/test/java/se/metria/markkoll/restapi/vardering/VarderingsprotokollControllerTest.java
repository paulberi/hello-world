package se.metria.markkoll.restapi.vardering;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.openapi.model.ElnatBilagaDto;
import se.metria.markkoll.openapi.model.BilagaTypDto;
import se.metria.markkoll.openapi.model.ElnatPrisomradeDto;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;
import se.metria.markkoll.restapi.vardering.elnat.ElnatVarderingsprotokollController;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;
import se.metria.markkoll.service.vardering.elnat.bilaga.ElnatBilagaImportService;
import se.metria.markkoll.service.vardering.elnat.bilaga.ElnatBilagaService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.testdata.VPTestData.*;
import static se.metria.markkoll.util.ResponseBodyMatchers.responseBody;

@DisplayName("Givet VarderingsprotokollController")
@MarkkollMVCTest(controllers = ElnatVarderingsprotokollController.class)
@WithMockUserMarkhandlaggare
public class VarderingsprotokollControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    ElnatBilagaImportService elnatBilagaImportService;

    @MockBean
    ElnatBilagaService elnatBilagaService;

    @MockBean
    ElnatVarderingsprotokollService varderingsprotokollService;

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void beforeEach() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
        mapper = mapper.setDateFormat(df);
    }

    @Test
    void så_ska_det_gå_att_hämta_ett_värderingsprotokoll() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var vpId = mockUUID(1);
        var vp = varderingsprotokollDto(mockUUID(2));

        when(varderingsprotokollService.get(any())).thenReturn(vp);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/elnat/{projektId}/varderingsprotokoll/{vpId}", projektId, vpId)
            .with(csrf()));

        // Then
        result
            .andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(vp, vp.getClass()));

        verify(varderingsprotokollService).get(eq(vpId));
    }

    @Test
    void så_ska_det_gå_att_uppdatera_varderingsprotokollets_metadata() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var vpId = mockUUID(1);
        var metadataDto = vpMetadataDto();

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .put("/api/projekt/elnat/{projektId}/varderingsprotokoll/{vpId}/meta", projektId, vpId)
            .content(mapper.writeValueAsString(metadataDto))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(varderingsprotokollService).updateMetadata(eq(vpId), eq(metadataDto));
    }

    @Test
    void så_ska_det_gå_att_uppdatera_varderingsprotokollets_beräkningskonfiguration() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var vpId = mockUUID(1);
        var configDto = vpConfigDto();

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .put("/api/projekt/elnat/{projektId}/varderingsprotokoll/{vpId}/config", projektId, vpId)
            .content(mapper.writeValueAsString(configDto))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(varderingsprotokollService).updateConfig(eq(vpId), eq(configDto));
    }

    @Test
    void så_ska_det_gå_att_uppdatera_prisområde() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var vpId = mockUUID(1);
        var prisomrade = ElnatPrisomradeDto.TILLVAXTOMRADE_3;

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .put("/api/projekt/elnat/{projektId}/varderingsprotokoll/{vpId}/prisomrade", projektId, vpId)
            .queryParam("prisomrade", prisomrade.toString())
            .with(csrf()));

        // Then
        result.andExpect(status().isOk());
        verify(varderingsprotokollService).updatePrisomrade(eq(vpId), eq(prisomrade));
    }

    @Test
    void så_ska_det_gå_att_lägga_till_en_bilaga_till_ett_värderingsprotokoll() throws Exception {
        // Given
        var file = new MockMultipartFile("file", "file", MediaType.APPLICATION_OCTET_STREAM.toString(),
            "file".getBytes());
        var typ = BilagaTypDto.AKERNORM_74;
        var typMultipart = new MockMultipartFile("typ", "typ", MediaType.APPLICATION_JSON.toString(),
            typ.toString().getBytes());
        var vpId = mockUUID(0);
        var projektId = mockUUID(2);
        var bilaga = new ElnatBilagaDto().id(mockUUID(1));

        when(elnatBilagaImportService.addBilaga(any(), eq(typ), eq(vpId))).thenReturn(bilaga);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .multipart("/api/projekt/elnat/{projektId}/varderingsprotokoll/{vpId}/bilaga", projektId, vpId)
            .file(file)
            .file(typMultipart)
            .with(csrf())
        );

        // Then
        result
            .andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(bilaga, ElnatBilagaDto.class));
    }

    @Test
    void så_ska_det_gå_att_ta_bort_en_bilaga_från_ett_värderingsprotokoll() throws Exception {
        // Given
        var file = new MockMultipartFile("file", "file", MediaType.APPLICATION_OCTET_STREAM.toString(),
            "file".getBytes());
        var typ = BilagaTypDto.AKERNORM_74;
        var bilagaId = mockUUID(0);
        var vpId = mockUUID(1);
        var projektId = mockUUID(2);
        var vp = new ElnatVarderingsprotokollDto().id(mockUUID(1));



        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .delete("/api/projekt/elnat/{projektId}/varderingsprotokoll/{vpId}/bilaga/{bilagaId}",
                projektId, vpId, bilagaId)
            .with(csrf())
        );

        // Then
        result
            .andExpect(status().isOk());

        verify(elnatBilagaImportService).removeBilaga(eq(bilagaId));
    }

    @Test
    void så_ska_det_gå_att_hämta_bilagor_till_ett_värderingsprotokoll() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var vpId = mockUUID(1);
        var bilagor = Arrays.asList(
            new ElnatBilagaDto().id(mockUUID(2)),
            new ElnatBilagaDto().id(mockUUID(3))
        );

        when(elnatBilagaService.getAll(eq(vpId))).thenReturn(bilagor);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/elnat/{projektId}/varderingsprotokoll/{vpId}/bilaga", projektId, vpId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsListAsJson(bilagor, ElnatBilagaDto.class));
    }
}
