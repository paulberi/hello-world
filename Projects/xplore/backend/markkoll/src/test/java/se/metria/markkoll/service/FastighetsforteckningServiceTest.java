package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningAnledning;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.repository.FastighetsforteckningRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.util.SkipIdMatcher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class FastighetsforteckningServiceTest {

    FastighetsforteckningService fastighetsforteckningService;

    AvtalRepository mockAvtalRepository;
    FastighetRepository mockFastighetRepository;
    FastighetsforteckningRepository mockFastighetsforteckningRepository;
    ProjektRepository mockProjektRepository;

    @BeforeEach
    void setUp() {
        mockAvtalRepository = mock(AvtalRepository.class);
        mockFastighetRepository = mock(FastighetRepository.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockFastighetRepository = mock(FastighetRepository.class);
        mockFastighetsforteckningRepository = mock(FastighetsforteckningRepository.class);

        fastighetsforteckningService = new FastighetsforteckningService(mockAvtalRepository, mockFastighetRepository,
            mockFastighetsforteckningRepository, mockProjektRepository);
    }

    @Test
    void så_ska_det_gå_att_skapa_en_fastighetsförteckning() {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var avtalId = mockUUID(2);
        var anledning = FastighetsforteckningAnledning.MANUELLT_TILLAGD;

        var projektEntity = new ProjektEntity();
        var fastighetEntity = new FastighetEntity();
        var avtalEntity = new AvtalEntity();
        var fastighetsforteckningEntity = FastighetsforteckningEntity.builder()
            .avtal(avtalEntity)
            .fastighet(fastighetEntity)
            .projekt(projektEntity)
            .anledning(anledning)
            .build();

        when(mockProjektRepository.findById(eq(projektId))).thenReturn(Optional.of(projektEntity));
        when(mockFastighetRepository.findById(eq(fastighetId))).thenReturn(Optional.of(fastighetEntity));
        when(mockAvtalRepository.findById(eq(avtalId))).thenReturn(Optional.of(avtalEntity));
        when(mockFastighetsforteckningRepository.save(SkipIdMatcher.eq(fastighetsforteckningEntity)))
            .thenReturn(fastighetsforteckningEntity);

        // When
        var ffId = fastighetsforteckningService.create(projektId, fastighetId, avtalId, anledning);

        // Then
        verify(mockProjektRepository).findById(eq(projektId));
        verify(mockFastighetRepository).findById(eq(fastighetId));
        verify(mockAvtalRepository).findById(eq(avtalId));
        verify(mockFastighetsforteckningRepository).save(SkipIdMatcher.eq(fastighetsforteckningEntity));

        assertEquals(ffId, fastighetsforteckningEntity.getId());
    }
}