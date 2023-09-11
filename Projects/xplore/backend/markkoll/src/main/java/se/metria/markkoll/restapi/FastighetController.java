package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.FastighetApi;
import se.metria.markkoll.openapi.model.FastighetDelomradeInfoDto;
import se.metria.markkoll.openapi.model.FastighetsProjektInfoDto;
import se.metria.markkoll.openapi.model.FastighetsinfoDto;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.fastighet.FastighetService;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class FastighetController implements FastighetApi {
    @NonNull
    private final FastighetService fastighetsService;

    @NonNull
    private final MarkagareService markagareService;

    @Override
    @Transactional
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<FastighetsinfoDto> getFastighetsinfo(UUID id, UUID projektId) {
        var fastighetsinfo = fastighetsService.fastighetsinfo(id, projektId);
        return ResponseEntity.ok(fastighetsinfo);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'WRITE')")
    public ResponseEntity<Integer> setFastighetProjektInfo(UUID id, UUID projektId, FastighetsProjektInfoDto info){
        var result = fastighetsService.setFastighetErsattning(id, projektId, info);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<FastighetDelomradeInfoDto>> fetchDelomradenForFastighet(UUID fastighetId) throws Exception {
        return ResponseEntity.ok(fastighetsService.fetchDelomradenForFastighet(fastighetId));
    }
}