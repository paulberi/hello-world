package se.metria.skogsmaskindata.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import se.metria.skogsmaskindata.service.imports.model.Info;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.Test;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUtilsTest {

	@Test
	void parseXml_info() throws JAXBException {
		File testxml = new File(getClass().getResource("/info.xml").getFile());

		Info info = FileUtils.parseXml(testxml, Info.class);

		assertTrue(info.getEnddate().equals("2016-08-12 15:05:17"));
		assertTrue(info.getExecutor().equals("RO Jönköping"));
		assertTrue(info.getObjectNumber().equals("G1254789"));
		assertTrue(info.getPurpose().equals("Markberedning"));
	}

	@Test
	void createAndDeleteTempDir() throws IOException {
		File tempDir = FileUtils.createTempDirectory("testdirectory");
		tempDir.deleteOnExit();

		assertTrue(tempDir.exists());
		assertTrue(tempDir.isDirectory());

		FileUtils.deleteDirectory(tempDir);

		assertFalse(tempDir.exists());
	}

	@Test
	void unzipFile() throws IOException, ArchiveException {
		File tempDir = FileUtils.createTempDirectory("testdirectory");
		tempDir.deleteOnExit();

		InputStream testpaket = getClass().getResourceAsStream("/imports/exempel_resultatpaket_mb.zip");

		FileUtils.unzipFile(testpaket, tempDir);
		Collection<File> files = listFiles(tempDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

		assertTrue(files.size() == 21);
	}
}
