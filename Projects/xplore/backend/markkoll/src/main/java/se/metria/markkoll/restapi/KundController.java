package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.KundApi;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.service.KundConfigService;
import se.metria.markkoll.service.admin.KundService;
import se.metria.markkoll.service.LedningsagareService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingConfigService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class KundController implements KundApi {

    @NonNull
    private final KundService kundService;

    @NonNull
    private final KundConfigService kundConfigService;

    @NonNull
    private final LedningsagareService ledningsagareService;

    @NonNull
    private final FiberVarderingConfigService fiberVarderingConfigService;

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'KUND_READ')")
    public ResponseEntity<List<MarkkollUserDto>> getKundUsers(String kundId) {
        var users = kundService.getUsers(kundId);

        return ResponseEntity.ok(users);
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'KUND_READ')")
    public ResponseEntity<List<LedningsagareDto>> getLedningsagare(String kundId) throws Exception {
        var ledningsagare = ledningsagareService.getLedningsagare(kundId);

        return ResponseEntity.ok(ledningsagare);
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'KUND_READ')")
    public ResponseEntity<FiberVarderingConfigDto> getDefaultFiberVarderingConfigForKund(String kundId) throws Exception {
        return ResponseEntity.ok(fiberVarderingConfigService.getFiberVarderingConfigForKund(kundId));
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION')")
    public ResponseEntity<List<FiberVarderingConfigNamnAgareDto>> getAllFiberVarderingConfigsForKund(String kundId) throws Exception {
        return ResponseEntity.ok(fiberVarderingConfigService.getAllFiberVarderingConfigsForKund(kundId));
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION')")
    public ResponseEntity<FiberVarderingConfigDto> updateDefaultFiberVarderingConfigForKund(String kundId, FiberVarderingConfigDto fiberVarderingConfigDto) throws Exception {
        var config = fiberVarderingConfigService.updateDefaultFiberVarderingConfigForKund(kundId, fiberVarderingConfigDto);
        return ResponseEntity.ok(config);
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION')")
    public ResponseEntity<FiberVarderingConfigNamnAgareDto> createFiberVarderingConfig(String kundId, FiberVarderingConfigCreateDto fiberVarderingConfigCreateDto) throws Exception {
        var created = fiberVarderingConfigService.createFiberVarderingConfig(kundId, fiberVarderingConfigCreateDto);
        return ResponseEntity.ok(created);
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION')")
    public ResponseEntity<FiberVarderingConfigNamnAgareDto> updateFiberVarderingConfig(String kundId, FiberVarderingConfigNamnAgareDto configNamnAgareDto) throws Exception {
        var updated = fiberVarderingConfigService.updateFiberVarderingConfig(kundId, configNamnAgareDto);
        return ResponseEntity.ok(updated);
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION')")
    public ResponseEntity<Void> deleteFiberVarderingConfig(String kundId, UUID varderingConfigId) throws Exception {
        fiberVarderingConfigService.deleteFiberVarderingConfig(varderingConfigId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION')")
    public ResponseEntity<LedningsagareDto> addLedningsagare(String kundId, @Valid String namn) throws Exception {
        var ledningsagare = ledningsagareService.addLedningsagare(namn, kundId);

        return ResponseEntity.ok(ledningsagare);
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION')")
    public ResponseEntity<Void> deleteLedningsagare(String kundId, UUID ledningsagareId) throws Exception {
        ledningsagareService.deleteLedningsagare(ledningsagareId, kundId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'KUND_READ')")
    public ResponseEntity<KundDto> getKund(String kundId){
        var kund = kundConfigService.getKund(kundId);
        return ResponseEntity.ok(kund);
    }
}
