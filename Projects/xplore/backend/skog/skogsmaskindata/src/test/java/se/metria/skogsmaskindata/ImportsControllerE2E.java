package se.metria.skogsmaskindata;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ImportsControllerE2E {

	static RequestSpecification restApi() {
		return given()
				.port(9007);
	}

	public void removeImportsFromDB(List<String> packageNames) {
		//Not implemented
	}

	private void uploadPackages(List<String> packageNames) {
		//Not implemented
	}

	@Test
	public void get_readImports_successful() {
		restApi().
				when().
				post("/readAll").
				then().
				assertThat().
				statusCode(200);
	}

}