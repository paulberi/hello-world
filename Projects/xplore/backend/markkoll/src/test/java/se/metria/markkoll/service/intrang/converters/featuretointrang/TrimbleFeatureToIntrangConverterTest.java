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

public class TrimbleFeatureToIntrangConverterTest {
    AvtalstypEvaluator evaluator = new ConstantAvtalstypEvaluator(AvtalstypDto.INTRANG);
    TrimbleFeatureToIntrangConverter converter = new TrimbleFeatureToIntrangConverter(evaluator);

    @ParameterizedTest
    @MethodSource("dpComTestdata")
    void
    ska_konvertera_intrångsstatus(String typeName,
                                  Integer classId,
                                  Integer status,
                                  String operationS,
                                  String remark,
                                  IntrangsStatusDto intrangsStatusDto,
                                  IntrangstypDto intrangstypDto,
                                  IntrangsSubtypDto intrangsSubtypDto,
                                  Double spanningsniva) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, typeName, classId, status, operationS, remark);

        // When
        var intrangStatus = converter.getIntrangStatus(feature);

        assertEquals(intrangsStatusDto, intrangStatus);
    }


    @ParameterizedTest
    @MethodSource("dpComTestdata")
    void
    ska_konvertera_subtyp(String typeName,
                          Integer classId,
                          Integer status,
                          String operationS,
                          String remark,
                          IntrangsStatusDto intrangsStatusDto,
                          IntrangstypDto intrangstypDto,
                          IntrangsSubtypDto intrangsSubtypDto,
                          Double spanningsniva) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, typeName, classId, status, operationS, remark);

        // When
        var subtyp = converter.getIntrangsSubtyp(feature);

        // Then
        assertEquals(intrangsSubtypDto, subtyp);
    }


    @ParameterizedTest
    @MethodSource("dpComTestdata")
    void
    ska_konvertera_intrångstyp(String typeName,
                               Integer classId,
                               Integer status,
                               String operationS,
                               String remark,
                               IntrangsStatusDto intrangsStatusDto,
                               IntrangstypDto intrangstypDto,
                               IntrangsSubtypDto intrangsSubtypDto,
                               Double spanningsniva) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, typeName, classId, status, operationS, remark);

        // When
        var typ = converter.getIntrangsTyp(feature);

        // Then
        assertEquals(intrangstypDto, typ);
    }

    @ParameterizedTest
    @MethodSource("dpComTestdata")
    void
    ska_konvertera_spänningsnivå(String typeName,
                                 Integer classId,
                                 Integer status,
                                 String operationS,
                                 String remark,
                                 IntrangsStatusDto intrangsStatusDto,
                                 IntrangstypDto intrangstypDto,
                                 IntrangsSubtypDto intrangsSubtypDto,
                                 Double spanningsniva) throws JsonProcessingException
    {
        // Given
        var geom = createPoint(1., 2.);
        var feature = createFeature(geom, typeName, classId, status, operationS, remark);

        // When
        var spanning = converter.getSpanningsniva(feature);

        // Then
        assertEquals(spanningsniva, spanning);
    }


    private SimpleFeature
    createFeature(Geometry geom,
                  String typeName,
                  Integer classId,
                  Integer status,
                  String operationS,
                  String remark) {

        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        simpleFeatureTypeBuilder.setName(typeName);
        simpleFeatureTypeBuilder.add("the_geom", Geometry.class);
        simpleFeatureTypeBuilder.add("ClassId", Long.class);
        simpleFeatureTypeBuilder.add("Status", Long.class);
        simpleFeatureTypeBuilder.add("OperationS", String.class);
        simpleFeatureTypeBuilder.add("remark", String.class);

        var simpleFeature = simpleFeatureTypeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(simpleFeature);
        builder.set("the_geom", geom);
        builder.set("ClassId", classId);
        builder.set("Status", status);
        builder.set("OperationS", operationS);
        builder.set("remark", remark);

        return builder.buildFeature(null);
    }

    private static Stream<Arguments> dpComTestdata() {
        return Stream.of(
                Arguments.of("Lägeskarta%20Element", 11002, 0, "", null, IntrangsStatusDto.NY, IntrangstypDto.HOGSPANNINGSLEDNING, IntrangsSubtypDto.MARKLEDNING, 24.0),
                Arguments.of("LSP%20Element", 1147, 32, "I bruk", "", IntrangsStatusDto.NY, IntrangstypDto.LAGSPANNINGSLEDNING, IntrangsSubtypDto.LUFTLEDNING, .4),
                Arguments.of("LSP%20Element", 1147, 64, "I bruk", "", IntrangsStatusDto.BEVARAS, IntrangstypDto.LAGSPANNINGSLEDNING, IntrangsSubtypDto.LUFTLEDNING, .4),
                Arguments.of("LSP%20Element", 1147, 32, "Raseras", "", IntrangsStatusDto.BEVARAS, IntrangstypDto.LAGSPANNINGSLEDNING, IntrangsSubtypDto.LUFTLEDNING, .4),
                Arguments.of("HSP%20Element", 1001, 32, "I bruk", "", IntrangsStatusDto.NY, IntrangstypDto.HOGSPANNINGSLEDNING, IntrangsSubtypDto.LUFTLEDNING, 24.0)
        );
    }
}
