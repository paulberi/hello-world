package se.metria.xplore.admin.keycloak;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import javax.ws.rs.core.Response;

/**
 * Factory for easy Keycloak instance dependency injection and admin access token authentication.
 */
public class KeycloakFactory {
    KeycloakProperties properties;
    KeycloakBuilder builder;

    /**
     * Constructor. Instantiates a KeycloakBuilder for later use.
     */
    public KeycloakFactory() {
        this.builder = KeycloakBuilder.builder();
    }

    /**
     * Returns a Keycloak client connected to specified realm using provided access token.
     * This method is suitable if the access token is provided by a front-end application.
     * @param properties (KeycloakProperties) Properties needs to contain server url and admin realm.
     * @param realm (String) Name of realm to connect returned instance to.
     * @param accessToken (String) Access token to use when communicating with Keycloak. Sufficient admin privileges are needed.
     * @return (Keycloak) Created Keycloak client with privileges from provided access token.
     */
    public Keycloak getClient(KeycloakProperties properties, String realm, String accessToken) {
        this.properties = properties;

        return Keycloak.getInstance(
                this.properties.getUrl(),
                realm,
                this.properties.getClientID(),
                accessToken
        );
    }

    /**
     * Returns a Keycloak client connected to the master realm using provided access token.
     * This method is suitable if the access token is provided by a front-end application.
     * @param properties (KeycloakProperties) Properties needs to contain server url and admin realm.
     * @param accessToken (String) Access token to use when communicating with Keycloak. Sufficient admin privileges are needed.
     * @return (Keycloak) Created Keycloak client with privileges from provided access token.
     */
    public Keycloak getMasterClient(KeycloakProperties properties, String accessToken) {
        this.properties = properties;

        return Keycloak.getInstance(
                this.properties.getUrl(),
                this.properties.getAdminRealm(),
                this.properties.getClientID(),
                accessToken
        );
    }

    /**
     * Returns a Keycloak client connected to specified realm with privileges from provided master realm user.
     * The client will acquire an access token using provided login credentials. This method i suitable for cli-application.
     * @param properties (KeycloakProperties) Properties needs to contain server url and admin realm.
     * @param realm (String) Name of realm to connect returned instance to.
     * @param username (String) Username of admin realm user.
     * @param password (String) Password of admin realm user.
     * @return (Keycloak) Created Keycloak Client with admin realm user privileges.
     */
    public Keycloak getClient(KeycloakProperties properties, String realm, String username, String password) {
        this.properties = properties;

        return Keycloak.getInstance(
                this.properties.getUrl(),
                realm,
                this.properties.getClientID(),
                this.getAccessToken(username, password)
        );
    }

    /**
     * Returns a Keycloak client connected to the master realm with privileges from provided master realm user.
     * The client will acquire an access token using provided login credentials. This method i suitable for cli-application.
     * @param properties (KeycloakProperties) Properties needs to contain server url and admin realm.
     * @param username (String) Username of admin realm user.
     * @param password (String) Password of admin realm user.
     * @return (Keycloak) Created Keycloak Client with admin realm user privileges.
     */
    public Keycloak getMasterClient(KeycloakProperties properties, String username, String password) {
        this.properties = properties;

        return Keycloak.getInstance(
                this.properties.getUrl(),
                this.properties.getAdminRealm(),
                this.properties.getClientID(),
                this.getAccessToken(username, password)
        );
    }

    /**
     * Returns the user id of a user created by the Keycloak client.
     * Wrapping static method for easy dependency injection.
     * @param response (Response) Response to extract user id from.
     * @return (String) Created user id.
     */
    public String getCreatedUserId(Response response) {
        return CreatedResponseUtil.getCreatedId(response);
    }

    private String getAccessToken(String username, String password) {
        Keycloak keycloak = this.builder
                .serverUrl(this.properties.getUrl())
                .realm(this.properties.getAdminRealm())
                .username(username)
                .password(password)
                .clientId(this.properties.getClientID())
                .build();
        return keycloak.tokenManager().getAccessTokenString();
    }
}
