package se.metria.xplore.keycloak;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("keycloakadmin")
public class KeyCloakProperties {
    private String baseUrl;

    private String adminBaseUrl;

    // Xplore
    private String appRealm;

    private String appClient;

    // master
    private String adminRealm;

    // matdatabas
    private String adminUser;

    // goTest4it!
    private String adminPassword;

    private String batchUser;
    private String batchPassword;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAdminBaseUrl() {
        return adminBaseUrl;
    }

    public void setAdminBaseUrl(String adminBaseUrl) {
        this.adminBaseUrl = adminBaseUrl;
    }

    public String getAppRealm() {
        return appRealm;
    }

    public String getAppClient() {
        return appClient;
    }

    public void setAppClient(String appClient) {
        this.appClient = appClient;
    }

    public void setAppRealm(String appRealm) {
        this.appRealm = appRealm;
    }

    public String getAdminRealm() {
        return adminRealm;
    }

    public void setAdminRealm(String adminRealm) {
        this.adminRealm = adminRealm;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getBatchUser() {
        return batchUser;
    }

    public void setBatchUser(String batchUser) {
        this.batchUser = batchUser;
    }

    public String getBatchPassword() {
        return batchPassword;
    }

    public void setBatchPassword(String batchPassword) {
        this.batchPassword = batchPassword;
    }
}
