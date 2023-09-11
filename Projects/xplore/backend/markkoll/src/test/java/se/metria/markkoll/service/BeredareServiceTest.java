package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.entity.BeredareEntity;
import se.metria.markkoll.openapi.model.BeredareDto;
import se.metria.markkoll.repository.BeredareRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class BeredareServiceTest {
    BeredareService beredareService;

    BeredareRepository mockBeredareRepository;

    @BeforeEach
    void beforeEach() {
        mockBeredareRepository = mock(BeredareRepository.class);

        beredareService = new BeredareService(mockBeredareRepository);
    }

    @Test
    void så_ska_det_gå_att_hämta_beredarinformation() {
        // Given
        var projektId = mockUUID(0);
        var beredareEntity = beredareEntity();

        when(mockBeredareRepository.findByProjektId(eq(projektId))).thenReturn(beredareEntity);

        // When
        var beredareActual = beredareService.get(projektId);

        // Then
        var beredareExpect = beredareDto();

        assertEquals(beredareExpect, beredareActual);
    }

    @Test
    void så_ska_det_gå_att_redigera_beredarinformation() {
        // Given
        var projektId = mockUUID(0);
        var beredareDto = beredareDto();

        when(mockBeredareRepository.findByProjektId(eq(projektId))).thenReturn(new BeredareEntity());

        // When
        beredareService.edit(beredareDto, projektId);

        // Then
        var entity = beredareEntity();

        verify(mockBeredareRepository).save(eq(entity));
    }

    private BeredareDto beredareDto() {
        return new BeredareDto()
            .adress("adress")
            .namn("namn")
            .ort("ort")
            .postnummer("postnummer")
            .telefonnummer("telefonnummer");
    }

    private BeredareEntity beredareEntity() {
        var entity = new BeredareEntity();
        entity.setAdress("adress");
        entity.setNamn("namn");
        entity.setOrt("ort");
        entity.setPostnummer("postnummer");
        entity.setTelefonnummer("telefonnummer");

        return entity;
    }
}