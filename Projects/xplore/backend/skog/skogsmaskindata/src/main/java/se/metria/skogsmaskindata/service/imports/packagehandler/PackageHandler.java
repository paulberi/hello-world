package se.metria.skogsmaskindata.service.imports.packagehandler;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import se.metria.skogsmaskindata.service.imports.exceptions.CorruptImportPackageException;
import se.metria.skogsmaskindata.service.imports.exceptions.InternalImportException;
import se.metria.skogsmaskindata.service.imports.model.Info;
import se.metria.skogsmaskindata.service.imports.shapefile.ShapefileException;
import se.metria.skogsmaskindata.service.imports.shapefile.ShapefileHandler;

import javax.xml.bind.JAXBException;
import java.io.*;

import static se.metria.skogsmaskindata.utils.FileUtils.*;

public class PackageHandler implements Closeable {

	private InputStream inputStream;
	private File dir;
	private File infoFile;
	private File pdfFile;
	private ShapefileHandler shapefileHandler;

	public PackageHandler(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void unzip() throws InternalImportException, CorruptImportPackageException {
		try {
			dir = createTempDirectory("temp");
		} catch (IOException e) {
			throw new InternalImportException(e.getMessage());
		}

		try {
			unzipFile(inputStream, dir);
		} catch (IOException | ArchiveException e) {
			throw new CorruptImportPackageException(e.getMessage());
		}
	}

	public void extractInfoFile() throws CorruptImportPackageException {
		try {
			this.infoFile = findFile(dir, new NameFileFilter("info.xml", IOCase.INSENSITIVE), true);
		} catch (FileNotFoundException e) {
			throw new CorruptImportPackageException(e.getMessage());
		}
	}

	public Info getInfo() throws CorruptImportPackageException {
		try {
			return infoFile != null  ? parseXml(infoFile, Info.class) : null;
		} catch (JAXBException e) {
			throw new CorruptImportPackageException(e.getMessage());
		}
	}

	public void extractPdfFile() {
		try {
			this.pdfFile = findFile(dir, new SuffixFileFilter("pdf", IOCase.INSENSITIVE), false);
		} catch (FileNotFoundException e) {
			// Intentional suppress since PDF is optional
		}
	}

	public void readShapefiles() throws CorruptImportPackageException {
		try {
			shapefileHandler = new ShapefileHandler(dir);
			shapefileHandler.readFiles();
		} catch (ShapefileException e) {
			throw new CorruptImportPackageException(e.getMessage());
		}
	}

	public void close() {
		if (dir != null) {
			deleteDirectory(dir);
		}
	}

	public File getInfoFile() {
		return infoFile;
	}

	public File getPdfFile() {
		return pdfFile;
	}

	public ShapefileHandler getShapefileHandler() {
		return shapefileHandler;
	}
}
