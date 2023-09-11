package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.AgareApi;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.service.MarkagareImportService;
import se.metria.markkoll.service.markagare.MarkagareService;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class MarkagareController implements AgareApi {
    @NonNull
    private final MarkagareService markagareService;

    @NonNull
    private final MarkagareImportService markagareImportService;

    @Override
    @PreAuthorize("hasMarkagarePermission(#agareId, 'WRITE')")
    public ResponseEntity<MarkagareDto> updateAgare(UUID agareId, @Valid MarkagareDto markagareDto) {
        var agareUpdated = markagareService.updateAgare(agareId, markagareDto);

        return ResponseEntity.ok(agareUpdated);
    }

    @Override
    @PreAuthorize("hasMarkagarePermission(#agareId, 'DELETE')")
    public ResponseEntity<Void> deleteAgare(UUID agareId) {
        markagareService.deleteAgare(agareId);
        return ResponseEntity.ok().build();
    }
}
