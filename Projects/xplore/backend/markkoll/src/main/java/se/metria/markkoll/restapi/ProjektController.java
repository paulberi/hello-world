package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.api.ProjektApi;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.security.MkPermission;
import se.metria.markkoll.service.AvtalsinstallningarService;
import se.metria.markkoll.service.BeredareService;
import se.metria.markkoll.service.dokument.forteckninggenerator.ForteckningGeneratorService;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.intrang.IntrangImportService;
import se.metria.markkoll.service.intrang.IntrangService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.projekt.ElnatProjektService;
import se.metria.markkoll.service.projekt.FiberProjektService;
import se.metria.markkoll.service.projekt.ProjektImportServiceFactory;
import se.metria.markkoll.service.projekt.ProjektService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;
import se.metria.markkoll.util.HttpUtil;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static se.metria.markkoll.util.CollectionUtil.isNullOrEmpty;
import static se.metria.markkoll.util.HttpUtil.responseHeaders;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjektController implements ProjektApi {
    @NonNull
    private final AvtalsinstallningarService avtalsinstallningarService;

    @NonNull
    private final FastighetService fastighetService;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final ProjektService projektService;

    @NonNull
    private final FiberProjektService fiberProjektService;

    @NonNull
    private final ElnatProjektService elnatProjektService;

    @NonNull
    private final ProjektImportServiceFactory projektImportServiceFactory;

    @NonNull
    private final IntrangService intrangService;

    @NonNull
    private final MarkagareService markagareService;

    @NonNull
    private final DokumentGeneratorService dokumentGeneratorService;

    @NonNull
    private final LoggService loggService;

    @NonNull
    private final UserService userService;

    @NonNull
    private final BeredareService beredareService;

    @NonNull
    private final IntrangImportService intrangImportService;

    @NonNull
    private final AvtalService avtalService;

    @NonNull
    private final ForteckningGeneratorService forteckningGeneratorService;

    @Override
    @Transactional
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<ProjektInfoDto> getProjekt(@PathVariable UUID projektId) {
		return ResponseEntity.of(fiberProjektService.getProjektInfo(projektId)
				.or(() -> elnatProjektService.getProjektInfo(projektId))
		);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<ProjektLoggPageDto>
    getProjektLoggPage(UUID projektId,
                       Integer pageNum,
                       Integer size,
                       List<ProjektLoggFilterDto> filter,
                       Sort.Direction sortDirection)
    {
        var page = loggService.getProjektLoggPage(projektId, pageNum, size, filter,
            sortDirection);

        return ResponseEntity.ok(page);
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'CREATE')")
    @Transactional
    public ResponseEntity<FiberProjektDto>
    createFiberProjekt(String kundId, FiberProjektDto fiberProjekt, MultipartFile file) {
        FiberProjektDto newFiberProjekt = fiberProjektService.createProjekt(kundId, fiberProjekt);

        if (file != null) {
            try {
                var projektId = newFiberProjekt.getProjektInfo().getId();
                var resource = new FileNameAwareByteArrayResource(file.getBytes(), file.getOriginalFilename());
                var version = intrangImportService.importShape(newFiberProjekt.getProjektInfo().getId(), resource,
                    fiberProjekt.getIndataTyp(), fiberProjekt.getBuffert());
                var fastighetIds = fastighetService.fastigheterFromVersion(version);
                projektImportServiceFactory.getProjektImportService(ProjektTypDto.FIBER)
                    .updateProjektVersion(projektId, version.getId(), fastighetIds);
            }
            catch(Exception e) {
                throw new MarkkollException(MarkkollError.IMPORT_ERROR, e);
            }
        }

        return ResponseEntity.ok(newFiberProjekt);
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'CREATE')")
    @Transactional
    public ResponseEntity<ElnatProjektDto>
    createElnatProjekt(String kundId, ElnatProjektDto elnatProjekt, MultipartFile file) {
        ElnatProjektDto newElnatProjekt = elnatProjektService.createProjekt(kundId, elnatProjekt);

        if (file != null) {
            try {
                var projektId = newElnatProjekt.getProjektInfo().getId();
                var resource = new FileNameAwareByteArrayResource(file.getBytes(), file.getOriginalFilename());
                var version = intrangImportService.importShape(newElnatProjekt.getProjektInfo().getId(), resource,
                    elnatProjekt.getIndataTyp(), elnatProjekt.getBuffert());
                var fastighetIds = fastighetService.fastigheterFromVersion(version);
                projektImportServiceFactory.getProjektImportService(elnatProjekt.getProjektInfo().getProjektTyp())
                    .updateProjektVersion(projektId, version.getId(), fastighetIds);
            }
            catch(Exception e) {
                throw new MarkkollException(MarkkollError.IMPORT_ERROR, e);
            }
        }

        return ResponseEntity.ok(newElnatProjekt);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<FiberProjektDto> getFiberProjekt(UUID projektId) {
        return  ResponseEntity.of(fiberProjektService.getProjektDto(projektId));
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<Resource> generateForteckning(UUID projektId, FilterAndTemplateDto filterAndTemplateDto) throws Exception {
        var resource = forteckningGeneratorService.generate(projektId, filterAndTemplateDto);

        var headers = HttpUtil.responseHeaders(resource.getFilename(), MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<ElnatProjektDto> getElnatProjekt(UUID projektId) {
        return  ResponseEntity.of(elnatProjektService.getProjektDto(projektId));
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<FiberProjektDto> updateFiberProjekt(UUID projektId, FiberProjektDto fiberProjektDto) {
        return  ResponseEntity.ok(fiberProjektService.updateProjektInfo(
            fiberProjektDto.getProjektInfo().getId(), fiberProjektDto));
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<ElnatProjektDto> updateElnatProjekt(UUID projektId, ElnatProjektDto elnatProjektDto) {
        return  ResponseEntity.ok(elnatProjektService.updateProjektInfo(
            elnatProjektDto.getProjektInfo().getId(), elnatProjektDto));
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'CREATE')")
    public ResponseEntity<String> createAvtalsjobb(UUID projektId, FilterAndTemplateDto filterAndTemplate) {
        var jobbId = this.projektService.createAvtalJob(projektId, filterAndTemplate);

        return ResponseEntity.ok().body(jobbId.toString());
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'CREATE')")
    public ResponseEntity<String> createInfobrevsjobb(UUID projektId, FilterAndTemplateDto filterAndTemplate) {
        var jobbId = this.projektService.createInfobrevJob(projektId, filterAndTemplate);

        return ResponseEntity.ok().body(jobbId.toString());
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<Void> deleteProjekt(UUID projektId) {
        projektService.deleteProjekt(projektId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<List<FastighetDto>> projektFastigheter(UUID projektId, @Valid FastighetsfilterDto filter) {
        var fastigheter = fastighetService.getFastigheter(projektId, filter);

        return ResponseEntity.ok(fastigheter);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<List<FastighetDelomradeInfoDto>> getDelomradenForProjekt(UUID projektId) throws Exception {
        return ResponseEntity.ok(projektService.getDelomradenForProjekt(projektId));
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<String> getProjektMeta(UUID projektId) {
        final Optional<String> extentGeoJson = intrangService.getExtentFromIntrangByProjektId(projektId);
        return ResponseEntity.of(extentGeoJson);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<List<MarkkollUserDto>> getProjektUsers(UUID projektId) throws Exception {
        var users = userService.getProjektUsers(projektId);

        return ResponseEntity.ok(users);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<List<MarkkollUserDto>> getProjektUsersRead(UUID projektId) {
        var users = userService.getUsersWithPermission(projektId, ProjektEntity.class,
            MkPermission.READ);

        return ResponseEntity.ok(users);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<Resource> getVarderingSkogsmarkProjekt(UUID projektId, @Valid FastighetsfilterDto filter) throws Exception {
        var xlsx = dokumentGeneratorService.getVarderingSkogsmark(projektId, filter);

        var headers = responseHeaders(xlsx.getFilename(), MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().headers(headers).body(xlsx);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<List<ProjektIntrangDto>> getProjektintrang(UUID projektId) throws Exception {
        var intrang = intrangService.getIntrang(projektId);

        return ResponseEntity.ok(intrang);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<String> getProjektintrangWithBuffert(UUID projektId) throws Exception {
        var intrang = projektService.getProjektintrangWithBuffert(projektId);
        return ResponseEntity.ok(intrang);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<VersionDto> projektCurrentVersion(UUID projektId) throws Exception {
        var version = projektService.getCurrentVersion(projektId);
        if (version.isPresent()) {
            return ResponseEntity.ok(version.get());
        }
        return ResponseEntity.ok(new VersionDto());
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    @Transactional
    public ResponseEntity<VersionDto>
    updateVersion(UUID projektId, IndataTypDto indataTyp, Double buffert, MultipartFile file) {

        try {
            var resource = new FileNameAwareByteArrayResource(file.getBytes(), file.getOriginalFilename());
            var version = intrangImportService.importShape(projektId, resource, indataTyp, buffert);
            var fastighetIds = fastighetService.fastigheterFromVersion(version);
            projektImportServiceFactory.getProjektImportService(projektId).updateProjektVersion(projektId,
                version.getId(), fastighetIds);

            return ResponseEntity.ok(version);
        }
        catch(Exception e) {
            throw new MarkkollException(MarkkollError.IMPORT_ERROR, e);
        }
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<VersionDto>
    updateVersionIntrang(UUID projektId, List<ProjektIntrangDto> projektIntrangDto) throws Exception {
        var version = intrangImportService.importIntrang(projektId, projektIntrangDto);
        var fastighetIds = fastighetService.fastigheterFromVersion(version);

        projektImportServiceFactory.getProjektImportService(projektId)
            .updateProjektVersion(projektId, version.getId(), fastighetIds);

        return ResponseEntity.ok(version);
    }

    @Override
    @PreAuthorize("hasVersionPermission(#versionId, 'ADMINISTRATION')")
    public ResponseEntity<Void> deleteVersion(UUID projektId, UUID versionId) {
        projektService.deleteVersion(versionId);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> editBeredare(UUID projektId, @Valid BeredareDto beredareDto) throws Exception {
        beredareService.edit(beredareDto, projektId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasVersionPermission(#versionId, 'ADMINISTRATION')")
    public ResponseEntity<Void> restoreVersion(UUID projektId, UUID versionId) {
        projektService.restoreVersion(versionId);

        // Vi uppdaterar värderingsprotokollen efter återställning
        if (projektService.getProjektTyp(projektId) == ProjektTypDto.FIBER) {
            if (fiberProjektService.shouldHaveVarderingsprotokoll(projektId)) {
                fiberProjektService.updateVarderingsprotokoll(projektId, LocalDateTime.now());
            }
        } else {
            elnatProjektService.updateVarderingsprotokoll(projektId);
        }

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<List<VersionDto>> projektVersions(UUID projektId) {
        List<VersionDto> allVersions = projektService.getAllVersions(projektId);
        return ResponseEntity.ok(allVersions);
    }

    @Override
    // Behörighetskollen sker i projektPage p.g.a. pagineringsbehov
    public ResponseEntity<ProjektPageDto>
    projektPage(Integer page,
                Integer size,
                String sortProperty,
                String sortDirection,
                String searchFilter)
    {
        var direction = isNullOrEmpty(sortDirection) ? Sort.Direction.ASC : Sort.Direction.valueOf(sortDirection.toUpperCase());

        var projektPageDto = projektService.projektPage(page, size, sortProperty, direction, searchFilter);

        return ResponseEntity.ok(projektPageDto);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<FastighetPageDto>
    projektFastigheterPage(UUID projektId,
                           Integer page,
                           Integer size,
                           FastighetsfilterDto fastighetsfilter)
    {
        var pageDto = fastighetService.fastighetPage(projektId, page, size, fastighetsfilter);

        return ResponseEntity.ok(pageDto);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<FastighetPageDto>
    projektSamfalligheterPage(UUID projektId,
                              Integer page,
                              Integer size,
                              FastighetsfilterDto fastighetsfilter)
    {
        var pageDto = fastighetService.samfallighetPage(projektId, page, size,
                fastighetsfilter);

        return ResponseEntity.ok(pageDto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasProjektPermission(#projektId, 'DELETE')")
    public ResponseEntity<Void> removeFastighet(UUID projektId, UUID fastighetsId) {
        this.fastighetService.removeFastighet(fastighetsId, projektId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'WRITE')")
    public ResponseEntity<Void> resetGeometryStatus(UUID projektId, UUID fastighetsId) {
        var versionId = projektService.getCurrentVersionId(projektId);

        fastighetService.setGeometristatus(fastighetsId, versionId, GeometristatusDto.OFORANDRAD);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<String> getAvtalId(UUID projektId, UUID fastighetsId) {
        // Tanken är att det här ska vara en temporär metod
        var avtalId = avtalRepository.getIdByProjektIdAndFastighetId(projektId, fastighetsId);

        return ResponseEntity.ok(avtalId.toString());
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<AvtalsinstallningarDto> getAvtalsinstallningar(UUID projektId) {

        var avtalsinstallningar = avtalsinstallningarService.getAvtalsinstallningar(projektId);

        return ResponseEntity.ok(avtalsinstallningar);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<BeredareDto> getBeredare(UUID projektId) throws Exception {
        var beredare = beredareService.get(projektId);

        return ResponseEntity.ok(beredare);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<AvtalsstatusDto> getFastighetStatus(UUID projektId, UUID fastighetId) {
        var status = avtalRepository.getAvtalsstatus(projektId, fastighetId);

        return ResponseEntity.ok(status);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'CREATE')")
    public ResponseEntity<MarkagareDto> addAgare(UUID projektId, UUID fastighetsId, @Valid MarkagareInfoDto markagare) {
        var agare = markagareService.addAgare(projektId, fastighetsId, markagare);

        return ResponseEntity.ok(agare);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'CREATE')")
    public ResponseEntity<Void> addFastighet(UUID projektId, @Valid UUID fastighetId) throws Exception {
        try {
            projektImportServiceFactory.getProjektImportService(projektId)
                .importRegisterenhet(projektId, fastighetId);

            return ResponseEntity.ok().build();
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'CREATE')")
    public ResponseEntity<Void> addFastigheter(UUID projektId, List<UUID> UUIDs) throws Exception {
        projektImportServiceFactory.getProjektImportService(projektId)
            .importRegisterenheter(projektId, UUIDs);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'DELETE')")
    public ResponseEntity<Void> excludeFastigheter(UUID projektId, List<UUID> UUIDs) throws Exception {
        projektService.removeFastigheter(projektId, UUIDs);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasProjektPermission(#projektId, 'WRITE')")
    public ResponseEntity<AvtalsstatusDto>
    setFastighetStatus(UUID projektId, UUID fastighetId, @NotNull @Valid AvtalsstatusDto avtalsstatus) {
        avtalService.updateAvtalsstatus(fastighetId, projektId, avtalsstatus);

        return ResponseEntity.ok(avtalsstatus);
    }

    @Override
    @Transactional
    @PreAuthorize("hasProjektPermission(#projektId, 'WRITE')")
    public ResponseEntity<Integer>
    setFastighetsStatusForSelection(UUID projektId,
                                    @NotNull @Valid AvtalsstatusDto status,
                                    @Valid FastighetsfilterDto filter)
    {
        var fastighetIds = fastighetService.getFastigheterAndSamfalligheter(projektId, filter).stream()
            .map(f -> f.getId())
            .collect(Collectors.toList());
        avtalService.updateAvtalsstatus(projektId, fastighetIds, status);

        return ResponseEntity.ok(fastighetIds.size());
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'WRITE')")
    public ResponseEntity<Void>
    updateAvtalsinstallningar(UUID projektId, AvtalsinstallningarDto avtalsinstallningarDto) {
        avtalsinstallningarService.updateAvtalsinstallningar(projektId, avtalsinstallningarDto);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'CREATE')")
    public ResponseEntity<Integer> importAgare(UUID projektId, FastighetIdsDto fastighetIdsDto) {
        var kund = userService.getCurrentUser().getKundId();
        var importedAgare = markagareService.importAgare(projektId, fastighetIdsDto.getIds(), kund);

        return ResponseEntity.ok(importedAgare);
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'READ')")
    public ResponseEntity<FastighetDto> getFastighet(UUID projektId, UUID fastighetId) {
        var fastighet = fastighetService.getFastighet(projektId, fastighetId);

        return ResponseEntity.ok(fastighet);
    }
}
