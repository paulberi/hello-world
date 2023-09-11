package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.avtal.AvtalsjobbEntity;
import se.metria.markkoll.entity.InfobrevsjobbEntity;
import se.metria.markkoll.entity.logging.projektlogg.AvtalhandelseEntity;
import se.metria.markkoll.entity.logging.projektlogg.InfobrevhandelseEntity;
import se.metria.markkoll.entity.logging.projektlogg.ManuellFastighethandelseEntity;
import se.metria.markkoll.entity.logging.projektlogg.ProjekthandelseEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalsjobbRepository;
import se.metria.markkoll.repository.infobrev.InfobrevsjobbRepository;
import se.metria.markkoll.repository.logging.projektlogg.ProjektLoggRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.logging.ProjektLoggService;
import se.metria.markkoll.util.SkipIdMatcher;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@DisplayName("Givet ProjektLoggService")
@MarkkollServiceTest
public class ProjektLoggServiceTest {
    ProjektLoggService projektLoggService;
    ProjektLoggRepository mockProjektLoggRepository;
    ProjektRepository mockProjektRepository;
    AvtalsjobbRepository mockAvtalsjobbRepository;
    InfobrevsjobbRepository mockInfobrevsjobbRepository;
    ModelMapper mockModelMapper;

    @BeforeEach
    void beforeEach() {
        mockProjektRepository = mock(ProjektRepository.class);
        mockProjektLoggRepository = mock(ProjektLoggRepository.class);
        mockAvtalsjobbRepository = mock(AvtalsjobbRepository.class);
        mockInfobrevsjobbRepository = mock(InfobrevsjobbRepository.class);
        mockModelMapper = mock(ModelMapper.class);

        projektLoggService = new ProjektLoggService(mockProjektLoggRepository, mockProjektRepository,
            mockAvtalsjobbRepository, mockInfobrevsjobbRepository, mockModelMapper);
    }

    @Test
    void så_ska_det_gå_att_skapa_en_projekthändelse() {
        // Given
        var projektId = mockUUID(0);
        var projekthandelsetyp = ProjekthandelseTypDto.HAMTA_MARKAGARE;
        var projektEntity = new ProjektEntity();
        var handelseEntity = ProjekthandelseEntity.builder()
            .projekt(projektEntity)
            .projekthandelsetyp(projekthandelsetyp)
            .build();
        var handelseDtoExpect = new ProjekthandelseDto();

        when(mockProjektRepository.findById(eq(projektId))).thenReturn(Optional.of(projektEntity));
        when(mockProjektLoggRepository.saveAndFlush(any())).then(returnsFirstArg());
        when(mockModelMapper.map(eq(handelseEntity), eq(ProjektLoggItemDto.class))).thenReturn(handelseDtoExpect);

        // When
        var handelseDto = projektLoggService.createProjektHandelse(projektId, projekthandelsetyp);

        // Then
        assertEquals(handelseDtoExpect, handelseDto);
        verify(mockProjektLoggRepository).saveAndFlush(eq(handelseEntity));
    }

    @Test
    void så_ska_det_gå_att_skapa_en_avtalshändelse() {
        // Given
        var projektId = mockUUID(0);
        var avtalsjobbId = mockUUID(1);
        var antalFastigheter = 3;
        var handelseDtoExpect = new AvtalhandelseDto();
        var projektEntity = new ProjektEntity();
        var avtalsjobbEntity = new AvtalsjobbEntity();
        var handelseEntity = AvtalhandelseEntity.builder()
            .projekt(projektEntity)
            .avtalsjobb(avtalsjobbEntity)
            .antalFastigheter(antalFastigheter)
            .build();

        when(mockProjektRepository.findById(eq(projektId))).thenReturn(Optional.of(projektEntity));
        when(mockAvtalsjobbRepository.findById(eq(avtalsjobbId))).thenReturn(Optional.of(avtalsjobbEntity));
        when(mockProjektLoggRepository.saveAndFlush(any())).then(returnsFirstArg());
        when(mockModelMapper.map(eq(handelseEntity), eq(AvtalhandelseDto.class))).thenReturn(handelseDtoExpect);

        // When
        var handelseDto = projektLoggService.createAvtalHandelse(projektId, avtalsjobbId,
            antalFastigheter);

        // Then
        assertEquals(handelseDtoExpect, handelseDto);
        verify(mockProjektLoggRepository).saveAndFlush(eq(handelseEntity));
    }

    @Test
    void så_ska_det_gå_att_skapa_en_infobrevhändelse() {
        // Given
        var projektId = mockUUID(0);
        var infobrevsjobbId = mockUUID(1);
        var antalFastigheter = 3;
        var handelseDtoExpect = new InfobrevhandelseDto();
        var projektEntity = new ProjektEntity();
        var infobrevsjobbEntity = new InfobrevsjobbEntity();
        var handelseEntity = InfobrevhandelseEntity.builder()
            .projekt(projektEntity)
            .infobrevsjobb(infobrevsjobbEntity)
            .antalFastigheter(antalFastigheter)
            .build();

        when(mockProjektRepository.findById(eq(projektId))).thenReturn(Optional.of(projektEntity));
        when(mockInfobrevsjobbRepository.findById(eq(infobrevsjobbId))).thenReturn(Optional.of(infobrevsjobbEntity));
        when(mockProjektLoggRepository.saveAndFlush(any())).then(returnsFirstArg());
        when(mockModelMapper.map(eq(handelseEntity), eq(InfobrevhandelseDto.class))).thenReturn(handelseDtoExpect);

        // When
        var handelseDto = projektLoggService.createInfobrevHandelse(projektId, infobrevsjobbId,
            antalFastigheter);

        // Then
        assertEquals(handelseDtoExpect, handelseDto);
        verify(mockProjektLoggRepository).saveAndFlush(eq(handelseEntity));
    }

    @Test
    void så_ska_det_gå_att_logga_en_händelse_relaterad_till_manuellt_tillagda_fastigheter() {
        // Given
        var projektId = mockUUID(0);
        var fastighetsbeteckning = "FASTIGHET 1:23";
        var typ = ManuellFastighethandelseTypDto.BORTTAGEN;
        var projektEntity = new ProjektEntity();
        var projektloggExpect = new ManuellFastighethandelseDto();
        var handelseEntity = ManuellFastighethandelseEntity.builder()
            .fastighetsbeteckning(fastighetsbeteckning)
            .projekt(projektEntity)
            .typ(typ)
            .build();

        when(mockProjektRepository.findById(eq(projektId))).thenReturn(Optional.of(projektEntity));
        when(mockProjektLoggRepository.save(SkipIdMatcher.eq(handelseEntity))).then(returnsFirstArg());
        when(mockModelMapper.map(eq(handelseEntity), eq(ManuellFastighethandelseDto.class))).thenReturn(projektloggExpect);

        // When
        var projektlogg = projektLoggService.createManuellFastighetHandelse(projektId,
            fastighetsbeteckning, typ);

        // Then
        verify(mockProjektRepository).findById(eq(projektId));
        verify(mockProjektLoggRepository).save(SkipIdMatcher.eq(handelseEntity));
        verify(mockModelMapper).map(eq(handelseEntity), eq(ManuellFastighethandelseDto.class));

        assertEquals(projektloggExpect, projektlogg);
    }

    @Test
    void så_ska_det_gå_att_hämta_en_sida_med_projektloggar() {
        // Given
        var projektId = mockUUID(0);
        var pageRequest = PageRequest.of(1, 2);
        var filter = Arrays.asList(
            ProjektLoggFilterDto.MARKUPPLATELSEAVTAL,
            ProjektLoggFilterDto.SKAPAT_AV_MIG);
        var pageExpect = new ProjektLoggPageDto();

        var projekthandelseDto = new ProjekthandelseDto();
        var avtalhandelseDto = new AvtalhandelseDto();
        var infobrevhandelseDto = new InfobrevhandelseDto();
        var manuellFastighethandelseDto = new ManuellFastighethandelseDto();

        var pageEntityContent = Arrays.asList(
            new ProjekthandelseEntity(),
            new AvtalhandelseEntity(),
            new InfobrevhandelseEntity(),
            new ManuellFastighethandelseEntity()
        );
        var pageEntity = new PageImpl<>(pageEntityContent);

        when(mockModelMapper.map(any(), eq(ProjektLoggPageDto.class))).thenReturn(pageExpect);
        when(mockModelMapper.map(any(), eq(ProjekthandelseDto.class))).thenReturn(projekthandelseDto);
        when(mockModelMapper.map(any(), eq(AvtalhandelseDto.class))).thenReturn(avtalhandelseDto);
        when(mockModelMapper.map(any(), eq(InfobrevhandelseDto.class))).thenReturn(infobrevhandelseDto);
        when(mockModelMapper.map(any(), eq(ManuellFastighethandelseDto.class))).thenReturn(manuellFastighethandelseDto);
        when(mockProjektLoggRepository.getProjektLoggPage(eq(projektId), eq(pageRequest), eq(filter)))
            .thenReturn(pageEntity);

        // When
        var page = projektLoggService.getProjektLoggPage(projektId, pageRequest, filter);

        // Then
        verify(mockModelMapper).map(any(), eq(ProjektLoggPageDto.class));
        verify(mockModelMapper).map(eq(new ProjekthandelseEntity()), eq(ProjekthandelseDto.class));
        verify(mockModelMapper).map(eq(new AvtalhandelseEntity()), eq(AvtalhandelseDto.class));
        verify(mockModelMapper).map(eq(new InfobrevhandelseEntity()), eq(InfobrevhandelseDto.class));
        verify(mockModelMapper).map(eq(new ManuellFastighethandelseEntity()), eq(ManuellFastighethandelseDto.class));
        verify(mockProjektLoggRepository).getProjektLoggPage(eq(projektId), eq(pageRequest), eq(filter));

        assertEquals(pageExpect, page);
    }
}
