package matdatabas.shared;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Config {

	private final Properties properties;

	public Config() throws IOException {
		String config = System.getProperty("matdatabas.config", "matdatabas-utv");
		properties = new Properties();
		try (InputStream input = Config.class.getClassLoader().getResourceAsStream("matdatabas/config/" + config + ".properties")) {
			properties.load(input);
		}
	}

	public String getProperty(String key) {
		return Objects.requireNonNull(properties.getProperty(key), key + " is not set");
	}
}
