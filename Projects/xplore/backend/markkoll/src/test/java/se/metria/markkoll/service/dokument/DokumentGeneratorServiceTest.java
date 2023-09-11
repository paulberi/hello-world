package se.metria.markkoll.service.dokument;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.openapi.model.FastighetsfilterDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.dokument.DokumentmallRepository;
import se.metria.markkoll.repository.fil.FilRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.DokumentConverterService;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.service.dokument.bindings.MarkkollBindingsService;
import se.metria.markkoll.service.dokument.bindings.data.MarkkollBindings;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.utskick.UtskickDto;
import se.metria.markkoll.service.utskick.UtskickService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;
import se.metria.markkoll.util.dokument.dokumentgenerator.DokumentGenerator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollServiceTest
@DisplayName("Givet DokumentService")
public class DokumentGeneratorServiceTest {

    DokumentGeneratorService dokumentGeneratorService;

    DokumentmallService mockDokumentmallService;
    FastighetService mockFastighetService;
    ProjektRepository mockProjektRepository;
    DokumentmallRepository mockDokumentmallRepository;
    AvtalRepository mockAvtalRepository;
    DokumentGenerator mockDokumentGenerator;
    DokumentConverterService mockDokumentConverterService;
    FilRepository mockFilRepository;
    FilService mockFilService;
    HMSGeneratorService mockHMSGeneratorService;
    Clock mockClock;
    MarkkollBindingsService mockMarkkollBindingsService;
    UtskickService mockUtskickService;

    @BeforeEach
    void before() {
        mockDokumentmallService = mock(DokumentmallService.class);
        mockFilService = mock(FilService.class);
        mockFastighetService = mock(FastighetService.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockDokumentGenerator = mock(DokumentGenerator.class);
        mockDokumentConverterService = mock(DokumentConverterService.class);
        mockDokumentmallRepository = mock(DokumentmallRepository.class);
        mockFilRepository = mock(FilRepository.class);
        mockHMSGeneratorService = mock(HMSGeneratorService.class);
        mockMarkkollBindingsService = mock(MarkkollBindingsService.class);
        mockUtskickService = mock(UtskickService.class);

        var date = LocalDate.of(2022, 01, 24);
        mockClock = Clock.fixed(date.atStartOfDay(ZoneId.systemDefault()).toInstant(),ZoneId.systemDefault());

        dokumentGeneratorService = new DokumentGeneratorService(mockFilService, mockDokumentmallService,
            mockProjektRepository, mockAvtalRepository, mockDokumentGenerator, mockDokumentConverterService,
            mockMarkkollBindingsService, mockHMSGeneratorService, mockClock, mockUtskickService);
    }

    @Test
    void så_ska_det_gå_att_generera_ett_avtal() throws Exception {
        // Given
        var avtalId = mockUUID(0);
        var utskick = new UtskickDto();
        var dokumentmallId = mockUUID(2);
        var mall = new ByteArrayResource("mall".getBytes());
        var bindings = new MarkkollBindings();
        var avtalExpect = new ByteArrayResource("avtal".getBytes());
        var pdfExpect = new ByteArrayResource("pdf".getBytes());

        when(mockDokumentmallService.getFileData(eq(dokumentmallId))).thenReturn(mall);
        when(mockMarkkollBindingsService.getBindings(eq(avtalId), eq(utskick))).thenReturn(bindings);
        when(mockDokumentGenerator.docxFromTemplate(eq(mall), eq(bindings))).thenReturn(avtalExpect);
        when(mockDokumentConverterService.convertToPdf(eq(avtalExpect))).thenReturn(pdfExpect);

        // When
        var pdfActual = dokumentGeneratorService.getAvtal(avtalId, dokumentmallId, utskick);

        // Then
        verify(mockDokumentmallService).getFileData(eq(dokumentmallId));
        verify(mockMarkkollBindingsService).getBindings(eq(avtalId), eq(utskick));
        verify(mockDokumentGenerator).docxFromTemplate(eq(mall), eq(bindings));
        verify(mockDokumentConverterService).convertToPdf(eq(avtalExpect));

        assertEquals(pdfExpect, pdfActual);
    }

    @Test
    void så_ska_det_gå_att_generera_ett_informationsbrev_för_en_fastighet() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var avtalId = mockUUID(2);
        var mall = new ByteArrayResource("mall".getBytes());
        var utskick = new UtskickDto();
        var bindings = new MarkkollBindings();
        var docx = new ByteArrayResource("docx".getBytes());
        var pdf = new ByteArrayResource("pdf".getBytes());
        var dokumentId = mockUUID(4);

        when(mockDokumentmallService.getFileData(eq(dokumentId))).thenReturn(mall);
        when(mockAvtalRepository.getIdByProjektIdAndFastighetId(eq(projektId), eq(fastighetId))).thenReturn(avtalId);
        when(mockUtskickService.getInfobrevUtskick(eq(avtalId))).thenReturn(utskick);
        when(mockMarkkollBindingsService.getBindings(eq(avtalId), eq(utskick))).thenReturn(bindings);

        when(mockDokumentGenerator.docxFromTemplate((Resource) any(), any())).thenReturn(docx);
        when(mockDokumentConverterService.convertToPdf(eq(docx))).thenReturn(pdf);

        // When
        var pdfActual = dokumentGeneratorService.getInfobrev(projektId, fastighetId, dokumentId);

        // Then
        verify(mockDokumentGenerator).docxFromTemplate((Resource) any(), any());
        verify(mockDokumentConverterService).convertToPdf(eq(docx));

        assertEquals(pdf, pdfActual);
    }

    @Test
    void så_ska_det_gå_att_göra_haglof_export_för_enskilda_avtal() throws Exception {
        // Given
        var avtalId = mockUUID(1);
        var dataExpect = "data".getBytes();

        when(mockHMSGeneratorService.getVarderingSkogsmark(eq(Arrays.asList(avtalId))))
            .thenReturn(dataExpect);
        when(mockAvtalRepository.getFastighetsbeteckning(eq(avtalId))).thenReturn("fastighet 1:23");

        // When
        var dataActual = dokumentGeneratorService.getVarderingSkogsmark(avtalId);

        // Then
        assertEquals(new FileNameAwareByteArrayResource(dataExpect, ""), dataActual);
        assertEquals("hms_fastighet_1_23_2022-01-24.xlsx", dataActual.getFilename());
    }

    @Test
    void så_ska_det_gå_att_göra_haglof_export_för_ett_projekt() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var filter = new FastighetsfilterDto();
        var dataExpect = "data".getBytes();
        var avtalIds = Arrays.asList(mockUUID(1), mockUUID(2));

        when(mockAvtalRepository.avtalIdsFiltered(eq(projektId), eq(filter))).thenReturn(
            Arrays.asList(mockUUID(1), mockUUID(2))
        );

        when(mockHMSGeneratorService.getVarderingSkogsmark(eq(avtalIds))).thenReturn(dataExpect);
        when(mockProjektRepository.getNamn(eq(projektId))).thenReturn("nya projektet");

        // When
        var dataActual = dokumentGeneratorService.getVarderingSkogsmark(projektId, filter);

        // Then
        assertEquals(new FileNameAwareByteArrayResource(dataExpect, ""), dataActual);
        assertEquals("hms_nya_projektet_2022-01-24.xlsx", dataActual.getFilename());
    }

    @Test
    void så_ska_det_gå_att_behandla_ett_dokument() throws Exception {
        // Given
        var resource = new ByteArrayResource("bytearrayresource".getBytes());
        var resourceNew = new ByteArrayResource("behandlat dokument".getBytes());

        when(mockDokumentGenerator.addDocProperties((Resource) any(), any())).thenReturn(resourceNew);

        // When
        var resourceExpect = dokumentGeneratorService.addDocProperties(resource);

        // Then
        assertEquals(resourceExpect, resourceNew);
        verify(mockDokumentGenerator).addDocProperties(eq(resource), eq(MarkkollBindings.class));
    }

    private Workbook openWorkbook(byte[] data) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        return WorkbookFactory.create(is);
    }
}
