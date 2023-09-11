package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.AvtalApi;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static se.metria.markkoll.util.HttpUtil.responseHeaders;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AvtalController implements AvtalApi {
    @NonNull
    private final AvtalService avtalService;
    @NonNull
    private final DokumentGeneratorService dokumentGeneratorService;
    @NonNull
    private final ElnatVarderingsprotokollService varderingsprotokollService;
    @NonNull
    private final FiberVarderingsprotokollService fiberVarderingsprotokollService;

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<Integer> avtalCount(UUID projektId, @Valid FastighetsfilterDto filter) throws Exception {
        var count = avtalService.avtalCount(projektId, filter);

        return ResponseEntity.ok(count);
    }

    @Override
    @PreAuthorize("hasAvtalsjobbPermission(#avtalsjobbId, 'READ')")
    public ResponseEntity<AvtalsjobbProgressDto> avtalsjobbProgress(UUID avtalsjobbId) {
        var progress = avtalService.getAvtalsjobbProgress(avtalsjobbId);

        return ResponseEntity.ok(progress);
    }

    @Override
    public ResponseEntity<Void> cancelAvtalsjobb(UUID projektId, UUID avtalsjobbId) throws Exception {
        avtalService.cancelAvtalsjobb(avtalsjobbId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAvtalPermission(#avtalId, 'READ')")
    public ResponseEntity<ElnatVarderingsprotokollDto> getElnatVarderingsprotokollWithAvtalId(UUID projektId, UUID avtalId)  {
        var vpOpt = varderingsprotokollService.getWithAvtalId(avtalId);
        return vpOpt.isPresent() ? ResponseEntity.ok(vpOpt.get()) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PreAuthorize("hasAvtalPermission(#avtalId, 'READ')")
    // TODO: Behöver inte styras på projektnivå, räcker med avtal
    public ResponseEntity<FiberVarderingsprotokollDto> getFiberVarderingsprotokollWithAvtalId(UUID projektId, UUID avtalId) throws Exception {
        var vpOpt = fiberVarderingsprotokollService.getWithAvtalId(avtalId);
        return vpOpt.isPresent() ? ResponseEntity.ok(vpOpt.get()) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PreAuthorize("hasAvtalPermission(#avtalId, 'READ')")
    public ResponseEntity<Resource> getVarderingSkogsmarkAvtal(UUID projektId, UUID avtalId) throws Exception {
        var excel = dokumentGeneratorService.getVarderingSkogsmark(avtalId);

        var headers = responseHeaders(excel.getFilename(), MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(excel);
    }

    @Override
    @PreAuthorize("hasAvtalPermission(#avtalId, 'WRITE')")
    public ResponseEntity<Void> setTillvaratagandeTyp(UUID avtalId, TillvaratagandeTypDto tillvaratagandeTyp) throws Exception {
        avtalService.setTillvaratagandeTyp(avtalId, tillvaratagandeTyp);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAvtalPermission(#avtalId, 'WRITE')")
    public ResponseEntity<Void> setSkogsfastighet(UUID projektId, UUID avtalId, @NotNull @Valid Boolean skogsfastighet) {
        avtalService.setSkogsfastighet(avtalId, skogsfastighet);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAvtalPermission(#avtalId, 'WRITE')")
    public ResponseEntity<AvtalMetadataDto> setAvtalMetadata(UUID avtalId, @Valid AvtalMetadataDto avtalMetadataDto) throws Exception {
        var avtalMetadata = avtalService.setAvtalMetadata(avtalId, avtalMetadataDto);
        return ResponseEntity.ok(avtalMetadata);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<Resource> getAvtal(UUID projektId, UUID fastighetsId) throws Exception {
        var fastighetIdObj = fastighetsId;

        Resource zip = avtalService.getAvtalZipFastighet(projektId, fastighetIdObj);

        HttpHeaders headers = responseHeaders(zip.getFilename(), MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
            .headers(headers)
            .body(zip);
    }

    @Override
    @PreAuthorize("hasAvtalsjobbPermission(#avtalsjobbId, 'READ')")
    public ResponseEntity<Resource> getAvtalForProjekt(UUID projektId, UUID avtalsjobbId) {
        var avtal = this.avtalService.getAvtalZipAvtalsjobb(projektId, avtalsjobbId);

        if (avtal.isPresent()) {
            HttpHeaders headers = responseHeaders(avtal.get().getFilename(), MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(avtal.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<List<EditElnatVpDto>> getEditElnatVpByUUID(UUID projektId, FastighetsfilterDto filter) throws Exception {
        var editElnatVpDtos = avtalService.getEditElnatVp(projektId, filter);
        return ResponseEntity.ok(editElnatVpDtos);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'WRITE')")
    public ResponseEntity<List<EditElnatVpDto>> updateElnatVpAndAvtalMetadata(UUID projektId, List<EditElnatVpDto> editElnatVpDto) throws Exception {
        var editElnatVpDtos = avtalService.updateEditElnatVpAndAvtalMetadata(projektId, editElnatVpDto);
        return ResponseEntity.ok(editElnatVpDtos);
    }
}
