package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.logging.avtalslogg.LogAvtalsstatusEntity;
import se.metria.markkoll.entity.logging.avtalslogg.LogGeometristatusEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.logging.avtalslogg.LogAvtalsstatusRepository;
import se.metria.markkoll.repository.logging.avtalslogg.LogGeometristatusRepository;
import se.metria.markkoll.repository.logging.avtalslogg.LogUtbetalningsdatumRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;
import se.metria.markkoll.service.logging.AvtalsloggService;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollServiceTest
@DisplayName("Givet AvtalsloggService")
public class AvtalsloggServiceTest {
    AvtalsloggService avtalsloggService;
    LogAvtalsstatusRepository mockLogAvtalsstatusRepository;
    LogGeometristatusRepository mockLogGeometristatusRepository;
    LogUtbetalningsdatumRepository mockLogUtbetalningsdatumRepository;
    AvtalRepository mockAvtalRepository;
    AvtalspartRepository mockAvtalspartRepository;

    @BeforeEach
    void before() {
        mockLogAvtalsstatusRepository = mock(LogAvtalsstatusRepository.class);
        mockLogGeometristatusRepository = mock(LogGeometristatusRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockAvtalspartRepository = mock(AvtalspartRepository.class);
        mockLogUtbetalningsdatumRepository = mock(LogUtbetalningsdatumRepository.class);

        avtalsloggService = new AvtalsloggService(
                mockLogAvtalsstatusRepository,
                mockLogGeometristatusRepository,
                mockLogUtbetalningsdatumRepository,
                mockAvtalRepository,
                mockAvtalspartRepository
        );
    }

    @Test
    void så_ska_avtalsstatus_loggas() {
        // Given
        var avtalsstatus = AvtalsstatusDto.AVTAL_SKICKAT;
        var avtalspart = mock(AvtalspartEntity.class);
        var avtalspartId = mock(UUID.class);

        when(mockAvtalspartRepository.getReferenceById(any())).thenReturn(avtalspart);

        // When
        avtalsloggService.logAvtalsstatus(avtalspartId, avtalsstatus);

        // Then
        var log = new LogAvtalsstatusEntity(avtalspart, avtalsstatus);
        verify(mockLogAvtalsstatusRepository).save(eq(log));
    }

    @Test
    void så_ska_geometristatus_loggas() {
        // Given
        var avtalId = mockUUID(0);
        var geometristatus = GeometristatusDto.BORTTAGEN;
        var avtal = mock(AvtalEntity.class);

        when(mockAvtalRepository.getReferenceById(eq(avtalId))).thenReturn(avtal);

        // When
        avtalsloggService.logGeometristatus(avtalId, geometristatus);

        // Then
        var log = new LogGeometristatusEntity(avtal, geometristatus);
        verify(mockLogGeometristatusRepository).save(eq(log));
    }
}
