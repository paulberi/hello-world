package common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LoginSession {
	private final ObjectMapper mapper;
	private String accessToken;

	public LoginSession() {
		this.mapper = new ObjectMapper();
	}

	public void login(String tokenUrl, String clientId, String username, String password) {
		this.accessToken = given()
				.formParam("client_id", clientId)
				.formParam("username", username)
				.formParam("password", password)
				.formParam("scope", "openid")
				.formParam("grant_type", "password")
				.post(tokenUrl)
				.then()
				.statusCode(200)
				.extract().body().jsonPath().getString("access_token");
	}

	/**
	 * Extracts the "payload" part of a JWT, parses the JSON, and returns it as a map.
	 */
	public Map<String, Object> getClaims() throws IOException {
		// See https://jwt.io/introduction/ for the structure of a JWT.
		String payload = accessToken.substring(accessToken.indexOf(".") + 1, accessToken.lastIndexOf("."));
		byte[] decodedPayload = Base64.getDecoder().decode(payload);
		return mapper.readValue(decodedPayload, HashMap.class);

	}

	public boolean isAnonymous() {
		return accessToken == null;
	}

	public String getAccessToken() {
		return accessToken;
	}
}
