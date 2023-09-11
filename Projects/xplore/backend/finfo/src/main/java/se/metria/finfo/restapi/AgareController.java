package se.metria.finfo.restapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.finfo.openapi.api.AgareApi;
import se.metria.finfo.openapi.model.AgareDto;
import se.metria.finfo.openapi.model.AgareRequestDto;
import se.metria.finfo.service.agare.FinfoAgareService;
import se.metria.finfo.service.agare.FinfoImportService;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AgareController implements AgareApi {

    private final FinfoImportService finfoImportService;

    private final FinfoAgareService finfoAgareService;

    @Override
    public ResponseEntity<List<AgareDto>> getAgare(List<UUID> agareIds) {
        var agare = finfoAgareService.getAgare(agareIds);

        return ResponseEntity.ok(agare);
    }

    @Override
    public ResponseEntity<List<UUID>> importAgare(AgareRequestDto finfoAgareRequestDto) throws Exception {
        var agareIds = finfoImportService.importAgare(finfoAgareRequestDto);

        return ResponseEntity.ok(agareIds);
    }
}
