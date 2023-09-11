package se.metria.skogsmaskindata;

import org.junit.jupiter.api.Test;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PingControllerE2E {

	static RequestSpecification pingApi() {
		return given()
				.port(9007);
	}

	@Test
	public void ping() {
		pingApi().
				when().
				get("/ping").
				then().
				assertThat().
				statusCode(200);
		System.out.println("PING!");
	}

}