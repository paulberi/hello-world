package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.SamfallighetApi;
import se.metria.markkoll.service.fastighet.SamfService;

import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SamfallighetController implements SamfallighetApi {
    @NonNull
    SamfService samfService;

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<Void> importMoreSamfInfo(UUID projektId, UUID samfId) throws Exception {
        samfService.importMoreSamfInfo(projektId, samfId);

        return ResponseEntity.ok().build();
    }
}
