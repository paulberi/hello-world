package se.metria.markkoll.service.vardering;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.AuditorAware;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.utskick.UtskickVpDto;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollGeneratorService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;
import se.metria.markkoll.util.MSE_CompareExcelFiles;
import se.metria.markkoll.util.vardering.ElnatErsattningDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollServiceTest
@DisplayName("Givet ElnatVarderingsprotokollGeneratorService")
public class ElnatVarderingsprotokollGeneratorServiceTest {
    VarderingsprotokollGeneratorService<ElnatVarderingsprotokollDto, ElnatErsattningDto> varderingsprotokollGeneratorService;

    AuditorAware<String> mockAuditorAware;
    AvtalService mockAvtalService;
    ElnatVarderingsprotokollService mockElnatVarderingsprotokollService;
    FastighetService mockFastighetService;
    ProjektRepository mockProjektRepository;

    UUID avtalId = mockUUID(0);
    String ort = "Ort";
    String fastighetsbeteckning = "fastighet";
    ElnatVarderingsprotokollDto vp = new ElnatVarderingsprotokollDto();

    AvtalMetadataDto avtalMetadata = new AvtalMetadataDto()
            .markslag("Markslag");

    final MarkagareDto markagare = new MarkagareDto()
        .id(mockUUID(2))
        .namn("Markägaren Ägarsson")
        .inkluderaIAvtal(true)
        .adress("Ägargatan 73")
        .postnummer("12345")
        .postort("Postort")
        .fodelsedatumEllerOrgnummer("1234-5678")
        .agartyp(AgartypDto.LF)
        .andel("1/1")
        .telefon("555-0451")
        .kontaktperson(true)
        .agareStatus(AvtalsstatusDto.AVTAL_SKICKAT)
        .bankkonto("111-222-333")
        .ePost("agaren@metria.se");

    final String lastModifiedBy = "MARK_KOLL";

    @BeforeEach
    void beforeEach() {
        mockAvtalService = mock(AvtalService.class);
        mockElnatVarderingsprotokollService = mock(ElnatVarderingsprotokollService.class);
        mockFastighetService = mock(FastighetService.class);
        mockProjektRepository = mock(ProjektRepository.class);

        mockAuditorAware = mock(AuditorAware.class);
        when(mockAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(lastModifiedBy));

        varderingsprotokollGeneratorService = new ElnatVarderingsprotokollGeneratorService(mockAuditorAware,
            mockAvtalService, mockFastighetService, mockProjektRepository, mockElnatVarderingsprotokollService);
    }

    @Test
    void så_ska_det_gå_att_generera_ett_värderingsprotokoll() throws Exception {
        // Given
        var utskick = UtskickVpDto.builder()
            .signatarer(Arrays.asList(markagare))
            .kontaktperson(Optional.of(markagare))
            .build();

        when(mockProjektRepository.getUppdragsnummerAvtal(eq(avtalId))).thenReturn("uppdragsnummer");
        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(Optional.of(vp));
        when(mockFastighetService.getFastighet(eq(avtalId))).thenReturn(new FastighetDto()
            .fastighetsbeteckning(fastighetsbeteckning).kommunnamn(ort.toUpperCase()));
        when(mockAvtalService.getAvtalMetadata(eq(avtalId))).thenReturn(avtalMetadata);

        // When
        var vpFiles = varderingsprotokollGeneratorService.generateVarderingsprotokoll(avtalId,
            utskick);

        // Then
        assertEquals(1, vpFiles.size());
        assertXlsxEqual("vp.xlsx", vpFiles.get(0));
    }

    @Test
    void så_ska_det_gå_att_generera_ett_värderingsprotokoll_som_saknar_kontaktperson() throws Exception {
        // Given
        var utskick = UtskickVpDto.builder()
            .signatarer(Arrays.asList(markagare))
            .kontaktperson(Optional.empty())
            .build();

        when(mockProjektRepository.getUppdragsnummerAvtal(eq(avtalId))).thenReturn("uppdragsnummer");
        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(Optional.of(vp));
        when(mockFastighetService.getFastighet(eq(avtalId))).thenReturn(new FastighetDto()
            .fastighetsbeteckning(fastighetsbeteckning).kommunnamn(ort.toUpperCase()));
        when(mockAvtalService.getAvtalMetadata(eq(avtalId))).thenReturn(avtalMetadata);


        // When
        var vpFiles = varderingsprotokollGeneratorService.generateVarderingsprotokoll(avtalId,
            utskick);

        // Then
        assertEquals(1, vpFiles.size());
        assertXlsxEqual("no_kontaktperson.xlsx", vpFiles.get(0));
    }

    @Test
    void så_ska_det_gå_att_generera_flera_värderingsprotokoll_för_fastigheter_med_många_ägare() throws Exception {
        // Given
        var agare = Arrays.asList(
            // vp1
            markagare, markagare, markagare, markagare, markagare, markagare, markagare, markagare,

            // vp2
            markagare
        );

        var utskick = UtskickVpDto
            .builder()
            .signatarer(agare)
            .kontaktperson(Optional.of(markagare))
            .build();

        when(mockProjektRepository.getUppdragsnummerAvtal(eq(avtalId))).thenReturn("uppdragsnummer");
        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(Optional.of(vp));
        when(mockFastighetService.getFastighet(eq(avtalId))).thenReturn(new FastighetDto()
            .fastighetsbeteckning(fastighetsbeteckning).kommunnamn(ort.toUpperCase()));
        when(mockAvtalService.getAvtalMetadata(eq(avtalId))).thenReturn(avtalMetadata);

        // When
        var vpFiles = varderingsprotokollGeneratorService.generateVarderingsprotokoll(avtalId,
            utskick);

        // Then
        assertEquals(2, vpFiles.size());
        assertXlsxEqual("vp_agare_1.xlsx", vpFiles.get(0));
        assertXlsxEqual("vp_agare_2.xlsx", vpFiles.get(1));
    }

    @Test
    void så_ska_värderingsprotokoll_genereras_för_fastigheter_som_saknar_ägare() throws Exception {
        // Given
        var utskick = new UtskickVpDto();

        when(mockProjektRepository.getUppdragsnummerAvtal(eq(avtalId))).thenReturn("uppdragsnummer");
        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(Optional.of(vp));
        when(mockFastighetService.getFastighet(eq(avtalId))).thenReturn(new FastighetDto()
            .fastighetsbeteckning(fastighetsbeteckning).kommunnamn(ort.toUpperCase()));
        when(mockAvtalService.getAvtalMetadata(eq(avtalId))).thenReturn(avtalMetadata);

        // When
        var vpFiles = varderingsprotokollGeneratorService.generateVarderingsprotokoll(avtalId,
            utskick);

        // Then
        assertEquals(1, vpFiles.size());
        assertXlsxEqual("no_agare.xlsx", vpFiles.get(0));
    }

    @Test
    void så_ska_inget_returnas_om_avtalet_saknar_ett_värderingsprotokoll() throws Exception {
        // Given
        when(mockElnatVarderingsprotokollService.getWithAvtalId(avtalId)).thenReturn(Optional.empty());

        // When
        var vpFile = varderingsprotokollGeneratorService.generateVarderingsprotokoll(avtalId,
            new UtskickVpDto());

        // Then
        verify(mockElnatVarderingsprotokollService).getWithAvtalId(eq(avtalId));

        assertEquals(new ArrayList<>(), vpFile);
    }

    @Test
    void så_ska_det_gå_att_hämta_ersättning_för_ett_värderingsprotokoll() throws Exception {
        // Given
        var avtalId = mockUUID(0);
        var ersattningExpect = new ElnatErsattningDto();
        ersattningExpect.setRotnetto(432.);
        ersattningExpect.setTotal(2545);
        var vp = new ElnatVarderingsprotokollDto().rotnetto(432.);

        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(Optional.of(vp));

        // When
        var ersattning = varderingsprotokollGeneratorService.getErsattning(avtalId);

        // Then
        assertEquals(Optional.of(ersattningExpect), ersattning);
    }

    @Test
    void så_ska_ingen_ersättning_returneras_om_värderingsprotokoll_saknas() throws Exception {
        // Given
        var avtalId = mockUUID(0);

        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(Optional.empty());

        // When
        var ersattning = varderingsprotokollGeneratorService.getErsattning(avtalId);

        // Then
        assertEquals(Optional.empty(), ersattning);
    }

    private void assertXlsxEqual(String expectFileName, Resource actual) throws IOException {
        var expect = new ClassPathResource("varderingsprotokoll/elnatVpGenerator/" + expectFileName);
        MSE_CompareExcelFiles.verifyDataInExcelBook(
            new XSSFWorkbook(expect.getInputStream()),
            new XSSFWorkbook(actual.getInputStream()),
            Arrays.asList(0)
        );
    }
}
