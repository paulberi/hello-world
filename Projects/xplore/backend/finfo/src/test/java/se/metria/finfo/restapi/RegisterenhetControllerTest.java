package se.metria.finfo.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.finfo.data.SamfallighetTestdata;
import se.metria.finfo.openapi.model.RegisterenhetRequestDto;
import se.metria.finfo.openapi.model.SamfallighetDto;
import se.metria.finfo.service.registerenhet.SamfallighetService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.finfo.restapi.RegisterenhetController.ALLMAN_MINI_BASE_PATH;
import static se.metria.finfo.util.ResponseBodyMatchers.responseBody;

@WebMvcTest(RegisterenhetController.class)
public class RegisterenhetControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    SamfallighetService samfallighetService;

    private final String BASE_PATH = "/api/registerenhet/";

    @Test
    void createAllmanMiniJob() throws Exception {
        // Given
        var request = new RegisterenhetRequestDto();
        request.setNyckel("e0e044a6-9b5b-4295-b67b-23709a6f147a");
        request.setUsername("username");
        request.setPassword("password");
        request.setKundmarke("finfo");

        var jobId = UUID.fromString("043b114d-99f4-41fe-ae45-29e08a5c073d");

        when(samfallighetService.createJob(eq(request), eq("http://localhost/api/" + ALLMAN_MINI_BASE_PATH))).thenReturn(jobId);

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .post(BASE_PATH + "allmanmini")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .with(csrf()));

        // Then
        var locationUrl = "http://localhost/api/importjob/" + jobId;
        result.andExpect(status().is(202))
            .andExpect(header().string("Location", locationUrl));
    }

    @Test
    void getAllmanMini() throws Exception {
        // Given
        var registerenhet = SamfallighetTestdata.samfallighetDto();
        var registerenhetId = UUID.fromString("3c36c863-618d-4773-bf19-c4043b5599b7");

        when(samfallighetService.get(eq(registerenhetId))).thenReturn(registerenhet);

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .get(BASE_PATH + "allmanmini/{registerenhetId}", registerenhetId)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(registerenhet, SamfallighetDto.class));
    }
}
