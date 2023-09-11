package se.metria.markkoll.service.intrang.converters.featurestointrangset;

import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.entity.intrang.IntrangEntity;

import java.util.Collection;
import java.util.Set;

public interface FeaturesToIntrangSetConverter {
    Set<IntrangEntity> convert(Collection<SimpleFeature> features);
}