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
import static se.metria.markkoll.openapi.model.IntrangstypDto.HOGSPANNINGSLEDNING;
import static se.metria.markkoll.openapi.model.IntrangstypDto.LAGSPANNINGSLEDNING;
import static se.metria.xplore.maputils.GeometryUtil.createPoint;

class DpPowerFeatureToIntrangConverterTest {
    AvtalstypEvaluator evaluator = new ConstantAvtalstypEvaluator(AvtalstypDto.INTRANG);
    DpPowerFeatureToIntrangConverter converter = new DpPowerFeatureToIntrangConverter(evaluator);

    @ParameterizedTest
    @MethodSource("dpPowerStatus")
    void
    ska_konvertera_intrångsstatus(Integer dpState,
                                  IntrangsStatusDto statusExpect) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, 0, 0, 0, dpState);

        // When
        var status = converter.getIntrangStatus(feature);

        assertEquals(statusExpect, status);
    }

    @ParameterizedTest
    @MethodSource("dpPowerSubtyp")
    void
    ska_konvertera_subtyp(Integer dpCtype,
                          Integer dpOtype,
                          Integer dpSubtype,
                          IntrangsSubtypDto subtypExpect) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, dpCtype, dpOtype, dpSubtype, 0);

        // When
        var subtyp = converter.getIntrangsSubtyp(feature);

        // Then
        assertEquals(subtypExpect, subtyp);
    }

    @ParameterizedTest
    @MethodSource("dpPowerIntrangstyp")
    void
    ska_konvertera_intrångstyp(Integer dpCtype,
                               Integer dpOtype,
                               Integer dpSubtype,
                               IntrangstypDto intrangstypExpect) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, dpCtype, dpOtype, dpSubtype, 0);

        // When
        var typ = converter.getIntrangsTyp(feature);

        // Then
        assertEquals(intrangstypExpect, typ);
    }

    @ParameterizedTest
    @MethodSource("dpPowerSpanningsniva")
    void
    ska_konvertera_spänningsnivå(Integer dpSubtype,
                                 Integer dpCtype,
                                 Integer dpOtype,
                                 Double spanningsnivaExpect) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, dpCtype, dpOtype, dpSubtype, 0);

        // When
        var spanningsniva = converter.getSpanningsniva(feature);

        // Then
        assertEquals(spanningsnivaExpect, spanningsniva);
    }

    private SimpleFeature
    createFeature(Geometry geom, Integer dpCtype, Integer dpOtype, Integer dpSubtype, Integer dpState) {
        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        simpleFeatureTypeBuilder.setName("Vem bryr sig? Vi behöver bara någon godtycklig featuretype");
        simpleFeatureTypeBuilder.add("the_geom", Geometry.class);
        simpleFeatureTypeBuilder.add("cp_oid", Long.class);
        simpleFeatureTypeBuilder.add("dp_ctype", Long.class);
        simpleFeatureTypeBuilder.add("dp_otype", Long.class);
        simpleFeatureTypeBuilder.add("dp_subtype", Long.class);
        simpleFeatureTypeBuilder.add("dp_state", Long.class);
        simpleFeatureTypeBuilder.add("text", String.class);

        var simpleFeature = simpleFeatureTypeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(simpleFeature);
        builder.set("the_geom", geom);
        builder.set("dp_ctype", dpCtype);
        builder.set("dp_otype", dpOtype);
        builder.set("dp_subtype", dpSubtype);
        builder.set("dp_state", dpState);

        return builder.buildFeature(null);
    }

    private static Stream<Arguments> dpPowerIntrangstyp() {
        return Stream.of(
            Arguments.of(4000, 700101, 4, LAGSPANNINGSLEDNING),
            Arguments.of(4000, 700104, 4, LAGSPANNINGSLEDNING),
            Arguments.of(4000, 700111, 4, LAGSPANNINGSLEDNING),
            Arguments.of(4000, 700101, 5, HOGSPANNINGSLEDNING),
            Arguments.of(4000, 700104, 5, HOGSPANNINGSLEDNING),
            Arguments.of(4000, 700111, 5, HOGSPANNINGSLEDNING),

            Arguments.of(4000, 700100, 4, LAGSPANNINGSLEDNING),
            Arguments.of(4000, 700102, 4, LAGSPANNINGSLEDNING),
            Arguments.of(4000, 700103, 4, LAGSPANNINGSLEDNING),
            Arguments.of(4000, 700112, 4, LAGSPANNINGSLEDNING),
            Arguments.of(4000, 700113, 4, LAGSPANNINGSLEDNING),
            Arguments.of(4000, 700100, 5, HOGSPANNINGSLEDNING),
            Arguments.of(4000, 700102, 5, HOGSPANNINGSLEDNING),
            Arguments.of(4000, 700103, 5, HOGSPANNINGSLEDNING),
            Arguments.of(4000, 700112, 5, HOGSPANNINGSLEDNING),
            Arguments.of(4000, 700113, 5, HOGSPANNINGSLEDNING),

            Arguments.of(4002, 702001, null, IntrangstypDto.NATSTATION),
            Arguments.of(4002, 702006, null, IntrangstypDto.NATSTATION),
            Arguments.of(4002, 702005, null, IntrangstypDto.NATSTATION),
            Arguments.of(4002, 709000, null, IntrangstypDto.KABELSKAP),

            Arguments.of(4000, 709000, null, IntrangstypDto.OKAND),
            Arguments.of(4002, 700100, null, IntrangstypDto.OKAND),
            Arguments.of(123, 456, null, IntrangstypDto.OKAND),

            Arguments.of(80010, 800003, 1, IntrangstypDto.STRAK),
            Arguments.of(80010, 800003, 11, IntrangstypDto.STRAK),
            Arguments.of(80010, 800003, 21, IntrangstypDto.STRAK),
            Arguments.of(80010, 800003, 31, IntrangstypDto.STRAK)
        );
    }

    private static Stream<Arguments> dpPowerSpanningsniva() {
        return Stream.of(
            Arguments.of(1, 4000, 700101, 0.23),
            Arguments.of(2, 4000, 700101, 0.4),
            Arguments.of(3, 4000, 700101, 0.5),
            Arguments.of(4, 4000, 700101, 1.),
            Arguments.of(5, 4000, 700101, 6.),
            Arguments.of(6, 4000, 700101, 10.),
            Arguments.of(7, 4000, 700101, 20.),
            Arguments.of(8, 4000, 700101, 30.),
            Arguments.of(9, 4000, 700101, 40.),
            Arguments.of(10, 4000, 700101, 50.),
            Arguments.of(11, 4000, 700101, 70.),
            Arguments.of(12, 4000, 700101, null),
            Arguments.of(13, 4000, 700101, 130.),
            Arguments.of(14, 4000, 700101, 150.),
            Arguments.of(15, 4000, 700101, 220.),
            Arguments.of(16, 4000, 700101, 400.),
            Arguments.of(17, 4000, 700101, null),

            Arguments.of(1, 80010, 800003, null),
            Arguments.of(11, 80010, 800003, null),
            Arguments.of(21, 80010, 800003, null),
            Arguments.of(31, 80010, 800003, null)
        );
    }

    private static Stream<Arguments> dpPowerStatus() {
        return Stream.of(
            Arguments.of(100, IntrangsStatusDto.NY),
            Arguments.of(200, IntrangsStatusDto.BEVARAS),
            Arguments.of(123, IntrangsStatusDto.RASERAS),
            Arguments.of(40001, IntrangsStatusDto.RASERAS)
        );
    }

    private static Stream<Arguments> dpPowerSubtyp() {
        return Stream.of(
            Arguments.of(4000, 700101, 0, IntrangsSubtypDto.MARKLEDNING),
            Arguments.of(4000, 700104, 0, IntrangsSubtypDto.MARKLEDNING),
            Arguments.of(4000, 700111, 0, IntrangsSubtypDto.MARKLEDNING),

            Arguments.of(4000, 700100, 0, IntrangsSubtypDto.LUFTLEDNING),
            Arguments.of(4000, 700102, 0, IntrangsSubtypDto.LUFTLEDNING),
            Arguments.of(4000, 700103, 0, IntrangsSubtypDto.LUFTLEDNING),
            Arguments.of(4000, 700112, 0, IntrangsSubtypDto.LUFTLEDNING),
            Arguments.of(4000, 700113, 0, IntrangsSubtypDto.LUFTLEDNING),

            Arguments.of(4005, 700113, 0, IntrangsSubtypDto.NONE),
            Arguments.of(4000, 711111, 0, IntrangsSubtypDto.NONE),

            Arguments.of(80010, 800003, 1, IntrangsSubtypDto.INMATT_STRAK),
            Arguments.of(80010, 800003, 11, IntrangsSubtypDto.INMATT_STRAK),
            Arguments.of(80010, 800003, 21, IntrangsSubtypDto.OSAKERT_LAGE),
            Arguments.of(80010, 800003, 31, IntrangsSubtypDto.OSAKERT_LAGE)
        );
    }
}
