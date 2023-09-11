package skogsanalys.feature;

import common.LoginSession;
import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import skogsanalys.shared.Config;

import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class SearchAddress {
	private final LoginSession session;
	private final String geocodeUrl;

	private String x;
	private String y;
	private List<String> result;

	public SearchAddress(Config config, LoginSession session) {
		this.session = session;
		this.geocodeUrl = config.getProperty("baseUrl") + "/sok/geocode";
	}

	@Givet("att kartan är centrerad över Luleå")
	public void kartan_centrerad_över_luleå() {
		x = "829374";
		y = "7292288";
	}

	@När("jag söker på adresser med söksträngen {string}")
	public void utför_sökning(String query) {
		RequestSpecification requestSpecification = given()
				.filters(new RequestLoggingFilter())
				.header("Authorization", "Bearer " + session.getAccessToken())
				.queryParam("q", query);

		if (x != null && y != null) {
			requestSpecification = requestSpecification
					.queryParam("x", x)
					.queryParam("y", y);
		}

		result = requestSpecification
				.get(geocodeUrl)
				.then()
				.statusCode(200)
				.extract().jsonPath().getList("address");
	}

	@Så("ska staden Stockholm komma överst i träfflistan")
	public void stockholm_ingår_i_träfflistan() {
		assertTrue(!result.isEmpty());
		// Här kollar vi specifikt att vi får staden och inte nåt annat som råkar heta Stockholm
		assertEquals( "Stockholm, Sverige", result.get(0));
	}

	@Så("ska Mjölkuddsvägen, Luleå komma överst i träfflistan")
	public void mjölkuddsvägen_luleå_överst() {
		assertTrue(!result.isEmpty());
		assertEquals("Mjölkuddsvägen, Luleå, Sverige", result.get(0));
	}

	@Så("ska Kungsgatan, Luleå komma överst i träfflistan")
	public void kungsgatan_luleå_överst() {
		assertTrue(!result.isEmpty());
		assertEquals("Kungsgatan, Luleå, Sverige", result.get(0));
	}

}
