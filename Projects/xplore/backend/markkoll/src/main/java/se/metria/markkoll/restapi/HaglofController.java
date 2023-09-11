package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.HaglofApi;
import se.metria.markkoll.openapi.model.HaglofImportVarningarDto;
import se.metria.markkoll.service.haglof.HaglofJsonImportService;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class HaglofController implements HaglofApi {

    @NonNull
    private final HaglofJsonImportService haglofJsonImportService;

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<HaglofImportVarningarDto> haglofImport(UUID projektId, @Valid String json) {
        var warnings = haglofJsonImportService.importJson(projektId, json);

        return ResponseEntity.ok(warnings);
    }
}
