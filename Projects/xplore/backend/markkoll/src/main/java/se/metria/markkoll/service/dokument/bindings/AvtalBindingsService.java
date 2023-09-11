package se.metria.markkoll.service.dokument.bindings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.AuditorAware;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.dokument.bindings.data.AvtalBindings;
import se.metria.markkoll.service.dokument.bindings.data.ElIntrangBindings;
import se.metria.markkoll.service.dokument.bindings.data.ErsattningBindings;
import se.metria.markkoll.service.dokument.bindings.data.IntrangBindings;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.klassificering.ElnatKlassificeringService;
import se.metria.markkoll.service.klassificering.FiberKlassificeringService;
import se.metria.markkoll.service.map.MapService;
import se.metria.markkoll.service.projekt.FiberProjektService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollGeneratorService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollGeneratorService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@MarkkollService
@RequiredArgsConstructor
@Slf4j
public class AvtalBindingsService {
    @NonNull
    AuditorAware<String> auditorAware;

    @NonNull
    private final ElnatKlassificeringService elnatKlassificeringService;

    @NonNull
    private final ElnatVarderingsprotokollService elnatVarderingsprotokollService;

    @NonNull
    private final ElnatVarderingsprotokollGeneratorService elnatVarderingsprotokollGeneratorService;

    @NonNull
    private final FastighetService fastighetService;

    @NonNull
    private final FiberKlassificeringService fiberKlassificeringService;

    @NonNull
    private final FiberProjektService fiberProjektService;

    @NonNull
    private final FiberVarderingsprotokollGeneratorService fiberVarderingsprotokollGeneratorService;

    @NonNull
    private final MapService mapService;

    @NonNull
    private final ProjektRepository projektRepository;

    /* Bara något standardvärde som det är tänkt att kunden får ändra på, fram tills det att vi implementerar något
    * system för att hantera bredden korrekt. */
    public static final Integer INLOST_BREDD_DEFAULT = 2;
    public static final int INLOST_BREDD_SKOG_LUFT_LOKALNAT = 8;
    public static final int INLOST_BREDD_SKOG_LUFT_REGIONNAT = 40;

    public AvtalBindings
    getAvtalBindings(UUID projektId, UUID fastighetId)
        throws IOException
    {
        var fastighetsInfo = fastighetService.fastighetsinfo(fastighetId, projektId);
        var kartor = getDelomradeskartor(projektId, fastighetId, fastighetsInfo.getOmraden());
        var handlaggare = auditorAware.getCurrentAuditor().orElse(null);
        var projekttyp = projektRepository.getProjekttyp(projektId);

        return AvtalBindings.builder()
            .egetTillvaratagandeAgarensVal(
                fastighetsInfo.getTillvaratagandeTyp() == TillvaratagandeTypDto.EGET_TILLVARATAGANDE)
            .rotnettoAgarensVal(fastighetsInfo.getTillvaratagandeTyp() == TillvaratagandeTypDto.ROTNETTO)
            .fastighetsbeteckning(fastighetsInfo.getFastighetsbeteckning())
            .kartor_multi(kartor)
            .kommun(nameWithFirstLetterCapitalized(fastighetsInfo.getKommunnamn()))
            .stationFran(fastighetsInfo.getFranStation())
            .stationsnamn(fastighetsInfo.getStationsnamn())
            .stationTill(fastighetsInfo.getTillStation())
            .markslag(fastighetsInfo.getMarkslag())
            .inlostBreddMeter(INLOST_BREDD_DEFAULT)
            .inlostBreddSkogLuftMeter(getInlostBredd(projekttyp))
            .inlostBreddSkogMeter(INLOST_BREDD_DEFAULT)
            .lan(nameWithFirstLetterCapitalized(fastighetsInfo.getLan()))
            .direktforlagdKabel(false)
            .handlaggare(handlaggare)
            .build();
    }

    public IntrangBindings
    getElnatIntrangBindings(UUID avtalId)
    {
        var klassificering = elnatKlassificeringService.klassificera(avtalId);

        var intrangBuilder = IntrangBindings.builder()
            .langdILuft(Math.round(klassificering.getLangdLuftledning()))
            .langdIMark(Math.round(klassificering.getLangdMarkledning()))
            .markforlagdKanalisation(klassificering.getLangdMarkledning() > 0.)
            .stolplinje(klassificering.getLangdLuftledning() > 0.);

        var vpOpt = elnatVarderingsprotokollService.getWithAvtalId(avtalId);
        if (vpOpt.isEmpty()) {
            log.warn("Fann inget värderingsprotokoll till avtal " + avtalId);
        }
        else {
            var vp = vpOpt.get();

            var hogstaSpanning = klassificering.getHogstaSpanningsniva();
            var elIntrangBindingsBuilder = ElIntrangBindings.builder()
                .ledningslittera(getLittera(vp))
                .hogstaSpanning(hogstaSpanning == null ? "" : new DecimalFormat("0.##").format(hogstaSpanning) + "kV");
            elIntrangBindingsBuilder = addTransformatorstationBindings(vp, elIntrangBindingsBuilder);

            intrangBuilder.elIntrangBindings(elIntrangBindingsBuilder.build());
        }

        return intrangBuilder.build();
    }

    public ErsattningBindings
    getErsattningBindings(UUID avtalId) throws IOException {
        var projekttyp = projektRepository.getProjekttypAvtal(avtalId);

        if (projekttyp == ProjektTypDto.FIBER) {
            var projektId = projektRepository.getIdByAvtalId(avtalId);
            var ersattningOpt = fiberVarderingsprotokollGeneratorService.getErsattning(avtalId);
            if (fiberProjektService.shouldHaveVarderingsprotokoll(projektId) && ersattningOpt.isPresent()) {
                var ersattning = ersattningOpt.get();
                return ErsattningBindings.builder()
                    .ersattningsbelopp(ersattning.getTotal())
                    .build();
            }
            else {
                return ErsattningBindings.builder()
                    .ersattningsbelopp(fastighetService.fastighetsinfo(avtalId).getErsattning())
                    .build();
            }
        }
        else {
            var ersattningOpt = elnatVarderingsprotokollGeneratorService.getErsattning(avtalId);
            if (ersattningOpt.isPresent()) {
                var ersattning = ersattningOpt.get();
                var rotnetto = ersattning.getRotnetto();
                var rotnettoMoms = rotnetto * 0.25;
                return ErsattningBindings.builder()
                    .ersattningsbelopp(ersattning.getTotal())
                    .rotnetto(round(rotnetto, 2))
                    .rotnettoMoms(round(rotnettoMoms, 2))
                    .rubrikUtanordningSjalvfaktura(rotnetto > 0. ? "Självfaktura" : "Utanordning")
                    .build();
            }
            return new ErsattningBindings();
        }
    }

    public IntrangBindings
    getFiberIntrangBindings(UUID avtalId)
    {
        var klassificering = fiberKlassificeringService.klassificera(avtalId);

        return IntrangBindings.builder()
            .langdILuft(Math.round(klassificering.getLangdLuftledning()))
            .langdIMark(Math.round(klassificering.getLangdMarkledning()))
            .markforlagdKanalisation(klassificering.getLangdMarkledning() > 0.)
            .stolplinje(klassificering.getLangdLuftledning() > 0.)
            .build();
    }

    private ElIntrangBindings.ElIntrangBindingsBuilder
    addTransformatorstationBindings(ElnatVarderingsprotokollDto vp, ElIntrangBindings.ElIntrangBindingsBuilder builder)
    {
        var natstationOpt = getLargestNatskap(vp);

        if (natstationOpt.isPresent()) {
            var natstation = natstationOpt.get();
            var maxYta = getYta(natstation.getTyp());

            return builder
                .hasTransformatorstation(true)
                .transformatorstationBreddAMeter(maxYta)
                .transformatorstationBreddBMeter(maxYta)
                .transformatorstationYta(maxYta + "x" + maxYta + "m")
                .transformatorstationNamn(natstation.getBeskrivning());
        }
        else {
            return builder;
        }
    }

    private Integer getInlostBredd(ProjektTypDto projektTyp) {
        switch (projektTyp) {
            case LOKALNAT:
                return INLOST_BREDD_SKOG_LUFT_LOKALNAT;
            case REGIONNAT:
                return INLOST_BREDD_SKOG_LUFT_REGIONNAT;
            case FIBER:
                return INLOST_BREDD_DEFAULT;
            default:
                throw new IllegalArgumentException("Okänd projekttyp: " + projektTyp);
        }
    }

    private Integer compareDelomraden(FastighetDelomradeDto omr1, FastighetDelomradeDto omr2) {
        return omr1.getOmradeNr().compareTo(omr2.getOmradeNr());
    }

    private List<ByteArrayResource>
    getDelomradeskartor(UUID projektId,
                        UUID fastighetId,
                        List<FastighetDelomradeDto> delomraden) throws IOException
    {
        var kartor = new ArrayList<ByteArrayResource>();

        if (delomraden == null) {
            return kartor;
        }

        for (FastighetDelomradeDto delomrade: sortDelomraden(delomraden)) {
            var karta = mapService.getAvtalskarta(projektId, fastighetId, delomrade.getOmradeNr().longValue());
            kartor.add(karta);
        }

        return kartor;
    }

    private String nameWithFirstLetterCapitalized(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        else if (name.length() == 1) {
            return name.toUpperCase();
        }
        else {
            var lowerCase = name.toLowerCase();
            return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
        }
    }

    private Optional<ElnatPunktersattningDto> getLargestNatskap(ElnatVarderingsprotokollDto vp) {
        if (vp.getPunktersattning() == null) {
            return Optional.empty();
        }

        var maxYta = 0;
        Optional<ElnatPunktersattningDto> largestNatskap = Optional.empty();
        for (var punktersattning: vp.getPunktersattning()) {
            var yta = getYta(punktersattning.getTyp());
            if (yta > maxYta) {
                maxYta = yta;
                largestNatskap = Optional.of(punktersattning);
            }
        }

        return largestNatskap;
    }

    private String getLittera(ElnatVarderingsprotokollDto vp) {
        if (vp.getMetadata() == null) {
            log.warn("Hittade ingen metadata för elnätvärderingsprotokoll " + vp.getId());
            return null;
        }
        else {
            return vp.getMetadata().getLedning();
        }
    }

    private Integer getYta(ElnatPunktersattningTypDto typ) {
        switch (typ) {
            case NATSTATION_JORDBRUKSIMPEDIMENT_6X6M:
            case NATSTATION_OVRIGMARK_6X6M:
            case NATSTATION_SKOG_6X6M:
                return 6;
            case NATSTATION_JORDBRUKSIMPEDIMENT_8X8M:
            case NATSTATION_OVRIGMARK_8X8M:
            case NATSTATION_SKOG_8X8M:
                return 8;
            case NATSTATION_JORDBRUKSIMPEDIMENT_10X10M:
            case NATSTATION_OVRIGMARK_10X10M:
            case NATSTATION_SKOG_10X10M:
                return 10;
            default:
                return 0;
        }
    }

    private double round(double val, int precision) {
        var rnd = Math.pow(10, precision);
        return Math.round(val * rnd) / rnd;
    }

    private List<FastighetDelomradeDto> sortDelomraden(Collection<FastighetDelomradeDto> delomraden) {
        return delomraden.stream()
            .sorted(this::compareDelomraden)
            .collect(Collectors.toList());
    }
}
