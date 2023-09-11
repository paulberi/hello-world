package se.metria.markkoll.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@DisplayName("Givet FilController")
@MarkkollMVCTest(controllers = FilController.class)
public class FilControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    FilService filService;

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_fildata() throws Exception {
        // Given
        var data = new FileNameAwareByteArrayResource("data".getBytes(), "filnamn");
        var filId = mockUUID(0);

        when(filService.getData(eq(filId))).thenReturn(data);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/fil/" + filId + "/data")
            .with(csrf())
        );

        // Then
        result.andExpect(status().isOk())
            .andExpect(content().bytes(data.getByteArray()))
            .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }
}
