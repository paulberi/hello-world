package se.metria.finfo.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.finfo.openapi.api.ImportJobApi;
import se.metria.finfo.openapi.model.ImportJobInfoDto;
import se.metria.finfo.service.importjob.ImportJobService;

import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ImportJobController implements ImportJobApi {
    @NonNull
    private ImportJobService importJobService;

    @Override
    public ResponseEntity<ImportJobInfoDto> getImportJobStatus(UUID jobId) throws Exception {
        var jobInfo = importJobService.getJobInfo(jobId);

        return ResponseEntity.ok(jobInfo);
    }
}
