package se.metria.markkoll.service.vardering.elnat.bilaga;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.FilEntity;
import se.metria.markkoll.entity.vardering.bilaga.BilagaEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.BilagaTypDto;
import se.metria.markkoll.openapi.model.ElnatBilagaDto;
import se.metria.markkoll.repository.fil.FilRepository;
import se.metria.markkoll.repository.vardering.bilaga.BilagaRepository;
import se.metria.markkoll.repository.vardering.elnat.ElnatVarderingsprotokollRepository;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.util.SkipIdMatcher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@SuppressWarnings("NonAsciiCharacters")
@MarkkollServiceTest
@DisplayName("Givet BilagaService")
public class BilagaServiceTest {
    ElnatBilagaService bilagaService;
    AclService mockAclService;
    BilagaRepository mockBilagaRepository;
    FilRepository mockFilRepository;
    FilService mockFilService;
    ElnatVarderingsprotokollRepository mockVarderingsprotokollRepository;
    ModelMapper mockModelMapper;

    @BeforeEach
    void beforeEach() {
        mockAclService = mock(AclService.class);
        mockBilagaRepository = mock(BilagaRepository.class);
        mockFilRepository = mock(FilRepository.class);
        mockFilService = mock(FilService.class);
        mockVarderingsprotokollRepository = mock(ElnatVarderingsprotokollRepository.class);
        mockModelMapper = mock(ModelMapper.class);
        bilagaService = new ElnatBilagaService(mockAclService, mockBilagaRepository, mockFilRepository,
            mockFilService, mockVarderingsprotokollRepository, mockModelMapper);
    }

    @Test
    void så_ska_det_gå_att_skapa_en_bilaga() {
        // Given
        var filId = mockUUID(0);
        var vpId = mockUUID(1);
        var bilagaTyp = BilagaTypDto.AKERNORM_74;
        var bilagaExpect = new ElnatBilagaDto();
        var vpEntity = new ElnatVarderingsprotokollEntity();
        var filEntity = new FilEntity();
        var bilagaEntity = BilagaEntity.builder()
            .bilagaTyp(bilagaTyp)
            .varderingsprotokoll(vpEntity)
            .fil(filEntity)
            .build();

        when(mockFilRepository.findById(eq(filId))).thenReturn(Optional.of(filEntity));
        when(mockVarderingsprotokollRepository.findById(eq(vpId))).thenReturn(Optional.of(vpEntity));
        when(mockBilagaRepository.save(any())).thenAnswer(returnsFirstArg());
        when(mockModelMapper.map(SkipIdMatcher.eq(bilagaEntity), eq(ElnatBilagaDto.class))).thenReturn(bilagaExpect);

        // When
        var bilagaActual = bilagaService.create(filId, vpId, bilagaTyp);

        // Then
        assertEquals(bilagaExpect, bilagaActual);
        verify(mockBilagaRepository).save(SkipIdMatcher.eq(bilagaEntity));
    }
}
