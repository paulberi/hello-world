package se.metria.xplore.admin.util;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper for static WorkbookFactory.create(file) for easy dependency injection.
 */
public class WorkbookFactoryWrapper {
    /**
     * Wraps static method WorkbookFactory.create(file). Creates a Workbook from a FileInputStream.
     * @param is (InputStream)
     * @return (Workbook)
     * @throws IOException
     */
    public Workbook createWorkbook(InputStream is) throws IOException {
        return WorkbookFactory.create(is);
    }
}
