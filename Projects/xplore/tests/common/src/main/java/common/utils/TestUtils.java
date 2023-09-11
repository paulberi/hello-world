package common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TestUtils {

	/**
	 * Reads given resource file as a string.
	 *
	 * @param fileName the path to the resource file
	 * @return the file's contents or null if the file could not be opened
	 */
	public static String getResourceFileAsString(String fileName) throws IOException {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream is = classLoader.getResourceAsStream(fileName);
		if (is != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			return reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} else {
			throw new IOException("Could not read " + fileName);
		}
	}
}
