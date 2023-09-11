package se.metria.markkoll;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

public class PropertyOverrideContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (SystemUtils.IS_OS_WINDOWS) {
            var environment = applicationContext.getEnvironment();

            var props = new Properties();
            removeUtf8(props, environment, "zonky.test.database.postgres.initdb.properties.lc-collate");
            removeUtf8(props, environment, "zonky.test.database.postgres.initdb.properties.lc-monetary");
            removeUtf8(props, environment, "zonky.test.database.postgres.initdb.properties.lc-numeric");

            environment.getPropertySources().addLast(new PropertiesPropertySource("windowsProps", props));
        }
    }

    private void removeUtf8(Properties properties, Environment environment, String key) {
        var property = environment.getProperty(key);

        if (property != null) {
            properties.put(key, property.replace(".UTF-8", ""));
        }
    }
}