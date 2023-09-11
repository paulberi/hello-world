package se.metria.markkoll.service.intrang.converters.featuretointrang;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.IntrangsStatusDto;
import se.metria.markkoll.openapi.model.IntrangsSubtypDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.AvtalstypEvaluator;
import se.metria.xplore.maputils.GeometryUtil;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DpPowerFeatureToIntrangConverter implements FeatureToIntrangConverter {
    private final List<Integer> otypeMarkledning = Arrays.asList(700101, 700104, 700111);
    private final List<Integer> otypeLuftledning = Arrays.asList(700100, 700102, 700103, 700112, 700113);
    private final List<Integer> otypeNatstation = Arrays.asList(702001, 702005, 702006);
    private final List<Integer> otypeKabelskap = Arrays.asList(709000);
    private final List<Integer> subtypeInmattStrak = Arrays.asList(1, 11);
    private final List<Integer> subtypeOsakertLage = Arrays.asList(21, 31);

    @NonNull
    private final AvtalstypEvaluator avtalstypEvaluator;

    @Override
    public IntrangsStatusDto getIntrangStatus(SimpleFeature feature) {
        var dpState = getDpState(feature);

        if(dpState == 100) {
            return IntrangsStatusDto.NY;
        }
        else if(dpState == 200) {
            return IntrangsStatusDto.BEVARAS;
        }
        else {
            return IntrangsStatusDto.RASERAS;
        }
    }

    @Override
    public IntrangsSubtypDto getIntrangsSubtyp(SimpleFeature feature) {
        var dpOtype = getDpOtype(feature);
        var dpCtype = getDpCtype(feature);
        var dpSubtype = getDpSubtype(feature);

        if (dpCtype == 4000 && otypeMarkledning.contains(dpOtype)) {
            return IntrangsSubtypDto.MARKLEDNING;
        }
        else if (dpCtype == 4000 && otypeLuftledning.contains(dpOtype)) {
            return IntrangsSubtypDto.LUFTLEDNING;
        }
        else if (isStrak(dpCtype, dpOtype) && subtypeInmattStrak.contains(dpSubtype)) {
            return IntrangsSubtypDto.INMATT_STRAK;
        }
        else if (isStrak(dpCtype, dpOtype) && subtypeOsakertLage.contains(dpSubtype)) {
            return IntrangsSubtypDto.OSAKERT_LAGE;
        }
        else {
            return IntrangsSubtypDto.NONE;
        }
    }

    @Override
    public IntrangstypDto getIntrangsTyp(SimpleFeature feature) {
        var dpOtype = getDpOtype(feature);
        var dpCtype = getDpCtype(feature);
        var dpSubtype = getDpSubtype(feature);

        if (dpCtype == 4000 && (otypeMarkledning.contains(dpOtype) || otypeLuftledning.contains(dpOtype))) {
            return dpSubtype <= 4 ? IntrangstypDto.LAGSPANNINGSLEDNING : IntrangstypDto.HOGSPANNINGSLEDNING;
        }
        else if (dpCtype == 4002 && otypeNatstation.contains(dpOtype)) {
            return IntrangstypDto.NATSTATION;
        }
        else if (dpCtype == 4002 && otypeKabelskap.contains(dpOtype)) {
            return IntrangstypDto.KABELSKAP;
        }
        else if (isStrak(dpCtype, dpOtype)) {
            return IntrangstypDto.STRAK;
        }
        else {
            var subCType = getSubCtype(feature);
            log.warn("Okänd dpPower feature '{}' (dp_ctype={}, dp_otype={}, dp_subtype={}", subCType, dpCtype, dpOtype,
                dpSubtype);
            return  IntrangstypDto.OKAND;
        }
    }

    @Override
    public Double getSpanningsniva(SimpleFeature feature) {
        var dpCtype = getDpCtype(feature);
        var dpOtype = getDpOtype(feature);
        var dpSubtype = getDpSubtype(feature);

        if (isStrak(dpCtype, dpOtype)) {
            return null;
        }
        else switch (dpSubtype) {
            case 1:
                return 0.230;
            case 2:
                return 0.400;
            case 3:
                return 0.500;
            case 4:
                return 1.0;
            case 5:
                return 6.0;
            case 6:
                return 10.0;
            case 7:
                return 20.0;
            case 8:
                return 30.0;
            case 9:
                return 40.0;
            case 10:
                return 50.0;
            case 11:
                return 70.0;
            case 13:
                return 130.0;
            case 14:
                return 150.0;
            case 15:
                return 220.0;
            case 16:
                return 400.0;
            default:
                return null;
        }
    }

    @Override
    public AvtalstypDto getAvtalstyp(SimpleFeature feature) {
        return avtalstypEvaluator.getAvtalstyp(feature, "OWNER");
    }

    private Integer getDpCtype(SimpleFeature feature) {
        return GeometryUtil.getAttributeInt(feature, "dp_ctype");
    }

    private Integer getDpOtype(SimpleFeature feature) {
        return GeometryUtil.getAttributeInt(feature, "dp_otype");
    }

    private Integer getDpState(SimpleFeature feature) {
        return GeometryUtil.getAttributeInt(feature, "dp_state");
    }

    private Integer getDpSubtype(SimpleFeature feature) {
        return GeometryUtil.getAttributeInt(feature, "dp_subtype");
    }

    private String getSubCtype(SimpleFeature feature) {
        return GeometryUtil.getAttributeString(feature, "sub_c_type");
    }

    private boolean isStrak(Integer dpCtype, Integer dpOtype) {
        return dpCtype == 80010 && dpOtype == 800003;
    }
}
