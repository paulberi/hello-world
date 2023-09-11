package se.metria.markkoll.service.dokument.bindings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.utskick.UtskickDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class UtskickBindingsServiceTest {

    UtskickBindingsService utskickBindingsService;

    MarkagareService mockMarkagareService;

    @BeforeEach
    void setUp() {
        mockMarkagareService = mock(MarkagareService.class);

        utskickBindingsService = new UtskickBindingsService(mockMarkagareService);
    }

    @Test
    void så_ska_löpnummer_vara_korrekt() {
        // Given
        var lopnummer = "10pnumm3r";
        var utskick = UtskickDto.builder()
            .lopnummer(lopnummer)
            .build();

        // When
        var bindings = utskickBindingsService.getUtskickBindings(utskick);

        // Then
        assertEquals(lopnummer, bindings.getLopnummer());
    }

    @Test
    void så_ska_information_för_en_kontaktperson_i_utskicket_vara_korrekt() {
        // Given
        var avtalspartId = mockUUID(0);

        var mottagare = Optional.of(
            new MarkagareDto().namn("Signatär Kontaktperson").kontaktperson(true).bankkonto("Bankkonto").adress("Adress 1")
                .postort("Ort").postnummer("12345").coAdress("CO-adress").id(avtalspartId)
        );

        var utskick = UtskickDto.builder()
            .kontaktperson(mottagare)
            .build();

        when(mockMarkagareService.getPersonnummer(eq(avtalspartId))).thenReturn("19911221");

        // When
        var bindings = utskickBindingsService.getUtskickBindings(utskick);

        // Then
        assertEquals("Adress 1", bindings.getMottagareAdress());
        assertEquals("Bankkonto", bindings.getMottagareBankkonto());
        assertEquals("Signatär Kontaktperson", bindings.getMottagareNamn());
        assertEquals("19911221", bindings.getMottagareOrganisationsnummer());
        assertEquals("12345", bindings.getMottagarePostnummer());
        assertEquals("Ort", bindings.getMottagarePostort());
        assertEquals("CO-adress", bindings.getMottagareCoAdress());
    }

    @Test
    void så_ska_information_vara_korrekt_om_kontaktperson_saknas() {
        // Given
        var utskick = UtskickDto.builder()
            .kontaktperson(Optional.empty())
            .build();

        // When
        var bindings = utskickBindingsService.getUtskickBindings(utskick);

        // Then
        assertEquals("", bindings.getMottagareAdress());
        assertEquals("", bindings.getMottagareBankkonto());
        assertEquals("", bindings.getMottagareNamn());
        assertEquals("", bindings.getMottagareOrganisationsnummer());
        assertEquals("", bindings.getMottagarePostnummer());
        assertEquals("", bindings.getMottagarePostort());
        assertEquals("", bindings.getMottagareCoAdress());
    }

    @Test
    void så_ska_signatärerna_vara_i_rätt_ordningen_om_utskicket_har_en_kontaktperson_som_ingår_bland_signatärerna() {
        // Given
        var signatarer = Arrays.asList(
            new MarkagareDto().namn("Signatär 2").kontaktperson(false),
            new MarkagareDto().namn("Signatär Kontaktperson").kontaktperson(true).bankkonto("Bankkonto").adress("Adress 1")
                .fodelsedatumEllerOrgnummer("19911221").postort("Ort").postnummer("12345").id(mockUUID(0)),
            new MarkagareDto().namn("Signatär 1").kontaktperson(false)
        );

        var utskick = UtskickDto.builder()
            .signatarer(signatarer)
            .kontaktperson(Optional.of(signatarer.get(1)))
            .build();

        // When
        var bindings = utskickBindingsService.getUtskickBindings(utskick);

        // Then
        assertEquals(Arrays.asList("Signatär Kontaktperson", "Signatär 1", "Signatär 2"), bindings.getSignatarerNamn());
    }

    @Test
    void så_ska_signatärerna_vara_i_rätt_ordning_om_utskicket_saknar_en_kontaktperson() {
        // Given
        final List<MarkagareDto> signatarer = Arrays.asList(
            new MarkagareDto().namn("Signatär 2").kontaktperson(false),
            new MarkagareDto().namn("Signatär Inte Kontaktperson").kontaktperson(false).bankkonto("Bankkonto").adress("Adress 1")
                .fodelsedatumEllerOrgnummer("19911221").postort("Ort").postnummer("12345"),
            new MarkagareDto().namn("Signatär 1").kontaktperson(false)
        );

        var utskick = UtskickDto.builder()
            .signatarer(signatarer)
            .kontaktperson(Optional.empty())
            .build();

        // When
        var bindings = utskickBindingsService.getUtskickBindings(utskick);

        assertEquals(Arrays.asList("Signatär 1", "Signatär 2", "Signatär Inte Kontaktperson"),
            bindings.getSignatarerNamn());
    }
}