package se.metria.markkoll.service.intrang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.IntrangsSubtypDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;
import se.metria.markkoll.openapi.model.ProjektIntrangDto;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.intrang.IntrangRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.xplore.maputils.GeometryUtil.getPointGeometry;

class IntrangServiceTest {
    IntrangService intrangService;

    IntrangRepository mockIntrangRepository;
    FastighetsintrangRepository mockFastighetsintrangRepository;

    @BeforeEach
    void beforeEach() {
        mockIntrangRepository = mock(IntrangRepository.class);
        mockFastighetsintrangRepository = mock(FastighetsintrangRepository.class);

        intrangService = new IntrangService(mockIntrangRepository, mockFastighetsintrangRepository);
    }

    @Test
    void så_ska_det_gå_att_hämta_intrång_som_tillhör_ett_projekt() {
        // Given
        var projektId = mockUUID(0);

        var entity = intrangEntity();

        when(mockIntrangRepository.findAllByProjektId(eq(projektId)))
            .thenReturn(Optional.of(Arrays.asList(entity)));

        // When
        var intrang = intrangService.getIntrang(projektId);

        // Then
        assertEquals(Arrays.asList(intrangDto()), intrang);
    }

    public IntrangEntity intrangEntity() {
        var entity = new IntrangEntity();
        entity.setAvtalstyp(AvtalstypDto.SERVIS);
        entity.setType(IntrangstypDto.BRUNN.toString());
        entity.setSubtype(IntrangsSubtypDto.LUFTLEDNING.toString());
        entity.setGeom(getPointGeometry(3, 4));

        return entity;
    }

    public ProjektIntrangDto intrangDto() {
        return new ProjektIntrangDto()
            .avtalstyp(AvtalstypDto.SERVIS)
            .type(IntrangstypDto.BRUNN)
            .subtype(IntrangsSubtypDto.LUFTLEDNING)
            .geom("{\"type\":\"Point\",\"coordinates\":[3,4],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:3006\"}}}");
    }
}