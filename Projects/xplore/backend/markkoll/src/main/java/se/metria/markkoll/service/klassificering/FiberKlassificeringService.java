package se.metria.markkoll.service.klassificering;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.intrang.OmradesintrangEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.VaghallareService;

import java.time.LocalDateTime;
import java.util.*;

import static se.metria.markkoll.util.AccountUtil.MARKKOLL_SYSTEM_USER_FRIENDLY_NAME;

/**
 * Klassificerar intrång för avtal i elnätsprojekt.
 * Utgår från områdesintrången, aggregerar i dagsläget ihop alla typer av ledningar (Hög/låg, luft/mark)
 * till en klass. Detta för att fylla i värderingsprotokollet. Framöver vill vi förmodligen även visa det
 * i avtalsinformationen uppdelat per område och presentera det ihop med avtalskartan.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FiberKlassificeringService {

    @NonNull
    private final OmradesintrangRepository omradesintrangRepository;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final VaghallareService vaghallareService;

    public static final Integer MARKLEDNING_STANDARD_BREDD = 2;
    public static final String MARKLEDNING_FRITEXT = "Optorör";
    public static final String MARKSKAP_FRITEXT = "Markskåp";
    public static final String OPTOBRUNN_FRITEXT = "Optobrunn";
    public static final String TEKNIKBOD_FRITEXT = "Site/Teknikbod";

    public FiberKlassificeringDto klassificera(UUID avtalId) {
        var projektId = avtalRepository.getProjektId(avtalId);
        var currentVersionId = projektRepository.getCurrentVersionId(projektId);

        return klassificera(avtalId, currentVersionId);
    }

    public FiberKlassificeringDto klassificera(Collection<OmradesintrangEntity> omradesintrang) {
        double langdMarkLedning = 0.;
        double langdLuftLedning = 0.;
        int markskap = 0;
        int optobrunn = 0;
        int teknikbod = 0;
        var littera = new ArrayList<String>();

        for (var intrang: omradesintrang) {

            // Endast nya intrång ska klassificeras
            if(!isStatusNew(intrang.getStatus())) {
                continue;
            }

            // Markledning, kan vara både hög/lågspänning
            if (isMarkledning(intrang.getSubtype() )) {
                langdMarkLedning += intrang.getGeom().getLength();
            }

            // Luftledning, kan vara både hög/lågspänning
            if (isLuftledning(intrang.getSubtype() )) {
                langdLuftLedning += intrang.getGeom().getLength();
            }

            // Markskaå
            else if(isMarkskap(intrang.getType())) {
                markskap += 1;
            }

            // Brunn
            else if(isOptobrunn(intrang.getType())) {
                optobrunn += 1;
            }

            // Teknikbod
            else if(isTeknikbod(intrang.getType())) {
                teknikbod += 1;
            }

            if (intrang.getLittera() != null) {
                littera.add(intrang.getLittera());
            }
        }

        return FiberKlassificeringDto.builder()
            .langdMarkledning(Math.ceil(langdMarkLedning))
            .langdLuftledning(Math.ceil(langdLuftLedning))
            .antalMarkskap(markskap)
            .antalOptobrunn(optobrunn)
            .antalTeknikbod(teknikbod)
            .littera(String.join(" ", littera))
            .build();
    }

    /**
     * Klassificera områdesintrång för ett visst avtal i ett fibersprojekt.
     */
    public FiberKlassificeringDto klassificera(UUID avtalId, UUID versionId) {
        var avtalstyper = getAvtalstyper(versionId);
        var omradesintrang = omradesintrangRepository.findByAvtalIdAndAvtalstyper(avtalId,
            versionId, avtalstyper);

        return klassificera(omradesintrang);
    }

    /**
     * Skapa klassificerat värderingsprotokoll för ett visst avtal.
     */
    @Transactional
    public FiberVarderingsprotokollDto
    getKlassificeratVarderingsprotokoll(FiberKlassificeringDto klassificering,
                                        LocalDateTime varderingstidpunkt) {

        var vp = new FiberVarderingsprotokollDto();

        if (klassificering.getLangdMarkledning() > 0) {
           FiberMarkledningDto markledning = new FiberMarkledningDto()
                    .langd(klassificering.getLangdMarkledning())
                    .bredd(MARKLEDNING_STANDARD_BREDD)
                    .beskrivning(MARKLEDNING_FRITEXT)
                    .skapadAv(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME);
           vp.addMarkledningItem(markledning);
        }

        if (klassificering.getAntalMarkskap() > 0) {
            FiberPunktersattningDto markskap = new FiberPunktersattningDto()
                    .antal(klassificering.getAntalMarkskap())
                    .beskrivning(MARKSKAP_FRITEXT)
                    .typ(FiberPunktersattningTypDto.MARKSKAP_EJ_KLASSIFICERAD)
                    .skapadAv(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME);
            vp.addPunktersattningItem(markskap);
        }

        if (klassificering.getAntalOptobrunn() > 0) {
            FiberPunktersattningDto optobrunn = new FiberPunktersattningDto()
                    .antal(klassificering.getAntalOptobrunn())
                    .beskrivning(OPTOBRUNN_FRITEXT)
                    .typ(FiberPunktersattningTypDto.OPTOBRUNN_EJ_KLASSIFICERAD)
                    .skapadAv(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME);
            vp.addPunktersattningItem(optobrunn);
        }

        if (klassificering.getAntalTeknikbod() > 0) {
            FiberPunktersattningDto teknikbod = new FiberPunktersattningDto()
                    .antal(klassificering.getAntalTeknikbod())
                    .beskrivning(TEKNIKBOD_FRITEXT)
                    .typ(FiberPunktersattningTypDto.SITE_EJ_KLASSIFICERAD)
                    .skapadAv(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME);
            vp.addPunktersattningItem(teknikbod);
        }

        vp.setMetadata(new FiberVarderingsprotokollMetadataDto()
                .varderingsmanOchForetag("")
                .varderingstidpunkt(varderingstidpunkt)
        );

        return vp;
    }

    private Boolean isMarkledning(String intrangssubtyp) {
        return IntrangsSubtypDto.fromValue(intrangssubtyp) == IntrangsSubtypDto.MARKSTRAK;
    }

    private Boolean isLuftledning(String intrangssubtyp) {
        return IntrangsSubtypDto.fromValue(intrangssubtyp) == IntrangsSubtypDto.LUFTSTRAK;
    }

    private Boolean isMarkskap(String intrangstyp) {
        return IntrangstypDto.fromValue(intrangstyp) == IntrangstypDto.MARKSKAP;
    }

    private Boolean isOptobrunn(String intrangstyp) {
        return IntrangstypDto.fromValue(intrangstyp) == IntrangstypDto.BRUNN;
    }

    private Boolean isTeknikbod(String intrangstyp) {
        return IntrangstypDto.fromValue(intrangstyp) == IntrangstypDto.TEKNIKBOD;
    }

    private Boolean isStatusNew(String intrangStatus) {
        return IntrangsStatusDto.fromValue(intrangStatus) == IntrangsStatusDto.NY;
    }

    private List<AvtalstypDto> getAvtalstyper(UUID versionId) {
        var avtalstyper = new ArrayList<AvtalstypDto>();
        avtalstyper.add(AvtalstypDto.INTRANG);

        var vaghallareAvtalstyper = vaghallareService.vaghallareAvtalstyperInVp(versionId);
        avtalstyper.addAll(vaghallareAvtalstyper);

        return avtalstyper;
    }
}

