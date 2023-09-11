package se.metria.markkoll.service.intrang.converters.avtalstypevaluator;

import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.openapi.model.AvtalstypDto;

public interface AvtalstypEvaluator {
    AvtalstypDto getAvtalstyp(SimpleFeature feature, String attributeName);
}
