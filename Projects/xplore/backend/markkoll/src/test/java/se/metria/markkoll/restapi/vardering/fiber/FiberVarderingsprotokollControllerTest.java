package se.metria.markkoll.restapi.vardering.fiber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.openapi.model.FiberVarderingConfigDto;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingConfigService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.util.ResponseBodyMatchers.responseBody;

@DisplayName("Givet VarderingsprotokollController")
@MarkkollMVCTest(controllers = FiberVarderingsprotokollController.class)
@WithMockUserMarkhandlaggare
class FiberVarderingsprotokollControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    FiberVarderingsprotokollService fiberVarderingsprotokollService;

    @MockBean
    FiberVarderingConfigService fiberVarderingConfigService;

    @Test
    void så_ska_det_gå_att_hämta_en_värderingskonfiguration() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var vpId = mockUUID(1);
        var cfg = new FiberVarderingConfigDto();

        when(fiberVarderingConfigService.getFiberVarderingConfig(eq(vpId))).thenReturn(cfg);
        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/fiber/{projektId}/varderingsprotokoll/{vpId}/varderingconfig", projektId, vpId)
            .with(csrf()));

        // Then
        result
            .andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(cfg, cfg.getClass()));
    }
}