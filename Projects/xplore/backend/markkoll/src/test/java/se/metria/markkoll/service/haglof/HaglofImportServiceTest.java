package se.metria.markkoll.service.haglof;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.service.projekt.ElnatProjektService;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.avtal.AvtalDto;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.haglof.model.HaglofFastighet;
import se.metria.markkoll.service.haglof.model.HaglofMetadata;
import se.metria.markkoll.service.haglof.model.HaglofOwner;
import se.metria.markkoll.service.haglof.model.Tillvaratagande;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService.ELEKTRISK_STARKSTROMSLEDNING;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class HaglofImportServiceTest {
    AvtalService mockAvtalService;
    ElnatVarderingsprotokollService mockElnatVarderingsprotokollService;
    MarkagareService mockMarkagareService;
    ElnatProjektService mockElnatProjektService;

    HaglofImportService haglofImportService;

    @BeforeEach
    void beforeEach() {
        mockAvtalService = mock(AvtalService.class);
        mockElnatVarderingsprotokollService = mock(ElnatVarderingsprotokollService.class);
        mockMarkagareService = mock(MarkagareService.class);
        mockElnatProjektService = mock(ElnatProjektService.class);

        haglofImportService = new HaglofImportService(mockAvtalService, mockElnatVarderingsprotokollService,
            mockMarkagareService, mockElnatProjektService);
    }

    @ParameterizedTest
    @MethodSource("importAvtalArgs")
    void så_ska_det_gå_att_importera_avtal(Tillvaratagande tillvaratagande,
                                           TillvaratagandeTypDto tillvaratagandeTyp) {

        // Given
        var avtalId = mockUUID(0);
        var avtal = new AvtalDto();
        var egetTillvaratagandeValue = 1234;

        var haglofFastighet = haglofFastighet(tillvaratagande, egetTillvaratagandeValue);

        when(mockAvtalService.getAvtal(eq(avtalId))).thenReturn(avtal);

        // When
        haglofImportService.importAvtal(avtalId, haglofFastighet);

        // Then
        var avtalExpect = avtalExpect(tillvaratagandeTyp, true, egetTillvaratagandeValue);
        verify(mockAvtalService).updateAvtal(eq(avtalExpect));
    }

    @Test
    void så_ska_det_gå_att_importera_värderingsprotokoll() {
        // Given
        var avtalId = mockUUID(0);
        var haglofFastighet = haglofFastighet();
        var haglofMetadata = haglofMetadata();

        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId)))
            .thenReturn(Optional.of(new ElnatVarderingsprotokollDto().metadata(new ElnatVarderingsprotokollMetadataDto())));

        // When
        haglofImportService.importVp(avtalId, haglofFastighet, haglofMetadata);

        // Then
        var vpExpect = vpExpect(haglofFastighet, haglofMetadata);

        verify(mockElnatVarderingsprotokollService).update(vpExpect);
    }

    @ParameterizedTest
    @MethodSource("importMarkagareArgs")
    void så_ska_det_gå_att_importera_markägare(HaglofOwner haglofOwner,
                                               MarkagareDto markagare,
                                               MarkagareDto markagareExpect) {

        // Given
        var avtalspartId = mockUUID(0);

        when(mockMarkagareService.getAgare(eq(avtalspartId))).thenReturn(markagare);

        // When
        haglofImportService.importMarkagare(avtalspartId, haglofOwner);

        // Then
        verify(mockMarkagareService).updateAgare(eq(avtalspartId), eq(markagareExpect));
    }

    private HaglofFastighet haglofFastighet() {
        var haglofFastighet = new HaglofFastighet();
        haglofFastighet.setFastighetsnummer("Fastighetsnummer");
        haglofFastighet.getEvaluation().setRotvalue(1234);
        haglofFastighet.getEvaluation().setBordertreevalue(1);
        haglofFastighet.getEvaluation().setLandvalue(2);
        haglofFastighet.getEvaluation().setEarlycutvalue(3);
        haglofFastighet.getEvaluation().setStormdryvalue(4);
        return haglofFastighet;
    }

    private HaglofMetadata haglofMetadata() {
        var haglofMetadata = new HaglofMetadata();
        haglofMetadata.setVarderingsmanOchForetag("Christoffer Karlsson");
        haglofMetadata.setVarderingstidpunkt(LocalDateTime.of(2022, 2, 2, 2, 2));
        return haglofMetadata;
    }

    private ElnatVarderingsprotokollDto vpExpect(HaglofFastighet haglofFastighet, HaglofMetadata haglofMetadata) {
        var evaluation = haglofFastighet.getEvaluation();
        var ersattning = evaluation.getBordertreevalue() + evaluation.getLandvalue() + evaluation.getEarlycutvalue() +
            evaluation.getStormdryvalue();
        var vpExpect = new ElnatVarderingsprotokollDto()
            .metadata(
                new ElnatVarderingsprotokollMetadataDto()
                    .varderingsmanOchForetag(haglofMetadata.getVarderingsmanOchForetag())
                    .varderingstidpunkt(haglofMetadata.getVarderingstidpunkt())
                    .fastighetsnummer(haglofFastighet.getFastighetsnummer())
            )
            .rotnetto(haglofFastighet.getEvaluation().getRotvalue().doubleValue())
            .ledningSkogsmark(Arrays.asList(new ElnatLedningSkogsmarkDto()
                .beskrivning(ELEKTRISK_STARKSTROMSLEDNING)
                .ersattning(ersattning)
            ));

        return vpExpect;
    }

    private static Stream<Arguments> importAvtalArgs() {
        return Stream.of(
            Arguments.of(Tillvaratagande.EGEN_REGI, TillvaratagandeTypDto.EGET_TILLVARATAGANDE),
            Arguments.of(Tillvaratagande.FORSALJNING, TillvaratagandeTypDto.ROTNETTO),
            Arguments.of(Tillvaratagande.EJ_VALT, TillvaratagandeTypDto.EJ_BESLUTAT)
        );
    }

    private static Stream<Arguments> importMarkagareArgs() {
        return Stream.of(
            Arguments.of(
                haglofOwner("bankgiro", "plusgiro", "bankaccount", "phonehome", "phonework", "mobilephone", "email"),
                markagare(null, null, null),
                markagare("bankgiro", "phonehome", "email")
            ),
            Arguments.of(
                haglofOwner("bankgiro", "plusgiro", "bankaccount", "phonehome", "phonework", "mobilephone", "email"),
                markagare("bankkonto", "telefon", "ePost"),
                markagare("bankkonto", "telefon", "ePost")
            ),
            Arguments.of(
                haglofOwner("", "plusgiro", "bankaccount", "", "phonework", "mobilephone", "email"),
                markagare(null, null, null),
                markagare("plusgiro", "phonework", "email")
            ),
            Arguments.of(
                haglofOwner("", "", "bankaccount", "", "", "mobilephone", "email"),
                markagare(null, null, null),
                markagare("bankaccount", "mobilephone", "email")
            )
        );
    }

    private static HaglofOwner
    haglofOwner(String bankgiro, String plusgiro, String bankkonto, String telefonHem, String telefonArbete,
                String mobiltelefon, String ePost) {

        var haglofOwner = new HaglofOwner();
        haglofOwner.setBankgiro(bankgiro);
        haglofOwner.setPlusgiro(plusgiro);
        haglofOwner.setBankkonto(bankkonto);
        haglofOwner.setTelefonHem(telefonHem);
        haglofOwner.setTelefonArbete(telefonArbete);
        haglofOwner.setMobiltelefon(mobiltelefon);
        haglofOwner.setEPost(ePost);
        return haglofOwner;
    }

    private static MarkagareDto
    markagare(String bankkonto, String telefon, String ePost) {
        return new MarkagareDto()
            .bankkonto(bankkonto)
            .telefon(telefon)
            .ePost(ePost);
    }

    private HaglofFastighet haglofFastighet(Tillvaratagande tillvaratagande, int egetTillvaratagandeValue) {
        var haglofFastighet = new HaglofFastighet();
        haglofFastighet.setTillvaratagande(tillvaratagande);
        haglofFastighet.getEvaluation().setHighcutvalue(egetTillvaratagandeValue);
        haglofFastighet.setFastighetsnummer("10");
        return haglofFastighet;
    }

    private AvtalDto avtalExpect(TillvaratagandeTypDto tillvaratagandeTyp,
                                 boolean skogsfastighet,
                                 int egetTillvaratagandeValue) {

        var avtalExpect = new AvtalDto();
        avtalExpect.setTillvaratagandeTyp(tillvaratagandeTyp);
        avtalExpect.setSkogsfastighet(skogsfastighet);
        avtalExpect.setEgetTillvaratagande(egetTillvaratagandeValue);
        return avtalExpect;
    }
}