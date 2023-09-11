package se.metria.xplore.admin.util;

import org.apache.poi.ss.usermodel.*;
import se.metria.xplore.admin.exceptions.IncompatibleOutputTypeException;
import se.metria.xplore.admin.model.KeycloakRole;
import se.metria.xplore.admin.model.KeycloakUser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Utility class to read excel files and map to objects.
 */
public class ExcelReader {
    private WorkbookFactoryWrapper workbookFactory;

    /**
     * Constructor.
     * @param workbookFactory (WorkbookFactoryWrapper) instance of wrapper class for org.apache.poi.ss.usermodel.WorkbookFactory.
     */
    public ExcelReader(WorkbookFactoryWrapper workbookFactory) {
        this.workbookFactory = workbookFactory;
    }

    /**
     * Attempt to create a list of objects of given type from an excel file.
     * @param file (FileInputStream) Excel file to read.
     * @param skipFirstRows (int) Skips given number of rows on each sheet when reading file.
     * @param numberOfSheets (int) Number of sheets to read. Value less than 0 or greater than number of sheets in file will read all sheets.
     * @param type (T) Type to attempt to map excel row content to.
     * @return (ArrayList) ArrayList of given type.
     * @throws IncompatibleOutputTypeException Thrown if excel row data can't be sufficiently mapped to given type.
     * @throws IOException
     */
    public <T> ArrayList<T> excelToObjects(InputStream file, int skipFirstRows, int numberOfSheets, Class<T> type) throws IncompatibleOutputTypeException, IOException {
        Workbook workbook = this.workbookFactory.createWorkbook(file);
        ArrayList<T> output = new ArrayList<>();

        int maxSheets = workbook.getNumberOfSheets();
        if (numberOfSheets > 0 && numberOfSheets < maxSheets) {
            maxSheets = numberOfSheets;
        }
        for (int i = 0; i < maxSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            int j = 0;
            for (Row row: sheet) {
                if (j >= skipFirstRows) {
                    //TODO: Googled around but not sure how to make this more generic.
                    // Is there a clever way to attempt to create an instance of type T with an arbitrary number of class
                    // variables without knowing anything about T? Right now supported classes needs to be added to
                    // if-statement below... Maybe take convert function as arg?
                    if (type == KeycloakUser.class) {
                        KeycloakUser keycloakUser = this.rowToKeycloakUser(row);
                        if (keycloakUser != null) {
                            output.add(type.cast(keycloakUser));
                        }
                    } else {
                        throw new IncompatibleOutputTypeException("Output type " + type.getCanonicalName() + " not supported.");
                    }
                }
                j++;
            }
        }

        return output;
    }

    private KeycloakUser rowToKeycloakUser(Row row) throws IncompatibleOutputTypeException {
        try {
            Cell c = row.getCell(0);
            //The first cell on a row NEEDS to contain a username to be valid.
            if (c != null && c.getCellType() != CellType.BLANK) {
                String username = c.getRichStringCellValue().getString().trim();

                c = row.getCell(1);
                String email = (c == null || c.getCellType() == CellType.BLANK) ? "" : c.getRichStringCellValue().getString().trim();

                c = row.getCell(2);
                String firstname = (c == null || c.getCellType() == CellType.BLANK) ? "" : c.getRichStringCellValue().getString().trim();

                c = row.getCell(3);
                String lastname = (c == null || c.getCellType() == CellType.BLANK) ? "" : c.getRichStringCellValue().getString().trim();

                c = row.getCell(4);
                String[] roleNames = (c == null || c.getCellType() == CellType.BLANK) ? new String[0] : c.getRichStringCellValue().getString()
                        .replaceAll("\\s+", "").split(",");
                ArrayList<KeycloakRole> roles = new ArrayList<>();
                for (String role : roleNames) {
                    roles.add(new KeycloakRole(role, ""));
                }

                c = row.getCell(5);
                boolean enabled = (c == null || c.getCellType() == CellType.BLANK) ? false : c.getBooleanCellValue();

                c = row.getCell(6);
                String tempPassword = (c == null || c.getCellType() == CellType.BLANK) ? "" : c.getRichStringCellValue().getString();

                return new KeycloakUser(username, email, firstname, lastname, roles, enabled, tempPassword);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IncompatibleOutputTypeException("Attempting to map given row data into type " +
                    KeycloakUser.class.getCanonicalName() + " resulted in an exception " + e.toString() + ": " + e.getMessage());
        }
    }
}
