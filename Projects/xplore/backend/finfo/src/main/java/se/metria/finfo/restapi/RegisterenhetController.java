package se.metria.finfo.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import se.metria.finfo.openapi.api.RegisterenhetApi;
import se.metria.finfo.openapi.model.RegisterenhetRequestDto;
import se.metria.finfo.openapi.model.SamfallighetDto;
import se.metria.finfo.service.registerenhet.SamfallighetService;
import se.metria.finfo.util.ImportJobUtil;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class RegisterenhetController implements RegisterenhetApi {

    @NonNull
    private final SamfallighetService samfallighetService;

    public final static String ALLMAN_MINI_BASE_PATH = "registerenhet/allmanmini/";

    @Override
    public ResponseEntity<Void> createAllmanMiniJob(@Valid RegisterenhetRequestDto request) {
        var resourcePath = ServletUriComponentsBuilder.fromCurrentRequest()
            .replacePath("/api/{base}")
            .buildAndExpand(ALLMAN_MINI_BASE_PATH)
            .toUri().toString();

        var jobId = samfallighetService.createJob(request, resourcePath);

        return ImportJobUtil.jobCreatedResponseEntity(ServletUriComponentsBuilder.fromCurrentRequest(), jobId);
    }

    @Override
    public ResponseEntity<SamfallighetDto> getAllmanMini(UUID samfUuid) {
        var samfDto = samfallighetService.get(samfUuid);

        return ResponseEntity.ok(samfDto);
    }
}
