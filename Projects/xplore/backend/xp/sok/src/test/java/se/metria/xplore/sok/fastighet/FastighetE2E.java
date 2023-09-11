package se.metria.xplore.sok.fastighet;

import io.restassured.path.json.JsonPath;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import se.metria.xplore.sok.SokControllerBaseE2E;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class FastighetE2E extends SokControllerBaseE2E {

	private final String fastighetQuery = "/fastighet?q={q}";
	private final String fastighetKommunQuery = "/fastighet?q={q}&k={k}";

	@Test
	void getFastighet_success_without_kommun() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetQuery, "aneby smultronet").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":7"));
	}

	@Test
	void getFastighet_success_with_kommun() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetKommunQuery, "smultronet", "aneby").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":7"));
	}

	@Test
	void getFastighet_success_with_kommun_but_empty() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetKommunQuery, "BIET 1", "aneby").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":0"));
	}

	@Test
	void getFastighet_auth_fail() {
		given().
				header(HttpHeaders.AUTHORIZATION, "").
				when().
				get(fastighetQuery, "ANEBY SMULTRONET").
				then().
				assertThat().
				statusCode(500);
	}

	@Test
	void getFastighet_uuid_success_one() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetQuery, "909a6a88-8aa0-90ec-e040-ed8f66444c3f").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":1"));
	}

	@Test
	void getFastighet_externid_success_one() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetQuery, "2580>KUNGSFÅGELN>3>1>>>>1").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":1"));
	}


	@Test
	void getFastighet_uuid_success_many() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetQuery, "909a6a88-5acd-90ec-e040-ed8f66444c3f").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":5"));
	}

	@Test
	void getFastighet_uuid_array_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetQuery, "909a6a88-8aa0-90ec-e040-ed8f66444c3f,909a6a88-5acd-90ec-e040-ed8f66444c3f").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":6"));
	}

	@Test
	void getFastighet_uuid_success_none() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetQuery, "909a6a88-5acd-90ec-e040-ed8f66444fff").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":0"));
	}

	@Test
	void getFastighet_uuid_invalid_format_failure() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetQuery, "909a6a88-5f9d-90ec-e040-ed8f66444c3").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":0"));
	}

	@Test
	void getFastighet_multi_part_kommun_and_trakt() {
		JsonPath jsonPath = get("lilla edet");
		assertThat(jsonPath.getList("features.properties.kommunnamn").size(), equalTo(100));
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));

		jsonPath = get("LILLA EDET AMNERÖD");
		assertThat(jsonPath.getList("features.properties.kommunnamn").size(), equalTo(34));
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("AMNERÖD")));

		jsonPath = get("SKELLEFTEÅ SKELLEFTEÅ PRÄSTBORD");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("SKELLEFTEÅ")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("SKELLEFTEÅ PRÄSTBORD")));

		jsonPath = get("SKELLEFTEÅ SKELLEFTEÅ PRÄSTBORD 8:8");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("SKELLEFTEÅ")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("SKELLEFTEÅ PRÄSTBORD")));
		assertThat(jsonPath.getList("features.properties.blockenhet"), everyItem(is("8:8")));

		jsonPath = get("LILLA EDET AMNERÖD 1:10");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("AMNERÖD")));
		assertThat(jsonPath.getList("features.properties.blockenhet"), everyItem(is("1:10")));

		jsonPath = get("LILLA EDET VÄSTRA BERG");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("VÄSTRA BERG")));

		jsonPath = get("OSKARSHAMN VIRBO MED EKÖ");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("OSKARSHAMN")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("VIRBO MED EKÖ")));

		jsonPath = get("LILLA EDET VÄSTRA BERG 2:12");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("VÄSTRA BERG")));
		assertThat(jsonPath.getList("features.properties.blockenhet"), everyItem(is("2:12")));

		jsonPath = get("GOTLAND NÄR BOMUNDS I BURGEN");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("GOTLAND")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("NÄR BOMUNDS I BURGEN")));

		jsonPath = get("LILLA EDET SANKT PEDERS ÄLEKÄRR");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("SANKT PEDERS ÄLEKÄRR")));

		jsonPath = get("OSKARSHAMN VIRBO MED EKÖ 1:8");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("OSKARSHAMN")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("VIRBO MED EKÖ")));
		assertThat(jsonPath.getList("features.properties.blockenhet"), everyItem(is("1:8")));

		jsonPath = get("LILLA EDET SANKT PEDERS ÄLEKÄRR 1:8");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("SANKT PEDERS ÄLEKÄRR")));
		assertThat(jsonPath.getList("features.properties.blockenhet"), everyItem(is("1:8")));

		jsonPath = get("GOTLAND NÄR BOMUNDS I BURGEN 1:11");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("GOTLAND")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("NÄR BOMUNDS I BURGEN")));
		assertThat(jsonPath.getList("features.properties.blockenhet"), everyItem(is("1:11")));
	}

	@Test
	void getFastighet_kommun_in_query_and_filter() {
		JsonPath jsonPath = getWithKommun("luleå", "Luleå");
		assertThat(jsonPath.getList("features.properties.kommunnamn").size(), equalTo(100));
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LULEÅ")));

		jsonPath = getWithKommun("lilla edet", "Lilla Edet");
		assertThat(jsonPath.getList("features.properties.kommunnamn").size(), equalTo(100));
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));

		jsonPath = getWithKommun("luleå", "Luleå,Piteå");
		assertThat(jsonPath.getList("features.properties.kommunnamn").size(), equalTo(100));
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LULEÅ")));

		jsonPath = getWithKommun("stockholm", "Luleå,Piteå");
		assertThat(jsonPath.getList("features.properties.kommunnamn").size(), equalTo(0));

		jsonPath = getWithKommun("luleå abborren", "Luleå");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LULEÅ")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("ABBORREN")));

		jsonPath = getWithKommun("abborren", "Luleå");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LULEÅ")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("ABBORREN")));

		jsonPath = getWithKommun("lilla edet amneröd", "Lilla Edet");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("AMNERÖD")));

		jsonPath = getWithKommun("amneröd", "Lilla Edet");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(is("LILLA EDET")));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("AMNERÖD")));

		jsonPath = getWithKommun("abborren", "Luleå,Piteå");
		assertThat(jsonPath.getList("features.properties.kommunnamn"), hasItem(is("LULEÅ")));
		assertThat(jsonPath.getList("features.properties.kommunnamn"), hasItem(is("PITEÅ")));
		assertThat(jsonPath.getList("features.properties.kommunnamn"), everyItem(anyOf(
				containsString("LULEÅ"), containsString("PITEÅ"))));
		assertThat(jsonPath.getList("features.properties.trakt"), everyItem(is("ABBORREN")));
	}

	private JsonPath get(String query) {
		return given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetQuery, query).
				then().
				assertThat().
				statusCode(200).
				extract().jsonPath();
	}

	private JsonPath getWithKommun(String query, String kommun) {
		return given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(fastighetKommunQuery, query, kommun).
				then().
				assertThat().
				statusCode(200).
				extract().jsonPath();
	}
}
