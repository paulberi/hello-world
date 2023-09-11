package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.HaglofImportVarningarDto;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.service.haglof.HaglofImportService;
import se.metria.markkoll.service.haglof.HaglofJsonImportService;
import se.metria.markkoll.service.haglof.model.*;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.util.FilesUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@DisplayName("Givet HaglofJsonImportServiceTest")
@MarkkollServiceTest
public class HaglofJsonImportServiceTest {
    HaglofJsonImportService haglofJsonImportService;

    AvtalRepository mockAvtalRepository;
    HaglofImportService mockHaglofImportService;
    MarkagareService mockMarkagareService;

    UUID projektId = mockUUID(0);

    @BeforeEach
    void beforeEach() {
        mockAvtalRepository = mock(AvtalRepository.class);
        mockHaglofImportService = mock(HaglofImportService.class);
        mockMarkagareService = mock(MarkagareService.class);

        haglofJsonImportService = new HaglofJsonImportService(mockAvtalRepository, mockHaglofImportService,
            mockMarkagareService);
    }

    @Test
    void ska_kasta_ett_markollexception_vid_ogiltig_json() {
        // When / Then
        var exception = assertThrows(MarkkollException.class,
            () -> haglofJsonImportService.importJson(projektId, "ogiltig json"));

        assertEquals(MarkkollError.HAGLOF_JSON_INVALID.getMessage(), exception.getMessage());
    }

    @Test
    void ska_importera_en_json() throws Exception {
        // Given
        var json = new ClassPathResource("hms/haglof_import.json");
        var avtalId = mockUUID(1);
        var avtalspartId = mockUUID(2);

        var haglofImport = getHaglofImport();

        when(mockAvtalRepository.getIdByFastighetsbeteckningAndKommun(eq(projektId), any(), any()))
            .thenReturn(avtalId);
        when(mockMarkagareService.getAgareForAvtal(eq(avtalId))).thenReturn(Arrays.asList(
            new MarkagareDto().id(avtalspartId).fodelsedatumEllerOrgnummer("19860304-8979").namn("a"),
            new MarkagareDto().id(mockUUID(3)).fodelsedatumEllerOrgnummer("12345678-9012").namn("b")
        ));

        // When
        haglofJsonImportService.importJson(projektId, FilesUtil.read(json));

        // Then
        verify(mockHaglofImportService).importProjekt(eq(projektId), eq(haglofImport.getMetadata()));
        verify(mockHaglofImportService, times(numOfAgare(haglofImport))).importMarkagare(any(), any());

        for (var fastighet: haglofImport.getFastigheter()) {
            verify(mockHaglofImportService).importAvtal(eq(avtalId), eq(fastighet));
            verify(mockHaglofImportService).importVp(eq(avtalId), eq(fastighet), eq(haglofImport.getMetadata()));

            for (var owner: fastighet.getOwners()) {
                verify(mockHaglofImportService).importMarkagare(eq(avtalspartId), eq(owner));
            }
        }
    }

    @Test
    void ska_returnera_varningar_om_fastigheter_i_importfilen_saknas_i_markkoll() throws Exception {
        // Given
        var json = new ClassPathResource("hms/haglof_import.json");

        when(mockAvtalRepository.getIdByFastighetsbeteckningAndKommun(any(), any(), any()))
            .thenReturn(null);

        var warningsExpect = new HaglofImportVarningarDto();
        warningsExpect.addFastigheterMissingItem("Fastighet PITEÅ");

        // When
        var warningsActual = haglofJsonImportService.importJson(projektId, FilesUtil.read(json));

        // Then
        assertEquals(warningsExpect, warningsActual);
    }

    @Test
    void ska_returnera_varningar_markägare_i_importfilen_saknas_i_markkoll() throws Exception {
        // Given
        var json = new ClassPathResource("hms/haglof_import.json");
        var avtalId = mockUUID(1);

        when(mockAvtalRepository.getIdByFastighetsbeteckningAndKommun(eq(projektId), any(), any()))
            .thenReturn(avtalId);
        when(mockMarkagareService.getAgareForAvtal(eq(avtalId))).thenReturn(new ArrayList<>());

        var warningsExpect = new HaglofImportVarningarDto();
        warningsExpect.addAgareMissingItem("Christoffer Karlsson");

        // When
        var warningsActual = haglofJsonImportService.importJson(projektId, FilesUtil.read(json));

        // Then
        assertEquals(warningsExpect, warningsActual);
    }

    @Test
    void ska_returnera_varning_om_det_finns_flera_agare_med_samma_personuppgifter() throws Exception {
        // Given
        var json = new ClassPathResource("hms/haglof_import.json");
        var avtalId = mockUUID(1);

        when(mockAvtalRepository.getIdByFastighetsbeteckningAndKommun(eq(projektId), any(), any()))
            .thenReturn(avtalId);
        when(mockMarkagareService.getAgareForAvtal(eq(avtalId))).thenReturn(Arrays.asList(
            new MarkagareDto().namn("Christoffer Karlsson").fodelsedatumEllerOrgnummer("19860304-8979"),
            new MarkagareDto().namn("Christoffer Karlsson").fodelsedatumEllerOrgnummer("19860304-8979")
        ));

        var warningsExpect = new HaglofImportVarningarDto();
        warningsExpect.addAgareAmbiguousItem("Christoffer Karlsson");

        // When
        var warningsActual = haglofJsonImportService.importJson(projektId, FilesUtil.read(json));

        // Then
        assertEquals(warningsExpect.getAgareAmbiguous(), warningsActual.getAgareAmbiguous());
    }

    @Test
    void ska_returnera_varning_om_kommunnamn_saknas_på_en_fastighet() throws Exception {
        // Given
        var json = new ClassPathResource("hms/haglof_import_kommun_missing.json");
        var avtalId = mockUUID(1);

        when(mockAvtalRepository.getIdByFastighetsbeteckningAndKommun(eq(projektId), any(), any()))
            .thenReturn(avtalId);
        when(mockMarkagareService.getAgareForAvtal(eq(avtalId))).thenReturn(Arrays.asList(
            new MarkagareDto().namn("Christoffer Karlsson").fodelsedatumEllerOrgnummer("19860304-8979"),
            new MarkagareDto().namn("Christoffer Karlsson").fodelsedatumEllerOrgnummer("19860304-8979")
        ));

        var warningsExpect = new HaglofImportVarningarDto();
        warningsExpect.addFastighetKommunMissingItem("Fastighet");

        // When
        var warningsActual = haglofJsonImportService.importJson(projektId, FilesUtil.read(json));

        // Then
        assertEquals(warningsExpect, warningsActual);
    }

    private int numOfAgare(HaglofImport haglofImport) {
        return haglofImport.getFastigheter().stream()
            .map(f -> f.getOwners().size())
            .reduce((acc, n) -> acc + n)
            .orElse(0);
    }

    private HaglofImport getHaglofImport() {
        var haglofImport = new HaglofImport();

        var fastighet = new HaglofFastighet();
        fastighet.setKommun("Piteå");
        fastighet.setFastighetsbeteckning("Fastighet");
        fastighet.setFastighetsnummer("10");
        fastighet.getEvaluation().setHighcutvalue(4511);
        fastighet.setTillvaratagande(Tillvaratagande.EGEN_REGI);
        fastighet.getEvaluation().setRotvalue(5651);
        fastighet.getEvaluation().setBordertreevalue(33);
        fastighet.getEvaluation().setLandvalue(9769);
        fastighet.getEvaluation().setEarlycutvalue(5591);
        fastighet.getEvaluation().setStormdryvalue(446);

        var owner = new HaglofOwner();
        owner.setNamn("Christoffer Karlsson");
        owner.setEPost("en@email.se");
        owner.setTelefonHem("000-000-000");
        owner.setTelefonArbete("0534-190 48");
        owner.setMobiltelefon("0123-456");
        owner.setBankgiro("Bankgiro");
        owner.setPlusgiro("Plusgiro");
        owner.setBankkonto("Bankaccount");
        owner.setPersonnummer("19860304-8979");

        var metadata = new HaglofMetadata();
        metadata.setProjektnummer("Projektnummer");
        metadata.setVarderingstidpunkt(LocalDateTime.of(2022, 2, 22, 22, 22));
        metadata.setVarderingsmanOchForetag("Christoffer Karlsson");

        haglofImport.setMetadata(metadata);
        fastighet.getOwners().add(owner);
        haglofImport.getFastigheter().add(fastighet);

        return haglofImport;
    }
}
