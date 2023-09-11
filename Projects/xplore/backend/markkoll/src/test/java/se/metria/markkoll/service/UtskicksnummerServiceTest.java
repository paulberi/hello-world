package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.entity.UtskicksnummerEntity;
import se.metria.markkoll.repository.UtskicksnummerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UtskicksnummerServiceTest {

    UtskicksnummerService utskicksnummerService;

    UtskicksnummerRepository mockUtskicksnummerRepository;

    @BeforeEach
    void setUp() {
        mockUtskicksnummerRepository = mock(UtskicksnummerRepository.class);

        utskicksnummerService = new UtskicksnummerService(mockUtskicksnummerRepository);
    }

    @Test
    void så_ska_det_gå_att_skapa_ett_utskicksnummerserie() {
        // Given
        var kundId = "kundId";
        var entity = UtskicksnummerEntity.builder()
            .kundId(kundId)
            .build();

        when(mockUtskicksnummerRepository.existsById(eq(kundId))).thenReturn(false);

        // When
        utskicksnummerService.create(kundId);

        // Then
        verify(mockUtskicksnummerRepository).save(eq(entity));
    }

    @Test
    void så_ska_det_gå_att_hämta_ett_utskicksnummer() {
        // Given
        var kundId = "kundId";
        var utskicksnummerExpect = 10;
        var entity = UtskicksnummerEntity.builder()
            .utskicksnummer(utskicksnummerExpect)
            .build();

        when(mockUtskicksnummerRepository.findById(eq(kundId))).thenReturn(Optional.of(entity));

        // When
        var utskicksnummerActual = utskicksnummerService.get(kundId);

        // Then
        assertEquals(utskicksnummerExpect, utskicksnummerActual);
    }

    @Test
    void så_ska_det_gå_att_inkrementera_ett_utskicksnummer() {
        // Given
        var kundId = "kundId";
        var utskicksnummerExpect = 10;
        var entity = UtskicksnummerEntity.builder()
            .utskicksnummer(utskicksnummerExpect)
            .build();

        when(mockUtskicksnummerRepository.findById(eq(kundId))).thenReturn(Optional.of(entity));

        // When
        utskicksnummerService.increment(kundId);

        // Then
        assertEquals(utskicksnummerExpect + 1, entity.getUtskicksnummer());
    }
}