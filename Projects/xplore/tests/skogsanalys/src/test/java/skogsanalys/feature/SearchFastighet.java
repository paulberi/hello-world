package skogsanalys.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import common.LoginSession;
import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import io.restassured.filter.log.RequestLoggingFilter;
import skogsanalys.shared.Config;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchFastighet {
	// Används som returvärde från tjänsten
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Fastighet {
		public Map<String, Object> properties;
	}

	private final LoginSession session;
	private final String fastighetsSokUrl;

	private List<Fastighet> response;

	public SearchFastighet(Config config, LoginSession session) {
		this.session = session;
		this.fastighetsSokUrl = config.getProperty("baseUrl") + "/sok/fastighet";
	}

	@När("jag söker på fastigheter med söksträngen {string}")
	public void utför_sökning(String query) {
		response = given()
				.filters(new RequestLoggingFilter())
				.header("Authorization", "Bearer " + session.getAccessToken())
				.queryParam("q", query)
				.get(fastighetsSokUrl)
				.then()
				.statusCode(200)
				.extract().jsonPath().getList("features", Fastighet.class);
	}

	@Så("ska LULEÅ FORELLEN komma överst i träfflistan")
	public void luleå_forellen_överst() {
		assertTrue(!response.isEmpty());
		assertEquals("LULEÅ", response.get(0).properties.get("kommunnamn"));
		assertEquals("FORELLEN", response.get(0).properties.get("trakt"));
	}

	@Så("ska jag bara få en exakt träff på LULEÅ HAREN 14")
	public void luleå_haren_exakt() {
		assertEquals(1, response.size());
		assertEquals("LULEÅ", response.get(0).properties.get("kommunnamn"));
		assertEquals("HAREN", response.get(0).properties.get("trakt"));
		assertEquals("14", response.get(0).properties.get("blockenhet"));
	}
}
