package se.metria.markkoll.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import se.metria.markkoll.entity.projekt.AvtalsinstallningarEntity;
import se.metria.markkoll.openapi.model.AvtalMappstrategiDto;
import se.metria.markkoll.openapi.model.AvtalsinstallningarDto;
import se.metria.markkoll.openapi.model.BerakningAbel07Dto;
import se.metria.markkoll.openapi.model.BerakningRevDto;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.service.projekt.AvtalUpdater;
import se.metria.markkoll.service.projekt.AvtalUpdaterFactory;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class AvtalsinstallningarServiceTest {
    @InjectMocks
    AvtalsinstallningarService avtalsinstallningarService;

    @Mock
    AvtalUpdaterFactory avtalUpdaterFactory;

    @Mock
    AvtalsinstallningarRepository avtalsinstallningarRepository;

    @Mock
    FastighetRepository fastighetRepository;

    @Spy
    ModelMapper modelMapper;

    @Captor
    ArgumentCaptor<AvtalsinstallningarEntity> installningarCaptor;

    AvtalUpdater mockAvtalUpdater = mock(AvtalUpdater.class);

    @Test
    void getAvtalsinstallningar() {
        // Given
        var projektId = mockUUID(0);
        var rev = BerakningRevDto.REV_FULL_ERSATTNING;
        var abel07 = BerakningAbel07Dto.ABEL07_GRUNDERSATTNING;
        var mappstrategi = AvtalMappstrategiDto.FLAT;

        var installningarEntity = new AvtalsinstallningarEntity();
        installningarEntity.setBerakningRev(rev);
        installningarEntity.setBerakningAbel07(abel07);
        installningarEntity.setMappstrategi(AvtalMappstrategiDto.FLAT);

        when(avtalsinstallningarRepository.findById(eq(projektId))).thenReturn(Optional.of(installningarEntity));

        // When
        var installningarActual = avtalsinstallningarService.getAvtalsinstallningar(projektId);

        // Then
        modelMapper.validate();

        var installningarExpect = new AvtalsinstallningarDto()
            .berakningAbel07(abel07)
            .berakningRev(rev)
            .mappstrategi(mappstrategi);

        assertEquals(installningarExpect, installningarActual);
    }

    @Test
    void updateAvtalsinstallningar() {
        // Given
        var projektId = mockUUID(0);
        var rev = BerakningRevDto.REV_FULL_ERSATTNING;
        var abel07 = BerakningAbel07Dto.ABEL07_GRUNDERSATTNING;

        var installningarDto = new AvtalsinstallningarDto()
            .berakningAbel07(abel07)
            .berakningRev(rev);

        var installningarEntity = new AvtalsinstallningarEntity();
        installningarEntity.setBerakningAbel07(abel07);
        installningarEntity.setBerakningRev(rev);
        installningarEntity.setId(projektId);

        when(avtalsinstallningarRepository.findById(eq(projektId))).thenReturn(Optional.of(installningarEntity));

        // When
        avtalsinstallningarService.updateAvtalsinstallningar(projektId, installningarDto);

        // Then
        verify(avtalsinstallningarRepository).save(installningarCaptor.capture());
        assertEquals(projektId, installningarCaptor.getValue().getId());
        assertEquals(abel07, installningarCaptor.getValue().getBerakningAbel07());
        assertEquals(rev, installningarCaptor.getValue().getBerakningRev());

        verify(mockAvtalUpdater, never()).updateVarderingsprotokoll(any(), any());
    }

    @Test
    void updateAvtalsinstallningarUpdateVp() {
        // Given
        var projektId = mockUUID(0);
        var fastighetIds = Arrays.asList(mockUUID(1));
        var rev = BerakningRevDto.REV_FULL_ERSATTNING;
        var abel07 = BerakningAbel07Dto.ABEL07_GRUNDERSATTNING;

        var installningarDto = new AvtalsinstallningarDto()
            .berakningAbel07(abel07)
            .berakningRev(rev);

        var installningarEntity = new AvtalsinstallningarEntity();
        installningarEntity.setBerakningAbel07(BerakningAbel07Dto.ABEL07_FULL_ERSATTNING);
        installningarEntity.setId(projektId);

        when(avtalsinstallningarRepository.findById(eq(projektId))).thenReturn(Optional.of(installningarEntity));
        when(avtalUpdaterFactory.getAvtalUpdater(eq(projektId))).thenReturn(mockAvtalUpdater);
        when(fastighetRepository.getFastighetIdsWithAvtalstyper(any(), any())).thenReturn(fastighetIds);

        // When
        avtalsinstallningarService.updateAvtalsinstallningar(projektId, installningarDto);

        // Then
        verify(avtalsinstallningarRepository).save(installningarCaptor.capture());
        assertEquals(projektId, installningarCaptor.getValue().getId());
        assertEquals(abel07, installningarCaptor.getValue().getBerakningAbel07());
        assertEquals(rev, installningarCaptor.getValue().getBerakningRev());

        verify(mockAvtalUpdater).updateVarderingsprotokoll(eq(projektId), eq(fastighetIds));
    }
}