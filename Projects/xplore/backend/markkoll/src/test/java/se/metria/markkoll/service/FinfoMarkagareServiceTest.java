package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.AvtalspartLabelsDto;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;
import se.metria.markkoll.repository.markagare.MarkagareRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.logging.AvtalsloggService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.testdata.TestData;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.avtalspartEntity;
import static se.metria.markkoll.testdata.TestData.mockAgare;

@MarkkollServiceTest
@DisplayName("Givet AgareService")
public class FinfoMarkagareServiceTest {
    AclService mockAclService;
    AvtalRepository mockAvtalRepository;
    AvtalspartRepository mockAvtalspartRepository;
    MarkagareService markagareService;
    MarkagareRepository mockMarkagareRepository;
    FastighetRepository mockFastighetRepository;
    ProjektRepository mockProjektRepository;
    MarkagareImportService mockMarkagareImportService;
    AvtalsloggService mockAvtalsloggService;
    LoggService mockLoggService;
    ApplicationEventPublisher mockPublisher;

    @Autowired
    ModelMapper modelMapper;

    @BeforeEach
    void before() {
        mockAclService = mock(AclService.class);
        mockMarkagareRepository = mock(MarkagareRepository.class);
        mockFastighetRepository = mock(FastighetRepository.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockMarkagareImportService = mock(MarkagareImportService.class);
        mockAvtalspartRepository = mock(AvtalspartRepository.class);
        mockAvtalsloggService = mock(AvtalsloggService.class);
        mockLoggService = mock(LoggService.class);
        mockPublisher = mock(ApplicationEventPublisher.class);
        markagareService = new MarkagareService(mockAclService, mockAvtalRepository, mockAvtalspartRepository, mockMarkagareRepository,
            mockFastighetRepository, mockProjektRepository, mockLoggService, modelMapper, mockMarkagareImportService,
            mockAvtalsloggService, mockPublisher);
    }

    @Test
    void så_ska_det_gå_att_lägga_till_en_ägare(){
        // Given
        var projektId = mock(UUID.class);
        var newMarkagarInfo = TestData.mockAgareInfo();
        var fastighet = TestData.fastighetEntity();
        var agareExpected = TestData.agareExpect(newMarkagarInfo);

        var avtal = AvtalEntity.builder()
                .projekt(ProjektEntity.builder().id(projektId).build())
                .build();
        var avtal2 = AvtalEntity.builder()
                .projekt(ProjektEntity.builder().id(mock(UUID.class)).build())
                .build();
        var avtalList = Arrays.asList(avtal, avtal2);

        when(mockFastighetRepository.getOne(any())).thenReturn(mock(FastighetEntity.class));
        when(mockAvtalRepository.findByFastighetIdAndKundId(any(), any())).thenReturn(avtalList);

        when(mockAvtalspartRepository.saveAndFlush(any())).then(AdditionalAnswers.returnsFirstArg());
        when(mockMarkagareRepository.saveAndFlush(any())).then(AdditionalAnswers.returnsFirstArg());

        // When
        var agareActual = markagareService.addAgare(projektId, fastighet.getId(), newMarkagarInfo);

        // Then
        /* ID sätts i metoden när vi instansierar en avtalspart, och är inget vi kan kontrollera. Sätt förväntade IDt
        till det genererade IDt. */
        agareExpected.setId(agareActual.getId());
        assertEquals(agareExpected, agareActual);

        verify(mockMarkagareRepository).saveAndFlush(any());
        verify(mockAvtalspartRepository, times(avtalList.size())).saveAndFlush(any());
    }

    @Test
    void så_ska_det_gå_att_ta_bort_ägare(){
        //Given
        var mockAgareId = mock(UUID.class);

        //When
        markagareService.deleteAgare(mockAgareId);

        //Then
        verify(mockAvtalspartRepository).deleteById(mockAgareId);
    }

    @Test
    void så_ska_det_gå_att_hämta_en_lista_med_markägare_för_en_fastighet() {
        // Given
        var kontaktpersonId = mock(UUID.class);
        var projektId = mock(UUID.class);
        var fastighetId = mock(UUID.class);

        var avtalspartOrgnummer = avtalspartEntity().withId(mock(UUID.class));
        avtalspartOrgnummer.getMarkagare().getPerson().setPersonnummer("212000-1777");
        var avtalspartKontaktperson = avtalspartEntity().withId(kontaktpersonId);
        var avtalsparter = Arrays.asList(avtalspartOrgnummer, avtalspartKontaktperson);

        var agareExpect = Arrays.asList(
                markagareExpect(avtalspartOrgnummer, kontaktpersonId, true),
                markagareExpect(avtalspartKontaktperson, kontaktpersonId, false)
        );

        when(mockAvtalspartRepository.findKontaktpersonId(any(), any())).thenReturn(Optional.of(kontaktpersonId));
        when(mockAvtalspartRepository.findByProjektIdAndFastighetId(any(), any())).thenReturn(avtalsparter);

        // When
        var agareActual = markagareService.getAgareForFastighet(projektId, fastighetId);

        // Then
        assertEquals(agareExpect, agareActual);
    }

    @Test
    void så_ska_det_gå_att_uppdatera_ägaruppgifter() {
        // Given
        var avtalspartId = mock(UUID.class);
        var markagare = mockAgare(avtalspartId);

        var avtalspart = new AvtalspartEntity()
                .withId(avtalspartId)
                .withAvtalsstatus(markagare.getAgareStatus())
                .withAvtal(new AvtalEntity());
        when(mockAvtalspartRepository.getOne(any())).thenReturn(avtalspart);

        // When
        var agareActual = markagareService.updateAgare(avtalspartId, markagare);

        // Then
        assertEquals(markagare, agareActual);
        //verify(mockAvtalsloggService, times(0)).logAvtalsstatus(any(), any()); // TODO: fixa
    }

    @Test
    void så_ska_det_gå_avtalsstatus_loggas_när_avtalsstatus_uppdateras() {
        // Given
        var avtalspartId = mock(UUID.class);
        var markagare = mockAgare(avtalspartId);

        var avtalspart = new AvtalspartEntity()
                .withId(avtalspartId)
                .withAvtalsstatus(AvtalsstatusDto.EJ_BEHANDLAT)
                .withAvtal(new AvtalEntity());
        when(mockAvtalspartRepository.getOne(any())).thenReturn(avtalspart);

        // When
        var agareActual = markagareService.updateAgare(avtalspartId, markagare);

        // Then
        assertEquals(markagare, agareActual);
        //verify(mockAvtalsloggService, times(1)).logAvtalsstatus(any(), any()); TODO: fixa
    }

    MarkagareDto markagareExpect(AvtalspartEntity avtalspart, UUID kontaktpersonId, boolean isOrganisation) {
        return new MarkagareDto()
                .id(avtalspart.getId())
                .adress(avtalspart.getMarkagare().getPerson().getAdress())
                .agareStatus(avtalspart.getAvtalsstatus())
                .agartyp(avtalspart.getMarkagare().getAgartyp())
                .andel(avtalspart.getMarkagare().getAndel())
                .bankkonto(avtalspart.getMarkagare().getPerson().getBankkonto())
                .ePost(avtalspart.getMarkagare().getPerson().getEPost())
                .kontaktperson(kontaktpersonId.equals(avtalspart.getId()))
                .labels(new AvtalspartLabelsDto().ofullstandingInformation(true))
                .namn(avtalspart.getMarkagare().getPerson().getNamn())
                .inkluderaIAvtal(avtalspart.isInkluderaIAvtal())
                .fodelsedatumEllerOrgnummer(isOrganisation ? avtalspart.getMarkagare().getPerson().getPersonnummer() : "860304");
    }
}
