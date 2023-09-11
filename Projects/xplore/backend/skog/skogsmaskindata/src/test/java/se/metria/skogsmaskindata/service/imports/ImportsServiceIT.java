package se.metria.skogsmaskindata.service.imports;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import se.metria.skogsmaskindata.service.imports.exceptions.AlreadyImportedException;
import se.metria.skogsmaskindata.service.imports.exceptions.CorruptImportPackageException;
import se.metria.skogsmaskindata.service.imports.exceptions.ImportException;
import se.metria.skogsmaskindata.service.imports.model.Import;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.jpa.show-sql=false"})
@Transactional
class ImportsServiceIT {

	@Autowired
	private ImportService importService;

	private Logger logger = LoggerFactory.getLogger(ImportsServiceIT.class);

	@Test
	void processForestlinkPackage() throws ImportException {

		Import output = importService.processForestlinkPackage(getPackage("/imports/exempel_resultatpaket_mb.zip"));

		assertNotNull(output);
		assertNotNull(output.getId());
		assertEquals("G1254789", output.getObjektnummer());
		assertEquals("RO Jönköping", output.getOrganisation());
		assertEquals("Markberedning", output.getPakettyp());
		assertEquals("2016-08-12T15:05:17Z", output.getEnddate().toString());
		assertTrue(output.isCompleted());
		assertEquals(0, output.getInformationslinjer().size());
		assertEquals(0, output.getInformationspolygoner().size());
		assertEquals(0, output.getInformationspunkter().size());
		assertEquals(2758, output.getKorspar().size());
		assertEquals(4, output.getKorsparForTransport().size());
		assertEquals(7, output.getProvytor().size());
		assertEquals(1, output.getResultat().size());
	}

	@Test
	void processForestlinkPackage_existing() throws ImportException {
		Import imp1 = importService.processForestlinkPackage(getPackage("/imports/exempel_resultatpaket_mb.zip"));

		assertNotNull(imp1);

		assertThrows(AlreadyImportedException.class, () -> {
			importService.processForestlinkPackage(getPackage("/imports/exempel_resultatpaket_mb.zip"));
		});
	}

	@Test
	void processForestlinkPackage_no_info_xml() {
		ImportException e = assertThrows(ImportException.class, () -> {
			importService.processForestlinkPackage(getPackage("/imports/G1254789_no_info_xml.zip"));
		});

		assertTrue(e.getMessage().contains("info.xml"));
	}

	@Test
	void processForestlinkPackage_corrupt_info_xml() {
		assertThrows(ImportException.class, () -> {
			importService.processForestlinkPackage(getPackage("/imports/G1254789_corrupt_info_xml.zip"));
		});
	}

	@Test
	void processForestlinkPackage_no_pdf() throws ImportException {
		Import output = importService.processForestlinkPackage(getPackage("/imports/G1254789_G.zip"));

		assertNotNull(output);
		assertNotNull(output.getId());
		assertEquals("G1254789", output.getObjektnummer());
		assertEquals("RO Jönköping", output.getOrganisation());
		assertEquals("Markberedning", output.getPakettyp());
		assertEquals("2016-08-12T15:05:17Z", output.getEnddate().toString());
		assertTrue(output.isCompleted());
		assertEquals(0, output.getInformationslinjer().size());
		assertEquals(0, output.getInformationspolygoner().size());
		assertEquals(0, output.getInformationspunkter().size());
		assertEquals(2758, output.getKorspar().size());
		assertEquals(4, output.getKorsparForTransport().size());
		assertEquals(7, output.getProvytor().size());
		assertEquals(1, output.getResultat().size());
	}

	@Test
	void processForestlinkPackage_missing_shapefile() throws ImportException {
		assertThrows(CorruptImportPackageException.class, () -> {
			importService.processForestlinkPackage(getPackage("/imports/G1254789_utan_korspar.zip"));
		});

		// The previous transaction should have been rolled back and it should be
		// possible to insert a corrected file with the same id
		Import output = importService.processForestlinkPackage(getPackage("/imports/G1254789_G.zip"));

		assertNotNull(output);
		assertNotNull(output.getId());
		assertEquals("G1254789", output.getObjektnummer());
		assertEquals("RO Jönköping", output.getOrganisation());
		assertEquals("Markberedning", output.getPakettyp());
		assertEquals("2016-08-12T15:05:17Z", output.getEnddate().toString());
		assertTrue(output.isCompleted());
		assertEquals(0, output.getInformationslinjer().size());
		assertEquals(0, output.getInformationspolygoner().size());
		assertEquals(0, output.getInformationspunkter().size());
		assertEquals(2758, output.getKorspar().size());
		assertEquals(4, output.getKorsparForTransport().size());
		assertEquals(7, output.getProvytor().size());
		assertEquals(1, output.getResultat().size());
	}

	@Test
	void processForestlinkPackageBatch() throws ImportException, URISyntaxException, FileNotFoundException {
		Collection<File> files = getFileList("/imports/batch");

		for (File file : files) {
			logger.debug("Trying to import file {}", file.getName());
			Import output = importService.processForestlinkPackage(new FileInputStream(file));
			assertNotNull(output);
		}
	}

	private InputStream getPackage(String path) {
		return getClass().getResourceAsStream(path);
	}

	private Collection<File> getFileList(String path) throws URISyntaxException {
		return FileUtils.listFiles(Paths.get(getClass().getResource(path).toURI()).toFile(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}
}