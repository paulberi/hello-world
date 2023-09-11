package se.metria.markkoll.service.dokument.bindings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.service.markagare.MarkagareService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class MarkagareBindingsServiceTest {
    MarkagareBindingsService markagareBindingsService;

    MarkagareService mockMarkagareService;

    @BeforeEach
    public void beforeEach() {
        mockMarkagareService = mock(MarkagareService.class);

        markagareBindingsService = new MarkagareBindingsService(mockMarkagareService);
    }

    @Test
    void så_ska_markägaruppgifter_med_kontaktperson_vara_korrekta() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        when(mockMarkagareService.getAgareForFastighet(eq(projektId), eq(fastighetId))).thenReturn(Arrays.asList(
            new MarkagareDto().kontaktperson(false).namn("Fastighetsägare").inkluderaIAvtal(true).adress("Adress 2")
                .postnummer("Saknar postort").andel("1/3"),
            new MarkagareDto().kontaktperson(true).namn("Huvudägare").inkluderaIAvtal(true).adress("Adress 1")
                .postnummer("12345").postort("Ort").bankkonto("Bankkonto").fodelsedatumEllerOrgnummer("19911221")
                .andel("1/2"),
            new MarkagareDto().kontaktperson(false).namn("En Annan Ägare").inkluderaIAvtal(true).adress("Adress 3")
                .postnummer("0").postort("Ja").fodelsedatumEllerOrgnummer("23").andel("1/4"),
            new MarkagareDto().kontaktperson(false).namn("Inte inkluderad").inkluderaIAvtal(false).adress("Adress 1")
                .postnummer("12345").postort("Ort").andel("1/5")
        ));

        var samtligaFastighetsagare = Arrays.asList(
            "Huvudägare, Adress 1, 12345, Ort",
            "En Annan Ägare, Adress 3, 0, Ja",
            "Fastighetsägare, Adress 2, Saknar postort, null"
        );

        var markagareNamn = Arrays.asList(
            "Huvudägare",
            "En Annan Ägare",
            "Fastighetsägare",
            "Inte inkluderad"
        );

        var markagareNamnAdress = Arrays.asList(
            "Huvudägare, Adress 1 12345 Ort",
            "En Annan Ägare, Adress 3 0 Ja",
            "Fastighetsägare, Adress 2 Saknar postort null",
            "Inte inkluderad, Adress 1 12345 Ort"
        );

        var markagareNamnAdressAndel = Arrays.asList(
            "Huvudägare, Adress 1 12345 Ort, Andel 1/2",
            "En Annan Ägare, Adress 3 0 Ja, Andel 1/4",
            "Fastighetsägare, Adress 2 Saknar postort null, Andel 1/3",
            "Inte inkluderad, Adress 1 12345 Ort, Andel 1/5"
        );

        var markagareNamnAndel = Arrays.asList(
                "Huvudägare, Andel 1/2",
                "En Annan Ägare, Andel 1/4",
                "Fastighetsägare, Andel 1/3",
                "Inte inkluderad, Andel 1/5"
        );

        // When
        var bindings = markagareBindingsService.getMarkagareBindings(projektId, fastighetId);

        // Then
        assertEquals(samtligaFastighetsagare, bindings.getSamtligaFastighetsagare());
        assertEquals(markagareNamn, bindings.getMarkagareNamn());
        assertEquals(markagareNamnAdress, bindings.getMarkagareNamnAdress());
        assertEquals(markagareNamnAdressAndel, bindings.getMarkagareNamnAdressAndel());
        assertEquals(markagareNamnAndel, bindings.getMarkagareNamnAndel());
    }

    @Test
    void så_ska_markägaruppgifter_utan_kontaktperson_vara_korrekta() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        when(mockMarkagareService.getAgareForFastighet(eq(projektId), eq(fastighetId))).thenReturn(Arrays.asList(
            new MarkagareDto().kontaktperson(false).namn("Fastighetsägare").inkluderaIAvtal(true).adress("Adress 2")
                .postnummer("Saknar postort").andel("1/3"),
            new MarkagareDto().kontaktperson(false).namn("Inte Huvudägare").inkluderaIAvtal(true).adress("Adress 1")
                .postnummer("12345").postort("Ort").bankkonto("Bankkonto").fodelsedatumEllerOrgnummer("19911221")
                .andel("1/2"),
            new MarkagareDto().kontaktperson(false).namn("En Annan Ägare").inkluderaIAvtal(true).adress("Adress 3")
                .postnummer("0").postort("Ja").fodelsedatumEllerOrgnummer("23").andel("1/4"),
            new MarkagareDto().kontaktperson(false).namn("Inte inkluderad").inkluderaIAvtal(false).adress("Adress 1")
                .postnummer("12345").postort("Ort").andel("1/5")
        ));

        var samtligaFastighetsagare = Arrays.asList(
            "En Annan Ägare, Adress 3, 0, Ja",
            "Fastighetsägare, Adress 2, Saknar postort, null",
            "Inte Huvudägare, Adress 1, 12345, Ort"
        );

        var markagareNamn = Arrays.asList(
            "En Annan Ägare",
            "Fastighetsägare",
            "Inte Huvudägare",
            "Inte inkluderad"
        );

        var markagareNamnAdress = Arrays.asList(
            "En Annan Ägare, Adress 3 0 Ja",
            "Fastighetsägare, Adress 2 Saknar postort null",
            "Inte Huvudägare, Adress 1 12345 Ort",
            "Inte inkluderad, Adress 1 12345 Ort"
        );

        var markagareNamnAdressAndel = Arrays.asList(
            "En Annan Ägare, Adress 3 0 Ja, Andel 1/4",
            "Fastighetsägare, Adress 2 Saknar postort null, Andel 1/3",
            "Inte Huvudägare, Adress 1 12345 Ort, Andel 1/2",
            "Inte inkluderad, Adress 1 12345 Ort, Andel 1/5"
        );

        var markagareNamnAndel = Arrays.asList(
            "En Annan Ägare, Andel 1/4",
            "Fastighetsägare, Andel 1/3",
            "Inte Huvudägare, Andel 1/2",
            "Inte inkluderad, Andel 1/5"
        );

        // When
        var bindings = markagareBindingsService.getMarkagareBindings(projektId, fastighetId);

        // Then
        assertEquals(samtligaFastighetsagare, bindings.getSamtligaFastighetsagare());
        assertEquals(markagareNamn, bindings.getMarkagareNamn());
        assertEquals(markagareNamnAdress, bindings.getMarkagareNamnAdress());
        assertEquals(markagareNamnAdressAndel, bindings.getMarkagareNamnAdressAndel());
        assertEquals(markagareNamnAndel, bindings.getMarkagareNamnAndel());
    }
}