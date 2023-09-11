package se.metria.skogsmaskindata.utils;

import java.io.*;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Enumeration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import static org.apache.commons.io.FileUtils.listFiles;

public class FileUtils {

	/**
	 * First writes the zip-file from the InputStream to a file in the target directory, then
	 * reads and unpacks the contents of zip-file to the same directory
	 * @param initialStream Inputstream of the zip-file to be unpacked
	 * @param directory Target directory
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void unzipFile(InputStream initialStream, File directory) throws IOException, ArchiveException {
		File file = new File(directory, "package.zip");
		org.apache.commons.io.FileUtils.copyInputStreamToFile(initialStream, file);

		Enumeration<ZipArchiveEntry> entries;

		try (ZipFile zipFile = new ZipFile(file)) {
			entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipArchiveEntry entry = entries.nextElement();
				File entryFile = new File(directory,  entry.getName());

				if (!entryFile.getCanonicalPath().startsWith(directory.getCanonicalPath() + File.separator)) {
					throw new ArchiveException("Entry is outside of the target dir: " + entry.getName());
				}

				if (entry.isDirectory()) {
					entryFile.mkdirs();
				} else {
					entryFile.getParentFile().mkdirs();
					try (InputStream in = zipFile.getInputStream(entry); OutputStream out = new FileOutputStream(entryFile)) {
						IOUtils.copy(in, out);
					}
				}
			}
		}
	}

	public static File createTempDirectory(String prefix) throws IOException {
		return Files.createTempDirectory(prefix).toFile();
	}

	/**
	 * Recursively deletes the target directory
	 * @param directory
	 */
	public static void deleteDirectory(File directory) {
		try {
			org.apache.commons.io.FileUtils.deleteDirectory(directory);
		} catch(Exception e) {
		}
	}

	/**
	 *  Parses and unmarshalls an xml file to an object
	 * @param file Xml file
	 * @param cls Unmarshalled object class. This class needs to mark the attributes that are to be mapped, see model/Info.java as an example
	 * @param <T>
	 * @return Object of class cls
	 * @throws JAXBException
	 */
	public static <T> T parseXml(File file, Class<T> cls) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(cls);
		return cls.cast(context.createUnmarshaller()
				.unmarshal(file));
	}

	public static File findFile(File directory, IOFileFilter filter, boolean required) throws FileNotFoundException {
		Collection<File> files = listFiles(directory, filter, TrueFileFilter.INSTANCE);
		if (files.isEmpty() && required) {
			throw new FileNotFoundException("Missing " + filter.toString() + " file");
		}

		return files.isEmpty() ? null : files.iterator().next();
	}
}
