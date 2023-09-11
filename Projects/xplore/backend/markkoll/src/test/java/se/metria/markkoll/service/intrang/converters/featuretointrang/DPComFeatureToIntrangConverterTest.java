package se.metria.markkoll.service.intrang.converters.featuretointrang;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.IntrangsStatusDto;
import se.metria.markkoll.openapi.model.IntrangsSubtypDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.AvtalstypEvaluator;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.ConstantAvtalstypEvaluator;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.metria.xplore.maputils.GeometryUtil.createPoint;

class DPComFeatureToIntrangConverterTest {
    AvtalstypEvaluator evaluator = new ConstantAvtalstypEvaluator(AvtalstypDto.INTRANG);
    DpComFeatureToIntrangConverter converter = new DpComFeatureToIntrangConverter(evaluator);

    @ParameterizedTest
    @MethodSource("dpComTestdata")
    void
    ska_konvertera_intrångsstatus(Integer dpCtype,
                                  Integer dpOtype,
                                  Integer dpSubtype,
                                  IntrangstypDto intrangstypDto,
                                  IntrangsSubtypDto intrangsSubtypDto) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, dpCtype, dpOtype, dpSubtype);

        // When
        var status = converter.getIntrangStatus(feature);

        assertEquals(IntrangsStatusDto.NY, status);
    }

    @ParameterizedTest
    @MethodSource("dpComTestdata")
    void
    ska_konvertera_subtyp(Integer dpCtype,
                          Integer dpOtype,
                          Integer dpSubtype,
                          IntrangstypDto intrangstypDto,
                          IntrangsSubtypDto intrangsSubtypDto) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, dpCtype, dpOtype, dpSubtype);

        // When
        var subtyp = converter.getIntrangsSubtyp(feature);

        // Then
        assertEquals(intrangsSubtypDto, subtyp);
    }

    @ParameterizedTest
    @MethodSource("dpComTestdata")
    void
    ska_konvertera_intrångstyp(Integer dpCtype,
                               Integer dpOtype,
                               Integer dpSubtype,
                               IntrangstypDto intrangstypDto,
                               IntrangsSubtypDto intrangsSubtypDto) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, dpCtype, dpOtype, dpSubtype);

        // When
        var typ = converter.getIntrangsTyp(feature);

        // Then
        assertEquals(intrangstypDto, typ);
    }

    @ParameterizedTest
    @MethodSource("dpComTestdata")
    void
    ska_konvertera_spänningsnivå(Integer dpCtype,
                                 Integer dpOtype,
                                 Integer dpSubtype,
                                 IntrangstypDto intrangstypDto,
                                 IntrangsSubtypDto intrangsSubtypDto) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, dpCtype, dpOtype, dpSubtype);

        // When
        var spanningsniva = converter.getSpanningsniva(feature);

        // Then
        assertEquals(null, spanningsniva);
    }

    private SimpleFeature
    createFeature(Geometry geom, Integer dpCtype, Integer dpOtype, Integer dpSubtype) {
        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        simpleFeatureTypeBuilder.setName("Vem bryr sig? Vi behöver bara någon godtycklig featuretype");
        simpleFeatureTypeBuilder.add("the_geom", Geometry.class);
        simpleFeatureTypeBuilder.add("dp_ctype", Long.class);
        simpleFeatureTypeBuilder.add("dp_otype", Long.class);
        simpleFeatureTypeBuilder.add("dp_subtype", Long.class);

        var simpleFeature = simpleFeatureTypeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(simpleFeature);
        builder.set("the_geom", geom);
        builder.set("dp_ctype", dpCtype);
        builder.set("dp_otype", dpOtype);
        builder.set("dp_subtype", dpSubtype);

        return builder.buildFeature(null);
    }

    private static Stream<Arguments> dpComTestdata() {
        return Stream.of(
            Arguments.of(80030, 800000, null, IntrangstypDto.BRUNN, IntrangsSubtypDto.NONE),
            Arguments.of(80030, 800027, null, IntrangstypDto.MARKSKAP, IntrangsSubtypDto.NONE),
            Arguments.of(80010, 800003, 25, IntrangstypDto.STRAK, IntrangsSubtypDto.LUFTSTRAK),
            Arguments.of(80010, 800003, 29, IntrangstypDto.STRAK, IntrangsSubtypDto.LUFTSTRAK),
            Arguments.of(80010, 800003, 32, IntrangstypDto.STRAK, IntrangsSubtypDto.MARKSTRAK),
            Arguments.of(80030, 800006, null, IntrangstypDto.TEKNIKBOD, IntrangsSubtypDto.NONE)
        );
    }
}
