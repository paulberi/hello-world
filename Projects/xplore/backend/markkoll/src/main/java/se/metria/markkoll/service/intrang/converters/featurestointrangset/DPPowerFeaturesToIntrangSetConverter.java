package se.metria.markkoll.service.intrang.converters.featurestointrangset;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.AvtalstypEvaluator;
import se.metria.markkoll.service.intrang.converters.featuretointrang.DpPowerFeatureToIntrangConverter;
import se.metria.xplore.maputils.GeometryUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class DPPowerFeaturesToIntrangSetConverter implements FeaturesToIntrangSetConverter {
    private DpPowerFeatureToIntrangConverter dpPowerFeatureToIntrangConverter;

    public DPPowerFeaturesToIntrangSetConverter(AvtalstypEvaluator avtalstypEvaluator) {
        this.dpPowerFeatureToIntrangConverter = new DpPowerFeatureToIntrangConverter(avtalstypEvaluator);
    }

    public Set<IntrangEntity> convert(Collection<SimpleFeature> features) {
        var intrangSet = new HashSet<IntrangEntity>();

        var featuresByOid = features.stream().collect(Collectors.groupingBy(this::getDpOid));

        for (var entry: featuresByOid.entrySet()) {
            var featureList = entry.getValue();
            var intrangFeatures = getIntrangFeatures(entry.getValue());
            var littera = getLittera(featureList);

            for (var intrangFeature: intrangFeatures) {
                var intrangEntity = getIntrangEntity(intrangFeature, littera);
                intrangSet.add(intrangEntity);
            }
        }

        return intrangSet;
    }

    private Integer getDpCtype(SimpleFeature feature) {
        return GeometryUtil.getAttributeInt(feature, "dp_ctype");
    }

    private Integer getDpOid(SimpleFeature feature) {
        return GeometryUtil.getAttributeInt(feature, "dp_oid");
    }

    private Geometry getGeometry(SimpleFeature feature) {
        return (Geometry)feature.getDefaultGeometryProperty().getValue();
    }

    private IntrangEntity getIntrangEntity(SimpleFeature feature, String littera) {
        var intrang = new IntrangEntity();

        intrang.setType(dpPowerFeatureToIntrangConverter.getIntrangsTyp(feature).toString());
        intrang.setSubtype(dpPowerFeatureToIntrangConverter.getIntrangsSubtyp(feature).toString());
        intrang.setStatus(dpPowerFeatureToIntrangConverter.getIntrangStatus(feature).toString());
        intrang.setGeom(getGeometry(feature));
        intrang.setSpanningsniva(dpPowerFeatureToIntrangConverter.getSpanningsniva(feature));
        intrang.setAvtalstyp(dpPowerFeatureToIntrangConverter.getAvtalstyp(feature));
        intrang.setLittera(littera);

        return intrang;
    }

    private List<SimpleFeature> getIntrangFeatures(Collection<SimpleFeature> featureList) {
        return featureList.stream()
            .filter(f -> getDpCtype(f) != 80000)
            .collect(Collectors.toList());
    }

    private String getLittera(Collection<SimpleFeature> featureList) {
        return featureList.stream()
            .filter(f -> getDpCtype(f) == 80000)
            .map(this::getText)
            .collect(Collectors.joining(" "));
    }

    private String getText(SimpleFeature feature) {
        return GeometryUtil.getAttributeString(feature, "text");
    }
}
