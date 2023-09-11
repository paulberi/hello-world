package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.GeometristatusEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.FastighetsforteckningRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetOmradeRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.geometristatus.GeometristatusRepository;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.intrang.IntrangRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.common.XpSearchService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.fastighet.RegisterenhetImportService;
import se.metria.markkoll.service.fastighet.SamfService;
import se.metria.markkoll.service.logging.AvtalsloggService;
import se.metria.markkoll.service.markagare.MarkagareService;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.*;
import static se.metria.markkoll.util.CollectionUtil.modelMapperList;

@MarkkollServiceTest
@DisplayName("Givet FastighetService")
public class FastighetServiceTest {
    EntityManager mockEntityManager;
    ProjektRepository mockProjektRepository;
    FastighetService fastighetService;
    SamfService mockSamfService;
    MarkagareService mockMarkagareService;
    FastighetsintrangRepository mockFastighetsintrangRepository;
    FastighetRepository mockFastighetRepository;
    FastighetOmradeRepository mockFastighetOmradeRepository;
    VersionRepository mockVersionRepository;
    GeometristatusRepository mockGeometristatusRepository;
    OmradesintrangRepository mockOmradesintrangRepository;
    AvtalRepository mockAvtalRepository;
    AvtalsloggService mockAvtalsloggService;
    RegisterenhetImportService mockRegisterenhetImportService;
    FastighetsforteckningRepository mockFastighetsforteckningRepository;
    XpSearchService mockXpSearchService;
    IntrangRepository mockIntrangRepository;


    @Autowired
    ModelMapper modelMapper;

    final FastighetsfilterDto filter = new FastighetsfilterDto()
            .avtalsstatus(AvtalsstatusDto.AVTAL_JUSTERAS)
            .intrangLength(7.5)
            .excludeWithMarkagare(true)
            .fastighetsbeteckning("fastighet")
            .showSenastUppdaterade(true);

    @BeforeEach
    void before() {
        mockEntityManager = mock(EntityManager.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockVersionRepository = mock(VersionRepository.class);
        mockFastighetRepository = mock(FastighetRepository.class);
        mockFastighetsintrangRepository = mock(FastighetsintrangRepository.class);
        mockMarkagareService = mock(MarkagareService.class);
        mockVersionRepository = mock(VersionRepository.class);
        mockFastighetOmradeRepository = mock(FastighetOmradeRepository.class);
        mockGeometristatusRepository = mock(GeometristatusRepository.class);
        mockSamfService = mock(SamfService.class);
        mockOmradesintrangRepository = mock(OmradesintrangRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockAvtalsloggService = mock(AvtalsloggService.class);
        mockRegisterenhetImportService = mock(RegisterenhetImportService.class);
        mockFastighetsforteckningRepository = mock(FastighetsforteckningRepository.class);
        mockXpSearchService = mock(XpSearchService.class);
        mockIntrangRepository = mock(IntrangRepository.class);
        fastighetService = new FastighetService(mockEntityManager, mockOmradesintrangRepository, mockProjektRepository,
                mockFastighetRepository, mockFastighetOmradeRepository, mockFastighetsintrangRepository, modelMapper,
                mockMarkagareService, mockSamfService, mockGeometristatusRepository, mockAvtalRepository,
                mockAvtalsloggService, mockRegisterenhetImportService, mockFastighetsforteckningRepository,
            mockXpSearchService, mockIntrangRepository);
    }

    @Test
    void så_ska_det_gå_att_hämta_en_lista_med_fastigheter() {
        // Given
        var projektId = mockUUID(0);

        var expect = Arrays.asList(mockFastighetDto(), mockFastighetDto());
        when(mockFastighetRepository.fastigheterFiltered(any(), any())).thenReturn(
                modelMapperList(expect, modelMapper, FastighetEntity.class)
        );

        // When
        var actual = fastighetService.getFastigheter(projektId, filter);

        // Then
        Mockito.verify(mockFastighetRepository).fastigheterFiltered(eq(projektId), eq(filter));
        assertEquals(expect, actual);
    }

    @Disabled("getAvvikelse() behöver göras om, svårt att testa den här funktionen. SQL-sats kanske?")
    @Test
    void så_ska_det_gå_att_hämta_en_sida_med_fastigheter() {
        // Given
        var projektId = mockUUID(0);
        var pageNum = 3;
        var pageSize = 17;

        var expect = mockFastighetPageEntity();
        when(mockFastighetRepository.fastighetPageFiltered(any(), any(), any())).thenReturn(expect);

        // When
        var actual = fastighetService.fastighetPage(projektId, pageNum, pageSize, filter);

        // Then
        Mockito.verify(mockFastighetRepository).fastighetPageFiltered(
                eq(projektId),
                eq(PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "fastighetsbeteckning")),
                eq(filter)
        );

        assertEquals(expect.getSize(), actual.getContent().size());
        assertEquals(expect.getContent(), actual.getContent());
    }

    /*@Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandlaggare_kunna_sätta_avtalsstatus_för_alla_ägare_för_en_fastighet() {
        // Given
        var fastighetId = mockUUID(0);
        var versionId = mockUUID(6);
        var status = AvtalsstatusDto.AVTALSKONFLIKT;
        var projektId = mockUUID(0);

        var agareList = new ArrayList<MarkagareDto>();
        agareList.add(mockAgare(mockUUID(1)));
        agareList.add(mockAgare(mockUUID(2)));
        agareList.add(mockAgare(mockUUID(3)));

        when(mockProjektRepository.projektIdByVersionId(any())).thenReturn(Optional.of(projektId));
        when(mockMarkagareService.getAgareForFastighet(any(), any())).thenReturn(agareList);

        // When
        fastighetService.setAvtalsstatus(fastighetId, versionId, status);

        // Then
        Mockito.verify(mockMarkagareService, times(3)).updateAgare(any(), any());
        for (var agare: agareList) {
            Mockito.verify(mockMarkagareService).updateAgare(agare.getId(), agare);
        }
    }*/

    /*@Test
    @WithMockUserMarkhandlaggare
    void så_ska_geometristatusen_uppdateras_till_oforandrad_om_avtalsstatusen_sätts_från_ej_behandlat() {
        // Given
        var fastighetId = mock(UUID.class);
        var versionId = mockUUID(6);
        var projektId = mock(UUID.class);
        var previousStatus = AvtalsstatusDto.EJ_BEHANDLAT;
        var avtalsstatus = AvtalsstatusDto.AVTAL_SKICKAT;
        var fastighetServiceSpy = Mockito.spy(fastighetService);

        doNothing().when(fastighetServiceSpy).setGeometristatus(any(), any(), any());
        when(mockMarkagareService.getAgareForFastighet(any(), any())).thenReturn(
                Arrays.asList(mockMarkagare(), mockMarkagare())
        );

        when(mockFastighetRepository.fastighetAvtalsstatus(any(), any())).thenReturn(previousStatus);
        when(mockProjektRepository.projektIdByVersionId(any())).thenReturn(Optional.of(projektId));

        // When
        fastighetServiceSpy.setAvtalsstatus(fastighetId, versionId, avtalsstatus);

        // Then
        Mockito.verify(fastighetServiceSpy).setGeometristatus(eq(fastighetId),
                eq(versionId), eq(GeometristatusDto.OFORANDRAD));
    }*/

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_uppdatera_geometristatus_för_en_fastighet() {
        // Given
        var fastighetId = mock(UUID.class);
        var versionId = mockUUID(6);
        var projektId = mock(UUID.class);
        var geometristatus = GeometristatusDto.BORTTAGEN;
        var previousStatus = GeometristatusDto.OFORANDRAD;

        var geometristatusEntity = new GeometristatusEntity();
        geometristatusEntity.setGeometristatus(previousStatus);

        when(mockGeometristatusRepository.findByFastighetIdAndVersionId(any(), any()))
                .thenReturn(Optional.of(geometristatusEntity));
        when(mockProjektRepository.projektIdByVersionId(any())).thenReturn(Optional.of(projektId));
        when(mockAvtalRepository.findIdByVersionIdAndFastighetId(any(), any())).thenReturn(mock(UUID.class));

        // When
        fastighetService.setGeometristatus(fastighetId, versionId, geometristatus);

        // Then
        assertEquals(geometristatus, geometristatusEntity.getGeometristatus());
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_geometristatus_loggas_när_den_ändras() {
        // Given
        var fastighetId = mock(UUID.class);
        var versionId = mockUUID(6);
        var projektId = mock(UUID.class);
        var fastighetServiceSpy = Mockito.spy(fastighetService);
        var avtalId = mock(UUID.class);

        var geometristatusEntity = new GeometristatusEntity();
        geometristatusEntity.setGeometristatus(GeometristatusDto.UPPDATERAD);

        //doNothing().when(fastighetServiceSpy).setAvtalsstatus(any(), any(), any());

        when(mockGeometristatusRepository.findByFastighetIdAndVersionId(any(), any()))
                .thenReturn(Optional.of(geometristatusEntity));
        when(mockFastighetRepository.fastighetAvtalsstatus(any(), any())).thenReturn(AvtalsstatusDto.AVTAL_SIGNERAT);
        when(mockProjektRepository.projektIdByVersionId(any())).thenReturn(Optional.of(projektId));
        when(mockAvtalRepository.findIdByVersionIdAndFastighetId(any(), any())).thenReturn(avtalId);
        // When
        fastighetServiceSpy.setGeometristatus(fastighetId, versionId, GeometristatusDto.BORTTAGEN);

        // Then
        Mockito.verify(mockAvtalsloggService).logGeometristatus(eq(avtalId), eq(GeometristatusDto.BORTTAGEN));
    }

    @Test
    void så_ska_det_gå_att_hämta_fastigheter_för_ett_projekt() {
        // Given
        var projektId = mockUUID(0);
        var fastighetDto = new FastighetDto();

        when(mockFastighetRepository.getFastigheter(projektId)).thenReturn(Arrays.asList(new FastighetEntity()));

        // When
        var fastigheterActual = fastighetService.getFastigheter(projektId);

        // Then
        verify(mockFastighetRepository).getFastigheter(eq(projektId));

        assertEquals(Arrays.asList(fastighetDto), fastigheterActual);
    }

    private MarkagareDto mockMarkagare() {
        var markagare = new MarkagareDto();
        markagare.setId(mock(UUID.class));
        return markagare;
    }
}
