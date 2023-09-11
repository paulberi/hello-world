package se.metria.xplore.admin.keycloak;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * KeyCloak API properties class.
 */
@ConfigurationProperties("keycloakadmin")
public class KeycloakProperties {
    private String url;
    private String ClientID;
    private String adminRealm;

    public String getUrl() {
        return this.url;
    }

    public String getClientID() {
        return this.ClientID;
    }

    public String getAdminRealm() {
        return this.adminRealm;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setClientID(String clientID) {
        this.ClientID = clientID;
    }

    public void setAdminRealm(String adminRealm) {
        this.adminRealm = adminRealm;
    }
}
