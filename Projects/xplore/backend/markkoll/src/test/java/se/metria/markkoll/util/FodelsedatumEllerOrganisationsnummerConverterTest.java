package se.metria.markkoll.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.spi.MappingContext;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.markagare.PersonEntity;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FodelsedatumEllerOrganisationsnummerConverterTest {
    FodelsedatumEllerOrganisationsnummerConverter converter = new FodelsedatumEllerOrganisationsnummerConverter();

    @ParameterizedTest
    @MethodSource("personnummerMappings")
    void korrekt_konvertering(String personnummer, String convertExpect) {
        // Given
        var mockMappingContext = mockMappingContext(markagareEntity(personnummer));

        // When
        var convertActual= converter.convert(mockMappingContext);

        // Then
        assertEquals(convertExpect, convertActual);
    }

    @Test
    void ska_kasta_exception_vid_ogiltigt_personnummer() {
        // Given
        var personnummer = "012345678";

        var mockMappingContext = mockMappingContext(markagareEntity(personnummer));

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> converter.convert(mockMappingContext));
    }

    @Test
    void ska_kasta_exception_vid_ogiltigt_personnummer_2() {
        // Given
        var personnummer = "1986A304-8979";

        var mockMappingContext = mockMappingContext(markagareEntity(personnummer));

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> converter.convert(mockMappingContext));
    }

    private static MarkagareEntity markagareEntity(String personnummer) {
        var markagare = new MarkagareEntity();
        var person = new PersonEntity();
        markagare.setPerson(person);
        person.setPersonnummer(personnummer);

        return markagare;
    }

    private static MappingContext<MarkagareEntity, String> mockMappingContext(MarkagareEntity markagareEntity) {
        var mockMappingContext = mock(MappingContext.class);
        when(mockMappingContext.getSource()).thenReturn(markagareEntity);

        return mockMappingContext;
    }

    private static Stream<Arguments> personnummerMappings() {
        return Stream.of(
            Arguments.of(null, ""),
            Arguments.of("", ""),
            Arguments.of("202100-1975", "202100-1975"),
            Arguments.of("559207-6391", "559207-6391"),
            Arguments.of("212000-1777", "212000-1777"),
            Arguments.of("19860304-8979", "19860304"),
            Arguments.of("860304-8979", "860304"),
            Arguments.of("19270529-6404", "19270529"),
            Arguments.of("270529-6404", "270529"),
            Arguments.of("860304", "860304"),
            Arguments.of("19860304", "19860304")
        );
    }
}