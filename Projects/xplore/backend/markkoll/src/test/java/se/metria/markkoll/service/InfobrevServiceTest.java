package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;
import se.metria.markkoll.repository.infobrev.InfobrevsjobbRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.dokument.DokumentmallService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.logging.LoggService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class InfobrevServiceTest {
    InfobrevService infobrevService;

    AclService mockAclService;
    CachingService mockCachingService;
    FastighetService mockFastighetService;
    DokumentGeneratorService mockDokumentGeneratorService;
    InfobrevsjobbRepository mockInfobrevsjobbRepository;
    ModelMapper mockModelMapper;
    LoggService mockLoggService;
    ProjektRepository mockProjektRepository;
    DokumentmallService mockDokumentmallService;

    @BeforeEach
    void beforeEach() {
        mockAclService = mock(AclService.class);
        mockCachingService = mock(CachingService.class);
        mockFastighetService = mock(FastighetService.class);
        mockDokumentGeneratorService = mock(DokumentGeneratorService.class);
        mockInfobrevsjobbRepository = mock(InfobrevsjobbRepository.class);
        mockModelMapper = mock(ModelMapper.class);
        mockLoggService = mock(LoggService.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockDokumentmallService = mock(DokumentmallService.class);

        infobrevService = new InfobrevService(mockAclService, mockCachingService, mockFastighetService,
            mockDokumentGeneratorService, mockInfobrevsjobbRepository, mockModelMapper, mockLoggService,
            mockProjektRepository, mockDokumentmallService);
    }

    @Test
    void så_ska_det_gå_att_avbryta_ett_infobrevsjobb() {
        // Given
        var infobrevsjobbId = mockUUID(0);

        // When
        infobrevService.cancelInfobrevsjobb(infobrevsjobbId);

        // Then
        verify(mockInfobrevsjobbRepository).setInfobrevsjobbStatus(eq(infobrevsjobbId),
            eq(AvtalsjobbStatusDto.CANCELLED));
    }
}