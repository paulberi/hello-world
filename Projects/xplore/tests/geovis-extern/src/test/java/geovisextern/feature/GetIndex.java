package geovisextern.feature;

import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import geovisextern.shared.Config;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetIndex {
	private final String baseUrl;

	private Response getResponse;

	public GetIndex(Config config) {
		this.baseUrl = config.getProperty("baseUrl");
	}

	@När("jag öppnar sidan")
	public void öppna_sidan() {
		getResponse = given().filters(new RequestLoggingFilter()).get(baseUrl);
	}

	@Så("ska startsidan hämtas utan problem")
	public void sida_hämtad() {
		getResponse.then()
				.statusCode(200)
				.contentType("text/html");
	}
}
