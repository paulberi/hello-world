package se.metria.xplore.configuration.model;

public class AppConfigRequest {
    public String app;
    public String config;


    public AppConfigRequest() {
    }

    public AppConfigRequest(String app, String config) {
        this.app = app;
        this.config = config;
    }
}
