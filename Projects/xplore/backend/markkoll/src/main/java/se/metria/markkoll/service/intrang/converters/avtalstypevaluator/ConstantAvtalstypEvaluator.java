package se.metria.markkoll.service.intrang.converters.avtalstypevaluator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.openapi.model.AvtalstypDto;

@RequiredArgsConstructor
public class ConstantAvtalstypEvaluator implements AvtalstypEvaluator {
    @NonNull
    public final AvtalstypDto avtalstyp;

    @Override
    public AvtalstypDto getAvtalstyp(SimpleFeature feature, String attributeName) {
        return this.avtalstyp;
    }
}
