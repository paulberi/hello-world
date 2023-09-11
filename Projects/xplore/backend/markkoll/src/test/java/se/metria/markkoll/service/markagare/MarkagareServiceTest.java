package se.metria.markkoll.service.markagare;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.repository.avtal.AvtalIdStatusUtbetalningsdatum;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;
import se.metria.markkoll.repository.markagare.MarkagareRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.MarkagareImportService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.logging.AvtalsloggService;
import se.metria.markkoll.service.logging.LoggService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class MarkagareServiceTest {
    @InjectMocks
    MarkagareService markagareService;

    @Mock
    AclService aclService;

    @Mock
    AvtalRepository avtalRepository;

    @Mock
    AvtalspartRepository avtalspartRepository;

    @Mock
    MarkagareRepository markagareRepository;

    @Mock
    FastighetRepository fastighetRepository;

    @Mock
    ProjektRepository projektRepository;

    @Mock
    LoggService loggService;

    @Mock
    ModelMapper modelMapper;

    @Mock
    MarkagareImportService markagareImportService;

    @Mock
    AvtalsloggService avtalsloggService;

    @Mock
    ApplicationEventPublisher publisher;

    @Test
    void updateAvtalsstatus() {
        // Given
        var avtalIds = Arrays.asList(mockUUID(0));
        var avtalsstatus = AvtalsstatusDto.EJ_BEHANDLAT;
        var avtalspartIds = Arrays.asList(mockUUID(1));
        List<AvtalIdStatusUtbetalningsdatum> query = Arrays.asList(
            new AvtalIdStatusUtbetalningsdatumImpl(
                avtalspartIds.get(0), AvtalsstatusDto.AVTAL_JUSTERAS, LocalDate.of(1986, 3, 4))
        );
        when(avtalspartRepository.findByAvtalIdIn(eq(avtalIds))).thenReturn(query);

        // When
        markagareService.updateAvtalsstatus(avtalIds, avtalsstatus);

        // Then
        verify(avtalspartRepository).setAvtalsstatus(eq(avtalspartIds), eq(avtalsstatus));
        verify(avtalRepository).refreshAvtalsstatus(eq(avtalIds), eq(avtalsstatus.toString()));

        verify(avtalspartRepository, never()).setUtbetalningsdatum(any(), any());
        verify(publisher, never()).publishEvent(any());
    }

    @Test
    void updateAvtalsstatusUtbetalning() {
        // Given
        var avtalIds = Arrays.asList(mockUUID(0));
        var avtalsstatus = AvtalsstatusDto.ERSATTNING_UTBETALD;
        var avtalspartIds = Arrays.asList(mockUUID(1));
        List<AvtalIdStatusUtbetalningsdatum> query = Arrays.asList(
            new AvtalIdStatusUtbetalningsdatumImpl(
                avtalspartIds.get(0), AvtalsstatusDto.AVTAL_JUSTERAS, LocalDate.of(1986, 3, 4)
            )
        );
        when(avtalspartRepository.findByAvtalIdIn(eq(avtalIds))).thenReturn(query);

        // When
        markagareService.updateAvtalsstatus(avtalIds, avtalsstatus);

        // Then
        verify(avtalspartRepository).setAvtalsstatus(eq(avtalspartIds), eq(avtalsstatus));
        verify(avtalRepository).refreshAvtalsstatus(eq(avtalIds), eq(avtalsstatus.toString()));

        verify(avtalspartRepository).setUtbetalningsdatum(eq(avtalspartIds), any());
        verify(publisher).publishEvent(any(AvtalsstatusUpdatedEvent.class));
        verify(publisher).publishEvent(any(UtbetalningEvent.class));
    }

    @Data
    @AllArgsConstructor
    class AvtalIdStatusUtbetalningsdatumImpl implements AvtalIdStatusUtbetalningsdatum {
        private UUID id;
        private AvtalsstatusDto avtalsstatus;
        private LocalDate utbetalningsdatum;
    }
}