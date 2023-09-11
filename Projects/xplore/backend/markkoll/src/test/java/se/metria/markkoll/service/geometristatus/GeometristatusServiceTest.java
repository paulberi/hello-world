package se.metria.markkoll.service.geometristatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.geometristatus.GeometristatusAvtalId;
import se.metria.markkoll.repository.geometristatus.GeometristatusRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class GeometristatusServiceTest {
    @InjectMocks
    GeometristatusService geometristatusService;

    @Mock
    AvtalRepository avtalRepository;

    @Mock
    GeometristatusRepository geometristatusRepository;

    @Mock
    VersionRepository versionRepository;

    @Mock
    ApplicationEventPublisher publisher;

    @Test
    void setGeometristatus() {
        // Given
        var projektId = mockUUID(0);
        var avtalIds = Arrays.asList(mockUUID(1));
        var geometristatus = GeometristatusDto.BORTTAGEN;
        var versionId = mockUUID(2);
        List<GeometristatusAvtalId> geometristatusAvtalId = Arrays.asList(
            new GeometristatusAvtalIdImpl(mockUUID(3), GeometristatusDto.OFORANDRAD),
            new GeometristatusAvtalIdImpl(mockUUID(4), GeometristatusDto.BORTTAGEN)
        );

        when(versionRepository.getCurrentVersionId(eq(projektId))).thenReturn(versionId);
        when(geometristatusRepository.findByVersionIdAndAvtalIdIn(eq(versionId), eq(avtalIds)))
            .thenReturn(geometristatusAvtalId);

        // When
        geometristatusService.setGeometristatus(projektId, avtalIds, geometristatus);

        // Then
        verify(geometristatusRepository).updateGeometristatus(eq(versionId), eq(Arrays.asList(mockUUID(3))),
            eq(GeometristatusDto.BORTTAGEN));
        verify(publisher).publishEvent(any(GeometristatusEvent.class));
    }

    @Data
    @AllArgsConstructor
    class GeometristatusAvtalIdImpl implements GeometristatusAvtalId {
        private UUID avtalId;
        private GeometristatusDto geometristatus;
    }
}