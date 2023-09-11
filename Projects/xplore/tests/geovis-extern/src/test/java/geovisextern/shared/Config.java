package geovisextern.shared;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Config {

	private final Properties properties;

	public Config() throws IOException {
		String kommun = System.getProperty("geovis.config", "aneby-utv");
		properties = new Properties();
		try (InputStream input = Config.class.getClassLoader().getResourceAsStream("geovisextern/config/" + kommun + ".properties")) {
			properties.load(input);
		}
	}

	public String getProperty(String key) {
		return Objects.requireNonNull(properties.getProperty(key), key + " is not set");
	}
}
