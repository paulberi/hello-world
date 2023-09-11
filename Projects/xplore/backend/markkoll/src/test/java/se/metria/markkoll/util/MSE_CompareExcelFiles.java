package se.metria.markkoll.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

//http://makeseleniumeasy.com/2020/05/17/compare-two-excel-workbooks-using-apache-poi/
public class MSE_CompareExcelFiles {

    public static void verify(byte[] workbookExpectData, byte[] workbookActualData, Integer... sheetNums) throws IOException {
        var workbookExpect = openWorkbook(workbookExpectData);
        var workbookActual = openWorkbook(workbookActualData);

        MSE_CompareExcelFiles.verifyIfExcelFilesHaveSameNumberAndNameOfSheets(workbookExpect, workbookActual);
        MSE_CompareExcelFiles.verifySheetsInExcelFilesHaveSameRowsAndColumns(workbookExpect, workbookActual);
        MSE_CompareExcelFiles.verifyDataInExcelBook(workbookExpect, workbookActual, Arrays.asList(sheetNums));
    }

    public static void verifyAllSheets(byte[] workbookExpectData, byte[] workbookActualData) throws IOException{
        var workbookExpect = openWorkbook(workbookExpectData);
        var workbookActual = openWorkbook(workbookActualData);

        MSE_CompareExcelFiles.verifyIfExcelFilesHaveSameNumberAndNameOfSheets(workbookExpect, workbookActual);
        MSE_CompareExcelFiles.verifySheetsInExcelFilesHaveSameRowsAndColumns(workbookExpect, workbookActual);
        MSE_CompareExcelFiles.verifyDataInExcelBookAllSheets(workbookExpect, workbookActual);
    }

    /*
    This method compares if both excel files have same number of sheets and same
    name of sheets
    */
    public static void verifyIfExcelFilesHaveSameNumberAndNameOfSheets(Workbook workbook1, Workbook workbook2) {
        System.out.println("Verifying if both work books have same number of sheets.............");
        // Get total sheets count from first excel file
        int sheetsInWorkbook1 = workbook1.getNumberOfSheets();
        // Get total sheets count from second excel file
        int sheetsInWorkbook2 = workbook2.getNumberOfSheets();
        // Compare if both excel files have same number of sheets
        assertEquals(sheetsInWorkbook1, sheetsInWorkbook2,
                "Excel work books have different number of sheets. \n "
                        + "Sheets in work book 1 : "+sheetsInWorkbook1 +"\n "
                        + "Number of sheets in work book 2 : "+sheetsInWorkbook2);
        // Printing number of sheets in each work book
        System.out.println("Sheets in first work book : "+sheetsInWorkbook1);
        System.out.println("Sheets in second work book : "+sheetsInWorkbook2);
        System.out.println("Both work books have same number of sheets.........................");

        // Verify if sheets have same name in both workbooks
        // Sheets may not be in same order in both excel. So I am relaxing order of sheets condition.
        // Change i as required.
        System.out.println("Verifying if both work books have same name of sheets.............");
        List<String> sheetsNameOfWb1 = new ArrayList<>();
        List<String> sheetsNameOfWb2 = new ArrayList<>();
        // Since we have already verified that both work books have same number of sheets so iteration can be done against any workbook sheet count
        for (int i = 0; i < sheetsInWorkbook1; i++) {
            // Retrieving sheet names from both work books and adding to different lists
            sheetsNameOfWb1.add(workbook1.getSheetName(i));
            sheetsNameOfWb2.add(workbook2.getSheetName(i));
        }
        // Since I am relaxing same sequence of sheets.
        Collections.sort(sheetsNameOfWb1);
        Collections.sort(sheetsNameOfWb2);
        assertEquals(sheetsNameOfWb1, sheetsNameOfWb2, "Provided excel work books have different name of sheets.");
        System.out.println("Sheet Names in first work book : "+sheetsNameOfWb1);
        System.out.println("Sheet Names in second work book : "+sheetsNameOfWb2);
        System.out.println("Both work books have same name of sheets.........................");
    }

    // This method compares if both excel files have same number of rows and corresponding columns
    public static void verifySheetsInExcelFilesHaveSameRowsAndColumns(Workbook workbook1, Workbook workbook2) {
        System.out.println(
                "Verifying if both work books have same number of rows and columns in all sheets.............");
        int sheetCounts = workbook1.getNumberOfSheets();
        for (int i = 0; i < sheetCounts; i++) {
            Sheet s1 = workbook1.getSheetAt(i);
            Sheet s2 = workbook2.getSheetAt(i);
            int rowsInSheet1 = s1.getPhysicalNumberOfRows();
            int rowsInSheet2 = s2.getPhysicalNumberOfRows();
            assertEquals(rowsInSheet1, rowsInSheet2, "Sheets have different count of rows..");

            Iterator<Row> rowInSheet1 = s1.rowIterator();
            Iterator<Row> rowInSheet2 = s2.rowIterator();
            while (rowInSheet1.hasNext()) {
                int cellCounts1 = rowInSheet1.next().getPhysicalNumberOfCells();
                int cellCounts2 = rowInSheet2.next().getPhysicalNumberOfCells();
                assertEquals(cellCounts1, cellCounts2, "Sheets have different count of columns..");
            }
        }
    }

    public static void verifyDataInExcelBookAllSheets(Workbook workbook1, Workbook workbook2) {
        int sheetCounts = workbook1.getNumberOfSheets();
        if (sheetCounts == 0) {
            throw new IllegalArgumentException();
        }
        var range = IntStream.rangeClosed(0, sheetCounts - 1).boxed().collect(Collectors.toList());
        verifyDataInExcelBook(workbook1, workbook2, range);
    }

    public static void verifyDataInExcelBook(Workbook workbook1, Workbook workbook2, List<Integer> sheetNums) {
        System.out.println("Verifying if both work books have same data.............");
        // Since we have already verified that both work books have same number of sheets so iteration can be done against any workbook sheet count
        int sheetCounts = workbook1.getNumberOfSheets();
        // So we will iterate through sheet by sheet
        for (var i: sheetNums) {
            // Get sheet at same index of both work books
            Sheet s1 = workbook1.getSheetAt(i);
            Sheet s2 = workbook2.getSheetAt(i);
            System.out.println("*********** Sheet Name : "+s1.getSheetName()+"*************");
            // Iterating through each row
            int rowCounts = s1.getPhysicalNumberOfRows();
            for (int j = 0; j < rowCounts; j++) {
                // Iterating through each cell
                if (s1.getRow(j) == null) {
                    System.out.println("WTF");
                }
                int cellCounts = s1.getRow(j).getPhysicalNumberOfCells();
                for (int k = 0; k < cellCounts; k++) {
                    // Getting individual cell
                    Cell c1 = s1.getRow(j).getCell(k);
                    Cell c2 = s2.getRow(j).getCell(k);

                    // Fick lägga till de här raderna för att slippa NullPointerException...
                    if (c1 == null && c2 == null) {
                        continue;
                    }
                    else if (c1 == null ^ c2 == null) {
                        fail("Non matching cell type.");
                    }

                    // Since cell have types and need o use different methods
                    else if (c1.getCellType().equals(c2.getCellType())) {
                        if (c1.getCellType() == CellType.STRING) {
                            String v1 = c1.getStringCellValue();
                            String v2 = c2.getStringCellValue();
                            assertEquals(v1, v2, "Cell values are different.....");
                            System.out.println("Its matched : "+ v1 + " === "+ v2);
                        }
                        if (c1.getCellType() == CellType.NUMERIC) {
                            // If cell type is numeric, we need to check if data is of Date type
                            if (DateUtil.isCellDateFormatted(c1) | DateUtil.isCellDateFormatted(c2)) {
                                // Need to use DataFormatter to get data in given style otherwise it will come as time stamp
                                DataFormatter df = new DataFormatter();
                                String v1 = df.formatCellValue(c1);
                                String v2 = df.formatCellValue(c2);
                                assertEquals(v1, v2, "Cell values are different.....");
                                System.out.println("Its matched : "+ v1 + " === "+ v2);
                            } else {
                                double v1 = c1.getNumericCellValue();
                                double v2 = c2.getNumericCellValue();
                                assertEquals(v1, v2, 1e-15, "Cell values are different.....");
                                System.out.println("Its matched : "+ v1 + " === "+ v2);
                            }
                        }
                        if (c1.getCellType() == CellType.BOOLEAN) {
                            boolean v1 = c1.getBooleanCellValue();
                            boolean v2 = c2.getBooleanCellValue();
                            assertEquals(v1, v2, "Cell values are different in cell " +
                                cellName(c1.getColumnIndex(), c1.getRowIndex()));
                            System.out.println("Its matched : "+ v1 + " === "+ v2);
                        }
                    } else
                    {
                        // If cell types are not same, exit comparison
                        fail("Non matching cell type in cell " +
                            (char)(c1.getColumnIndex() + 65) + (c1.getRowIndex() + 1) +
                            ": " + c1.getCellType() + " != " + c2.getCellType());
                    }
                }
            }
        }
        System.out.println("Hurray! Both work books have same data.");
    }

    private static Workbook openWorkbook(byte[] data) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        return new XSSFWorkbook(is);
    }

    private static String cellName(Integer colNum, Integer rowNum) {
        return "" + (char)(colNum + 65) + (rowNum + 1);
    }
}
