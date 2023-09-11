package se.metria.markkoll.service.intrang.converters.featurestointrangset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.openapi.model.IndataTypDto;
import se.metria.markkoll.service.intrang.converters.featuretointrang.FeatureToIntrangConverter;
import se.metria.xplore.maputils.GeometryUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class SingleFeatureToIntrangSetConverter implements FeaturesToIntrangSetConverter {
    @NonNull
    private final FeatureToIntrangConverter featureToIntrangConverter;

    @NonNull
    private final IndataTypDto indataTypDto;

    @Override
    public Set<IntrangEntity> convert(Collection<SimpleFeature> features) {
        var intrangSet = new HashSet<IntrangEntity>();

        for (var feature: features) {
            var intrang = IntrangEntity.builder()
                .type(featureToIntrangConverter.getIntrangsTyp(feature).toString())
                .subtype(featureToIntrangConverter.getIntrangsSubtyp(feature).toString())
                .status(featureToIntrangConverter.getIntrangStatus(feature).toString())
                .spanningsniva(featureToIntrangConverter.getSpanningsniva(feature))
                .geom(getGeometry(feature))
                .avtalstyp(featureToIntrangConverter.getAvtalstyp(feature))
                .build();

            intrangSet.add(intrang);
        }

        return intrangSet;
    }

    private Geometry getGeometry(SimpleFeature feature) {
        return (Geometry)feature.getDefaultGeometryProperty().getValue();
    }
}
