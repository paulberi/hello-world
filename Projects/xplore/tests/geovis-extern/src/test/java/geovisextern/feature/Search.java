package geovisextern.feature;

import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import geovisextern.shared.Config;

import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class Search {
	private final Config config;
	private final String geocodeUrl;

	private String query;
	private List<String> result;

	public Search(Config config) {
		this.config = config;
		this.geocodeUrl = config.getProperty("baseUrl") + "/sok/geocode";
	}

	@Givet("en adress som ligger inom kommunens gränser")
	public void en_adress_som_ligger_inom_kommunens_gränser() {
		query = config.getProperty("existingAddress");
	}

	@Givet("en adress som ligger utanför kommunens gränser")
	public void en_adress_som_ligger_utanför_kommunens_gränser() {
        query = config.getProperty("nonExistingAddress");
	}

	@När("jag söker på adressen")
	public void sök_på_adressen() {
		result = given()
				.queryParam("q", query)
				.get(geocodeUrl)
				.then()
				.statusCode(200)
				.extract().jsonPath().getList("address");
	}

	@Så("ska den ingå i träfflistan")
	public void ingår_i_träfflistan() {
       assertTrue(query + " ska ingå i resultatet", result.stream().anyMatch(address -> address.startsWith(query)));
	}

	@Så("ska den inte ingå i träfflistan")
	public void ingår_inte_träfflistan() {
		assertFalse(query + " ska inte ingå i resultatet", result.stream().anyMatch(address -> address.startsWith(query)));
	}
}
