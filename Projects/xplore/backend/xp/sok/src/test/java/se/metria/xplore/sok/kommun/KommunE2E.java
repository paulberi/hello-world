package se.metria.xplore.sok.kommun;

import io.restassured.path.json.JsonPath;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import se.metria.xplore.sok.SokControllerBaseE2E;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class KommunE2E extends SokControllerBaseE2E {

	private final String kommunQuery = "/kommun?q={q}";
	private final String kommunLanQuery = "/kommun?q={q}&l={l}";

	@Test
	void getKommun_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunQuery, "luleå").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":1")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Luleå\""));
	}

	@Test
	void getKommun_multi_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunQuery, "ar").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":4")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Arboga\"")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Arjeplog\"")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Arvidsjaur\"")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Arvika\""));
	}

	@Test
	void getKommun_merged_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunQuery, "östersund").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":1")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Östersund\""));

		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunQuery, "leksand").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":1")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Leksand\""));

		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunQuery, "KROKOM").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":1")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Krokom\""));

		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunQuery, "Strömsund").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":1")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Strömsund\""));
	}

	@Test
	void getKommun_merged_many_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunQuery, "öster").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":2")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Östersund\"")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Österåker\""));
	}

	@Test
	void getKommun_lan_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunLanQuery, "ar", "norr").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":2")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Arjeplog\"")).
				and().
				assertThat().body(CoreMatchers.containsString("\"KOMMUNNAMN\":\"Arvidsjaur\""));
	}

	@Test
	void getKommun_success_but_empty() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunQuery, "norge").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":0"));
	}

	@Test
	void getKommun_failure() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(kommunQuery, "").
				then().
				assertThat().
				statusCode(400);
	}

	@Test
	void getKommun_auth_fail() {
		given().
				header(HttpHeaders.AUTHORIZATION, "").
				when().
				get(kommunQuery, "LULEÅ").
				then().
				assertThat().
				statusCode(500);
	}

	@Test
	void getKommun_and_verify_kommunnamn() {
		JsonPath jsonPath = get(kommunQuery, new String[]{"LULEÅ"});
		assertThat(jsonPath.getList("features").size(), equalTo(1));
		assertThat(jsonPath.getList("features.properties.KOMMUNNAMN"), hasItem(is("Luleå")));

		jsonPath = get(kommunQuery, new String[]{"malung"});
		assertThat(jsonPath.getList("features").size(), equalTo(1));
		assertThat(jsonPath.getList("features.properties.KOMMUNNAMN"), hasItem(is("Malung-Sälen")));

		jsonPath = get(kommunQuery, new String[]{"dals-ed"});
		assertThat(jsonPath.getList("features").size(), equalTo(1));
		assertThat(jsonPath.getList("features.properties.KOMMUNNAMN"), hasItem(is("Dals-Ed")));

		jsonPath = get(kommunQuery, new String[]{"lilla"});
		assertThat(jsonPath.getList("features").size(), equalTo(1));
		assertThat(jsonPath.getList("features.properties.KOMMUNNAMN"), hasItem(is("Lilla Edet")));
	}

	@Test
	void getKommun_with_lan_and_verify_lansnamn() {
		JsonPath jsonPath = get(kommunLanQuery, new String[]{"so", "väst"});

		assertThat(jsonPath.get("totalFeatures"), equalTo(3));
		assertThat(jsonPath.getList("features").size(), equalTo(3));
		assertThat(jsonPath.getList("features.properties.KOMMUNNAMN"), hasItem(is("Sorsele")));
		assertThat(jsonPath.getList("features.properties.KOMMUNNAMN"), hasItem(is("Sollefteå")));
		assertThat(jsonPath.getList("features.properties.KOMMUNNAMN"), hasItem(is("Sotenäs")));
		assertThat(jsonPath.getList("features.properties.LANSNAMN"), hasItem(is("Västerbottens län")));
		assertThat(jsonPath.getList("features.properties.LANSNAMN"), hasItem(is("Västernorrlands län")));
		assertThat(jsonPath.getList("features.properties.LANSNAMN"), hasItem(is("Västra Götalands län")));
	}

	private JsonPath get(String queryTemplate, String... query) {
		return given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(queryTemplate, query).
				then().
				assertThat().
				statusCode(200).
				extract().jsonPath();
	}
}