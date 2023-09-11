package se.metria.finfo.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import se.metria.finfo.openapi.api.SamfallighetsforeningApi;
import se.metria.finfo.openapi.model.RegisterenhetRequestDto;
import se.metria.finfo.openapi.model.SamfallighetsforeningDto;
import se.metria.finfo.service.SamfallighetsforeningService;
import se.metria.finfo.util.ImportJobUtil;

import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SamfallighetsforeningController implements SamfallighetsforeningApi {
    @NonNull
    private final SamfallighetsforeningService samfallighetsforeningService;

    public final static String SAMFALLIGHETSFORENING_BASE_PATH = "samfallighetsforening/";

    @Override
    public ResponseEntity<Void>
    createSamfallighetsforeningJob(RegisterenhetRequestDto request) {
        var resourcePath = ServletUriComponentsBuilder.fromCurrentRequest()
            .replacePath("/api/{base}")
            .buildAndExpand(SAMFALLIGHETSFORENING_BASE_PATH)
            .toUri().toString();

        var jobId = samfallighetsforeningService.createJob(request, resourcePath);

        return ImportJobUtil.jobCreatedResponseEntity(ServletUriComponentsBuilder.fromCurrentRequest(), jobId);
    }

    @Override
    public ResponseEntity<SamfallighetsforeningDto> getSamfallighetsforening(UUID samfUuid) throws Exception {
        var samfDto = samfallighetsforeningService.get(samfUuid);

        return ResponseEntity.ok(samfDto);
    }
}
