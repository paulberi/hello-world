package se.metria.markkoll.service.projekt;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.projekt.ElnatProjektEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ElnatProjektRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.FastighetsforteckningService;
import se.metria.markkoll.service.geometristatus.GeometristatusService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.klassificering.ElnatKlassificeringService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ElnatProjektService extends AbstractProjektService<ElnatProjektEntity, ElnatProjektDto, ElnatVarderingsprotokollDto> {
    @NonNull
    private final ElnatVarderingsprotokollService elnatVarderingsprotokollService;
    @NonNull
    private final AclService aclService;
    @NonNull
    private final ElnatProjektRepository elnatProjektRepository;
    @NonNull
    private final ElnatKlassificeringService elnatKlassificeringService;
    @NonNull
    private final UserService userService;

    public ElnatProjektService(AvtalService avtalService,
                               FastighetsforteckningService fastighetsforteckningService,
                               LoggService loggService,
                               ElnatVarderingsprotokollService elnatVarderingsprotokollService,
                               ElnatProjektRepository elnatProjektRepository,
                               OmradesintrangRepository omradesintrangRepository,
                               AvtalRepository avtalRepository,
                               ElnatKlassificeringService elnatKlassificeringService,
                               AclService aclService,
                               GeometristatusService geometristatusService,
                               UserService userService,
                               ProjektRepository projektRepository)
    {
        super(avtalService, avtalRepository, fastighetsforteckningService, geometristatusService, loggService, omradesintrangRepository,
            elnatVarderingsprotokollService, projektRepository);

        this.elnatVarderingsprotokollService = elnatVarderingsprotokollService;
        this.elnatProjektRepository = elnatProjektRepository;
        this.elnatKlassificeringService = elnatKlassificeringService;
        this.aclService = aclService;
        this.userService = userService;
    }

    protected Optional<ElnatProjektEntity> getProjektEntity(UUID projektId) {
        return elnatProjektRepository.findById(projektId);
    }

    /**
     * Skapar ett nytt elnätsprojekt.
     */
    @Transactional
    public ElnatProjektDto createProjekt(String kundId, ElnatProjektDto elnatprojekt) {
        var projektEntity = new ElnatProjektEntity();
        mapProjektInfo(elnatprojekt.getProjektInfo(), projektEntity);
        mapElnatInfo(elnatprojekt.getElnatInfo(), projektEntity);
        projektEntity.setKundId(kundId);
        var savedProjektEntity = elnatProjektRepository.saveAndFlush(projektEntity);

        aclService.createObject(savedProjektEntity.getId(), ProjektEntity.class, kundId, KundEntity.class);

        userService.addRole(userService.getCurrentUser().getEmail(), RoleTypeDto.PROJEKTADMIN,
            savedProjektEntity.getId());

        log.info("Nytt elnatprojekt med id {} skapat", projektEntity.getId());

        return this.createProjektDto(savedProjektEntity);
    }

    /* TODO: projektId är med som parameter här, även om den inte behövs, för att få auktoriseringen på projektId
     * att fungera. Vi skulle kunna göra auktoriseringen på ett ElnatProjektDto direkt. Problemet är att man måste
     * implementera interfacet Identifiable, och eftersom det är automatgenererade klasser så kommer det att skrivas
     * över om vi gör klassen till en Identifiable. Skulle behöva modifiera kodgenereringen kanske? */
    @Transactional
    public ElnatProjektDto updateProjektInfo(@SuppressWarnings("unused") UUID projektId, ElnatProjektDto elnatprojekt) {
        var projektEntity = elnatProjektRepository.getOne(elnatprojekt.getProjektInfo().getId());
        mapProjektInfo(elnatprojekt.getProjektInfo(), projektEntity);
        mapElnatInfo(elnatprojekt.getElnatInfo(), projektEntity);

        elnatProjektRepository.saveAndFlush(projektEntity);
        loggService.addProjektHandelse(projektEntity.getId(), ProjekthandelseTypDto.PROJEKTINFORMATION_REDIGERAD);

        return createProjektDto(projektEntity);
    }

    /**
     * Uppdatera klassificering av värderingsprotokoll för nya och uppdaterade avtal.
     */
   @Transactional
   @Deprecated //gammal rutin för att uppdatera värderingsprotokoll
   public void updateVarderingsprotokoll(UUID projektId) {

       // För nya avtal skapas nytt klassificerat värderingsprotokoll.
        var nyaAvtal = this.avtalRepository.findNewByProjektId(projektId);
       var varderingstidpunkt = LocalDateTime.now();
        for (var nyttAvtal: nyaAvtal) {
            if(nyttAvtal.getVarderingsprotokoll() != null){
                elnatVarderingsprotokollService.delete(nyttAvtal.getVarderingsprotokoll().getId());
                nyttAvtal.setVarderingsprotokoll(null);
            }
            var klassificering = elnatKlassificeringService.klassificera(nyttAvtal.getId());
            var vp = elnatKlassificeringService.getKlassificeratVarderingsprotokoll(klassificering,
                varderingstidpunkt);
            varderingsprotokollService.create(vp, nyttAvtal.getId());
        }

        // För uppdaterade avtal tar vi bort alla befintliga värderingsprotokoll och skapar sedan nya klassificerade.
        // Borde utvärderas om det känns OK eller scary för användaren...
        var uppdateradeAvtal = this.avtalRepository.findUpdatedByProjektId(projektId);
        for (var uppdateratAvtal: uppdateradeAvtal) {
            elnatVarderingsprotokollService.delete(uppdateratAvtal.getVarderingsprotokoll().getId());
            uppdateratAvtal.setVarderingsprotokoll(null);

            var klassificering = elnatKlassificeringService.klassificera(uppdateratAvtal.getId());
            var vp = elnatKlassificeringService.getKlassificeratVarderingsprotokoll(klassificering,
                varderingstidpunkt);
            varderingsprotokollService.create(vp, uppdateratAvtal.getId());
        }
    }

    private void mapElnatInfo(ElnatInfoDto elnatInfo, ElnatProjektEntity projektEntity) {
        projektEntity.setLedningsagare(elnatInfo.getLedningsagare());
        projektEntity.setLedningsstracka(elnatInfo.getLedningsstracka());
        projektEntity.setBestallare(elnatInfo.getBestallare());
    }

    private ElnatInfoDto createElnatInfoDto(ElnatProjektEntity projektEntity) {
        return new ElnatInfoDto()
                .bestallare(projektEntity.getBestallare())
                .ledningsagare(projektEntity.getLedningsagare())
                .ledningsstracka(projektEntity.getLedningsstracka());
    }

    protected ElnatProjektDto createProjektDto(ElnatProjektEntity projektEntity) {
        var projektInfo = createProjektInfoDto(projektEntity);
        var elnatInfo = createElnatInfoDto(projektEntity);

        return new ElnatProjektDto()
                .projektInfo(projektInfo)
                .elnatInfo(elnatInfo);
    }

    @Override
    public Boolean shouldHaveVarderingsprotokoll(UUID projektId) {
        return true;
    }
}

