package se.metria.markkoll.service.projekt;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.projekt.FiberProjektEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.FiberProjektRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.FastighetsforteckningService;
import se.metria.markkoll.service.geometristatus.GeometristatusService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.klassificering.FiberKlassificeringService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollService;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FiberProjektService extends AbstractProjektService<FiberProjektEntity, FiberProjektDto, FiberVarderingsprotokollDto> {
    @NonNull
    private final FiberVarderingsprotokollService fiberVarderingsprotokollService;
    @NonNull
    private final FiberKlassificeringService fiberKlassificeringService;
    @NonNull
    private final FiberProjektRepository fiberProjektRepository;
    @NonNull
    private final AclService aclService;
    @NonNull
    private final UserService userService;

    public FiberProjektService(AvtalService avtalService,
                               FastighetsforteckningService fastighetsforteckningService,
                               LoggService loggService,
                               GeometristatusService geometristatusService,
                               FiberVarderingsprotokollService fiberVarderingsprotokollService,
                               FiberKlassificeringService fiberKlassificeringService,
                               FiberProjektRepository fiberProjektRepository,
                               OmradesintrangRepository omradesintrangRepository,
                               AvtalRepository avtalRepository,
                               AclService aclService,
                               UserService userService,
                               ProjektRepository projektRepository)
    {
        super(avtalService, avtalRepository, fastighetsforteckningService, geometristatusService, loggService, omradesintrangRepository,
            fiberVarderingsprotokollService, projektRepository);

        this.fiberVarderingsprotokollService = fiberVarderingsprotokollService;
        this.fiberKlassificeringService = fiberKlassificeringService;
        this.fiberProjektRepository = fiberProjektRepository;
        this.aclService = aclService;
        this.userService = userService;
    }

    protected Optional<FiberProjektEntity> getProjektEntity(UUID projektId) {
        return fiberProjektRepository.findById(projektId);
    }

    public FiberProjektDto createProjekt(String kundId, FiberProjektDto fiberprojekt) {
        var projektEntity = new FiberProjektEntity();

        mapProjektInfo(fiberprojekt.getProjektInfo(), projektEntity);
        mapFiberInfo(fiberprojekt.getFiberInfo(), projektEntity);
        projektEntity.setKundId(kundId);

        var savedProjektEntity = fiberProjektRepository.saveAndFlush(projektEntity);
        log.info("Nytt fiberprojekt med id {} skapat", projektEntity.getId());

        aclService.createObject(savedProjektEntity.getId(), ProjektEntity.class, kundId, KundEntity.class);

        userService.addRole(userService.getCurrentUser().getEmail(), RoleTypeDto.PROJEKTADMIN,
            savedProjektEntity.getId());

        return this.createProjektDto(savedProjektEntity).indataTyp(fiberprojekt.getIndataTyp());
    }

    /**
     * Uppdatera klassificering av värderingsprotokoll för nya och uppdaterade avtal.
     */
    @Transactional
    public void updateVarderingsprotokoll(UUID projektId, LocalDateTime varderingstidpunkt) {

        // För nya avtal skapas nytt klassificerat värderingsprotokoll.
        var nyaAvtal = this.avtalRepository.findNewByProjektId(projektId);
        for (var nyttAvtal: nyaAvtal) {
            if(nyttAvtal.getFiberVarderingsprotokoll() != null){
                fiberVarderingsprotokollService.delete(nyttAvtal.getFiberVarderingsprotokoll().getId());
                nyttAvtal.setVarderingsprotokoll(null);
            }

            var klassificering = fiberKlassificeringService.klassificera(nyttAvtal.getId());
            var vp = fiberKlassificeringService.getKlassificeratVarderingsprotokoll(klassificering, varderingstidpunkt);
            fiberVarderingsprotokollService.create(vp, nyttAvtal.getId());
        }

        // För uppdaterade avtal tar vi bort alla befintliga värderingsprotokoll och skapar sedan nya klassificerade.
        // Borde utvärderas om det känns OK eller scary för användaren...
        var uppdateradeAvtalIds = this.avtalRepository.findUpdatedIdsByProjektId(projektId);
        for (var avtalId: uppdateradeAvtalIds) {
            var vpId = this.fiberVarderingsprotokollService.getWithAvtalId(avtalId).get().getId();
            fiberVarderingsprotokollService.delete(vpId);

            var klassificering = fiberKlassificeringService.klassificera(avtalId);
            var vp = fiberKlassificeringService.getKlassificeratVarderingsprotokoll(klassificering, varderingstidpunkt);
            fiberVarderingsprotokollService.create(vp, avtalId);
        }
    }

    @Transactional
    public FiberProjektDto updateProjektInfo(@SuppressWarnings("unused") UUID projektId, FiberProjektDto fiberprojekt) {
        var projektEntity = fiberProjektRepository.findById(fiberprojekt.getProjektInfo().getId())
            .orElseThrow(EntityExistsException::new);

        mapProjektInfo(fiberprojekt.getProjektInfo(), projektEntity);
        mapFiberInfo(fiberprojekt.getFiberInfo(), projektEntity);

        fiberProjektRepository.saveAndFlush(projektEntity);
        loggService.addProjektHandelse(projektEntity.getId(), ProjekthandelseTypDto.PROJEKTINFORMATION_REDIGERAD);

        return createProjektDto(projektEntity);
    }

    private void mapFiberInfo(FiberInfoDto fiberInfo, FiberProjektEntity projektEntity) {
        projektEntity.setLedningsagare(fiberInfo.getLedningsagare());
        projektEntity.setLedningsstracka(fiberInfo.getLedningsstracka());
        projektEntity.setBidragsprojekt(fiberInfo.getBidragsprojekt());
        projektEntity.setVarderingsprotokoll(fiberInfo.getVarderingsprotokoll());
        projektEntity.setBestallare(fiberInfo.getBestallare());
        projektEntity.setBidragsprojekt(fiberInfo.getBidragsprojekt());
    }

    private FiberInfoDto createFiberInfoDto(FiberProjektEntity projektEntity) {
        return new FiberInfoDto()
                .bestallare(projektEntity.getBestallare())
                .varderingsprotokoll(projektEntity.isVarderingsprotokoll())
                .bidragsprojekt(projektEntity.isBidragsprojekt())
                .ledningsagare(projektEntity.getLedningsagare())
                .ledningsstracka(projektEntity.getLedningsstracka());
    }

    protected FiberProjektDto createProjektDto(FiberProjektEntity projektEntity) {
        var projektInfo = createProjektInfoDto(projektEntity);
        var fiberInfo = createFiberInfoDto(projektEntity);

        return new FiberProjektDto()
                .projektInfo(projektInfo)
                .fiberInfo(fiberInfo);
    }

    @Override
    public Boolean shouldHaveVarderingsprotokoll(UUID projektId) {
        return fiberProjektRepository.getShouldHaveVarderingsprotokoll(projektId);
    }
}

