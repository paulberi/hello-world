package se.metria.markkoll.service.fastighet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import se.metria.markkoll.config.ModelMapperConfiguration;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.samfallighet.*;
import se.metria.markkoll.openapi.finfo.model.*;
import se.metria.markkoll.openapi.model.SamfallighetDto;
import se.metria.markkoll.openapi.model.BerordAvAtgardDto;
import se.metria.markkoll.openapi.model.PagaendeFastighetsbildningDto;
import se.metria.markkoll.openapi.model.SamfallighetMerInformationDto;
import se.metria.markkoll.openapi.model.RattighetDto;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.SamfallighetMerInfoRepository;
import se.metria.markkoll.repository.fastighet.SamfallighetRepository;
import se.metria.markkoll.service.finfo.FinfoService;
import se.metria.markkoll.service.markagare.StyrelsemedlemService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class SamfServiceTest {
    @InjectMocks
    SamfService samfService;

    @Mock
    FinfoService finfoService;

    @Spy
    ModelMapper modelMapper = new ModelMapperConfiguration().modelMapper();

    @Mock
    SamfServiceWebServiceTemplateConfig samfConfig;

    @Mock
    KundRepository kundRepository;

    @Mock
    SamfallighetRepository samfallighetRepository;

    @Mock
    SamfallighetMerInfoRepository samfallighetMerInfoRepository;

    @Mock
    AvtalRepository avtalRepository;

    @Mock
    StyrelsemedlemService styrelsemedlemService;

    @Captor
    ArgumentCaptor<SamfallighetMerInfoEntity> saveCaptor;

    @Test
    void getSamfMedSamfallighetsforening() {
        // Given
        var kundId = "kundId";
        var samfId = mockUUID(0);

        when(samfallighetRepository.findById(any())).thenReturn(Optional.of(samfallighetEntity()));
        when(samfallighetMerInfoRepository.findBySamfallighetIdAndKundId(any(), any()))
            .thenReturn(Optional.of(samfallighetMerInfoEntity()));

        // When
        var samfDtoActual = samfService.getSamf(kundId, samfId);

        // Then
        var samfDtoExpect = samfallighetDto();
        assertEquals(samfDtoExpect, samfDtoActual);
    }

    @Test
    void getSamfUtanSamfallighetsforening() {
        // Given
        var kundId = "kundId";
        var samfId = mockUUID(0);

        when(samfallighetRepository.findById(any())).thenReturn(Optional.of(samfallighetEntity()));
        when(samfallighetMerInfoRepository.findBySamfallighetIdAndKundId(any(), any()))
            .thenReturn(Optional.empty());

        // When
        var samfDtoActual = samfService.getSamf(kundId, samfId);

        // Then
        var samfDtoExpect = samfallighetDto().merInformation(null);
        assertEquals(samfDtoExpect, samfDtoActual);
    }

    @Test
    void importMoreSamfInfoMedSamfallighetsforening() throws Exception {
        var projektId = mockUUID(0);
        var samfId = mockUUID(1);

        var samf = new SamfallighetEntity();
        when(samfallighetRepository.getReferenceById(any())).thenReturn(samf);

        var kund = new KundEntity();
        when(kundRepository.getKundByProjektId(any())).thenReturn(kund);

        var samfForeningDto = finfoSamfallighetsforeningDto();
        when(finfoService.importSamfallighetsforening(any())).thenReturn(samfForeningDto);

        var samfDto = finfoSamfallighetDto();
        when(finfoService.importRegisterenhet(any())).thenReturn(samfDto);

        // When
        samfService.importMoreSamfInfo(projektId, samfId);

        // Then
        verify(samfallighetMerInfoRepository).save(saveCaptor.capture());
        verifySamfForening(saveCaptor.getValue(), samfForeningDto);
        verifySamf(saveCaptor.getValue(), samfDto);
        assertSame(samf, saveCaptor.getValue().getSamfallighet());
        assertSame(kund, saveCaptor.getValue().getKund());

        verify(styrelsemedlemService).addStyrelsemedlemmar(any(), eq(samfId), any(), eq(samfForeningDto.getStyrelsemedlemmar()));
    }

    @Test
    void importMoreSamfInfoUtanSamfallighetsforening() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var samfId = mockUUID(1);

        var samf = new SamfallighetEntity();
        when(samfallighetRepository.getReferenceById(any())).thenReturn(samf);

        var kund = new KundEntity();
        when(kundRepository.getKundByProjektId(any())).thenReturn(kund);

        var samfDto = finfoSamfallighetDto().forvaltandeBeteckning(null);
        when(finfoService.importRegisterenhet(any())).thenReturn(samfDto);

        // When
        samfService.importMoreSamfInfo(projektId, samfId);

        // Then
        verify(finfoService, never()).importSamfallighetsforening(any());
        verify(samfallighetMerInfoRepository).save(saveCaptor.capture());
        verifySamf(saveCaptor.getValue(), samfDto);
        assertSame(samf, saveCaptor.getValue().getSamfallighet());
        assertSame(kund, saveCaptor.getValue().getKund());

        verify(styrelsemedlemService, never()).addStyrelsemedlemmar(any(), any(), any(), any());
    }

    private void verifySamf(SamfallighetMerInfoEntity samfMerInfoEntity, FinfoSamfallighetDto samfDto) {
        assertEquals(samfDto.getAndamal(), samfMerInfoEntity.getAndamal());

        assertEquals(samfDto.getRattighet().size(), samfMerInfoEntity.getRattighet().size());
        for (int i = 0; i < samfDto.getRattighet().size(); i++) {
            var rattighetDto = samfDto.getRattighet().get(i);
            var rattighet = samfMerInfoEntity.getRattighet().get(i);
            assertEquals(rattighetDto.getRattighetsbeskrivning(), rattighet.getRattighetsbeskrivning());
            assertEquals(rattighetDto.getAktbeteckning(), rattighet.getAktbeteckning());
            assertEquals(rattighetDto.getRattsforhallande(), rattighet.getRattsforhallande());
            assertEquals(rattighetDto.getAndamal(), rattighet.getAndamal());
        }

        assertEquals(samfDto.getBerordAvAtgard().size(), samfMerInfoEntity.getBerordAvAtgard().size());
        assertEquals(samfDto.getForvaltandeBeteckning(), samfMerInfoEntity.getForvaltandeBeteckning());
        for (int i = 0; i < samfDto.getBerordAvAtgard().size(); i++) {
            var atgardDto = samfDto.getBerordAvAtgard().get(i);
            var atgard = samfMerInfoEntity.getBerordAvAtgard().get(i);
            assertEquals(atgardDto.getAktbeteckning(), atgard.getAktbeteckning());
            assertEquals(atgardDto.getAtgardstyp(), atgard.getAtgardstyp());
            assertEquals(atgardDto.getRegistreringsdatum(), atgard.getRegistreringsdatum());
            assertEquals(String.join(" ", atgardDto.getAtgarder()), atgard.getAtgard());
        }

        assertEquals(samfDto.getPagaendeFastighetsbildning().size(), samfMerInfoEntity.getPagaendeFastighetsbildning().size());
        for (int i = 0; i < samfDto.getPagaendeFastighetsbildning().size(); i++) {
            var pgDto = samfDto.getPagaendeFastighetsbildning().get(i);
            var pg = samfMerInfoEntity.getPagaendeFastighetsbildning().get(i);
            assertEquals(pgDto.getArendeDagboksnummer(), pg.getDagboksnummer());
            assertEquals(pgDto.getArendestatus(), pg.getArendestatus());
        }
    }

    private SamfallighetEntity samfallighetEntity() {
        var entity = new SamfallighetEntity();
        entity.setDelagandeFastigheter(Arrays.asList(
            ingaendeFastighetEntity("Delägande fastighet")
        ));
        return entity;
    }

    private SamfallighetMerInfoEntity samfallighetMerInfoEntity() {
        var entity = new SamfallighetMerInfoEntity();
        entity.setFirmatecknare("Firmatecknare");
        entity.setCoNamn("Namn");
        entity.setCoAdress("Adress");
        entity.setCoPostnummer("Postnummer");
        entity.setCoPostort("Postort");
        entity.setAndamal("Ändamål");
        entity.setForeningstyp("Föreningstyp");
        entity.setOrganisationsnummer("Organisationsnummer");
        entity.setForvaltandeBeteckning("Förvaltning");
        entity.setBerordAvAtgard(Arrays.asList(
            new BerordAvAtgard(
                FinfoAtgardstypDto.FASTIGHETSRATTLIG,
                LocalDate.of(1234, 5, 6),
                "Aktbeteckning",
                "Åtgärd"
            )
        ));
        entity.setPagaendeFastighetsbildning(Arrays.asList(
            new PagaendeFastighetsbildning("Arendestatus", "Dagboksnummer")
        ));
        entity.setRattighet(Arrays.asList(
            new Rattighet("Ändamål", "Rättsförhållande", "Aktbeteckning", "Rättighetsbeskrivning")
        ));
        return entity;
    }

    private SamfallighetIngaendeFastighetEntity ingaendeFastighetEntity(String fastighetsbeteckning) {
        var ingaendeFastighet = new SamfallighetIngaendeFastighetEntity();
        ingaendeFastighet.setFastighetsbeteckning(fastighetsbeteckning);
        return ingaendeFastighet;
    }

    private FinfoSamfallighetsforeningDto finfoSamfallighetsforeningDto() {
        return new FinfoSamfallighetsforeningDto()
            .firmatecknare("Firmatecknare")
            .foreningstyp("Föreningstyp")
            .orgnr("Orgnr")
            .coNamn("Namn")
            .coAdress("Adress")
            .coPostnummer("12345")
            .coPostort("Postort")
            .styrelsemedlemmar(new ArrayList<>());
    }

    private FinfoSamfallighetDto finfoSamfallighetDto() {
        return new FinfoSamfallighetDto()
            .andamal("Ändamål")
            .forvaltandeBeteckning("Lillpite bys samfällighetsförening")
            .rattighet(Arrays.asList(
                new FinfoRattighetDto()
                    .andamal("Väg")
                    .rattsforhallande("Last")
                    .aktbeteckning("2361-03/9.1")
                    .rattighetsbeskrivning("Bla bla bla")
            ))
            .berordAvAtgard(Arrays.asList(
                new FinfoBerordAvAtgardDto()
                    .aktbeteckning("23-SVJ-1302")
                    .atgardstyp(FinfoAtgardstypDto.FASTIGHETSRATTLIG)
                    .atgarder(Arrays.asList("Fastighetsbestämning"))
                    .registreringsdatum(LocalDate.of(1980, 3, 28))
            ))
            .pagaendeFastighetsbildning(Arrays.asList(
                new FinfoPagaendeFastighetsbildningDto()
                    .arendeDagboksnummer("21 3023")
                    .arendestatus("Lantmäteriförrättning pågår")
            ))
            .rattighet(Arrays.asList(
                new FinfoRattighetDto()
                    .andamal("Ändamål")
                    .rattsforhallande("Rättsförhållande")
                    .aktbeteckning("Aktbeteckning")
                    .rattighetsbeskrivning("Rättighetsbeskrivning")
            ));
    }

    private SamfallighetDto samfallighetDto() {
        return new SamfallighetDto()
            .delagandeFastigheter(Arrays.asList("Delägande fastighet"))
            .merInformation(new SamfallighetMerInformationDto()
                .firmatecknare("Firmatecknare")
                .coNamn("Namn")
                .coAdress("Adress")
                .coPostnummer("Postnummer")
                .coPostort("Postort")
                .forvaltandeBeteckning("Förvaltning")
                .andamal("Ändamål")
                .foreningstyp("Föreningstyp")
                .organisationsnummer("Organisationsnummer")
                .berordAvAtgard(Arrays.asList(
                    new BerordAvAtgardDto()
                        .aktbeteckning("Aktbeteckning")
                        .atgard("Åtgärd")
                        .atgardstyp("FASTIGHETSRATTLIG")
                        .registreringsdatum(LocalDate.of(1234, 5, 6))
                ))
                .pagaendeFastighetsbildning(Arrays.asList(
                    new PagaendeFastighetsbildningDto()
                        .arendestatus("Arendestatus")
                        .dagboksnummer("Dagboksnummer")
                ))
                .rattighet(Arrays.asList(
                    new RattighetDto()
                        .aktbeteckning("Aktbeteckning")
                        .andamal("Ändamål")
                        .rattighetsbeskrivning("Rättighetsbeskrivning")
                        .rattsforhallande("Rättsförhållande")
                ))
                .styrelsemedlemmar(new ArrayList<>())
            );
    }

    void verifySamfForening(SamfallighetMerInfoEntity samfMerInfoEntity,
                            FinfoSamfallighetsforeningDto samfForeningDto) {

        assertEquals(samfForeningDto.getFirmatecknare(), samfMerInfoEntity.getFirmatecknare());
        assertEquals(samfForeningDto.getForeningstyp(), samfMerInfoEntity.getForeningstyp());
        assertEquals(samfForeningDto.getOrgnr(), samfMerInfoEntity.getOrganisationsnummer());
        assertEquals(samfForeningDto.getCoNamn(), samfMerInfoEntity.getCoNamn());
        assertEquals(samfForeningDto.getCoAdress(), samfMerInfoEntity.getCoAdress());
        assertEquals(samfForeningDto.getCoPostnummer(), samfMerInfoEntity.getCoPostnummer());
        assertEquals(samfForeningDto.getCoPostort(), samfMerInfoEntity.getCoPostort());
    }
}