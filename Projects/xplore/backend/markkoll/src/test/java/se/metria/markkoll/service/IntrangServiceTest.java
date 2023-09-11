package se.metria.markkoll.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.modelmapper.ModelMapper;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.IntrangstypDto;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.intrang.IntrangRepository;
import se.metria.markkoll.service.intrang.IntrangService;
import se.metria.markkoll.service.intrang.helper.ShapeFilerHelper;

import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Givet IntrangService")
@MarkkollServiceTest
public class IntrangServiceTest {
    IntrangService intrangService;
    IntrangRepository mockIntrangRepository;
    FastighetsintrangRepository mockFastighetsintrangRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ModelMapper modelMapper;

    final String basePath = "src/test/resources/testData/";

    @BeforeEach
    void before() {
        mockFastighetsintrangRepository = mock(FastighetsintrangRepository.class);
        mockIntrangRepository = mock(IntrangRepository.class);
        intrangService = new IntrangService(mockIntrangRepository, mockFastighetsintrangRepository);
    }

    @Test
    void så_ska_det_gå_att_läsa_rätt_antal_features_från_shapefil() {
        // Given
        String fileName = basePath + "oneco-skanova-owner.zip";
        var file = new FileSystemResource(fileName);
        // TODO: Beräkna expected på annat sätt, det här är taget från resultatet
        //       i MarkKoll (filter på OWNER, filter på intrångstyp)
        //       blev lite svårare att bara kolla i QGIS eftersom readFeaturesFromFile
        //       gör filtrering också, det borde flyttas ut ur readFeaturesFromFile
        int antalFeatures = 736;

        // When
        Set<SimpleFeature> features = ShapeFilerHelper.readFeaturesFromFile(file);

        // Then
        Assert.assertEquals(antalFeatures, features.size());
    }

    @Test
    void så_ska_exception_kastas_om_shapefil_saknas() {
        // Given
        String fileName = basePath + "saknar-shapefil.zip";
        var file = new FileSystemResource(fileName);

        // When, Then
        assertThrows(MarkkollException.class,
            () -> ShapeFilerHelper.readFeaturesFromFile(file));
    }

    @Test
    void så_ska_exception_kastas_vid_felaktigt_format_på_importfil() {
        // Given
        String fileName = basePath + "fel-format-importfil.txt";
        var file = new FileSystemResource(fileName);

        // When, Then
        assertThrows(MarkkollException.class,
                () -> ShapeFilerHelper.readFeaturesFromFile(file));
    }

    @Test
    void så_ska_features_ha_SRID_3006() {
        // Given
        String fileName = basePath + "oneco-skanova-owner.zip";
        var file = new FileSystemResource(fileName);
        var features = ShapeFilerHelper.readFeaturesFromFile(file);

        // When
        when(mockIntrangRepository.saveAndFlush(any())).then(AdditionalAnswers.returnsFirstArg());
        Set<IntrangEntity> importedIntrang = ShapeFilerHelper.intrangFromFeatures(features);

        // Then
        importedIntrang.forEach(
                feature -> Assert.assertEquals(3006,feature.getGeom().getSRID())
        );
    }

    @Test
    void så_ska_det_gå_att_importera_intrång_från_shapefil_1() {
        // Given
        String fileName = basePath + "oneco-skanova-owner.zip";
        var file = new FileSystemResource(fileName);
        var features = ShapeFilerHelper.readFeaturesFromFile(file);

        // Beräknat i QGIS
        int antalBrunnar = 0;
        int antalMarkskåp = 21;
        int antalStråk = 171;

        int antalIntrang = antalBrunnar + antalMarkskåp + antalStråk;

        // When
        Set<IntrangEntity> savedIntrangSet = ShapeFilerHelper.intrangFromFeatures(features);

        // Then
        assertEquals(antalIntrang, savedIntrangSet.size());
        assertEquals(antalBrunnar, filterIntrangByType(savedIntrangSet, IntrangstypDto.BRUNN).size());
        assertEquals(antalMarkskåp, filterIntrangByType(savedIntrangSet, IntrangstypDto.MARKSKAP).size());
        var importedStrak =filterIntrangByType(savedIntrangSet, IntrangstypDto.STRAK).size();

        assertEquals(antalStråk, importedStrak);
    }

    @Test
    void så_ska_det_gå_att_importera_intrång_från_shapefil_2() {
        // Given
        String fileName = basePath + "oneco-skanova-owner.zip";
        var file = new FileSystemResource(fileName);
        var features = ShapeFilerHelper.readFeaturesFromFile(file);

        // Beräknat i QGIS
        int antalBrunnar = 0;
        int antalMarkskåp = 21;
        int antalStråk = 171;

        int antalIntrang = antalBrunnar + antalMarkskåp + antalStråk;

        // When
        Set<IntrangEntity> savedIntrangSet = ShapeFilerHelper.intrangFromFeatures(features);

        // Then
        assertEquals(antalIntrang, savedIntrangSet.size());
        assertEquals(antalBrunnar, filterIntrangByType(savedIntrangSet, IntrangstypDto.BRUNN).size());
        assertEquals(antalMarkskåp, filterIntrangByType(savedIntrangSet, IntrangstypDto.MARKSKAP).size());
        var importedStrak =filterIntrangByType(savedIntrangSet, IntrangstypDto.STRAK).size();

        assertEquals(antalStråk, importedStrak);
    }

    private Set<IntrangEntity> filterIntrangByType(Set<IntrangEntity> intrangSet, IntrangstypDto intrangType) {
        return intrangSet.stream()
                .filter(e -> e.getType().startsWith(intrangType.toString()))
                .collect(Collectors.toSet());
    }
}
