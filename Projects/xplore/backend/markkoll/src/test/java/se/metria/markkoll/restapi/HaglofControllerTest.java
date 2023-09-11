package se.metria.markkoll.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.openapi.model.HaglofImportVarningarDto;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.haglof.HaglofJsonImportService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.util.ResponseBodyMatchers.responseBody;

@DisplayName("Givet HaglofController")
@MarkkollMVCTest(controllers = HaglofController.class)
public class HaglofControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private HaglofJsonImportService haglofJsonImportService;

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_utföra_en_import_från_haglof() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var json = "json";
        var warnings = new HaglofImportVarningarDto();

        when(haglofJsonImportService.importJson(eq(projektId), eq(json))).thenReturn(warnings);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .post("/api/projekt/{projektId}/haglof", projektId)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // Then
        verify(haglofJsonImportService).importJson(eq(projektId), eq(json));

        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(warnings, warnings.getClass()));
    }
}
