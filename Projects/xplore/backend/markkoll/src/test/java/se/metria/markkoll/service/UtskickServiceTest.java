package se.metria.markkoll.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.projekt.UtskickProjektView;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.utskick.UtskickBatchDto;
import se.metria.markkoll.service.utskick.UtskickDto;
import se.metria.markkoll.service.utskick.UtskickService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.testdata.UtskickTestData.*;

class UtskickServiceTest {

    UtskickService utskickService;

    AvtalService mockAvtalService;
    FastighetService mockFastighetService;
    MarkagareService mockMarkagareService;
    ProjektRepository mockProjektRepository;

    @BeforeEach
    void beforeEach() {
        var date = LocalDate.of(2022, 03, 14);
        mockAvtalService = mock(AvtalService.class);
        mockFastighetService = mock(FastighetService.class);
        mockMarkagareService = mock(MarkagareService.class);
        mockProjektRepository = mock(ProjektRepository.class);

        utskickService = new UtskickService(mockAvtalService, mockFastighetService, mockMarkagareService,
            mockProjektRepository);
    }

    @ParameterizedTest(name = "{index} {0}, {2}, {3}")
    @MethodSource("utskickTestData")
    void så_ska_det_gå_att_hämta_utskick_för_avtal(String _name,
                                                   List<MarkagareDto> markagareDtoList,
                                                   UtskicksstrategiDto utskicksstrategi,
                                                   DetaljtypDto detaljtyp,
                                                   UtskickBatchDto utskickBatchExpect)
    {
        // Given
        var avtalId = mockUUID(0);
        var projektInfo = new UtskickProjektViewImpl(ProjektTypDto.LOKALNAT, utskicksstrategi, false);
        var fastighet = new FastighetDto().fastighetsbeteckning("fbet").detaljtyp(detaljtyp.getValue());

        when(mockFastighetService.getFastighet(eq(avtalId))).thenReturn(fastighet);
        when(mockMarkagareService.getAgareForAvtal(eq(avtalId))).thenReturn(markagareDtoList);
        when(mockProjektRepository.getUtskickProjektInfo(eq(avtalId))).thenReturn(projektInfo); //TODO: FIIIIX
        when(mockAvtalService.updateUtskicksnummer(eq(avtalId))).thenReturn(23);

        // When
        var utskickBatch = utskickService.getAvtalUtskickBatch(avtalId);

        // Then
        assertEquals(utskickBatchExpect, utskickBatch);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void så_ska_det_gå_att_hämta_utskick_för_infobrev(Boolean kontaktperson) {
        // Given
        var avtalId = mockUUID(0);
        var signatarer = Arrays.asList(
            new MarkagareDto().namn("annan signatar"),
            new MarkagareDto().namn("signatar").kontaktperson(kontaktperson)
        );
        var utskickExpect = UtskickDto.builder()
            .signatarer(signatarer)
            .kontaktperson(kontaktperson ? Optional.of(signatarer.get(1)) : Optional.of(signatarer.get(0)))
            .lopnummer("1312")
            .build();

        when(mockAvtalService.getUtskicksnummer(eq(avtalId))).thenReturn(1312);
        when(mockMarkagareService.getAgareForAvtal(eq(avtalId))).thenReturn(signatarer);

        // When
        var utskick = utskickService.getInfobrevUtskick(avtalId);

        // Then
        assertEquals(utskickExpect, utskick);
    }

    private static Stream<Arguments> utskickTestData() {
        return Stream.of(
            Arguments.of("markägare", markagare(), UtskicksstrategiDto.FASTIGHETSAGARE, DetaljtypDto.SAMF, utskickBatchFastighet()),
            Arguments.of("markägare", markagare(), UtskicksstrategiDto.FASTIGHET, DetaljtypDto.SAMF, utskickBatchFastighet()),
            Arguments.of("markägare", markagare(), UtskicksstrategiDto.ADRESS, DetaljtypDto.SAMF, utskickBatchFastighet()),
            Arguments.of("markägare", markagare(), UtskicksstrategiDto.FASTIGHET, DetaljtypDto.FASTIGHET, utskickBatchFastighet()),
            Arguments.of("markägare", markagare(), UtskicksstrategiDto.FASTIGHETSAGARE, DetaljtypDto.FASTIGHET, utskickBatchFastighetsagare()),
            Arguments.of("markägare", markagare(), UtskicksstrategiDto.ADRESS, DetaljtypDto.FASTIGHET, utskickBatchAdress()),

            Arguments.of("markägare enskild", markagareEnskild(), UtskicksstrategiDto.FASTIGHETSAGARE, DetaljtypDto.FASTIGHET, utskickmarkagareEnskild()),
            Arguments.of("markägare enskild", markagareEnskild(), UtskicksstrategiDto.ADRESS, DetaljtypDto.FASTIGHET, utskickmarkagareEnskild()),
            Arguments.of("markägare enskild", markagareEnskild(), UtskicksstrategiDto.FASTIGHET, DetaljtypDto.FASTIGHET, utskickmarkagareEnskildFastighet()),
            Arguments.of("markägare enskild", markagareEnskild(), UtskicksstrategiDto.FASTIGHETSAGARE, DetaljtypDto.SAMF, utskickmarkagareEnskildFastighet()),
            Arguments.of("markägare enskild", markagareEnskild(), UtskicksstrategiDto.ADRESS, DetaljtypDto.SAMF, utskickmarkagareEnskildFastighet()),
            Arguments.of("markägare enskild", markagareEnskild(), UtskicksstrategiDto.FASTIGHET, DetaljtypDto.SAMF, utskickmarkagareEnskildFastighet()),

            Arguments.of("ombud enskild", ombudEnskild(), UtskicksstrategiDto.FASTIGHETSAGARE, DetaljtypDto.FASTIGHET, utskickOmbudEnskild()),
            Arguments.of("ombud enskild", ombudEnskild(), UtskicksstrategiDto.ADRESS, DetaljtypDto.FASTIGHET, utskickOmbudEnskild()),
            Arguments.of("ombud enskild", ombudEnskild(), UtskicksstrategiDto.FASTIGHET, DetaljtypDto.FASTIGHET, utskickOmbudEnskild()),
            Arguments.of("ombud enskild", ombudEnskild(), UtskicksstrategiDto.FASTIGHETSAGARE, DetaljtypDto.SAMF, utskickOmbudEnskild()),
            Arguments.of("ombud enskild", ombudEnskild(), UtskicksstrategiDto.ADRESS, DetaljtypDto.SAMF, utskickOmbudEnskild()),
            Arguments.of("ombud enskild", ombudEnskild(), UtskicksstrategiDto.FASTIGHET, DetaljtypDto.SAMF, utskickOmbudEnskild()),

            Arguments.of("markägare fastighetsstrategi ingen kontaktperson", markagareFastighetIngenKontaktperson(),
                UtskicksstrategiDto.FASTIGHET, DetaljtypDto.FASTIGHET, utskickMarkagareFastighetIngenKontaktperson()),
            Arguments.of("markägare fastighetsstrategi ingen kontaktperson", markagareFastighetIngenKontaktperson(),
                UtskicksstrategiDto.FASTIGHET, DetaljtypDto.SAMF, utskickMarkagareFastighetIngenKontaktperson())
        );
    }

    @Data
    @AllArgsConstructor
    class UtskickProjektViewImpl implements UtskickProjektView {
        private ProjektTypDto projektTyp;
        private UtskicksstrategiDto utskicksstrategi;
        private Boolean shouldHaveVp;
    }
}