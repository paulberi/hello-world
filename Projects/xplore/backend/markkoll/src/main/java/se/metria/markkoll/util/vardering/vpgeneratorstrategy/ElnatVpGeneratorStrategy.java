package se.metria.markkoll.util.vardering.vpgeneratorstrategy;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.util.dokument.ExcelUtil;
import se.metria.markkoll.util.vardering.ElnatErsattningDto;
import se.metria.markkoll.util.vardering.Signatar;
import se.metria.markkoll.util.vardering.VarderingsprotokollMetadataExtra;

import java.util.List;

import static se.metria.markkoll.util.dokument.ExcelUtil.setCellValue;

@Slf4j
public class ElnatVpGeneratorStrategy implements VpGeneratorStrategy<ElnatVarderingsprotokollDto, ElnatErsattningDto> {
    public static final Integer FASTIGHETSAGARE_MAX = 8;
    private static final Integer MARKLEDNING_MAX = 4;
    private static final Integer PUNKTERSATTNING_MAX = 4;
    private static final Integer VAGANLAGGNING_MAX = 2;
    private static final Integer SKOGSMARK_MAX = 4;
    private static final Integer HINDER_AKERMARK_MAX = 2;
    private static final Integer LEDNING_SKOGSMARK_MAX = 2;
    private static final Integer OVRIGT_INTRANG_MAX = 2;

    @Override
    public ElnatErsattningDto getErsattningCellValues(Workbook workbook) {
        var ersattning = new ElnatErsattningDto();

        try {
            ersattning.setRotnetto(ExcelUtil.getDoubleCellValue(workbook, 'F', 35));
        } catch (IllegalStateException e) {
            log.warn("Misslyckas med att hämta rotnetto från cell på position (F35), sätter värdet till 0");
            ersattning.setRotnetto(0.);
        }

        try {
            ersattning.setTotal(ExcelUtil.getIntegerCellValue(workbook, 'J', 58));
        } catch (IllegalStateException e) {
            log.warn("Misslyckas med att hämta total ersättning från cell på position (J58), sätter värdet till 0");
            ersattning.setTotal(0);
        }

        return ersattning;
    }

    @Override
    public Integer getMaxSignatarer() {
        return 8;
    }

    @Override
    public String getVpPath() {
        return "värderingsprotokoll/varderingsprotokoll-2022.xlsx";
    }

    @Override
    public void
    updateWorkbook(Workbook workbook,
                   ElnatVarderingsprotokollDto elnatVarderingsprotokollDto,
                   VarderingsprotokollMetadataExtra metadataExtra,
                   List<Signatar> signatarer)
    {
        setMarkledningValue(workbook, elnatVarderingsprotokollDto.getMarkledning());
        setPunktersattningValue(workbook, elnatVarderingsprotokollDto.getPunktersattning());
        setSsbSkogsmarkValue(workbook, elnatVarderingsprotokollDto.getSsbSkogsmark());
        setSsbVaganlaggningValue(workbook, elnatVarderingsprotokollDto.getSsbVaganlaggning());
        setHinderAkermarkValue(workbook, elnatVarderingsprotokollDto.getHinderAkermark());
        setLedningSkogsmarkValue(workbook, elnatVarderingsprotokollDto.getLedningSkogsmark());
        setOvrigtIntrangValue(workbook, elnatVarderingsprotokollDto.getOvrigtIntrang());
        setRotnetto(workbook, elnatVarderingsprotokollDto.getRotnetto());
        setConfig(workbook, elnatVarderingsprotokollDto.getConfig());
        setMetadata(workbook, elnatVarderingsprotokollDto.getMetadata());
        setMetadataExtra(workbook, metadataExtra);
        setSignatarer(workbook, signatarer);
        setCellValue(workbook, 'F', 42, prisomradeValue(elnatVarderingsprotokollDto.getPrisomrade()));
    }

    private void setMarkledningValue(Workbook workbook, List<ElnatMarkledningDto> markledning) {
        if (markledning == null) {
            return;
        }
        if (markledning.size() > MARKLEDNING_MAX) {
            throw rowErrorException("1. SCHABLONERSÄTTNING FÖR MARKLEDNING I ÅKER, BETE, JORDBRUKSIMPEDIMENT, " +
                "samt ÖVRIG MARK UTANFÖR DETALJPLAN", markledning.size(), MARKLEDNING_MAX);
        }

        for (int i = 0; i < markledning.size(); i++) {
            setCellValue(workbook, 'B', 11 + i, markledning.get(i).getBeskrivning());
            setCellValue(workbook, 'H', 11 + i, markledning.get(i).getLangd());
            setCellValue(workbook, 'I', 11 + i, markledning.get(i).getBredd());
        }
    }

    private void setPunktersattningValue(Workbook workbook, List<ElnatPunktersattningDto> punktersattning) {
        if (punktersattning == null) {
            return;
        }
        if (punktersattning.size() > PUNKTERSATTNING_MAX) {
            throw rowErrorException("2. SCHABLONERSÄTTNING FÖR NÄTSTATIONER, KABELSKÅP och SJÖKABELSKYLTAR " +
                "(Ej inom detaljplan)", punktersattning.size(), PUNKTERSATTNING_MAX);
        }

        for (int i = 0; i < punktersattning.size(); i++) {
            setCellValue(workbook, 'B', 18 + i, punktersattning.get(i).getBeskrivning());
            setCellValue(workbook, 'F', 18 + i, punktersattningTypValue(punktersattning.get(i).getTyp()));
            setCellValue(workbook, 'I', 18 + i, punktersattning.get(i).getAntal());
        }
    }

    private void setSsbSkogsmarkValue(Workbook workbook, List<ElnatSsbSkogsmarkDto> ssbSkogsmark) {
        if (ssbSkogsmark == null) {
            return;
        }
        if (ssbSkogsmark.size() > SKOGSMARK_MAX) {
            throw rowErrorException("6 a. ERSÄTTNING FÖR LEDNING I SKOGSMARK ENLIGT SKOGSBRUKSAVTALET",
                ssbSkogsmark.size(), SKOGSMARK_MAX);
        }

        for (int i = 0; i < ssbSkogsmark.size(); i++) {
            setCellValue(workbook, 'B', 43 + i, ssbSkogsmark.get(i).getBeskrivning());
            setCellValue(workbook, 'H', 43 + i, ssbSkogsmark.get(i).getLangd());
            setCellValue(workbook, 'I', 43 + i, ssbSkogsmark.get(i).getBredd());
        }
    }

    private void setSsbVaganlaggningValue(Workbook workbook, List<ElnatSsbVaganlaggningDto> ssbVaganlaggning) {
        if (ssbVaganlaggning == null) {
            return;
        }
        if (ssbVaganlaggning.size() > VAGANLAGGNING_MAX) {
            throw rowErrorException("6 b. ERSÄTTNING FÖR INTRÅNG INOM VÄGANLÄGGNING ENLIGT SKOGSBRUKSAVTALET",
                ssbVaganlaggning.size(), VAGANLAGGNING_MAX);
        }

        for (int i = 0; i < ssbVaganlaggning.size(); i++) {
            setCellValue(workbook, 'B', 50 + i, ssbVaganlaggning.get(i).getBeskrivning());
            setCellValue(workbook, 'H', 50 + i, ssbVaganlaggning.get(i).getLangd());
            setCellValue(workbook, 'I', 50 + i, zonValue(ssbVaganlaggning.get(i).getZon()));
        }
    }

    private void setHinderAkermarkValue(Workbook workbook, List<ElnatHinderAkermarkDto> hinderAkermarkDtos) {
        if (hinderAkermarkDtos == null) {
            return;
        }
        if (hinderAkermarkDtos.size() > HINDER_AKERMARK_MAX) {
            throw rowErrorException("3. ERSÄTTNING FÖR HINDER I ÅKERMARK",hinderAkermarkDtos.size(),
                HINDER_AKERMARK_MAX);
        }

        for (int i = 0; i < hinderAkermarkDtos.size(); i++) {
            setCellValue(workbook, 'B', 25 + i, hinderAkermarkDtos.get(i).getBeskrivning());
            setCellValue(workbook, 'J', 25 + i, hinderAkermarkDtos.get(i).getErsattning());
        }
    }

    private void setLedningSkogsmarkValue(Workbook workbook, List<ElnatLedningSkogsmarkDto> ledningSkogsmarkDtos) {
        if (ledningSkogsmarkDtos == null) {
            return;
        }
        if (ledningSkogsmarkDtos.size() > LEDNING_SKOGSMARK_MAX) {
            throw rowErrorException("4 a. ERSÄTTNING FÖR LEDNING I SKOGSMARK", ledningSkogsmarkDtos.size(),
                LEDNING_SKOGSMARK_MAX);
        }

        for (int i = 0; i < ledningSkogsmarkDtos.size(); i++) {
            setCellValue(workbook, 'B', 30 + i, ledningSkogsmarkDtos.get(i).getBeskrivning());
            setCellValue(workbook, 'J', 30 + i, ledningSkogsmarkDtos.get(i).getErsattning());
        }
    }

    private void setOvrigtIntrangValue(Workbook workbook, List<ElnatOvrigtIntrangDto> ovrigtIntrangDtos) {
        if (ovrigtIntrangDtos == null) {
            return;
        }
        if (ovrigtIntrangDtos.size() > OVRIGT_INTRANG_MAX) {
            throw rowErrorException("5. ERSÄTTNING FÖR ÖVRIGT INTRÅNG", ovrigtIntrangDtos.size(),
                OVRIGT_INTRANG_MAX);
        }

        for (int i = 0; i < ovrigtIntrangDtos.size(); i++) {
            setCellValue(workbook, 'B', 38 + i, ovrigtIntrangDtos.get(i).getBeskrivning());
            setCellValue(workbook, 'J', 38 + i, ovrigtIntrangDtos.get(i).getErsattning());
        }
    }

    private void setConfig(Workbook workbook, ElnatVarderingsprotokollConfigDto config) {
        if (config == null) {
            return;
        }

        setCellValue(workbook, 'L', 11, config.getLagspanning());
        setCellValue(workbook, 'L', 13, config.getStorskogsbruksavtalet());
        setCellValue(workbook, 'L', 15, config.getIngenGrundersattning());
        setCellValue(workbook, 'L', 17, config.getForhojdMinimumersattning());
    }

    private void
    setMetadata(Workbook workbook,
                ElnatVarderingsprotokollMetadataDto metadata)
    {
        if (metadata == null) {
            return;
        }

        setCellValue(workbook, 'I', 5, metadata.getLedning());
        setCellValue(workbook, 'D', 6, metadata.getFastighetsnummer());
        setCellValue(workbook, 'I', 6, metadata.getKoncessionslopnr());
        setCellValue(workbook, 'I', 7, metadata.getVarderingstidpunkt());
        setCellValue(workbook, 'I', 8, metadata.getVarderingsmanOchForetag());
    }

    private void
    setMetadataExtra(Workbook workbook, VarderingsprotokollMetadataExtra metadataExtra) {
        if (metadataExtra == null) {
            return;
        }

        setCellValue(workbook, 'D', 4, metadataExtra.getFastighetsbeteckning());
        setCellValue(workbook, 'D', 5, metadataExtra.getKommun());
        setCellValue(workbook, 'D', 7, metadataExtra.getKontaktpersonOchAdress());
        setCellValue(workbook, 'I', 4, metadataExtra.getProjektnummer());
    }

    private void setSignatarer(Workbook workbook, List<Signatar> signatarer) {
        if (signatarer == null) {
            return;
        }

        if (signatarer.size() > FASTIGHETSAGARE_MAX) {
            throw rowErrorException("Fastighetsägare", signatarer.size(), FASTIGHETSAGARE_MAX);
        }

        for (int i = 0; i < signatarer.size(); i++) {
            var namn = signatarer.get(i).getNamn();
            var personnummer = signatarer.get(i).getPersonnummer();

            var namnString = namn == null ? "" : namn;
            var personnummerString = personnummer == null ? "" : String.format(" (%s)", personnummer);
            var personuppgifter = namnString + personnummerString;

            var rowNum = 65 + 5 * i;
            if (i == 0) { // irregularitet i stegandet i Excelfilen...
                rowNum -= 2;
            }

            setCellValue(workbook, 'B', rowNum, personuppgifter);
            setCellValue(workbook, 'E', rowNum, signatarer.get(i).getAndel());
        }
    }

    private void setRotnetto(Workbook workbook, Double rotnetto) {
        if (rotnetto == null) {
            return;
        }
        setCellValue(workbook, 'F', 35, rotnetto);
    }

    private String
    prisomradeValue(ElnatPrisomradeDto prisomradeDto) {
        if (prisomradeDto == null) {
            return null;
        }

        switch (prisomradeDto) {
            case NORRLANDS_INLAND:
                return "Norrlands inland";
            case NORRLANDS_KUSTLAND:
                return "Norrlands kustland";
            case TILLVAXTOMRADE_3:
                return "Tillväxtområde 3";
            case TILLVAXTOMRADE_4A:
                return "Tillväxtområde 4A";
            case TILLVAXTOMRADE_4B:
                return "Tillväxtområde 4B";
            default:
                throw new IllegalArgumentException("Okänt prisområde: " + prisomradeDto);
        }
    }

    private String
    punktersattningTypValue(ElnatPunktersattningTypDto punktersattningTypDto)
    {
        if (punktersattningTypDto == null) {
            return null;
        }

        switch (punktersattningTypDto) {
            case KABELSKAP_JORDBRUKSIMPEDIMENT:
                return "Kabelskåp - Jordbruksimp.";
            case KABELSKAP_OVRIGMARK:
                return "Kabelskåp - Övrig mark";
            case KABELSKAP_SKOG:
                return "Kabelskåp - Skog";

            case NATSTATION_SKOG_6X6M:
                return "Nätstation - Skog (yta 6 x 6 meter)";
            case NATSTATION_SKOG_8X8M:
                return "Nätstation - Skog (yta 8 x 8 meter)";
            case NATSTATION_SKOG_10X10M:
                return "Nätstation - Skog (yta 10 x 10 meter)";
            case NATSTATION_JORDBRUKSIMPEDIMENT_6X6M:
                return "Nätstation - Jordbruksimp. (yta 6 x6 meter)";
            case NATSTATION_JORDBRUKSIMPEDIMENT_8X8M:
                return "Nätstation - Jordbruksimp. (yta 8 x 8 meter)";
            case NATSTATION_JORDBRUKSIMPEDIMENT_10X10M:
                return "Nätstation - Jordbruksimp. (yta 10 x 10 meter)";
            case NATSTATION_OVRIGMARK_6X6M:
                return "Nätstation - Övrig mark (yta 6 x 6 meter)";
            case NATSTATION_OVRIGMARK_8X8M:
                return "Nätstation - Övrig mark (yta 8 x 8 meter)";
            case NATSTATION_OVRIGMARK_10X10M:
                return "Nätstation - Övrig mark (yta 10 x 10 meter)";

            case SJOKABELSKYLT_SKOG_6X6M:
                return "Sjökabelskylt - Skog (yta 6 x 6 meter)";
            case SJOKABELSKYLT_SKOG_8X8M:
                return "Sjökabelskylt - Skog (yta 8 x 8 meter)";
            case SJOKABELSKYLT_SKOG_10X10M:
                return "Sjökabelskylt - Skog (yta 10 x 10 meter)";
            case SJOKABELSKYLT_JORDBRUKSIMPEDIMENT_6X6M:
                return "Sjökabelskylt - Jordbruksimp. (yta 6 x 6 meter)";
            case SJOKABELSKYLT_JORDBRUKSIMPEDIMENT_8X8M:
                return "Sjökabelskylt - Jordbruksimp. (yta 8 x 8 meter)";
            case SJOKABELSKYLT_JORDBRUKSIMPEDIMENT_10X10M:
                return "Sjökabelskylt - Jordbruksimp. (yta 10 x 10 meter)";
            case SJOKABELSKYLT_OVRIGMARK_6X6M:
                return "Sjökabelskylt - Övrig mark (yta 6 x 6 meter)";
            case SJOKABELSKYLT_OVRIGMARK_8X8M:
                return "Sjökabelskylt - Övrig mark (yta 8 x 8 meter)";
            case SJOKABELSKYLT_OVRIGMARK_10X10M:
                return "Sjökabelskylt - Övrig mark (yta 10 x 10 meter)";

            case NATSTATION_EJ_KLASSIFICERAD:
            case KABELSKAP_EJ_KLASSIFICERAD:
            case SJOKABELSKYLT_EJ_KLASSIFICERAD:
                return null;

            default:
                throw new IllegalArgumentException("Okänd punktersättningstyp: " + punktersattningTypDto);
        }
    }

    private String
    zonValue(ElnatZonDto zonDto) {
        if (zonDto == null) {
            return null;
        }

        switch (zonDto) {
            case ZON_1:
                return "Zon 1";
            case ZON_2:
                return "Zon 2";
            default:
                throw new IllegalArgumentException("Okänd zon: " + zonDto);
        }
    }

    private IllegalArgumentException rowErrorException(String type, Integer actual, Integer max) {
        var msg = String.format("För många rader ({}) i ({}). Högst {} tillåtna.", actual, type, max);

        return new IllegalArgumentException(msg);
    }
}
