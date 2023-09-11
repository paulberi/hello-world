package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalsjobbRepository;
import se.metria.markkoll.repository.infobrev.InfobrevsjobbRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.logging.AvtalsloggService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.logging.ProjektLoggService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@DisplayName("Givet LoggService")
@MarkkollServiceTest
public class LoggServiceTest {
    LoggService loggService;

    AvtalsloggService mockAvtalsloggService;
    FastighetService mockFastighetService;
    ProjektLoggService mockProjektLoggService;
    AvtalsjobbRepository mockAvtalsjobbRepository;
    InfobrevsjobbRepository mockInfobrevsjobbRepository;
    ProjektRepository mockProjektRepository;

    @BeforeEach
    void beforeEach() {
        mockAvtalsloggService = mock(AvtalsloggService.class);
        mockFastighetService = mock(FastighetService.class);
        mockProjektLoggService = mock(ProjektLoggService.class);
        mockAvtalsjobbRepository = mock(AvtalsjobbRepository.class);
        mockInfobrevsjobbRepository = mock(InfobrevsjobbRepository.class);
        mockProjektRepository = mock(ProjektRepository.class);

        loggService = new LoggService(mockFastighetService, mockProjektLoggService, mockAvtalsjobbRepository,
            mockInfobrevsjobbRepository, mockProjektRepository);
    }

    @Test
    void så_ska_det_gå_att_logga_en_ny_avtalshändelse() {
        // Given
        var avtalsjobbId = mockUUID(0);
        var projektId = mockUUID(1);
        var avtalTotal = 3;
        var numOfAvtal = 4;
        var projektloggExpect = new ProjektLoggItemDto()
            .projektLoggType(ProjektLoggTypeDto.AVTALHANDELSE);

        when(mockAvtalsjobbRepository.getProjektId(eq(avtalsjobbId))).thenReturn(projektId);
        when(mockAvtalsjobbRepository.getTotal(eq(avtalsjobbId))).thenReturn(avtalTotal);
        when(mockProjektRepository.getNumOfAvtal(eq(projektId))).thenReturn(numOfAvtal);
        when(mockProjektLoggService.createAvtalHandelse(any(), any(), any())).thenReturn(projektloggExpect);

        // When
        var projektlogg = loggService.addAvtalHandelse(avtalsjobbId);

        // Then
        assertEquals(projektloggExpect, projektlogg);
        verify(mockProjektLoggService).createAvtalHandelse(eq(projektId), eq(avtalsjobbId), eq(avtalTotal));
    }

    @Test
    void så_ska_det_gå_att_logga_en_ny_avtalshändelse_för_samtliga_fastigheter() {
        // Given
        var avtalsjobbId = mockUUID(0);
        var projektId = mockUUID(1);
        var avtalTotal = 3;
        var numOfAvtal = 3;
        var projektloggExpect = new ProjektLoggItemDto()
            .projektLoggType(ProjektLoggTypeDto.AVTALHANDELSE);

        when(mockAvtalsjobbRepository.getProjektId(eq(avtalsjobbId))).thenReturn(projektId);
        when(mockAvtalsjobbRepository.getTotal(eq(avtalsjobbId))).thenReturn(avtalTotal);
        when(mockProjektRepository.getNumOfAvtal(eq(projektId))).thenReturn(numOfAvtal);
        when(mockProjektLoggService.createAvtalHandelse(any(), any(), any())).thenReturn(projektloggExpect);

        // When
        var projektlogg = loggService.addAvtalHandelse(avtalsjobbId);

        // Then
        assertEquals(projektloggExpect, projektlogg);
        verify(mockProjektLoggService).createAvtalHandelse(eq(projektId), eq(avtalsjobbId), eq(LoggService.SAMTLIGA));
    }

    @Test
    void så_ska_det_gå_att_logga_en_ny_infobrevshändelse() {
        // Given
        var projektId = mockUUID(1);
        var avtalTotal = 3;
        var numOfAvtal = 4;
        var projektloggExpect = new ProjektLoggItemDto()
            .projektLoggType(ProjektLoggTypeDto.INFOBREVHANDELSE);

        var infobrevsjobbId = mockUUID(0);
        when(mockInfobrevsjobbRepository.getProjektId(eq(infobrevsjobbId))).thenReturn(projektId);
        when(mockInfobrevsjobbRepository.getTotal(eq(infobrevsjobbId))).thenReturn(avtalTotal);
        when(mockProjektRepository.getNumOfAvtal(eq(projektId))).thenReturn(numOfAvtal);
        when(mockProjektLoggService.createInfobrevHandelse(any(), any(), any())).thenReturn(projektloggExpect);

        // When
        var projektlogg = loggService.addInfobrevHandelse(infobrevsjobbId);

        // Then
        assertEquals(projektloggExpect, projektlogg);
        verify(mockProjektLoggService).createInfobrevHandelse(eq(projektId), eq(infobrevsjobbId), eq(avtalTotal));
    }

    @Test
    void så_ska_det_gå_att_logga_en_ny_infobrevshändelse_för_samtliga_fastigheter() {
        // Given
        var projektId = mockUUID(1);
        var avtalTotal = 4;
        var numOfAvtal = 4;
        var projektloggExpect = new ProjektLoggItemDto()
            .projektLoggType(ProjektLoggTypeDto.INFOBREVHANDELSE);

        var infobrevsjobbId = mockUUID(0);
        when(mockInfobrevsjobbRepository.getProjektId(eq(infobrevsjobbId))).thenReturn(projektId);
        when(mockInfobrevsjobbRepository.getTotal(eq(infobrevsjobbId))).thenReturn(avtalTotal);
        when(mockProjektRepository.getNumOfAvtal(eq(projektId))).thenReturn(numOfAvtal);
        when(mockProjektLoggService.createInfobrevHandelse(any(), any(), any())).thenReturn(projektloggExpect);

        // When
        var projektlogg = loggService.addInfobrevHandelse(infobrevsjobbId);

        // Then
        assertEquals(projektloggExpect, projektlogg);
        verify(mockProjektLoggService).createInfobrevHandelse(eq(projektId), eq(infobrevsjobbId), eq(LoggService.SAMTLIGA));
    }

    @Test
    void så_ska_det_gå_att_logga_en_händelse_relaterad_till_manuellt_tillagda_fastigheter() {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var typ = ManuellFastighethandelseTypDto.TILLAGD;
        var fastighetsbeteckning = "FASTIGHET 1:23";
        var projektloggExpect = new ProjektLoggItemDto();

        when(mockFastighetService.getFastighet(eq(projektId), eq(fastighetId))).thenReturn(
            new FastighetDto().fastighetsbeteckning(fastighetsbeteckning)
        );
        when(mockProjektLoggService.createManuellFastighetHandelse(eq(projektId), eq(fastighetsbeteckning),
            eq(typ))).thenReturn(projektloggExpect);

        // When
        var projektlogg = loggService.addManuellFastighetHandelse(projektId, fastighetId, typ);

        // Then
        verify(mockFastighetService).getFastighet(eq(projektId), eq(fastighetId));
        verify(mockProjektLoggService).createManuellFastighetHandelse(eq(projektId), eq(fastighetsbeteckning),
            eq(typ));
        assertEquals(projektloggExpect, projektlogg);
    }

    @Test
    void så_ska_det_gå_att_hämta_en_sida_med_projektloggar() {
        // Given
        var projektId = mockUUID(0);
        var pageNum = 2;
        var size = 3;
        var filter = Arrays.asList(ProjektLoggFilterDto.SKAPAT_AV_MIG,
            ProjektLoggFilterDto.MARKUPPLATELSEAVTAL);
        var sortDirection = Sort.Direction.DESC;
        var pageExpect = new ProjektLoggPageDto().number(0).totalPages(1).totalElements(2);
        var pageRequest = PageRequest.of(pageNum, size, sortDirection, "datum");

        when(mockProjektLoggService.getProjektLoggPage(any(), any(), any())).thenReturn(pageExpect);

        // When
        var page = loggService.getProjektLoggPage(projektId, pageNum, size, filter, sortDirection);

        // Then
        assertEquals(pageExpect, page);
        verify(mockProjektLoggService).getProjektLoggPage(eq(projektId), eq(pageRequest), eq(filter));
    }
}
