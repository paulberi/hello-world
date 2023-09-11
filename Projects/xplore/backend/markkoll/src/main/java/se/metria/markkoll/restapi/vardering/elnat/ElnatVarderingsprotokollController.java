package se.metria.markkoll.restapi.vardering.elnat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.markkoll.openapi.api.ElnatVarderingsprotokollApi;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;
import se.metria.markkoll.service.vardering.elnat.bilaga.ElnatBilagaImportService;
import se.metria.markkoll.service.vardering.elnat.bilaga.ElnatBilagaService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ElnatVarderingsprotokollController implements ElnatVarderingsprotokollApi {

    @NonNull
    private final ElnatBilagaImportService elnatBilagaImportService;

    @NonNull
    private final ElnatBilagaService elnatBilagaService;

    @NonNull
    private final ElnatVarderingsprotokollService varderingsprotokollService;

    @Override
    @PreAuthorize("hasElnatVpPermission(#varderingsprotokollId, 'CREATE')")
    public ResponseEntity<ElnatBilagaDto>
    addElnatBilaga(UUID projektId, UUID varderingsprotokollId, MultipartFile file, String typ) throws IOException
    {
        var typEnum = BilagaTypDto.valueOf(typ);
        var bilaga = elnatBilagaImportService.addBilaga(file, typEnum, varderingsprotokollId);

        return ResponseEntity.ok(bilaga);
    }

    @Override
    @PreAuthorize("hasElnatVpPermission(#vpId, 'READ')")
    public ResponseEntity<List<ElnatBilagaDto>> getBilagor(UUID projektId, UUID vpId) throws Exception {
        var bilagor = elnatBilagaService.getAll(vpId);

        return ResponseEntity.ok(bilagor);
    }

    @Override
    @PreAuthorize("hasElnatVpPermission(#varderingsprotokollId, 'READ')")
    public ResponseEntity<ElnatVarderingsprotokollDto> getElnatVarderingsprotokoll(UUID projektId, UUID varderingsprotokollId) {
        var vp = varderingsprotokollService.get(varderingsprotokollId);
        return ResponseEntity.ok(vp);
    }

    @Override
    @PreAuthorize("hasElnatBilagaPermission(#bilagaId, 'DELETE')")
    public ResponseEntity<Void> removeElnatBilaga(UUID projektId, UUID varderingsprotokollId, UUID bilagaId) throws IOException {
        elnatBilagaImportService.removeBilaga(bilagaId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasElnatVpPermission(#varderingsprotokollId, 'WRITE')")
    public ResponseEntity<Void> updateElnatPrisomrade(UUID projektId, UUID varderingsprotokollId, ElnatPrisomradeDto prisomrade)  {
        varderingsprotokollService.updatePrisomrade(varderingsprotokollId, prisomrade);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasElnatVpPermission(#varderingsprotokollId, 'WRITE')")
    public ResponseEntity<Void> updateElnatRotnetto(UUID projektId, UUID varderingsprotokollId, @Valid ElnatRotnettoDto rotnettoDto) throws Exception {
        varderingsprotokollService.updateRotnetto(varderingsprotokollId, rotnettoDto.getErsattning());

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasElnatVpPermission(#elnatVarderingsprotokollDto.getId(), 'WRITE')")
    public ResponseEntity<Void>
    updateElnatVarderingsprotokoll(UUID projektId,
                                   UUID vpId,
                                   ElnatVarderingsprotokollDto elnatVarderingsprotokollDto) {

        varderingsprotokollService.update(elnatVarderingsprotokollDto);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasElnatVpPermission(#varderingsprotokollId, 'WRITE')")
    public ResponseEntity<Void>
    updateElnatVarderingsprotokollConfig(UUID projektId,
                                    UUID varderingsprotokollId,
                                    @Valid ElnatVarderingsprotokollConfigDto varderingsprotokollConfigDto)
    {
        varderingsprotokollService.updateConfig(varderingsprotokollId, varderingsprotokollConfigDto);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasElnatVpPermission(#varderingsprotokollId, 'WRITE')")
    public ResponseEntity<Void>
    updateElnatVarderingsprotokollMetadata(UUID projektId,
                                      UUID varderingsprotokollId,
                                      @Valid ElnatVarderingsprotokollMetadataDto varderingsprotokollMetadataDto)
    {
        varderingsprotokollService.updateMetadata(varderingsprotokollId, varderingsprotokollMetadataDto);

        return ResponseEntity.ok().build();
    }
}
