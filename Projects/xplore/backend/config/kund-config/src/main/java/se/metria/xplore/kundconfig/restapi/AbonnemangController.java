package se.metria.xplore.kundconfig.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.kundconfig.openapi.api.AbonnemangApi;
import se.metria.xplore.kundconfig.service.AbonnemangService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class AbonnemangController implements AbonnemangApi {
    @NonNull
    private final AbonnemangService abonnemangService;

    @Override
    public ResponseEntity<Void> deleteAbonnemang(UUID abonnemangId) {
        abonnemangService.deleteAbonnemang(abonnemangId);
        return ResponseEntity.ok().build();
    }
}
