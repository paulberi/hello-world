package se.metria.markkoll.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.openapi.model.LedningsagareDto;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.KundConfigService;
import se.metria.markkoll.service.admin.KundService;
import se.metria.markkoll.service.LedningsagareService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingConfigService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.util.ResponseBodyMatchers.responseBody;

@DisplayName("Givet KundController")
@MarkkollMVCTest(controllers = KundController.class)
class KundControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    KundConfigService kundConfigService;

    @MockBean
    KundService kundService;

    @MockBean
    FiberVarderingConfigService fiberVarderingConfigService;

    @MockBean
    LedningsagareService ledningsagareService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_ledningsägare_för_en_kund() throws Exception {
        // Given
        var kundId = "kundId";
        var ledningsagare = Arrays.asList(
            new LedningsagareDto().id(mockUUID(0)).namn("ledningsägare 0"),
            new LedningsagareDto().id(mockUUID(1)).namn("ledningsägare 1")
        );

        when(ledningsagareService.getLedningsagare(eq(kundId))).thenReturn(ledningsagare);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/kund/{kundId}/ledningsagare", kundId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsListAsJson(ledningsagare, LedningsagareDto.class));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_lägga_till_en_ny_ledningsägare() throws Exception{
        // Given
        var kundId = "kundId";
        var namn = "ledningsägare";
        var ledningsagare = new LedningsagareDto().id(mockUUID(0)).namn(namn);

        when(ledningsagareService.addLedningsagare(eq(namn), eq(kundId))).thenReturn(ledningsagare);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .post("/api/kund/{kundId}/ledningsagare", kundId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(namn)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(ledningsagare, LedningsagareDto.class));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_ta_bort_en_ledningsägare() throws Exception {
        // Given
        var kundId = "kundId";
        var ledningsagareId = mockUUID(0);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .delete("/api/kund/{kundId}/ledningsagare/{ledningsagareId}", kundId, ledningsagareId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk());
        verify(ledningsagareService).deleteLedningsagare(eq(ledningsagareId), eq(kundId));
    }
}