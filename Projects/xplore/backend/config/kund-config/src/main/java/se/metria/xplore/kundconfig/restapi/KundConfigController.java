package se.metria.xplore.kundconfig.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.kundconfig.openapi.api.KundApi;
import se.metria.xplore.kundconfig.openapi.model.*;
import se.metria.xplore.kundconfig.security.KundConfigRole;
import se.metria.xplore.kundconfig.service.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class KundConfigController implements KundApi {
    
    @NonNull
    private final KundService kundService;

    @NonNull
    private final AbonnemangService abonnemangService;

    @NonNull
    private final AuthService authService;

    @Override
    public ResponseEntity<KundDto> getKund(String kundId) {
        var kund = kundService.getKundById(kundId);
        return ResponseEntity.ok(kund);
    }

    @Override
    @RolesAllowed({KundConfigRole.ADMIN_API, KundConfigRole.GLOBAL_ADMIN})
    public ResponseEntity<KundPageDto> getKundPage(@Valid Integer page, @Valid Integer size) {
        var kundPage = kundService.getKundPage(page, size);
        return ResponseEntity.ok(kundPage);
    }

    @Override
    public ResponseEntity<AbonnemangDto> addAbonnemang(String kundId, @Valid AbonnemangDto abonnemangDto) {
        var abonnemang = abonnemangService.addAbonnemang(kundId, abonnemangDto);
        return ResponseEntity.ok(abonnemang);
    }

    @Override
    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public ResponseEntity<KundDto> createKund(@Valid @NotNull KundInfoDto kundInfo) throws Exception {
        var newKund = kundService.createKund(kundInfo);
        return ResponseEntity.ok(newKund);
    }

    @Override
    @RolesAllowed({KundConfigRole.ADMIN_API, KundConfigRole.GLOBAL_ADMIN})
    public ResponseEntity<List<KundDto>> getKunder() {
        return ResponseEntity.ok(kundService.getKunder());
    }

    @Override
    public ResponseEntity<String> getKundmarke(String kundId) {
        return null;
    }

    @Override
    @RolesAllowed({KundConfigRole.GLOBAL_ADMIN})
    public ResponseEntity<KundDto> updateKund(String kundId, @Valid KundInfoDto kundInfoDto) {
        var updatedKund = kundService.updateKund(kundId, kundInfoDto);

        return ResponseEntity.ok(updatedKund);
    }

    @Override
    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public ResponseEntity<Void> deleteKund(String kundId) throws JsonProcessingException {
        kundService.deleteKund(kundId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> editFastighetsokAuth(String kundId, @Valid FastighetsokAuthDto fastighetsokAuthDto) {
        authService.editFastighetsokAuth(fastighetsokAuthDto);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> editMetriaMapsAuth(String kundId, @Valid MetriaMapsAuthDto metriaMapsAuthDto) {
        authService.editMetriaMapsAuth(metriaMapsAuthDto);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<FastighetsokAuthDto> getFastighetsokAuth(String kundId) {
        var auth = authService.getFastighetsokAuth(kundId);

        return ResponseEntity.ok(auth);
    }

    @Override
    public ResponseEntity<MetriaMapsAuthDto> getMetriaMapsAuth(String kundId) {
        var auth = authService.getMetriaMapsAuth(kundId);

        return ResponseEntity.ok(auth);
    }

    @Override
    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public ResponseEntity<Void> resetGeofenceRules() throws Exception {
        kundService.resetGeofenceRules();
        return ResponseEntity.ok().build();
    }

}
