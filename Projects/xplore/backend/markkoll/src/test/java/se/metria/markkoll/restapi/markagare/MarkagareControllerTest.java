package se.metria.markkoll.restapi.markagare;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.markkoll.testdata.TestData;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.restapi.MarkagareController;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.MarkagareImportService;
import se.metria.markkoll.service.markagare.MarkagareService;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.util.ResponseBodyMatchers.responseBody;

@DisplayName("Givet AgareController")
@MarkkollMVCTest(controllers = MarkagareController.class)
public class MarkagareControllerTest {

    final String BASE_PATH = "/api/agare/";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MarkagareService markagareService;

    @MockBean
    private MarkagareImportService markagareImportService;

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandlaggare_kunna_uppdatera_en_markägare() throws Exception {
        // Given
        var agareId = mockUUID(0);
        var agare = TestData.mockAgare(agareId);

        when(markagareService.updateAgare(any(), any())).thenReturn(agare);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .put(BASE_PATH + agareId)
                .content(objectMapper.writeValueAsString(agare))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(agare, agare.getClass()));

        verify(markagareService, times(1)).updateAgare(eq(agareId),
                eq(agare));
    }
}
