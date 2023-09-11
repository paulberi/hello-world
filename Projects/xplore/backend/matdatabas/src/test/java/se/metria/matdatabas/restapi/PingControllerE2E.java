package se.metria.matdatabas.restapi;

import org.junit.jupiter.api.Test;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

class PingControllerE2E {

	RequestSpecification pingApi() {
		return given()
				.port(9008)
				.basePath("/api/ping")
				.redirects().follow(false);
	}

	@Test
	void ping() {
		pingApi().
				when().
				get("/anon-ping").
				then().
				assertThat().
				statusCode(200)
				.body(equalTo("pong!"));
	}

	@Test
	void authPing() {
		pingApi().
				when().
				get("/auth-ping").
				then().
				assertThat().
				statusCode(403);
	}
}