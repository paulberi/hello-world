package se.metria.xplore.configuration.exceptions;

import se.metria.xplore.configuration.model.AppConfigRequest;

public class ConfigurationNotFoundException extends Exception {
    private AppConfigRequest appConfigRequest;

    public ConfigurationNotFoundException(AppConfigRequest appConfigRequest) {
        super();
        this.appConfigRequest = appConfigRequest;
    }

    public AppConfigRequest getAppConfigRequest() {
        return appConfigRequest;
    }
}
