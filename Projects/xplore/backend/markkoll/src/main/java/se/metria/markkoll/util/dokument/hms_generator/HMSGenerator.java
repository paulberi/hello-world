package se.metria.markkoll.util.dokument.hms_generator;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static se.metria.markkoll.util.dokument.ExcelUtil.setCellValue;

@Component
@Slf4j
public class HMSGenerator {
    private final Character COL_LAN = 'A';
    private final Character COL_KOMMUN = 'B';
    private final Character COL_FASTIGHETSNUMMER = 'D';
    private final Character COL_FASTIGHETSBETECKNING = 'E';
    private final Character COL_SOKID = 'F';
    private final Character COL_MOTTAGARREFERENS = 'G';
    private final Character COL_ANDEL = 'H';
    private final Character COL_NAMN = 'J';
    private final Character COL_ADRESS = 'K';
    private final Character COL_CO = 'L';
    private final Character COL_POSTNUMMER = 'M';
    private final Character COL_POSTORT = 'N';
    private final Character COL_TELE_ARBETE = 'O';
    private final Character COL_E_POST = 'V';
    private final Character COL_PERSONNUMMER = 'W';
    private final Character COL_BANKKONTO = 'Z';
    private final Integer COL_WIDTH_DEFAULT = 10;

    public byte[] generate(Collection<Entry> entries) throws IOException {
        log.info("Genererar Excelfil för skogsmarksvärdering...");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Blad1");
        sheet.setDefaultColumnWidth(COL_WIDTH_DEFAULT);

        fillHeaderRow(workbook);

        var index = 1;
        for (var entry: entries) {
            fillRow(workbook, index++, entry);
        }

        autoSizeColumn(sheet, COL_FASTIGHETSBETECKNING);
        autoSizeColumn(sheet, COL_FASTIGHETSNUMMER);
        autoSizeColumn(sheet, COL_SOKID);
        autoSizeColumn(sheet, COL_ANDEL);
        autoSizeColumn(sheet, COL_NAMN);
        autoSizeColumn(sheet, COL_ADRESS);
        autoSizeColumn(sheet, COL_POSTNUMMER);
        autoSizeColumn(sheet, COL_POSTORT);
        autoSizeColumn(sheet, COL_PERSONNUMMER);
        autoSizeColumn(sheet, COL_LAN);
        autoSizeColumn(sheet, COL_KOMMUN);
        autoSizeColumn(sheet, COL_CO);
        autoSizeColumn(sheet, COL_TELE_ARBETE);
        autoSizeColumn(sheet, COL_E_POST);
        autoSizeColumn(sheet, COL_BANKKONTO);
        autoSizeColumn(sheet, COL_MOTTAGARREFERENS);

        var baos = new ByteArrayOutputStream();
        workbook.write(baos);

        return baos.toByteArray();
    }

    private void fillHeaderRow(Workbook workbook) {
        var headerStyle = cellStyle(workbook, true);
        var row = workbook.getSheetAt(0).createRow(0);

        var headers = Arrays.asList("Län", "Kommun", "Församling", "Fastighetsnummer", "Fastighet", "Sök-ID",
            "Mottagarreferens", "Andel", "Kontakt", "Namn", "Gatuadress", "C/o-adress", "Postnr", "Postort", "Tele (arb)",
            "Tele (hem)", "Mobil", "Fax", "Region", "Land", "Noteringar", "E-Post", "Personnummer", "Bankgiro",
            "Plusgiro", "Bankkonto", "Levnummer");

        for (char i = 0; i < headers.size(); i++) {
            row.createCell(i);
            setCellValue(workbook, (char)(i + 65), 1, headers.get(i), headerStyle);
        }
    }

    private void fillRow(Workbook workbook, Integer rowNum, Entry entry) {
        CellStyle bodyStyle = cellStyle(workbook, false);

        workbook.getSheetAt(0).createRow(rowNum);

        createCell(workbook, COL_FASTIGHETSBETECKNING, rowNum, entry.getFastighet(), bodyStyle);
        createCell(workbook, COL_FASTIGHETSNUMMER, rowNum, entry.getFastighetsnummer(), bodyStyle);
        createCell(workbook, COL_SOKID, rowNum, entry.getSokId(), bodyStyle);
        createCell(workbook, COL_ANDEL, rowNum, entry.getAndel(), bodyStyle);
        createCell(workbook, COL_NAMN, rowNum, entry.getNamn(), bodyStyle);
        createCell(workbook, COL_ADRESS, rowNum, entry.getGatuadress(), bodyStyle);
        createCell(workbook, COL_POSTNUMMER, rowNum, entry.getPostnr(), bodyStyle);
        createCell(workbook, COL_POSTORT, rowNum, entry.getPostort(), bodyStyle);
        createCell(workbook, COL_PERSONNUMMER, rowNum, entry.getPersonnummer(), bodyStyle);
        createCell(workbook, COL_LAN, rowNum, entry.getLan(), bodyStyle);
        createCell(workbook, COL_KOMMUN, rowNum, entry.getKommun(), bodyStyle);
        createCell(workbook, COL_CO, rowNum, entry.getCo(), bodyStyle);
        createCell(workbook, COL_TELE_ARBETE, rowNum, entry.getTelefonArbete(), bodyStyle);
        createCell(workbook, COL_E_POST, rowNum, entry.getEPost(), bodyStyle);
        createCell(workbook, COL_BANKKONTO, rowNum, entry.getBankkonto(), bodyStyle);
        createCell(workbook, COL_MOTTAGARREFERENS, rowNum, entry.getMottagarreferens(), bodyStyle);
    }

    private void autoSizeColumn(Sheet sheet, Character col) {
        sheet.autoSizeColumn(col - 65);
    }

    private void
    createCell(Workbook workbook, Character col, Integer rowNum, String value, CellStyle cellStyle) {
        var row = workbook.getSheetAt(0).getRow(rowNum);

        row.createCell(col - 65);
        setCellValue(workbook, col, rowNum + 1, value, cellStyle);
    }

    private CellStyle cellStyle(Workbook workbook, Boolean bold) {
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(bold);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);

        return style;
    }

}
