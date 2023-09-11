package se.metria.markkoll.service.intrang.converters.avtalstypevaluator;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.openapi.model.AvtalstypDto;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultAvtalstypEvaluatorTest {

    @ParameterizedTest
    @MethodSource("filters")
    void så_ska_evaluatorn_returnera_ratt_avtalstyp(String attributeValue, AvtalstypDto avtalstyp) {
        // Given
        var map = new HashMap<String, AvtalstypDto>();
        map.put("INTRANG", AvtalstypDto.INTRANG);
        map.put("SERVIS", AvtalstypDto.SERVIS);
        map.put("REV", AvtalstypDto.REV);
        var evaulator = new DefaultAvtalstypEvaluator(map, AvtalstypDto.INTRANG);

        var feature = createFeature("CaSe_InSeNsItIvE", attributeValue);

        // When
        var avtalstypActual = evaulator.getAvtalstyp(feature, "case_insensitive");

        // Then
        assertEquals(avtalstyp, avtalstypActual);
    }

    @Test
    void så_ska_defaultvärdet_returneras_om_attributet_saknas() {
        // Given
        var defaulAvtalstyp =  AvtalstypDto.INTRANG;
        var map = new HashMap<String, AvtalstypDto>();
        map.put("INTRANG", AvtalstypDto.INTRANG);
        map.put("SERVIS", AvtalstypDto.SERVIS);
        map.put("REV", AvtalstypDto.REV);
        var evaulator = new DefaultAvtalstypEvaluator(map, defaulAvtalstyp);

        var feature = createFeature("another_attribute", "another value");

        // When
        var avtalstypActual = evaulator.getAvtalstyp(feature, "wanted_attribute");

        // Then
        assertEquals(defaulAvtalstyp, avtalstypActual);
    }

    private SimpleFeature
    createFeature(String attributeName, String attributeValue) {
        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        simpleFeatureTypeBuilder.setName("feature");
        simpleFeatureTypeBuilder.add(attributeName, String.class);

        var simpleFeature = simpleFeatureTypeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(simpleFeature);
        builder.set(attributeName, attributeValue);

        return builder.buildFeature(null);
    }

    private static Stream<Arguments> filters() {
        return Stream.of(
            Arguments.of("intrang", AvtalstypDto.INTRANG),
            Arguments.of("servis", AvtalstypDto.SERVIS),
            Arguments.of("rev", AvtalstypDto.REV),
            Arguments.of("något knasigt",  AvtalstypDto.INTRANG)
        );
    }
}