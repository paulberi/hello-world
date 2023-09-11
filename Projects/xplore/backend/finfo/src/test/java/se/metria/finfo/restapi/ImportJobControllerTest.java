package se.metria.finfo.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.finfo.openapi.model.ImportJobInfoDto;
import se.metria.finfo.openapi.model.ImportJobStatusDto;
import se.metria.finfo.service.importjob.ImportJobService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.finfo.util.ResponseBodyMatchers.responseBody;

@WebMvcTest(ImportJobController.class)
class ImportJobControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    ImportJobService importJobService;

    private final String BASE_PATH = "/api/importjob/";

    @Test
    void getImportJobStatus() throws Exception {
        // Given
        var jobId = UUID.fromString("e0e044a6-9b5b-4295-b67b-23709a6f147a");

        var jobStatus = ImportJobStatusDto.IN_PROGRESS;
        var jobInfo = new ImportJobInfoDto();
        jobInfo.setStatus(jobStatus);
        jobInfo.setType("032");

        when(importJobService.getJobInfo(eq(jobId))).thenReturn(jobInfo);

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .get(BASE_PATH + jobId)
                .with(csrf()));

        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(jobInfo, ImportJobInfoDto.class));
    }
}