package se.metria.markkoll.service.intrang.converters.featuretointrang;

import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.IntrangsStatusDto;
import se.metria.markkoll.openapi.model.IntrangsSubtypDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;

public interface FeatureToIntrangConverter {
    IntrangsStatusDto getIntrangStatus(SimpleFeature feature);
    IntrangsSubtypDto getIntrangsSubtyp(SimpleFeature feature);
    IntrangstypDto getIntrangsTyp(SimpleFeature feature);
    Double getSpanningsniva(SimpleFeature feature);
    AvtalstypDto getAvtalstyp(SimpleFeature feature);
}
