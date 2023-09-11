package se.metria.markkoll.util.vardering;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.util.MSE_CompareExcelFiles;
import se.metria.markkoll.util.vardering.vpgeneratorstrategy.ElnatVpGeneratorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MarkkollServiceTest
@DisplayName("Givet ElnatVarderingsprotokollGenerator")
public class ElnatVpGeneratorStrategyTest {

    ElnatVpGeneratorStrategy elnatVpGeneratorStrategy = new ElnatVpGeneratorStrategy();

    @ParameterizedTest
    @MethodSource("vpTestData")
    void
    test_vp(String vpExpectName,
            ElnatVarderingsprotokollDto vp,
            VarderingsprotokollMetadataExtra metadataExtra,
            List<Signatar> fastighetsagare) throws Exception
    {
        // Given
        var vpFile = new ClassPathResource("värderingsprotokoll/varderingsprotokoll-2022.xlsx").getInputStream();
        var vpExpect = new ClassPathResource("varderingsprotokoll/" + vpExpectName).getInputStream();

        var workbook = openWorkbook(vpFile.readAllBytes());

        // When
        elnatVpGeneratorStrategy.updateWorkbook(workbook, vp, metadataExtra, fastighetsagare);


        // Then
        var generated = readWorkbook(workbook);
        MSE_CompareExcelFiles.verify(vpExpect.readAllBytes(), generated.getByteArray(), 0);
    }

    @Test
    void ska_hämta_ersättning() throws IOException {
        // Given
        var vpFile = new ClassPathResource("varderingsprotokoll/elnat_ersattning.xlsx").getInputStream();
        var ersattningExpect = new ElnatErsattningDto();
        ersattningExpect.setRotnetto(1234.56);
        ersattningExpect.setTotal(2786);

        var workbook = openWorkbook(vpFile.readAllBytes());

        // When
        var ersattningActual = elnatVpGeneratorStrategy.getErsattningCellValues(workbook);

        // Then
        assertEquals(ersattningExpect, ersattningActual);
    }

    private XSSFWorkbook openWorkbook(byte[] data) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        return new XSSFWorkbook(is);
    }

    private ByteArrayResource readWorkbook(Workbook workbook) throws IOException {
        var baos = new ByteArrayOutputStream();
        workbook.write(baos);

        return new ByteArrayResource(baos.toByteArray());
    }

    private static Stream<Arguments> vpTestData() {
        return Stream.of(
            Arguments.of("vp_metadata.xlsx", vpMetadata(), vpMetadataExtra(), null),
            Arguments.of("vp_config.xlsx", vpConfig(), null, null),
            Arguments.of("vp_markledning.xlsx", vpMarkledning(), null, null),
            Arguments.of("vp_kabelskap.xlsx", vpKabelskap(), null, null),
            Arguments.of("vp_natstation_skog.xlsx", vpNatstationSkog(), null, null),
            Arguments.of("vp_natstation_jordbruksimpediment.xlsx", vpNatstationJordbruksimpediment(), null, null),
            Arguments.of("vp_natstation_ovrigmark.xlsx", vpNatstationOvrigMark(), null, null),
            Arguments.of("vp_sjokabelskylt_skog.xlsx", vpSjokabelskyltSkog(), null, null),
            Arguments.of("vp_sjokabelskylt_jordbruksimpediment.xlsx", vpSjokabelskyltJordbruksimpediment(), null, null),
            Arguments.of("vp_sjokabelskylt_ovrigmark.xlsx", vpSjokabelskyltOvrigMark(), null, null),
            Arguments.of("vp_skogsmark.xlsx", vpSkogsmarkNorrlandsInland(), null, null),
            Arguments.of("vp_vaganlaggning.xlsx", vpVaganlaggning(), null, null),
            Arguments.of("vp_hinder_akermark.xlsx", vphinderAkermark(), null, null),
            Arguments.of("vp_ledning_skogsmark.xlsx", vpLedningSkogsmark(), null, null),
            Arguments.of("vp_ovrigt_intrang.xlsx", vpOvrigtIntrang(), null, null),
            Arguments.of("vp_rotnetto.xlsx", vpRotnetto(), null, null),
            Arguments.of("vp_signatarer.xlsx", new ElnatVarderingsprotokollDto(), null, vpSignatarer()),
            Arguments.of("vp_prisomrade_norrlands_inland.xlsx", vpElnatPrisomradeNorrlandsInland(), null, null),
            Arguments.of("vp_prisomrade_norrlands_kustland.xlsx", vpElnatPrisomradeNorrlandsKustland(), null, null),
            Arguments.of("vp_prisomrade_tillvaxtomrade_3.xlsx", vpElnatPrisomradeTillvaxtomrade3(), null, null),
            Arguments.of("vp_prisomrade_tillvaxtomrade_4a.xlsx", vpElnatPrisomradeTillvaxtomrade4A(), null, null),
            Arguments.of("vp_prisomrade_tillvaxtomrade_4b.xlsx", vpElnatPrisomradeTillvaxtomrade4B(), null, null)
        );
    }

    private static ElnatVarderingsprotokollDto vpMetadata() {
        return new ElnatVarderingsprotokollDto()
            .metadata(new ElnatVarderingsprotokollMetadataDto()
                .koncessionslopnr("2nr")
                .ledning("??")
                .fastighetsnummer("123s")
                .varderingsmanOchForetag("CK")
                .varderingstidpunkt(LocalDateTime.of(1991, 1, 1, 12, 34, 56)));
    }

    private static ElnatVarderingsprotokollDto vpConfig() {
        return new ElnatVarderingsprotokollDto()
            .config(new ElnatVarderingsprotokollConfigDto()
                .forhojdMinimumersattning(true)
                .ingenGrundersattning(true)
                .lagspanning(true)
                .storskogsbruksavtalet(true));
    }

    private static ElnatVarderingsprotokollDto vpMarkledning() {
        return new ElnatVarderingsprotokollDto()
            .markledning(Arrays.asList(
                new ElnatMarkledningDto().beskrivning("Jääättelång markledning").langd(10000).bredd(2),
                new ElnatMarkledningDto().beskrivning("Något kortare markledning").langd(500).bredd(1),
                new ElnatMarkledningDto().beskrivning("Ytterligare en markledning").langd(100).bredd(2),
                new ElnatMarkledningDto().beskrivning("Och en sista").langd(1).bredd(1)
            ));
    }

    private static ElnatVarderingsprotokollDto vpKabelskap() {
        return new ElnatVarderingsprotokollDto().punktersattning(Arrays.asList(
            new ElnatPunktersattningDto().beskrivning("Kabelskåp jordbruksimpediment").typ(ElnatPunktersattningTypDto.KABELSKAP_JORDBRUKSIMPEDIMENT).antal(1),
            new ElnatPunktersattningDto().beskrivning("Kabelskåp övrig mark").typ(ElnatPunktersattningTypDto.KABELSKAP_OVRIGMARK).antal(2),
            new ElnatPunktersattningDto().beskrivning("Kabelskåp skog").typ(ElnatPunktersattningTypDto.KABELSKAP_SKOG).antal(3)
        ));
    }

    private static ElnatVarderingsprotokollDto vpNatstationSkog() {
        return new ElnatVarderingsprotokollDto().punktersattning(Arrays.asList(
            new ElnatPunktersattningDto().beskrivning("Nätstation skog 6x6").typ(ElnatPunktersattningTypDto.NATSTATION_SKOG_6X6M).antal(1),
            new ElnatPunktersattningDto().beskrivning("Nätstation skog 8x8").typ(ElnatPunktersattningTypDto.NATSTATION_SKOG_8X8M).antal(2),
            new ElnatPunktersattningDto().beskrivning("Nätstation skog 10x10").typ(ElnatPunktersattningTypDto.NATSTATION_SKOG_10X10M).antal(3)
        ));
    }

    private static ElnatVarderingsprotokollDto vpNatstationJordbruksimpediment() {
        return new ElnatVarderingsprotokollDto().punktersattning(Arrays.asList(
            new ElnatPunktersattningDto().beskrivning("Nätstation jordbruksimpediment 6x6").typ(ElnatPunktersattningTypDto.NATSTATION_JORDBRUKSIMPEDIMENT_6X6M).antal(1),
            new ElnatPunktersattningDto().beskrivning("Nätstation jordbruksimpediment 8x8").typ(ElnatPunktersattningTypDto.NATSTATION_JORDBRUKSIMPEDIMENT_8X8M).antal(2),
            new ElnatPunktersattningDto().beskrivning("Nätstation jordbruksimpediment 10x10").typ(ElnatPunktersattningTypDto.NATSTATION_JORDBRUKSIMPEDIMENT_10X10M).antal(3)
        ));
    }

    private static ElnatVarderingsprotokollDto vpNatstationOvrigMark() {
        return new ElnatVarderingsprotokollDto().punktersattning(Arrays.asList(
            new ElnatPunktersattningDto().beskrivning("Nätstation övrig mark 6x6").typ(ElnatPunktersattningTypDto.NATSTATION_OVRIGMARK_6X6M).antal(1),
            new ElnatPunktersattningDto().beskrivning("Nätstation övrig mark 8x8").typ(ElnatPunktersattningTypDto.NATSTATION_OVRIGMARK_8X8M).antal(2),
            new ElnatPunktersattningDto().beskrivning("Nätstation övrig mark 10x10").typ(ElnatPunktersattningTypDto.NATSTATION_OVRIGMARK_10X10M).antal(3)
        ));
    }

    private static ElnatVarderingsprotokollDto vpSjokabelskyltSkog() {
        return new ElnatVarderingsprotokollDto().punktersattning(Arrays.asList(
            new ElnatPunktersattningDto().beskrivning("Sjökabelskylt skog 6x6")
                .typ(ElnatPunktersattningTypDto.SJOKABELSKYLT_SKOG_6X6M)
                .antal(1),
            new ElnatPunktersattningDto()
                .beskrivning("Sjökabelskylt skog 8x8")
                .typ(ElnatPunktersattningTypDto.SJOKABELSKYLT_SKOG_8X8M)
                .antal(2),
            new ElnatPunktersattningDto().beskrivning("Sjökabelskylt skog 10x10")
                .typ(ElnatPunktersattningTypDto.SJOKABELSKYLT_SKOG_10X10M)
                .antal(3)
        ));
    }

    private static ElnatVarderingsprotokollDto vpSjokabelskyltJordbruksimpediment() {
        return new ElnatVarderingsprotokollDto().punktersattning(Arrays.asList(
            new ElnatPunktersattningDto().beskrivning("Sjökabelskylt jordbruksimpediment 6x6")
                .typ(ElnatPunktersattningTypDto.SJOKABELSKYLT_JORDBRUKSIMPEDIMENT_6X6M)
                .antal(1),
            new ElnatPunktersattningDto().beskrivning("Sjökabelskylt jordbruksimpediment 8x8")
                .typ(ElnatPunktersattningTypDto.SJOKABELSKYLT_JORDBRUKSIMPEDIMENT_8X8M)
                .antal(2),
            new ElnatPunktersattningDto().beskrivning("Sjökabelskylt jordbruksimpediment 10x10")
                .typ(ElnatPunktersattningTypDto.SJOKABELSKYLT_JORDBRUKSIMPEDIMENT_10X10M)
                .antal(3)
        ));
    }

    private static ElnatVarderingsprotokollDto vpSjokabelskyltOvrigMark() {
        return new ElnatVarderingsprotokollDto().punktersattning(Arrays.asList(
            new ElnatPunktersattningDto().beskrivning("Sjökabelskylt övrig mark 6x6")
                .typ(ElnatPunktersattningTypDto.SJOKABELSKYLT_OVRIGMARK_6X6M)
                .antal(1),
            new ElnatPunktersattningDto().beskrivning("Sjökabelskylt övrig mark 8x8")
                .typ(ElnatPunktersattningTypDto.SJOKABELSKYLT_OVRIGMARK_8X8M)
                .antal(2),
            new ElnatPunktersattningDto().beskrivning("Sjökabelskylt övrig mark 10x10")
                .typ(ElnatPunktersattningTypDto.SJOKABELSKYLT_OVRIGMARK_10X10M)
                .antal(3)
        ));
    }

    private static ElnatVarderingsprotokollDto vpSkogsmarkNorrlandsInland() {
        return new ElnatVarderingsprotokollDto().ssbSkogsmark(Arrays.asList(
            new ElnatSsbSkogsmarkDto().beskrivning("Skogsmarksledning 1").langd(1).bredd(2),
            new ElnatSsbSkogsmarkDto().beskrivning("Skogsmarksledning 2").langd(3).bredd(4),
            new ElnatSsbSkogsmarkDto().beskrivning("Skogsmarksledning 3").langd(5).bredd(6),
            new ElnatSsbSkogsmarkDto().beskrivning("Skogsmarksledning 4").langd(7).bredd(8)
        ));
    }

    private static ElnatVarderingsprotokollDto vpVaganlaggning() {
        return new ElnatVarderingsprotokollDto().ssbVaganlaggning(Arrays.asList(
            new ElnatSsbVaganlaggningDto().beskrivning("Zon 1").langd(1).zon(ElnatZonDto.ZON_1),
            new ElnatSsbVaganlaggningDto().beskrivning("Zon 2").langd(2).zon(ElnatZonDto.ZON_2)
        ));
    }

    private static ElnatVarderingsprotokollDto vphinderAkermark() {
        return new ElnatVarderingsprotokollDto().hinderAkermark(Arrays.asList(
            new ElnatHinderAkermarkDto().beskrivning("Hinder 1").ersattning(1729),
            new ElnatHinderAkermarkDto().beskrivning("Hinder 2").ersattning(101010)
        ));
    }

    private static ElnatVarderingsprotokollDto vpLedningSkogsmark() {
        return new ElnatVarderingsprotokollDto().ledningSkogsmark(Arrays.asList(
            new ElnatLedningSkogsmarkDto().beskrivning("Ledning 1").ersattning(1000),
            new ElnatLedningSkogsmarkDto().beskrivning("Ledning 2").ersattning(500)
        ));
    }

    private static ElnatVarderingsprotokollDto vpOvrigtIntrang() {
        return new ElnatVarderingsprotokollDto().ovrigtIntrang(Arrays.asList(
            new ElnatOvrigtIntrangDto().beskrivning("Övrigt intrång 1").ersattning(1),
            new ElnatOvrigtIntrangDto().beskrivning("Övrigt intrång 2").ersattning(2)
        ));
    }

    private static ElnatVarderingsprotokollDto vpRotnetto() {
        return new ElnatVarderingsprotokollDto().rotnetto(123456.);
    }

    private static List<Signatar> vpSignatarer() {
        return Arrays.asList(
            Signatar.builder().andel(1./36.).namn("Ägare 1").personnummer(null).build(),
            Signatar.builder().andel(2./36.).namn("Ägare 2").personnummer("2").build(),
            Signatar.builder().andel(3./36.).namn("Ägare 3").personnummer("3").build(),
            Signatar.builder().andel(4./36.).namn("Ägare 4").personnummer("4").build(),
            Signatar.builder().andel(5./36.).namn("Ägare 5").personnummer("5").build(),
            Signatar.builder().andel(6./36.).namn("Ägare 6").personnummer("6").build(),
            Signatar.builder().andel(7./36.).namn("Ägare 7").personnummer("7").build(),
            Signatar.builder().andel(8./36.).namn("Ägare 8").personnummer("8").build()
        );
    }

    private static VarderingsprotokollMetadataExtra vpMetadataExtra() {
        return VarderingsprotokollMetadataExtra.builder()
            .fastighetsbeteckning("ÖJEBYN 13:37")
            .kommun("Piteå")
            .kontaktpersonOchAdress("CK")
            .projektnummer("1p")
            .build();
    }

    private static ElnatVarderingsprotokollDto vpElnatPrisomradeNorrlandsInland() {
        return new ElnatVarderingsprotokollDto().prisomrade(ElnatPrisomradeDto.NORRLANDS_INLAND);
    }

    private static ElnatVarderingsprotokollDto vpElnatPrisomradeNorrlandsKustland() {
        return new ElnatVarderingsprotokollDto().prisomrade(ElnatPrisomradeDto.NORRLANDS_KUSTLAND);
    }

    private static ElnatVarderingsprotokollDto vpElnatPrisomradeTillvaxtomrade3() {
        return new ElnatVarderingsprotokollDto().prisomrade(ElnatPrisomradeDto.TILLVAXTOMRADE_3);
    }

    private static ElnatVarderingsprotokollDto vpElnatPrisomradeTillvaxtomrade4A() {
        return new ElnatVarderingsprotokollDto().prisomrade(ElnatPrisomradeDto.TILLVAXTOMRADE_4A);
    }

    private static ElnatVarderingsprotokollDto vpElnatPrisomradeTillvaxtomrade4B() {
        return new ElnatVarderingsprotokollDto().prisomrade(ElnatPrisomradeDto.TILLVAXTOMRADE_4B);
    }
}