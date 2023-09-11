package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.openapi.model.FiberInfoDto;
import se.metria.markkoll.openapi.model.FiberProjektDto;
import se.metria.markkoll.openapi.model.ProjektInfoDto;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.projekt.FiberProjektService;
import se.metria.markkoll.service.projekt.ProjektService;
import se.metria.markkoll.service.utskick.UtskickDto;
import se.metria.markkoll.service.utskick.UtskickService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollGeneratorService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollGeneratorService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.testdata.UtskickTestData.utskickBatchFastighetMedVp;
import static se.metria.markkoll.testdata.UtskickTestData.utskickBatchSingle;

@MarkkollServiceTest
@DisplayName("Givet AvtalUtskickZipService")
public class AvtalUtskickZipServiceTest {
    AvtalUtskickZipService avtalUtskickZipService;

    AvtalRepository mockAvtalRepository;
    DokumentGeneratorService mockDokumentGeneratorService;
    ElnatVarderingsprotokollGeneratorService mockElnatVarderingsprotokollGeneratorService;
    FiberVarderingsprotokollGeneratorService mockFiberVarderingsprotokollGeneratorService;
    FilService mockFilService;
    ProjektService mockProjektService;
    UtskickService mockUtskickService;
    FiberProjektService mockFiberProjektService;

    final UUID avtalId = mockUUID(0);
    final UUID projektId = mockUUID(1);
    final String zipFilename = "fbet.zip";
    final UUID dokumentmallId = mockUUID(6);

    @BeforeEach
    void beforeEach() throws Exception {
        mockAvtalRepository = mock(AvtalRepository.class);
        mockDokumentGeneratorService = mock(DokumentGeneratorService.class);
        mockElnatVarderingsprotokollGeneratorService = mock(ElnatVarderingsprotokollGeneratorService.class);
        mockFiberVarderingsprotokollGeneratorService = mock(FiberVarderingsprotokollGeneratorService.class);
        mockFilService = mock(FilService.class);
        mockProjektService = mock(ProjektService.class);
        mockUtskickService = mock(UtskickService.class);
        mockFiberProjektService = mock(FiberProjektService.class);

        avtalUtskickZipService = new AvtalUtskickZipService(mockAvtalRepository, mockDokumentGeneratorService,
            mockElnatVarderingsprotokollGeneratorService,
            mockFiberVarderingsprotokollGeneratorService, mockFilService, mockProjektService, mockUtskickService,
            mockFiberProjektService);
    }

    @ParameterizedTest
    @MethodSource("zipTestDataSingleVp")
    void ziptest(ProjektTypDto projektTyp, String zipExpectPath) throws Exception {
        // Given
        var zipExpect = fil(zipExpectPath, "zip");
        List<ByteArrayResource> vpFiles = Arrays.asList(fil("avtal/vp.xlsx", "vp"));
        var fiberProjekt = new FiberProjektDto().fiberInfo(new FiberInfoDto().varderingsprotokoll(true));

        var projektinfo = new ProjektInfoDto()
            .projektTyp(projektTyp)
            .id(projektId)
            .namn("projekt");

        when(mockFilService.getBilagorFilerData(eq(avtalId))).thenReturn(
            Arrays.asList(fil("avtal/bilaga.xlsx", "bilaga.xlsx"))
        );

        when(mockProjektService.getProjektForAvtal(eq(avtalId))).thenReturn(projektinfo);
        when(mockUtskickService.getAvtalUtskickBatch(eq(avtalId))).thenReturn(utskickBatchFastighetMedVp());
        when(mockDokumentGeneratorService.getAvtal(eq(avtalId), eq(dokumentmallId), (UtskickDto) any()))
            .thenReturn(fil("avtal/avtal.pdf", "avtal"));
        when(mockFiberProjektService.getProjektDto(eq(projektId)))
            .thenReturn(java.util.Optional.ofNullable(fiberProjekt));
        when(mockElnatVarderingsprotokollGeneratorService.generateVarderingsprotokoll(eq(avtalId), any()))
            .thenReturn(vpFiles);
        when(mockFiberVarderingsprotokollGeneratorService.generateVarderingsprotokoll(eq(avtalId), any()))
            .thenReturn(vpFiles);

        // When
        var zip = avtalUtskickZipService.getAvtalZip(avtalId, dokumentmallId);

        // Then
        assertEquals(zipFilename, zip.getFilename());
        assertZipFiles(new ZipInputStream(zipExpect.getInputStream()), new ZipInputStream(zip.getInputStream()));
    }

    @Test
    void ziptestSingle() throws Exception {
        // Given
        var zipExpectPath = "avtal/single_utskick.zip";
        var projektTyp = ProjektTypDto.LOKALNAT;
        var zipExpect = fil(zipExpectPath, "zip");
        List<ByteArrayResource> vpFiles = Arrays.asList(fil("avtal/vp.xlsx", "vp"));
        var fiberProjekt = new FiberProjektDto().fiberInfo(new FiberInfoDto().varderingsprotokoll(true));

        var projektinfo = new ProjektInfoDto()
            .projektTyp(projektTyp)
            .id(projektId)
            .namn("projekt");

        when(mockFilService.getBilagorFilerData(eq(avtalId))).thenReturn(
            Arrays.asList(fil("avtal/bilaga.xlsx", "bilaga.xlsx"))
        );

        when(mockProjektService.getProjektForAvtal(eq(avtalId))).thenReturn(projektinfo);
        when(mockUtskickService.getAvtalUtskickBatch(eq(avtalId))).thenReturn(utskickBatchSingle());
        when(mockDokumentGeneratorService.getAvtal(eq(avtalId), eq(dokumentmallId), (UtskickDto) any()))
            .thenReturn(fil("avtal/avtal.pdf", "avtal"));
        when(mockFiberProjektService.getProjektDto(eq(projektId)))
            .thenReturn(java.util.Optional.ofNullable(fiberProjekt));
        when(mockElnatVarderingsprotokollGeneratorService.generateVarderingsprotokoll(eq(avtalId), any()))
            .thenReturn(vpFiles);
        when(mockFiberVarderingsprotokollGeneratorService.generateVarderingsprotokoll(eq(avtalId), any()))
            .thenReturn(vpFiles);

        // When
        var zip = avtalUtskickZipService.getAvtalZip(avtalId, dokumentmallId);

        // Then
        assertEquals(zipFilename, zip.getFilename());
        assertZipFiles(new ZipInputStream(zipExpect.getInputStream()), new ZipInputStream(zip.getInputStream()));
    }

    @ParameterizedTest
    @MethodSource("zipTestMultipleVp")
    void ziptest2(ProjektTypDto projektTyp, String zipExpectPath) throws Exception {
        // Given
        var zipExpect = fil(zipExpectPath, "zip");
        var fiberProjekt = new FiberProjektDto().fiberInfo(new FiberInfoDto().varderingsprotokoll(true));
        List<ByteArrayResource> vpFiles = Arrays.asList(
            fil("avtal/vp.xlsx", "vp"),
            fil("avtal/vp.xlsx", "vp2")
        );

        when(mockFilService.getBilagorFilerData(eq(avtalId))).thenReturn(
            Arrays.asList(fil("avtal/bilaga.xlsx", "bilaga.xlsx"))
        );
        var projektinfo = new ProjektInfoDto()
            .projektTyp(projektTyp)
            .id(projektId)
            .namn("projekt");

        when(mockProjektService.getProjektForAvtal(eq(avtalId))).thenReturn(projektinfo);
        when(mockUtskickService.getAvtalUtskickBatch(eq(avtalId))).thenReturn(utskickBatchFastighetMedVp());
        when(mockDokumentGeneratorService.getAvtal(eq(avtalId), eq(dokumentmallId), (UtskickDto) any()))
            .thenReturn(fil("avtal/avtal.pdf", "avtal"));
        when(mockFiberProjektService.getProjektDto(eq(projektId)))
            .thenReturn(java.util.Optional.ofNullable(fiberProjekt));
        when(mockElnatVarderingsprotokollGeneratorService.generateVarderingsprotokoll(eq(avtalId), any()))
            .thenReturn(vpFiles);
        when(mockFiberVarderingsprotokollGeneratorService.generateVarderingsprotokoll(eq(avtalId), any()))
            .thenReturn(vpFiles);

        // When
        var zip = avtalUtskickZipService.getAvtalZip(avtalId, dokumentmallId);
        
        // Then
        assertEquals(zipFilename, zip.getFilename());
        assertZipFiles(new ZipInputStream(zipExpect.getInputStream()), new ZipInputStream(zip.getInputStream()));
    }

    /* Egentligen borde man jämföra innehållet i alla entries, men jag ser det som good enough att jämföra zipfilernas
    storlek, antalet zippade filer, och deras filnamn */
    private void assertZipFiles(ZipInputStream zip1, ZipInputStream zip2) throws Exception {
        assertEquals(zip1.readAllBytes().length, zip2.readAllBytes().length);

        var zipEntries1 = zipEntries(zip1);
        var zipEntries2 = zipEntries(zip2);
        assertEquals(zipEntries1.size(), zipEntries2.size(), "Olika antal filer i zipfilerna");

        for (var entry1: zipEntries1) {
            if (zipEntries2.stream().filter(entry2 -> entry2.getName().equals(entry1.getName())).count() == 0) {
                throw new Exception(entry1.getName() + " finns inte");
            }
        }
    }

    private FileNameAwareByteArrayResource fil(String classPath, String fileName) throws IOException {
        return new FileNameAwareByteArrayResource(new ClassPathResource(classPath).getInputStream().readAllBytes(),
            fileName);
    }

    private List<ZipEntry> zipEntries(ZipInputStream zip) throws IOException {
        List<ZipEntry> entries = new ArrayList<>();

        while(true) {
            var entry = zip.getNextEntry();
            if (entry != null) {
                entries.add(entry);
            } else {
                return entries;
            }
        }
    }

    private static Stream<Arguments> zipTestDataSingleVp() {
        return Stream.of(
            Arguments.of(ProjektTypDto.LOKALNAT, "avtal/single_vp.zip"),
            Arguments.of(ProjektTypDto.REGIONNAT, "avtal/single_vp.zip"),
            Arguments.of(ProjektTypDto.FIBER, "avtal/single_vp.zip")
        );
    }

    private static Stream<Arguments> zipTestMultipleVp() {
        return Stream.of(
            Arguments.of(ProjektTypDto.LOKALNAT, "avtal/multiple_vp.zip"),
            Arguments.of(ProjektTypDto.REGIONNAT, "avtal/multiple_vp.zip"),
            Arguments.of(ProjektTypDto.FIBER, "avtal/multiple_vp.zip")
        );
    }
}
