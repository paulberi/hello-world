package se.metria.markkoll.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.LedningsagareEntity;
import se.metria.markkoll.openapi.model.LedningsagareDto;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.LedningsagareRepository;
import se.metria.markkoll.service.LedningsagareService;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class LedningsagareServiceTest {
    LedningsagareService ledningsagareService;

    LedningsagareRepository mockLedningsagareRepository;
    KundRepository mockKundrepository;

    @BeforeEach
    void beforeEach() {
        mockLedningsagareRepository = mock(LedningsagareRepository.class);
        mockKundrepository = mock(KundRepository.class);

        ledningsagareService = new LedningsagareService(mockLedningsagareRepository,
            mockKundrepository);
    }

    @Test
    void så_ska_det_gå_att_hämta_ledningsägare_för_en_kund() {
        // Given
        var kundId = "kundId";
        var entities = Arrays.asList(
            ledningsagareEntity(mockUUID(0), "ägare 0"),
            ledningsagareEntity(mockUUID(1), "ägare 1")
        );
        var dtosExpect = Arrays.asList(
            new LedningsagareDto().id(mockUUID(0)).namn("ägare 0"),
            new LedningsagareDto().id(mockUUID(1)).namn("ägare 1")
        );

        when(mockLedningsagareRepository.findAllByKundId(eq(kundId))).thenReturn(entities);

        // When
        var dtosActual = ledningsagareService.getLedningsagare(kundId);

        // Then
        assertEquals(dtosExpect, dtosActual);
    }

    @Test
    void så_ska_det_gå_att_lägga_till_en_ny_ledningsägare() throws Exception{
        // Given
        var namn = "Ledningsägare";
        var kundId = "kundId";
        var kundEntity = new KundEntity();
        var expect = new LedningsagareDto().namn(namn);

        when(mockKundrepository.findById(eq(kundId))).thenReturn(Optional.of(kundEntity));

        // When
        var actual = ledningsagareService.addLedningsagare(namn, kundId);

        // Then
        assertEquals(expect, actual);
        verify(mockLedningsagareRepository).save(eq(new LedningsagareEntity(namn, kundEntity)));
    }

    @Test
    void så_ska_det_gå_att_ta_bort_en_ledningsägare() throws Exception {
        // Given
        var ledningsagareId = mockUUID(0);
        var kundId = "kundId";

        // When
        ledningsagareService.deleteLedningsagare(ledningsagareId, kundId);

        // Then
        verify(mockLedningsagareRepository).deleteByIdAndKundId(eq(ledningsagareId), eq(kundId));
    }

    private LedningsagareEntity ledningsagareEntity(UUID id, String namn) {
        var entity = new LedningsagareEntity();
        entity.setId(id);
        entity.setNamn(namn);
        return entity;
    }
}