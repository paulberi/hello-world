package se.metria.markkoll.service.klassificering;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.metria.markkoll.entity.intrang.OmradesintrangEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.VaghallareService;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Klassificerar intrång för avtal i elnätsprojekt.
 * Utgår från områdesintrången, aggregerar i dagsläget ihop alla typer av ledningar (Hög/låg, luft/mark)
 * till en klass. Detta för att fylla i värderingsprotokollet. Framöver vill vi förmodligen även visa det
 * i avtalsinformationen uppdelat per område och presentera det ihop med avtalskartan.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ElnatKlassificeringService {

    @NonNull
    private final OmradesintrangRepository omradesintrangRepository;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final VaghallareService vaghallareService;

    public static final Integer MARKLEDNING_STANDARD_BREDD = 2;
    public static final String MARKLEDNING_FRITEXT = "Elektrisk starkströmsledning";
    public static final String STRAK_FRITEXT = "Stråk";
    public static final String KABELSKAP_FRITEXT = "Kabelskåp";
    public static final String NATSTATION_FRITEXT = "Nätstation";

    public ElnatKlassificeringDto klassificera(UUID avtalId) {
        var projektId = avtalRepository.getProjektId(avtalId);
        var currentVersionId = projektRepository.getCurrentVersionId(projektId);

        return klassificera(avtalId, currentVersionId);
    }

    public ElnatKlassificeringDto klassificera(Collection<OmradesintrangEntity> omradesintrang) {
        Double langdMarkLedning = 0.;
        Double langdMarkstrak = 0.;
        Double langdLuftLedning = 0.;
        Integer kabelskap = 0;
        Integer natstationer = 0;
        Double hogstaSpanningsniva = null;
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

            if (isMarkstrak(intrang.getSubtype())) {
                langdMarkstrak += intrang.getGeom().getLength();
            }

            // Luftledning, kan vara både hög/lågspänning
            if (isLuftledning(intrang.getSubtype() )) {
                langdLuftLedning += intrang.getGeom().getLength();
            }

            // Kabelskåp
            else if(isKabelskap(intrang.getType())) {
                kabelskap += 1;
            }
            // Nätstation
            else if(isNatstation(intrang.getType())) {
                natstationer += 1;
            }

            if (intrang.getLittera() != null) {
                littera.add(intrang.getLittera());
            }

            if (intrang.getSpanningsniva() != null &&
                (hogstaSpanningsniva == null || intrang.getSpanningsniva() > hogstaSpanningsniva))
            {
                hogstaSpanningsniva = intrang.getSpanningsniva();
            }
        }

        var elnatKlassificeringDto = ElnatKlassificeringDto.builder()
            .langdMarkledning(langdMarkLedning)
            .langdLuftledning(langdLuftLedning)
            .langdMarkstrak(langdMarkstrak)
            .antalKabelskap(kabelskap)
            .antalNatstationer(natstationer)
            .littera(String.join(" ", littera))
            .hogstaSpanningsniva(hogstaSpanningsniva)
            .build();

        return elnatKlassificeringDto;
    }

    /**
     * Klassificera områdesintrång för ett visst avtal i ett elnätsprojekt.
     */
    public ElnatKlassificeringDto klassificera(UUID avtalId, UUID versionId) {
        var avtalstyper = getAvtalstyper(versionId);
        var omradesintrang = omradesintrangRepository.findByAvtalIdAndAvtalstyper(avtalId,
            versionId, avtalstyper);

        return klassificera(omradesintrang);
    }

    /**
     * Skapa klassificerat värderingsprotokoll för ett visst avtal.
     */
    public ElnatVarderingsprotokollDto
    getKlassificeratVarderingsprotokoll(ElnatKlassificeringDto klassificering,
                                        LocalDateTime varderingstidpunkt)
    {
        var vp = new ElnatVarderingsprotokollDto();

        if (klassificering.getLangdMarkledning() > 0) {
            ElnatMarkledningDto markledning = new ElnatMarkledningDto()
                    .langd(klassificering.getLangdMarkledning().intValue())
                    .beskrivning(MARKLEDNING_FRITEXT)
                    .bredd(MARKLEDNING_STANDARD_BREDD);
            vp.addMarkledningItem(markledning);
        }

        if (klassificering.getLangdMarkstrak() > 0) {
            ElnatMarkledningDto markledning = new ElnatMarkledningDto()
                .langd(klassificering.getLangdMarkstrak().intValue())
                .beskrivning(STRAK_FRITEXT)
                .bredd(MARKLEDNING_STANDARD_BREDD);
            vp.addMarkledningItem(markledning);
        }

        if (klassificering.getAntalKabelskap() > 0) {
            ElnatPunktersattningDto kabelskap = new ElnatPunktersattningDto()
                    .antal(klassificering.getAntalKabelskap())
                    .beskrivning(KABELSKAP_FRITEXT)
                    .typ(ElnatPunktersattningTypDto.KABELSKAP_EJ_KLASSIFICERAD);
            vp.addPunktersattningItem(kabelskap);
        }

        if (klassificering.getAntalNatstationer() > 0) {
            ElnatPunktersattningDto natstation = new ElnatPunktersattningDto()
                    .antal(klassificering.getAntalNatstationer())
                    .beskrivning(NATSTATION_FRITEXT)
                    .typ(ElnatPunktersattningTypDto.NATSTATION_EJ_KLASSIFICERAD);
            vp.addPunktersattningItem(natstation);
        }

        vp.metadata(
            new ElnatVarderingsprotokollMetadataDto()
                .ledning(klassificering.getLittera())
                .varderingstidpunkt(varderingstidpunkt)
                .spanningsniva(getHogstaSpanningsniva(klassificering.getHogstaSpanningsniva()))
        );

        vp.setPrisomrade(ElnatPrisomradeDto.NORRLANDS_INLAND);

        return vp;
    }

    private String getHogstaSpanningsniva(Double spanningsniva) {
        return String.format(spanningsniva == null ? "" : new DecimalFormat("0.##").format(spanningsniva) + "kV");
    }

    private Boolean isMarkledning(String intrangssubtyp) {
        return intrangssubtyp != null && IntrangsSubtypDto.fromValue(intrangssubtyp) == IntrangsSubtypDto.MARKLEDNING;
    }

    private Boolean isLuftledning(String intrangssubtyp) {
        return intrangssubtyp != null && IntrangsSubtypDto.fromValue(intrangssubtyp) == IntrangsSubtypDto.LUFTLEDNING;
    }

    private boolean isMarkstrak(String intrangssubtyp) {
        return intrangssubtyp != null &&
                (IntrangsSubtypDto.fromValue(intrangssubtyp) == IntrangsSubtypDto.INMATT_STRAK ||
                 IntrangsSubtypDto.fromValue(intrangssubtyp) == IntrangsSubtypDto.OSAKERT_LAGE);
    }

    private Boolean isNatstation(String intrangstyp) {
        return intrangstyp != null && IntrangstypDto.fromValue(intrangstyp) == IntrangstypDto.NATSTATION;
    }

    private Boolean isKabelskap(String intrangstyp) {
        return intrangstyp != null && IntrangstypDto.fromValue(intrangstyp) == IntrangstypDto.KABELSKAP;
    }

    private Boolean isStatusNew(String intrangStatus) {
        return intrangStatus != null && IntrangsStatusDto.fromValue(intrangStatus) == IntrangsStatusDto.NY;
    }

    private List<AvtalstypDto> getAvtalstyper(UUID versionId) {
        var avtalstyper = new ArrayList<AvtalstypDto>();
        avtalstyper.add(AvtalstypDto.INTRANG);

        var vaghallareAvtalstyper = vaghallareService.vaghallareAvtalstyperInVp(versionId);
        avtalstyper.addAll(vaghallareAvtalstyper);

        return avtalstyper;
    }
}

