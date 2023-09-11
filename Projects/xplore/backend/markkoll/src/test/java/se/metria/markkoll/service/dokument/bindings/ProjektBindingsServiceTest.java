package se.metria.markkoll.service.dokument.bindings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ByteArrayResource;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.service.projekt.ElnatProjektService;
import se.metria.markkoll.service.projekt.FiberProjektService;
import se.metria.markkoll.service.dokument.bindings.data.ProjektBindings;
import se.metria.markkoll.service.map.MapService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class ProjektBindingsServiceTest {

    ProjektBindingsService projektBindingsService;

    ElnatProjektService mockElnatProjektService;
    FiberProjektService mockFiberProjektService;
    MapService mockMapService;

    @BeforeEach
    void beforeEach() {
        mockElnatProjektService = mock(ElnatProjektService.class);
        mockFiberProjektService = mock(FiberProjektService.class);
        mockMapService = mock(MapService.class);

        projektBindingsService = new ProjektBindingsService(mockElnatProjektService, mockFiberProjektService, mockMapService);
    }

    @Test
    void så_ska_fiberprojektinformation_vara_korrekt() throws IOException {
        // Given
        var projektId = mockUUID(0);
        var projekt = new FiberProjektDto()
            .projektInfo(
                new ProjektInfoDto()
                    .id(projektId)
                    .namn("Projektnamn")
                    .startDatum(LocalDateTime.of(1986, 3, 4, 5, 6))
                    .uppdragsnummer("Uppdragsnummer")
                    .utbetalningskonto("utbetalningskonto")
                    .projektnummer("projektnummer")
                    .ansvarigKonstruktor("Ansvarig konstruktör")
                    .ansvarigProjektledare("Ansvarig projektledare")
                    .beskrivning("Beskrivning")
            )
            .fiberInfo(
                new FiberInfoDto()
                    .ledningsagare("Ledningsägare")
                    .ledningsstracka("Ledningssträcka")
            );
        var kartaMedProjektomrade = new ByteArrayResource("kartaMedProjektomrade".getBytes());
        var kartaMedStrackning = new ByteArrayResource("kartaMedStrackning".getBytes());

        when(mockFiberProjektService.getProjektDto(eq(projektId))).thenReturn(Optional.of(projekt));
        when(mockMapService.getProjektkarta(eq(projektId), eq(true))).thenReturn(kartaMedProjektomrade);
        when(mockMapService.getProjektkarta(eq(projektId), eq(false))).thenReturn(kartaMedStrackning);

        // When
        var bindings = projektBindingsService.getFiberProjektProperties(projektId);

        // Then
        assertGeneralProjektBindings(projekt.getProjektInfo(), List.of(kartaMedProjektomrade, kartaMedStrackning), bindings);
        assertEquals(projekt.getFiberInfo().getLedningsagare(), bindings.getLedningsagare());
        assertEquals(projekt.getFiberInfo().getLedningsstracka(), bindings.getLedningsstracka());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void så_ska_bidragsprojekt_sättas_korrekt_för_fiberprojekt(Boolean isBidragsprojekt) throws IOException {
        // Given
        var projektId = mockUUID(0);
        var projekt = new FiberProjektDto().fiberInfo(new FiberInfoDto().bidragsprojekt(isBidragsprojekt));

        when(mockFiberProjektService.getProjektDto(eq(projektId))).thenReturn(Optional.of(projekt));

        // When
        var bindings = projektBindingsService.getFiberProjektProperties(projektId);

        // Then
        assertEquals(isBidragsprojekt, bindings.getBidragsprojekt());
        assertEquals(!isBidragsprojekt, bindings.getErsattningSkaErlaggas());
        assertEquals(isBidragsprojekt, bindings.getErsattningSkaInteUtga());
        assertEquals(!isBidragsprojekt, bindings.getSeBilagaA());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void så_ska_värderingsprotokoll_bifogas_sättas_korrekt_för_fiberprojekt(Boolean vp) throws IOException {
        // Given
        var projektId = mockUUID(0);
        var projekt = new FiberProjektDto().fiberInfo(new FiberInfoDto().varderingsprotokoll(vp));

        when(mockFiberProjektService.getProjektDto(eq(projektId))).thenReturn(Optional.of(projekt));

        // When
        var bindings = projektBindingsService.getFiberProjektProperties(projektId);

        // Then
        assertEquals(vp, bindings.getVarderingsprotokollBifogas());
    }

    @Test
    void så_ska_elnätsinformation_vara_korrekt() throws IOException {
        // Given
        var projektId = mockUUID(0);
        var projekt = new ElnatProjektDto()
            .projektInfo(
                new ProjektInfoDto()
                    .id(projektId)
                    .namn("Projektnamn")
                    .startDatum(LocalDateTime.of(1986, 3, 4, 5, 6))
                    .uppdragsnummer("Uppdragsnummer")
                    .utbetalningskonto("utbetalningskonto")
                    .projektnummer("projektnummer")
                    .ansvarigKonstruktor("Ansvarig konstruktör")
                    .ansvarigProjektledare("Ansvarig projektledare")
                    .beskrivning("Beskrivning")
            )
            .elnatInfo(
                new ElnatInfoDto()
                    .ledningsagare("Ledningsägare")
                    .ledningsstracka("Ledningssträcka")
            );
        var kartaMedProjektomrade = new ByteArrayResource("kartaMedProjektomrade".getBytes());
        var kartaMedStrackning = new ByteArrayResource("kartaMedStrackning".getBytes());

        when(mockElnatProjektService.getProjektDto(eq(projektId))).thenReturn(Optional.of(projekt));
        when(mockMapService.getProjektkarta(eq(projektId), eq(true))).thenReturn(kartaMedProjektomrade);
        when(mockMapService.getProjektkarta(eq(projektId), eq(false))).thenReturn(kartaMedStrackning);

        // When
        var bindings = projektBindingsService.getElnatProjektProperties(projektId);

        // Then
        assertGeneralProjektBindings(projekt.getProjektInfo(), List.of(kartaMedProjektomrade, kartaMedStrackning), bindings);
        assertEquals(projekt.getElnatInfo().getLedningsagare(), bindings.getLedningsagare());
        assertEquals(projekt.getElnatInfo().getLedningsstracka(), bindings.getLedningsstracka());
        assertEquals(true, bindings.getVarderingsprotokollBifogas());

    }

    private void assertGeneralProjektBindings(ProjektInfoDto projekt, List<ByteArrayResource> expectedMaps, ProjektBindings bindings) {
        assertEquals(projekt.getNamn(), bindings.getProjektnamn());
        assertEquals(projekt.getStartDatum().toLocalDate(), bindings.getStartdatum());
        assertEquals(projekt.getUppdragsnummer(), bindings.getUppdragsnummer());
        assertEquals(Arrays.asList(expectedMaps.get(0)), bindings.getProjektkartaMedProjektomrade());
        assertEquals(Arrays.asList(expectedMaps.get(1)), bindings.getProjektkartaMedStrackning());
        assertEquals(projekt.getUtbetalningskonto(), bindings.getUtbetalningskonto());
        assertEquals(projekt.getProjektnummer(), bindings.getProjektnummer());
        assertEquals(projekt.getAnsvarigKonstruktor(), bindings.getAnsvarigKontruktor());
        assertEquals(projekt.getAnsvarigProjektledare(), bindings.getAnsvarigProjektledare());
        assertEquals(projekt.getBeskrivning(), bindings.getBeskrivning());
    }
}
