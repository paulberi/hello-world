package se.metria.markkoll.service.projekt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.GeometristatusEntity;
import se.metria.markkoll.entity.fastighet.FastighetOmradeEntity;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;
import se.metria.markkoll.security.MkPermission;
import se.metria.markkoll.service.FastighetsforteckningService;
import se.metria.markkoll.service.InfobrevService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.admin.AdminService;
import se.metria.markkoll.service.admin.PermissionService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.intrang.IntrangService;
import se.metria.markkoll.service.logging.LoggService;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjektService {
    @NonNull
    private final AclService aclService;
    @NonNull
    private final FastighetService fastighetService;
    @NonNull
    private final FastighetsforteckningService fastighetsforteckningService;
    @NonNull
    private final ProjektRepository projektRepository;
    @NonNull
    private final VersionRepository versionRepository;
    @NonNull
    private final ModelMapper modelMapper;
    @NonNull
    private final AvtalService avtalService;
    @NonNull
    private final InfobrevService infobrevService;
    @NonNull
    private final IntrangService intrangService;
    @NonNull
    private final AvtalRepository avtalRepository;
    @NonNull
    private final LoggService loggService;
    @NonNull
    private final AdminService adminService;
    @NonNull
    private final PermissionService permissionService;
    @NonNull
    private final UserService userService;

    public Optional<ProjektInfoDto> getProjektForProjekt(UUID projektId) {
        return projektRepository.findById(projektId).map(this::createProjektInfoDto);
    }

    public ProjektInfoDto getProjektForAvtal(UUID avtalId) {
        var projektId = avtalRepository.getProjektId(avtalId);
        return getProjektForProjekt(projektId).orElseThrow(EntityNotFoundException::new);
    }

    public UUID getCurrentVersionId(UUID projektId) {
        var currentVersion = projektRepository.getOne(projektId).getCurrentVersion();
        return currentVersion.getId();
    }

    public Optional<VersionDto> getCurrentVersion(UUID projektId) {
        var version = projektRepository.getOne(projektId).getCurrentVersion();
        if (version != null) {
            return Optional.of(modelMapper.map(version, VersionDto.class));
        }
        else {
            return Optional.empty();
        }
    }

    public ProjektPageDto
    projektPage(int page,
                int size,
                String sortProperty,
                Sort.Direction sortDirection,
                String searchFilter)
    {
        var pageRequest = PageRequest.of(page, size, sortDirection, sortProperty);

        var projektFilter = permissionService.<UUID>filterObjectsByPermission(userService.getCurrentUserId(),
            ProjektEntity.class, MkPermission.READ);

        Page<ProjektEntity> pageEntity;
        try {
            pageEntity = projektRepository.pageFiltered(pageRequest, searchFilter, projektFilter);

            if (pageEntity == null) {
                throw new MarkkollException(MarkkollError.PROJEKT_ERROR);
            }
        } catch (PropertyReferenceException e) {
            throw new MarkkollException(MarkkollError.PROJEKT_ERROR);
        }

        return modelMapper.map(pageEntity, ProjektPageDto.class);
    }

    @Transactional
    public UUID setCurrentVersion(UUID projektId, UUID versionId) {
        var projektEntity = projektRepository.findById(projektId).orElseThrow(EntityNotFoundException::new);
        var previousVersion = projektEntity.getCurrentVersion();
        var previousVersionId = previousVersion == null ? null : previousVersion.getId();
        var currentVersion = versionRepository.findById(versionId)
            .orElseThrow(EntityNotFoundException::new);

        projektEntity.setCurrentVersion(currentVersion);

        projektRepository.save(projektEntity);

        return previousVersionId;
    }

    @Transactional
    public VersionDto restoreVersion(UUID versionId) {
        var versionEntity = versionRepository.getOne(versionId);
        var projekt = versionEntity.getProjekt();

        log.info("Återställer projekt '{}' till version {}", projekt.getNamn(), versionId);

        var newCurrentVersion = projekt
                .getVersioner()
                .stream()
                .filter(version -> version.getId().equals(versionId))
                .findAny()
                .orElseThrow();

        projekt.setCurrentVersion(newCurrentVersion);

        var versionsToDelete = projekt
                .getVersioner()
                .stream()
                .filter(version -> version.getSkapadDatum().isAfter(newCurrentVersion.getSkapadDatum()))
                .collect(Collectors.toList());

        // Alla fastighetsförteckningar som inte längre är med sätts som exkluderade så att de inte syns i listan eller kartan.
        versionsToDelete.forEach(version -> {
            version.getGeometristatus()
                    .stream().map(GeometristatusEntity::getAvtal)
                    .filter(avtal ->
                            newCurrentVersion.getGeometristatus().stream()
                            .noneMatch(geometristatus -> geometristatus.getAvtal().equals(avtal))
                            )
                            .map(AvtalEntity::getFastighetsforteckning)
                            .forEach(fastighetsforteckning -> fastighetsforteckning.setExcluded(true));
        }
        );

        for (var version : versionsToDelete) {
            projekt.removeVersion(version);
            aclService.deleteObject(version.getId(), ImportVersionEntity.class);
        }

        loggService.addProjektHandelse(projekt.getId(), ProjekthandelseTypDto.VERSION_ATERSTALLD);

        return modelMapper.map(newCurrentVersion, VersionDto.class);
    }

    // TODO: Kolla att cascades är rätt uppsatta för El & Fiber-projektentiteterna
    @Transactional
    public void deleteProjekt(UUID projektId) {
        projektRepository.deleteById(projektId);
        adminService.deleteAllProjektUsersRoles(projektId);
        aclService.deleteObject(projektId, ProjektEntity.class);
        log.info("Projekt {} raderat", projektId);
    }

    @Transactional
    public void deleteVersion(UUID versionId) {
        if (!versionRepository.isCurrentVersion(versionId)) {
            var projektId = versionRepository.getProjektId(versionId);
            versionRepository.deleteById(versionId);
            aclService.deleteObject(versionId, ImportVersionEntity.class);
            loggService.addProjektHandelse(projektId, ProjekthandelseTypDto.VERSION_BORTTAGEN);
        } else {
            throw new MarkkollException(MarkkollError.IMPORT_ERROR_NEW_VERSION);
        }
    }

    public List<VersionDto> getAllVersions(UUID projektId) {
        return versionRepository.findAllByProjektId(projektId).stream()
                .map(version -> new VersionDto()
                        .id(version.getId())
                        .filnamn(version.getFilnamn())
                        .skapadDatum(version.getSkapadDatum()))
                .collect(Collectors.toList());
    }

    public UUID createAvtalJob(UUID projektId, FilterAndTemplateDto filterAndTemplate) {
        var jobbId = this.avtalService.createAvtalsjobb(projektId, filterAndTemplate);

        this.avtalService.runAvtalsjobb(jobbId, filterAndTemplate.getTemplate());

        return jobbId;
    }

    public UUID createInfobrevJob(UUID projektId, FilterAndTemplateDto filterAndTemplate) {
        var jobbId = this.infobrevService.createInfobrevsjobb(projektId, filterAndTemplate);

        this.infobrevService.runInfobrevsjobb(jobbId, filterAndTemplate.getTemplate());

        return jobbId;
    }

    public ProjektTypDto getProjektTyp(UUID projektId) {
        return projektRepository.getProjekttyp(projektId);
    }

    public void removeFastigheter(UUID projektId, List<UUID> fastighetIds) {
        fastighetsforteckningService.setExcluded(projektId, fastighetIds, true);
    }

    public List<FastighetDelomradeInfoDto> getDelomradenForProjekt(UUID projektId) {

        var fastigheter = new HashMap<UUID, String>();

       fastighetsforteckningService.getIncludedByProjektId(projektId)
                .stream()
                    .map(FastighetsforteckningEntity::getFastighet)
                    .forEach(entry -> fastigheter.put(entry.getId(), entry.getFastighetsbeteckning()));

        return fastighetService.getDelomradenForFastigheter(new ArrayList<>(fastigheter.keySet()))
                .stream()
                .map(value -> convertFastighetOmradeEntityToFastighetDelomradeInfoDto(value, fastigheter.get(value.getFastighetId())))
                .collect(toList());
    }

    private FastighetDelomradeInfoDto convertFastighetOmradeEntityToFastighetDelomradeInfoDto(FastighetOmradeEntity entity, String fastighetsbeteckning) {
        return new FastighetDelomradeInfoDto()
                .fastighetId(entity.getFastighetId())
                .fastighetsbeteckning(fastighetsbeteckning)
                .omradeNr(entity.getOmradeNr())
                .geometry(new GeometryJSON().toString(entity.getGeom()));
    }
    
    public String getProjektintrangWithBuffert(UUID projektId) {
        var intrang = intrangService.getIntrangWithBuffertByProjekt(projektId).orElse("");
        return intrang;
    }

    public ProjektInfoDto createProjektInfoDto(ProjektEntity projektEntity) {
        return new ProjektInfoDto()
                .id(projektEntity.getId())
                .namn(projektEntity.getNamn())
                .kundId(projektEntity.getKundId())
                .projektTyp(projektEntity.getProjekttyp())
                .ort(projektEntity.getOrt())
                .startDatum(projektEntity.getStartDatum())
                .beskrivning(projektEntity.getBeskrivning())
                .uppdragsnummer(projektEntity.getUppdragsnummer())
                .utskicksstrategi(projektEntity.getUtskicksstrategi());
    }
}

