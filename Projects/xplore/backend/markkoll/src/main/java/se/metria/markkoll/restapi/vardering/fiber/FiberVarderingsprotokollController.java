package se.metria.markkoll.restapi.vardering.fiber;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.FiberVarderingsprotokollApi;
import se.metria.markkoll.openapi.model.FiberVarderingConfigDto;
import se.metria.markkoll.openapi.model.FiberVarderingsprotokollDto;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingConfigService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollService;

import javax.ws.rs.HEAD;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class FiberVarderingsprotokollController implements FiberVarderingsprotokollApi {
    @NonNull
    private final FiberVarderingsprotokollService fiberVarderingsprotokollService;

    @NonNull
    private final FiberVarderingConfigService fiberVarderingConfigService;

    @Override
    @PreAuthorize("hasFiberVpPermission(#fiberVarderingsprotokollDto.getId(), 'WRITE')")
    public ResponseEntity<Void>
    updateFiberVarderingsprotokoll(UUID projektId,
                                   UUID fiberVpId,
                                   FiberVarderingsprotokollDto fiberVarderingsprotokollDto) {

        fiberVarderingsprotokollService.update(fiberVarderingsprotokollDto);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasFiberVpPermission(#fiberVpId, 'READ')")
    public ResponseEntity<FiberVarderingConfigDto> getFiberVarderingConfig(UUID projektId, UUID fiberVpId) throws Exception {
        var varderingConfig = fiberVarderingConfigService.getFiberVarderingConfig(fiberVpId);

        return ResponseEntity.ok(varderingConfig);
    }
}
