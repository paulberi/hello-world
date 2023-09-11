package se.metria.markkoll.service.indata;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.entity.indata.IndataEntity;
import se.metria.markkoll.entity.indata.KallfilEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.IndataTypDto;
import se.metria.markkoll.repository.indata.IndataRepository;
import se.metria.markkoll.repository.indata.KallfilRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.xplore.maputils.GeometryUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class IndataServiceTest {
    @InjectMocks
    IndataService indataService;

    @Mock
    IndataRepository indataRepository;

    @Mock
    KallfilRepository kallfilRepository;

    @Mock
    ProjektRepository projektRepository;

    @Test
    void så_ska_det_gå_att_importera_indatan_för_ett_projekt() {
        // Given
        var projektId = mockUUID(0);
        var kallfilId = mockUUID(1);
        var featureMap = new HashMap<String, Collection<SimpleFeature>>();
        featureMap.put("layer1", Arrays.asList(createFeature(GeometryUtil.createPoint(1., 2.))));
        featureMap.put("layer2", Arrays.asList(createFeature(GeometryUtil.createPoint(3., 4.))));

        var indata = new Indata(featureMap, IndataTypDto.DPCOM);

        var projekt = new ProjektEntity();
        var kallfil = new KallfilEntity();

        when(projektRepository.findById(eq(projektId))).thenReturn(Optional.of(projekt));
        when(kallfilRepository.findById(eq(kallfilId))).thenReturn(Optional.of(kallfil));

        // When
        var ids = indataService.update(projektId, kallfilId, indata);

        // Then
        verify(indataRepository).deleteByProjektId(eq(projektId));
        for (var entry: featureMap.entrySet()) {
            var features = entry.getValue();
            var layer = entry.getKey();

            var entities = features.stream()
                .map(f -> getIndataEntity(f, layer, projekt, kallfil))
                .collect(Collectors.toList());

            verify(indataRepository).saveAll(eq(entities));
        }
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

    private IndataEntity
    getIndataEntity(SimpleFeature feature, String layer, ProjektEntity projektEntity, KallfilEntity kallfilEntity) {

        var entity = new IndataEntity();
        entity.setGeom((Geometry)feature.getDefaultGeometryProperty().getValue());
        entity.setFeatureProperties(GeometryUtil.getPropertiesAsJson(feature));
        entity.setLayer(layer);
        entity.setProjekt(projektEntity);
        entity.setKallfil(kallfilEntity);

        return entity;
    }
}