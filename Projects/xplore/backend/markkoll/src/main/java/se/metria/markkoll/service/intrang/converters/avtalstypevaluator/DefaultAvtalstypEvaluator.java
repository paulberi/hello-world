package se.metria.markkoll.service.intrang.converters.avtalstypevaluator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.xplore.maputils.GeometryUtil;

import java.util.Map;

@RequiredArgsConstructor
public class DefaultAvtalstypEvaluator implements AvtalstypEvaluator {

    @NonNull
    private final Map<String, AvtalstypDto> filters;

    @NonNull
    private final AvtalstypDto defaultAvtalstyp;

    @Override
    public AvtalstypDto getAvtalstyp(SimpleFeature feature, String attributeName) {
        try {
            /* Attributnamnen är case sensitive. Kan vara problematiskt om det t.ex. är ett attribut som en användare
             * plitat i manuellt, där vi inte vet vad som är skrivet med gemener eller versaler. */
            var realAttributeName = feature.getFeatureType().getAttributeDescriptors().stream()
                .map(a -> a.getLocalName())
                .filter(name -> name.toUpperCase().equals(attributeName.toUpperCase()))
                .findAny();

            if (realAttributeName.isEmpty()) {
                return defaultAvtalstyp;
            }

            var attribute = GeometryUtil.extractAttribute(feature, realAttributeName.get(), String.class).toUpperCase();

            if(!filters.containsKey(attribute)) {
                return  defaultAvtalstyp;
            }

            return filters.get(attribute);
        }
        catch(IllegalArgumentException e) {
            throw new MarkkollException(MarkkollError.IMPORT_ERROR, e);
        }
    }
}
