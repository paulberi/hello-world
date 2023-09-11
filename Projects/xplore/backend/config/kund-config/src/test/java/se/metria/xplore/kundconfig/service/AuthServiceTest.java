package se.metria.xplore.kundconfig.service;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import se.metria.xplore.kundconfig.entity.FastighetsokAuthEntity;
import se.metria.xplore.kundconfig.entity.KundEntity;
import se.metria.xplore.kundconfig.entity.MetriaMapsAuthEntity;
import se.metria.xplore.kundconfig.openapi.model.FastighetsokAuthDto;
import se.metria.xplore.kundconfig.openapi.model.MetriaMapsAuthDto;
import se.metria.xplore.kundconfig.repository.FastighetsokAuthRepository;
import se.metria.xplore.kundconfig.repository.KundConfigRepository;
import se.metria.xplore.kundconfig.repository.MetriaMapsAuthRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Givet AuthService")
public class AuthServiceTest {

    AuthService authService;

    MetriaMapsAuthRepository metriaMapsAuthRepository;
    FastighetsokAuthRepository fastighetsokAuthRepository;
    KundConfigRepository mockKundConfigRepository;
    ModelMapper modelMapper;

    @BeforeEach
    void beforeEach() {
        metriaMapsAuthRepository = mock(MetriaMapsAuthRepository.class);
        fastighetsokAuthRepository = mock(FastighetsokAuthRepository.class);
        mockKundConfigRepository = mock(KundConfigRepository.class);
        modelMapper = mock(ModelMapper.class);

        authService = new AuthService(metriaMapsAuthRepository, fastighetsokAuthRepository, mockKundConfigRepository,
            modelMapper);
    }

    @Test
    void så_ska_det_gå_att_hämta_autentiseringsuppgifter_för_fsök() {
        // Given
        var kundId = "kundId";
        var authExpect = mock(FastighetsokAuthDto.class);
        var authEntity = mock(FastighetsokAuthEntity.class);

        when(fastighetsokAuthRepository.findByKundId(any())).thenReturn(Optional.of(authEntity));
        when(modelMapper.map(any(), any())).thenReturn(authExpect);

        // When
        var auth = authService.getFastighetsokAuth(kundId);

        // Then
        assertEquals(authExpect, auth);

        verify(fastighetsokAuthRepository).findByKundId(eq(kundId));
        verify(modelMapper).map(eq(authEntity), eq(FastighetsokAuthDto.class));
    }

    @Test
    void så_ska_det_gå_att_hämta_autentiseringsuppgifter_för_metria_maps() {
        // Given
        var kundId = "kundId";
        var authExpect = mock(MetriaMapsAuthDto.class);
        var authEntity = mock(MetriaMapsAuthEntity.class);

        when(metriaMapsAuthRepository.findByKundId(any())).thenReturn(Optional.of(authEntity));
        when(modelMapper.map(any(), any())).thenReturn(authExpect);

        // When
        var auth = authService.getMetriaMapsAuth(kundId);

        // Then
        assertEquals(authExpect, auth);

        verify(metriaMapsAuthRepository).findByKundId(eq(kundId));
        verify(modelMapper).map(eq(authEntity), eq(MetriaMapsAuthDto.class));
    }

    @Test
    void så_ska_det_gå_att_redigera_autentiseringsuppgifter_för_fastighetsök() {
        // Given
        var authDto = mock(FastighetsokAuthDto.class);
        var authEntity = mock(FastighetsokAuthEntity.class);

        when(modelMapper.map(any(), any())).thenReturn(authEntity);

        // When
        authService.editFastighetsokAuth(authDto);

        verify(fastighetsokAuthRepository).saveAndFlush(eq(authEntity));
        verify(modelMapper).map(eq(authDto), eq(FastighetsokAuthEntity.class));
    }

    @Test
    void så_ska_det_gå_att_redigera_autentiseringsuppgifter_för_metria_maps() {
        // Given
        var authDto = mock(MetriaMapsAuthDto.class);
        var authEntity = mock(MetriaMapsAuthEntity.class);

        when(modelMapper.map(any(), any())).thenReturn(authEntity);

        // When
        authService.editMetriaMapsAuth(authDto);

        verify(metriaMapsAuthRepository).saveAndFlush(eq(authEntity));
        verify(modelMapper).map(eq(authDto), eq(MetriaMapsAuthEntity.class));
    }

    @Test
    void så_ska_det_gå_att_skapa_autentiseringsuppgifter_för_metria_maps() {
        // Given
        var kundId = "kundId";
        var authDto = mock(MetriaMapsAuthDto.class);
        var authEntity = mock(MetriaMapsAuthEntity.class);
        var kund = mock(KundEntity.class);

        when(mockKundConfigRepository.findById(any())).thenReturn(Optional.of(kund));
        when(metriaMapsAuthRepository.saveAndFlush(any())).thenReturn(authEntity);
        when(modelMapper.map(any(), any())).thenReturn(authDto);

        // When
        var auth = authService.createMetriaMapsAuth(kundId);

        // Then
        assertEquals(authDto, auth);

        verify(metriaMapsAuthRepository).saveAndFlush(eq(new MetriaMapsAuthEntity(kund)));
        verify(modelMapper).map(eq(authEntity), eq(MetriaMapsAuthDto.class));
    }

    @Test
    void så_ska_det_gå_att_skapa_autentiseringsuppgifter_för_fastighetsök() {
        // Given
        var kundId = "kundId";
        var authDto = mock(FastighetsokAuthDto.class);
        var authEntity = mock(FastighetsokAuthEntity.class);
        var kund = mock(KundEntity.class);

        when(mockKundConfigRepository.findById(any())).thenReturn(Optional.of(kund));
        when(fastighetsokAuthRepository.saveAndFlush(any())).thenReturn(authEntity);
        when(modelMapper.map(any(), any())).thenReturn(authDto);

        // When
        var auth = authService.createFastighetsokAuth(kundId);

        // Then
        assertEquals(authDto, auth);

        verify(fastighetsokAuthRepository).saveAndFlush(eq(new FastighetsokAuthEntity(kund)));
        verify(modelMapper).map(eq(authEntity), eq(FastighetsokAuthDto.class));
    }
}
