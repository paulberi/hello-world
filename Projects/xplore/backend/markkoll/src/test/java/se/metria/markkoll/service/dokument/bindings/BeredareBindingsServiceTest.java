package se.metria.markkoll.service.dokument.bindings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.openapi.model.BeredareDto;
import se.metria.markkoll.service.BeredareService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class BeredareBindingsServiceTest {
    BeredareBindingsService beredareBindingsService;

    BeredareService mockBeredareService;

    @BeforeEach
    void beforeEach() {
        mockBeredareService = mock(BeredareService.class);

        beredareBindingsService = new BeredareBindingsService(mockBeredareService);
    }

    @Test
    void så_ska_beredarinformation_vara_korrekt() {
        // Given
        var projektId = mockUUID(0);

        var beredare = new BeredareDto()
            .adress("adress")
            .namn("namn")
            .ort("ort")
            .postnummer("postnummer")
            .telefonnummer("telefonnummer");

        when(mockBeredareService.get(eq(projektId))).thenReturn(beredare);

        // When
        var bindings = beredareBindingsService.getBeredareBindings(projektId);

        // Then
        assertEquals(beredare.getAdress(), bindings.getAdress());
        assertEquals(beredare.getNamn(), bindings.getNamn());
        assertEquals(beredare.getOrt(), bindings.getOrt());
        assertEquals(beredare.getPostnummer(), bindings.getPostnummer());
        assertEquals(beredare.getTelefonnummer(), bindings.getTelefonnummer());
    }
}