package se.metria.xplore.keycloak.service;

import okhttp3.mockwebserver.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import se.metria.xplore.keycloak.KeyCloakProperties;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class KeyCloakServiceTest {

    public static MockWebServer mockBackEnd;
    String baseUrl;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();

        QueueDispatcher dispatcher = new QueueDispatcher();
        dispatcher.setFailFast(true);
        mockBackEnd.setDispatcher(dispatcher);

        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
    }

    @Test
    void createUser() {
        KeyCloakProperties keyCloakProperties = new KeyCloakProperties();
        keyCloakProperties.setBaseUrl(baseUrl);
        keyCloakProperties.setAdminBaseUrl(baseUrl);
        keyCloakProperties.setAppRealm("Xplore");
        keyCloakProperties.setAdminRealm("master");
        keyCloakProperties.setAdminUser("matdatabas");
        keyCloakProperties.setAdminPassword("adminPassword");

        KeyCloakService keyCloakService = new KeyCloakService(keyCloakProperties);

        // One http token request expected
        enqueueMockToken("Token1");

        // Create user request expected
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Location",baseUrl+"/auth/admin/master/console/#/realms/Xplore/users/b3a446ba-2369-45f1-b9db-0fcdde4d4cd9")
                .setResponseCode(HttpStatus.CREATED.value())
                .setBody(""));

        // Set password request expected
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .setResponseCode(HttpStatus.OK.value())
                .setBody(""));

        // Call service
        KeyCloakService.CreateUserResult status = keyCloakService.createUser("user", "firstName", "lastName", "password");

        assertEquals(KeyCloakService.CreateUserResult.USER_CREATED, status);
    }

    @Test
    void createUserOldToken() {
        KeyCloakProperties keyCloakProperties = new KeyCloakProperties();
        keyCloakProperties.setBaseUrl(baseUrl);
        keyCloakProperties.setAdminBaseUrl(baseUrl);
        keyCloakProperties.setAppRealm("Xplore");
        keyCloakProperties.setAdminRealm("master");
        keyCloakProperties.setAdminUser("matdatabas");
        keyCloakProperties.setAdminPassword("adminPassword");

        KeyCloakService keyCloakService = new KeyCloakService(keyCloakProperties);

        // First http token request expected
        enqueueMockToken("Token1");

        // Create user request expected, answer authentication error
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .setResponseCode(HttpStatus.UNAUTHORIZED.value())
                .setBody(""));

        // Second http token request expected
        enqueueMockToken("Token2");

        // Create user request expected
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Location",baseUrl+"/auth/admin/master/console/#/realms/Xplore/users/b3a446ba-2369-45f1-b9db-0fcdde4d4cd9")
                .setResponseCode(HttpStatus.CREATED.value())
                .setBody(""));

        // Set password request expected
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .setResponseCode(HttpStatus.OK.value())
                .setBody(""));

        // Call service
        KeyCloakService.CreateUserResult status = keyCloakService.createUser("user", "firstName", "lastName", "password");

        assertEquals(KeyCloakService.CreateUserResult.USER_CREATED, status);
    }

    @Test
    void createUserUserExists() {
        KeyCloakProperties keyCloakProperties = new KeyCloakProperties();
        keyCloakProperties.setBaseUrl(baseUrl);
        keyCloakProperties.setAdminBaseUrl(baseUrl);
        keyCloakProperties.setAppRealm("Xplore");
        keyCloakProperties.setAdminRealm("master");
        keyCloakProperties.setAdminUser("matdatabas");
        keyCloakProperties.setAdminPassword("adminPassword");

        KeyCloakService keyCloakService = new KeyCloakService(keyCloakProperties);

        // One http token request expected
        enqueueMockToken("Token1");

        // Create user request expected
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Location","https://keycloak-utv.prodstod.se/auth/admin/master/console/#/realms/Xplore/users/b3a446ba-2369-45f1-b9db-0fcdde4d4cd9")
                .setResponseCode(HttpStatus.CONFLICT.value())
                .setBody(""));


        // Call service
        KeyCloakService.CreateUserResult status = keyCloakService.createUser("user", "firstName", "lastName", "password");

        assertEquals(KeyCloakService.CreateUserResult.USER_EXISTS, status);
    }

    @Test
    void getKeyCloakToken() throws InterruptedException {
        KeyCloakProperties keyCloakProperties = new KeyCloakProperties();
        keyCloakProperties.setBaseUrl(baseUrl);
        keyCloakProperties.setAdminBaseUrl(baseUrl);
        keyCloakProperties.setAppRealm("Xplore");
        keyCloakProperties.setAdminRealm("master");
        keyCloakProperties.setAdminUser("matdatabas");
        keyCloakProperties.setAdminPassword("adminPassword");

        KeyCloakService keyCloakService = new KeyCloakService(keyCloakProperties);

        {
            // Get token, cache is empty

            // One http request expected
            enqueueMockToken("Token1");

            KeyCloakService.Token token = keyCloakService.getKeyCloakAdminToken(false).block();
            assertEquals("Token1", token.getAccess_token());

            // Check that the request was done
            RecordedRequest request1 = mockBackEnd.takeRequest();
            assertEquals("/auth/realms/master/protocol/openid-connect/token", request1.getPath());
        }

        {
            // Get cached token
            // No request to server should happen
            KeyCloakService.Token token = keyCloakService.getKeyCloakAdminToken(false).block();
            assertEquals("Token1", token.getAccess_token());
        }

        {
            // Update cached token
            // One http request expected
            enqueueMockToken("Token2");

            KeyCloakService.Token token = keyCloakService.getKeyCloakAdminToken(true).block();
            assertEquals("Token2", token.getAccess_token());

            // Check that the request was done
            RecordedRequest request1 = mockBackEnd.takeRequest();
            assertEquals("/auth/realms/master/protocol/openid-connect/token", request1.getPath());
        }
    }

    private void enqueueMockToken(String accessToken) {
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .setBody("{\"access_token\": \""+accessToken+"\"}"));
    }
}
