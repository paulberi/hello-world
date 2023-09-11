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

@Slf4j
@RequiredArgsConstructor
public class DpComFeatureToIntrangConverter implements FeatureToIntrangConverter {
    @NonNull
    private final AvtalstypEvaluator avtalstypEvaluator;

    @Override
    public IntrangsStatusDto getIntrangStatus(SimpleFeature feature) {
        return IntrangsStatusDto.NY;
    }

    @Override
    public IntrangsSubtypDto getIntrangsSubtyp(SimpleFeature feature) {
        if (isStrak(feature)) {
            var dpSubtype = getDpSubtype(feature);
            switch (dpSubtype) {
                case 29:
                case 25:
                    return IntrangsSubtypDto.LUFTSTRAK;
                default:
                    return IntrangsSubtypDto.MARKSTRAK;
            }
        } else {
            return IntrangsSubtypDto.NONE;
        }
    }

    @Override
    public IntrangstypDto getIntrangsTyp(SimpleFeature feature) {
        var dpOtype = getDpOtype(feature);
        var dpCtype = getDpCtype(feature);

        if (dpOtype == 800027 && dpCtype == 80030) {
            return IntrangstypDto.MARKSKAP;
        } else if (dpOtype == 800000 && dpCtype == 80030) {
            return IntrangstypDto.BRUNN;
        } else if (dpOtype == 800006 && dpCtype == 80030) {
            return IntrangstypDto.TEKNIKBOD;
        } else if (isStrak(feature)) {
            return IntrangstypDto.STRAK;
        } else {
            var subCType = getSubCType(feature);
            var dpSubtype = getDpSubtype(feature);
            log.warn("Okänd dpCom feature '{}' (dp_ctype={}, dp_otype={}, dp_subtype={}", subCType, dpCtype, dpOtype,
                dpSubtype);
            return  IntrangstypDto.OKAND;
        }
    }

    @Override
    public Double getSpanningsniva(SimpleFeature feature) {
        return null;
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

    private Integer getDpSubtype(SimpleFeature feature) {
        return GeometryUtil.getAttributeInt(feature, "dp_subtype");
    }

    private String getSubCType(SimpleFeature feature) {
        return GeometryUtil.getAttributeString(feature, "sub_c_type");
    }

    private Boolean isStrak(SimpleFeature feature) {
        var dpOtype = getDpOtype(feature);
        var dpCtype = getDpCtype(feature);

        return dpOtype == 800003 && dpCtype == 80010;
    }
}
