package se.metria.markkoll.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.metria.markkoll.entity.projekt.AvtalsinstallningarEntity;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.BerakningAbel07Dto;
import se.metria.markkoll.openapi.model.BerakningRevDto;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class VaghallareServiceTest {
    @InjectMocks
    VaghallareService vaghallareService;

    @Mock
    AvtalsinstallningarRepository avtalsinstallningarRepository;

    @ParameterizedTest
    @MethodSource("installningarVp")
    void vaghallareAvtalstyperVp(BerakningRevDto rev, BerakningAbel07Dto abel07, List<AvtalstypDto> expect) {
        // Given
        var versionId = mockUUID(0);

        var avtalsinstallningar = new AvtalsinstallningarEntity();
        avtalsinstallningar.setBerakningRev(rev);
        avtalsinstallningar.setBerakningAbel07(abel07);
        when(avtalsinstallningarRepository.findByVersionId(any())).thenReturn(avtalsinstallningar);

        // When
        var avtalstyper = vaghallareService.vaghallareAvtalstyperInVp(versionId);

        // Then
        assertEquals(expect, avtalstyper);
    }

    @ParameterizedTest
    @MethodSource("installningarMap")
    void vaghallareAvtalstyperMap(BerakningRevDto rev, BerakningAbel07Dto abel07, List<AvtalstypDto> expect) {
        // Given
        var projektId = mockUUID(0);

        when(avtalsinstallningarRepository.getBerakningRev(projektId)).thenReturn(rev);
        when(avtalsinstallningarRepository.getBerakningAbel07(projektId)).thenReturn(abel07);

        // When
        var avtalstyper = vaghallareService.vaghallareAvtalstyperInMap(projektId);

        // Then
        assertEquals(expect, avtalstyper);
    }

    private static Stream<Arguments> installningarVp() {
        return Stream.of(
            Arguments.of(BerakningRevDto.ENBART_REV, BerakningAbel07Dto.ENBART_ABEL07,
                Collections.emptyList()),
            Arguments.of(BerakningRevDto.REV_GRUNDERSATTNING, BerakningAbel07Dto.ABEL07_FULL_ERSATTNING,
                Arrays.asList(AvtalstypDto.ABEL07)),
            Arguments.of(BerakningRevDto.REV_FULL_ERSATTNING, BerakningAbel07Dto.ABEL07_GRUNDERSATTNING,
                Arrays.asList(AvtalstypDto.REV))
        );
    }

    private static Stream<Arguments> installningarMap() {
        return Stream.of(
            Arguments.of(BerakningRevDto.ENBART_REV, BerakningAbel07Dto.ENBART_ABEL07,
                Collections.emptyList()),
            Arguments.of(BerakningRevDto.REV_GRUNDERSATTNING, BerakningAbel07Dto.ABEL07_FULL_ERSATTNING,
                Arrays.asList(AvtalstypDto.REV, AvtalstypDto.ABEL07)),
            Arguments.of(BerakningRevDto.REV_FULL_ERSATTNING, BerakningAbel07Dto.ABEL07_GRUNDERSATTNING,
                Arrays.asList(AvtalstypDto.REV, AvtalstypDto.ABEL07))
        );
    }
}