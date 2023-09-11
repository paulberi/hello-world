package se.metria.markkoll.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.InfobrevService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@DisplayName("Givet InfobrevController")
@MarkkollMVCTest(controllers = InfobrevController.class)
public class InfobrevControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    InfobrevService infobrevService;

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_avbryta_ett_infobrevsjobb() throws Exception {
        // Given
        var infobrevsjobbId = mockUUID(1);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .delete("/api/infobrev/{infobrevsjobbId}", infobrevsjobbId)
            .with(csrf())
        );

        // Then
        result.andExpect(status().isOk());

        verify(infobrevService).cancelInfobrevsjobb(eq(infobrevsjobbId));
    }
}
