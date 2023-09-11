package se.metria.xplore.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app-config")
public class AppConfigProperties {

    @Value("${configuration.use-config-from-resource:false}")
    private boolean useConfigFromResource;

    public boolean isUseConfigFromResource() {
        return useConfigFromResource;
    }

    public void setUseConfigFromResource(boolean useConfigFromResource) {
        this.useConfigFromResource = useConfigFromResource;
    }
}
