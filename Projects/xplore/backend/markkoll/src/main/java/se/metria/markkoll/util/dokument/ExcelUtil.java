package se.metria.markkoll.util.dokument;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Slf4j
public class ExcelUtil {
    public static XSSFWorkbook openWorkbook(byte[] data) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        return new XSSFWorkbook(is);
    }

    public static Double
    getDoubleCellValue(Workbook workbook, Character col, Integer rowNum) throws IllegalStateException {
        var cell = getCell(workbook, col, rowNum);

        return cell.getNumericCellValue();
    }

    public static Integer
    getIntegerCellValue(Workbook workbook, Character col, Integer rowNum) throws IllegalStateException {
        var val = getDoubleCellValue(workbook, col, rowNum);
        if(val == null) {
            return null;
        }
        return val.intValue();
    }

    public static void
    setCellValue(Workbook workbook, Character col, Integer rowNum, String value, CellStyle cellStyle)
    {
        if (value == null) {
            return;
        }

        var cell = getCell(workbook, col, rowNum);

        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
    }

    public static void setCellValue(Workbook workbook, Character col, Integer rowNum, String value) {
        if (value == null) {
            return;
        }

        var cell = getCell(workbook, col, rowNum);

        cell.setCellValue(value);
    }

    public static void setCellValue(Workbook workbook, Character col, Integer rowNum, Integer value) {
        setCellValue(workbook, 0, col, rowNum, value);
    }

    public static void setCellValue(Workbook workbook, Character col, Integer rowNum, Boolean value) {
        if (value == null) {
            return;
        }

        var cell = getCell(workbook, col, rowNum);

        cell.setCellValue(value);
    }

    public static void setCellValue(Workbook workbook, Character col, Integer rowNum, LocalDateTime value) {
        if (value == null) {
            return;
        }

        var cell = getCell(workbook, col, rowNum);

        cell.setCellValue(value);
    }

    public static void setCellValue(Workbook workbook, Character col, Integer rowNum, Double value) {
        setCellValue(workbook, 0, col, rowNum, value);
    }

    public static void
    setCellValue(Workbook workbook, int sheetNum, Character col, Integer rowNum, String value) {
        if (value == null) {
            return;
        }

        var cell = getCell(workbook, sheetNum, col, rowNum);

        cell.setCellValue(value);
    }

    public static void
    setCellValue(Workbook workbook, int sheetNum, Character col, Integer rowNum, Integer value) {
        if (value == null) {
            return;
        }

        var cell = getCell(workbook, sheetNum, col, rowNum);

        cell.setCellValue(value);
    }

    public static void
    setCellValue(Workbook workbook, int sheetNum, Character col, Integer rowNum, Double value) {
        if (value == null) {
            return;
        }

        var cell = getCell(workbook, sheetNum, col, rowNum);

        cell.setCellValue(value);
    }

    private static Cell getCell(Workbook workbook, Character col, Integer rowNum) {
        return getCell(workbook, 0, col, rowNum);
    }

    private static Cell getCell(Workbook workbook, int sheetNum, Character col, Integer rowNum) {
        var sheet = workbook.getSheetAt(sheetNum);
        var colNum = Character.toUpperCase(col) - 65;

        var row = sheet.getRow(rowNum - 1);
        if (row == null) {
            throw new IllegalArgumentException("Rad existerar inte: " + rowNum);
        }

        var cell = row.getCell(colNum);
        if (cell == null) {
            throw new IllegalArgumentException("Cell existerar inte: " + (char)(colNum + 65) + (rowNum + 1));
        }
        return cell;
    }
}
