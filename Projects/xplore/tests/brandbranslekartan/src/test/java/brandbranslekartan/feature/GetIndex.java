package brandbranslekartan.feature;

import brandbranslekartan.shared.Config;
import common.WmsClient;
import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import io.restassured.response.Response;

import static io.restassured.RestAssured.get;

public class GetIndex {
	private final String baseUrl;

	private Response getResponse;

	public GetIndex(Config config) {
		this.baseUrl = config.getProperty("baseUrl");
	}

	@När("jag öppnar sidan")
	public void öppna_sidan() {
		getResponse = get(baseUrl);
	}

	@Så("ska startsidan hämtas utan problem")
	public void sida_hämtad() {
		getResponse.then()
				.statusCode(200)
				.contentType("text/html");
	}
}
