package se.metria.xplore.keycloak.service;

import org.junit.jupiter.api.Test;
import se.metria.xplore.keycloak.KeyCloakProperties;

import java.util.UUID;

import static org.junit.Assert.*;

class KeyCloakServiceIT {
	static KeyCloakProperties properties() {
		KeyCloakProperties keyCloakProperties = new KeyCloakProperties();
		keyCloakProperties.setBaseUrl(System.getProperty("keycloak.baseUrl"));
		keyCloakProperties.setAdminBaseUrl(System.getProperty("keycloak.adminBaseUrl"));
		keyCloakProperties.setAppRealm(System.getProperty("keycloak.appRealm"));
		keyCloakProperties.setAdminRealm(System.getProperty("keycloak.adminRealm"));
		keyCloakProperties.setAdminUser(System.getProperty("keycloak.adminUser"));
		keyCloakProperties.setAdminPassword(System.getProperty("keycloak.adminPassword"));
		return keyCloakProperties;
	}

	@Test
	void testGetToken() {
		KeyCloakService keyCloakService = new KeyCloakService(properties());
		KeyCloakService.Token token = keyCloakService.getKeyCloakAdminToken(true).block();
		assertNotNull(token);
		assertNotNull(token.getAccess_token());
	}

	@Test
	void testCreateUser() {
		KeyCloakService keyCloakService = new KeyCloakService(properties());
		String username = "testuser" + UUID.randomUUID().toString();
		KeyCloakService.CreateUserResult user = keyCloakService.createUser(username, "Test", "Test", UUID.randomUUID().toString());
		assertEquals(KeyCloakService.CreateUserResult.USER_CREATED, user);

		KeyCloakService.UserRepresentation[] newUser = keyCloakService.findUser(username);
		assertEquals(1, newUser.length);
		assertNotNull(newUser[0].id);
		keyCloakService.deleteUser(newUser[0].id);

		KeyCloakService.UserRepresentation[] result = keyCloakService.findUser(username);
		assertEquals(0, result.length);
	}
}
