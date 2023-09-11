package se.metria.markkoll.service.intrang.converters.featurestointrangset;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.tuple.Pair;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.IntrangsStatusDto;
import se.metria.markkoll.openapi.model.IntrangsSubtypDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.ConstantAvtalstypEvaluator;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static se.metria.xplore.maputils.GeometryUtil.createPoint;

class DPPowerFeaturesToIntrangSetConverterTest {
    DPPowerFeaturesToIntrangSetConverter converter;

    @BeforeEach
    void beforeEach() {
        var evaluator = new ConstantAvtalstypEvaluator(AvtalstypDto.INTRANG);

        converter = new DPPowerFeaturesToIntrangSetConverter(evaluator);
    }

    @Test
    void ska_konvertera() throws JsonProcessingException {
        // Given
        var featurePair1 = createTestCase(0, 800003, createPoint(0., 0.), IntrangsStatusDto.RASERAS, IntrangstypDto.STRAK,
            IntrangsSubtypDto.NONE, null, "littera1 littera2", "SKANOVA");

        var features = Arrays.asList(
            featurePair1.getLeft(),
            createFeatureLittera(0, "littera1"),
            createFeatureLittera(0, "littera2")
        );

        // When
        var intrangSet = converter.convert(features);

        // Then
        assertThat(intrangSet, hasItem(featurePair1.getRight()));
    }

    private Pair<SimpleFeature, IntrangEntity>
    createTestCase(Integer dpOid,
                   Integer dpOtype,
                   Geometry geom,
                   IntrangsStatusDto intrangsStatusDto,
                   IntrangstypDto intrangstypDto,
                   IntrangsSubtypDto intrangsSubtypDto,
                   Double spanningsniva,
                   String littera,
                   String owner) throws JsonProcessingException
    {
        var intrang = intrangEntity(geom, dpOid, dpOtype, intrangsStatusDto, intrangstypDto, intrangsSubtypDto,
            spanningsniva, littera, owner);

        var featureIntrang = createFeatureIntrang(dpOid, intrang);

        return Pair.of(featureIntrang, intrang);
    }

    private SimpleFeature createFeatureIntrang(Integer dpOid, IntrangEntity intrangEntity) {
        return createFeatureIntrang(dpOid, intrangEntity.getGeom());
    }

    private SimpleFeature
    createFeature(Integer dpOid, Geometry geom, Integer dpCtype, Integer dpOtype, String text, String owner) {
        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        simpleFeatureTypeBuilder.setName("Vem bryr sig? Vi behöver bara någon godtycklig featuretype");
        simpleFeatureTypeBuilder.add("the_geom", Geometry.class);
        simpleFeatureTypeBuilder.add("dp_ctype", Long.class);
        simpleFeatureTypeBuilder.add("dp_otype", Long.class);
        simpleFeatureTypeBuilder.add("dp_oid", Long.class);
        simpleFeatureTypeBuilder.add("text", String.class);
        simpleFeatureTypeBuilder.add("OWNER", String.class);

        var simpleFeature = simpleFeatureTypeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(simpleFeature);
        builder.set("the_geom", geom);
        builder.set("dp_ctype", dpCtype);
        builder.set("dp_oid", dpOid);
        builder.set("dp_otype", dpOtype);
        builder.set("text", text);
        builder.set("OWNER", owner);

        return builder.buildFeature(null);
    }

    private SimpleFeature
    createFeatureLittera(Integer dpOid, String text) {
        return createFeature(dpOid, createPoint(0., 0.), 80000,800003, text, "SKANOVA");
    }

    private SimpleFeature
    createFeatureIntrang(Integer dpOid, Geometry geometry) {
        return createFeature(dpOid, geometry, 80010, 800003,null, "SKANOVA");
    }

    private IntrangEntity
    intrangEntity(Geometry geom,
                  Integer dpOid,
                  Integer dpOtype,
                  IntrangsStatusDto intrangsStatusDto,
                  IntrangstypDto intrangstypDto,
                  IntrangsSubtypDto intrangsSubtypDto,
                  Double spanningsniva,
                  String littera,
                  String owner) throws JsonProcessingException
    {
        return IntrangEntity.builder()
            .geom(geom)
            .littera(littera)
            .status(intrangsStatusDto.toString())
            .type(intrangstypDto.toString())
            .subtype(intrangsSubtypDto.toString())
            .spanningsniva(spanningsniva)
            .avtalstyp(AvtalstypDto.INTRANG)
            .build();
    }
}
