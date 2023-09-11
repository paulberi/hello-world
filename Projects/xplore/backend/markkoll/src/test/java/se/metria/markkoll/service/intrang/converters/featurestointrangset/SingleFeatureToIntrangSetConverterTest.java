package se.metria.markkoll.service.intrang.converters.featurestointrangset;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.service.intrang.converters.featuretointrang.FeatureToIntrangConverter;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.xplore.maputils.GeometryUtil.createPoint;

class SingleFeatureToIntrangSetConverterTest {

    SingleFeatureToIntrangSetConverter singleFeatureToIntrangSetConverter;
    FeatureToIntrangConverter mockFeatureToIntrangConverter;
    IndataTypDto indataTypDto = IndataTypDto.DPCOM;

    @BeforeEach
    void beforeEach() {
        mockFeatureToIntrangConverter = mock(FeatureToIntrangConverter.class);

        singleFeatureToIntrangSetConverter = new SingleFeatureToIntrangSetConverter(mockFeatureToIntrangConverter,
            indataTypDto);
    }

    @Test
    void konvertera() throws JsonProcessingException {
        // Given
        var geom = createPoint(1., 1.);
        var intrang = intrangEntity(IntrangstypDto.KABELSKAP, IntrangsSubtypDto.NONE, IntrangsStatusDto.NY,
            1.2, geom, AvtalstypDto.INTRANG);
        var feature = createFeature(geom);


        setupConverterMock(feature, intrang);

        // When
        var intrangOut = singleFeatureToIntrangSetConverter.convert(Arrays.asList(feature));

        // Then
        assertEquals(1, intrangOut.size());
        assertThat(intrangOut, hasItem(intrang));
    }

    private IntrangEntity
    intrangEntity(IntrangstypDto intrangstypDto,
                  IntrangsSubtypDto intrangsSubtypDto,
                  IntrangsStatusDto intrangStatus,
                  Double spanningsniva,
                  Geometry geom,
                  AvtalstypDto avtalstyp) throws JsonProcessingException
    {
        return IntrangEntity.builder()
            .type(intrangstypDto.toString())
            .subtype(intrangsSubtypDto.toString())
            .status(intrangStatus.toString())
            .spanningsniva(spanningsniva)
            .geom(geom)
            .avtalstyp(avtalstyp)
            .build();
    }

    private SimpleFeature
    createFeature(Geometry geom) {
        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        simpleFeatureTypeBuilder.setName("Vem bryr sig? Vi behöver bara någon godtycklig featuretype");
        simpleFeatureTypeBuilder.add("the_geom", Geometry.class);

        var simpleFeature = simpleFeatureTypeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(simpleFeature);
        builder.set("the_geom", geom);

        return builder.buildFeature(null);
    }

    private JsonNode
    featureProperties(Geometry geom)
        throws JsonProcessingException
    {
        var objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);

        return objectMapper.readTree("{\"the_geom\":\"" + geom.toString() + "\"}");
    }

    private void setupConverterMock(SimpleFeature feature, IntrangEntity intrangEntity) {
        when(mockFeatureToIntrangConverter.getIntrangsTyp(eq(feature)))
            .thenReturn(IntrangstypDto.valueOf(intrangEntity.getType()));
        when(mockFeatureToIntrangConverter.getIntrangsSubtyp(eq(feature)))
            .thenReturn(IntrangsSubtypDto.valueOf(intrangEntity.getSubtype()));
        when(mockFeatureToIntrangConverter.getIntrangStatus(eq(feature)))
            .thenReturn(IntrangsStatusDto.valueOf(intrangEntity.getStatus()));
        when(mockFeatureToIntrangConverter.getSpanningsniva(eq(feature)))
            .thenReturn(intrangEntity.getSpanningsniva());
        when(mockFeatureToIntrangConverter.getAvtalstyp(eq(feature)))
            .thenReturn(intrangEntity.getAvtalstyp());
    }
}
