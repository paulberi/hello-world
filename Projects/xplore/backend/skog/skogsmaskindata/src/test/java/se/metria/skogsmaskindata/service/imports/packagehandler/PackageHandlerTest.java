package se.metria.skogsmaskindata.service.imports.packagehandler;

import org.junit.jupiter.api.Test;
import se.metria.skogsmaskindata.service.imports.exceptions.CorruptImportPackageException;
import se.metria.skogsmaskindata.service.imports.exceptions.ImportException;
import se.metria.skogsmaskindata.service.imports.model.Info;
import se.metria.skogsmaskindata.service.imports.shapefile.ShapefileHandler;
import se.metria.skogsmaskindata.service.imports.shapefile.ShapefileType;

import java.io.File;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class PackageHandlerTest {

	@Test
	void testValid() throws ImportException {
		PackageHandler packageHandler = new PackageHandler(getPackageStream("/imports/exempel_resultatpaket_mb.zip"));

		packageHandler.unzip();
		packageHandler.extractInfoFile();
		packageHandler.extractPdfFile();
		packageHandler.readShapefiles();

		Info info = packageHandler.getInfo();
		assertNotNull(info);

		File infoFile = packageHandler.getInfoFile();
		assertNotNull(infoFile);

		File pdfFile = packageHandler.getPdfFile();
		assertNotNull(pdfFile);

		ShapefileHandler shapefileHandler = packageHandler.getShapefileHandler();
		assertNotNull(shapefileHandler);

		packageHandler.close();
	}

	@Test
	void testValidNoPdf() throws ImportException {
		PackageHandler packageHandler = new PackageHandler(getPackageStream("/imports/G1254789_G.zip"));

		packageHandler.unzip();
		packageHandler.extractInfoFile();
		packageHandler.extractPdfFile();
		packageHandler.readShapefiles();

		Info info = packageHandler.getInfo();
		assertNotNull(info);

		File infoFile = packageHandler.getInfoFile();
		assertNotNull(infoFile);

		File pdfFile = packageHandler.getPdfFile();
		assertNull(pdfFile);

		ShapefileHandler shapefileHandler = packageHandler.getShapefileHandler();
		assertNotNull(shapefileHandler);

		packageHandler.close();
	}

	@Test
	void testCorruptZip() {
		PackageHandler packageHandler = new PackageHandler(getPackageStream("/imports/G1254789_corrupt_file.zip"));

		assertThrows(CorruptImportPackageException.class, () -> {
			packageHandler.unzip();
		});

		packageHandler.close();
	}

	@Test
	void testNoInfoXml() throws ImportException {
		PackageHandler packageHandler = new PackageHandler(getPackageStream("/imports/G1254789_no_info_xml.zip"));

		packageHandler.unzip();

		assertThrows(CorruptImportPackageException.class, () -> {
			packageHandler.extractInfoFile();
		});

		assertNull(packageHandler.getInfo());

		packageHandler.close();
	}

	@Test
	void testCorruptInfoXml() throws ImportException {
		PackageHandler packageHandler = new PackageHandler(getPackageStream("/imports/G1254789_corrupt_info_xml.zip"));

		packageHandler.unzip();
		packageHandler.extractInfoFile();

		assertThrows(CorruptImportPackageException.class, () -> {
			packageHandler.getInfo();
		});

		packageHandler.close();
	}

	@Test
	void testMissingShapefile() throws ImportException {
		PackageHandler packageHandler = new PackageHandler(getPackageStream("/imports/G1254789_utan_resultat.zip"));

		packageHandler.unzip();
		packageHandler.extractInfoFile();
		packageHandler.extractPdfFile();

		assertThrows(CorruptImportPackageException.class, () -> {
			packageHandler.readShapefiles();
		});

		assertNotNull(packageHandler.getShapefileHandler());

		assertFalse(packageHandler.getShapefileHandler().getFeatures(ShapefileType.KORSPAR).isEmpty());
		assertNull(packageHandler.getShapefileHandler().getFeatures(ShapefileType.RESULTAT));

		packageHandler.close();
	}

	private InputStream getPackageStream(String path) {
		return getClass().getResourceAsStream(path);
	}
}
