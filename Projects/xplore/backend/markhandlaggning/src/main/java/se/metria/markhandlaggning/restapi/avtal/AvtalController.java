package se.metria.markhandlaggning.restapi.avtal;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markhandlaggning.auth.MarkhandlaggningRole;
import se.metria.markhandlaggning.openapi.api.AvtalApi;
import se.metria.markhandlaggning.service.avtal.AvtalService;

import javax.annotation.security.RolesAllowed;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
public class AvtalController implements AvtalApi {

    @NonNull
    private final AvtalService avtalService;

    public AvtalController(AvtalService avtalService){
        this.avtalService = avtalService;
    }
    
    @Override
    @RolesAllowed(MarkhandlaggningRole.MARKHANDLAGGARE)
    public ResponseEntity<String> getAvtalUrl(UUID id) {
        String url = avtalService.getUrl(id);

        if(url != null){
            return ResponseEntity.ok(url);
        }

        return ResponseEntity.notFound().build();
    }
}
