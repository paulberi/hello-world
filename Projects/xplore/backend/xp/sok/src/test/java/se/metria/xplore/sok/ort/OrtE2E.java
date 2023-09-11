package se.metria.xplore.sok.ort;

import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import se.metria.xplore.sok.SokControllerBaseE2E;
import se.metria.xplore.sok.ort.model.OrtSokResultat;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Every.everyItem;

class OrtE2E extends SokControllerBaseE2E {

	private final String ortQuery = "/ort?q={q}";
	private final String ortKommunQuery = "/ort?q={q}&k={k}";
	private final String ortLanQuery = "/ort?q={q}&l={l}";
	private final String ortKommunLanQuery = "/ort?q={q}&k={k}&l={l}";
	private final String ortCoordinateQuery = "/ort?q={q}&x={x}&y={y}";

	private List<OrtSokResultat> ortSok(String query, String... pathParams) {
		return Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(query, pathParams).
				then().
				assertThat().
				statusCode(200).
				extract().as(OrtSokResultat[].class));
	}

	@Test
	void getOrt_success() {
		OrtSokResultat[] resultat = given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(ortQuery, "grannäsudden").
				then().
				assertThat().
				statusCode(200).
				extract().as(OrtSokResultat[].class);

		assertThat(resultat.length, is(not(0)));
	}

	@Test
	void getOrt_partial_success() {
		List<OrtSokResultat> resultat = ortSok(ortQuery, "Grannäsu");
		assertThat(resultat.size(), is(6));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", is("Grannäsudden"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("typ", anyOf(is("BEBTX"), is("TERRTX")))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("kommun", anyOf(
				is("Norsjö"), is("Storuman"), is("Piteå"), is("Ovanåker"), is("Luleå")))));
	}

	@Test
	void getOrt_many_partial_success() {
		List<OrtSokResultat> resultat = ortSok(ortQuery, "GRANNÄS");
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", startsWith("Grannäs"))));
		assertThat(resultat, hasItem(HasPropertyWithValue.hasProperty("ort", is("Grannäsfjärden"))));
		assertThat(resultat.size(), is(100));
	}

	@Test
	void getOrt_empty_response() {
		List<OrtSokResultat> resultat = ortSok(ortQuery, "STOKCHOLM");
		assertThat(resultat.size(), is(0));
	}

	@Test
	void getOrt_success_with_kommun() {
		List<OrtSokResultat> resultat = ortSok(ortKommunQuery, "STOCKHOLM", "VÄNERSBORG");
		assertThat(resultat.size(), is(2));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", is("Stockholm"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("kommun", is("Vänersborg"))));
	}

	@Test
	void getOrt_success_with_kommun_but_empty() {
		List<OrtSokResultat> resultat = ortSok(ortKommunQuery, "stockholm", "luleå");
		assertThat(resultat.size(), is(0));
	}

	@Test
	void getOrt_success_with_kommun_list() {
		List<OrtSokResultat> resultat = ortSok(ortKommunQuery, "stockholm", "stockholm,luleå");
		assertThat(resultat.size(), is(2));
	}

	@Test
	void getOrt_success_with_lan() {
		List<OrtSokResultat> resultat = ortSok(ortLanQuery, "stockholm", "VÄSTRA GÖTALANDS LÄN");
		assertThat(resultat.size(), is(21));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", startsWith("Stockholm"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("lan", is("Västra Götalands Län"))));
	}

	@Test
	void getOrt_success_with_lan_olika_kommuner() {
		List<OrtSokResultat> resultat = ortSok(ortLanQuery, "stockholm", "stockholms län");
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", startsWith("Stockholm"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("lan", is("Stockholms Län"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("kommun", anyOf(
				is("Stockholm"), is("Södertälje"), is("Nynäshamn"), is("Norrtälje"), is("Nacka"), is("Värmdö")))));
	}

	@Test
	void getOrt_success_with_kommun_och_lan() {
		List<OrtSokResultat> resultat = ortSok(ortKommunLanQuery, "stockholm", "Stockholm", "stockholms län");
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", startsWith("Stockholm"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("kommun", is("Stockholm"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("lan", is("Stockholms Län"))));
	}

	@Test
	void getOrt_success_with_lan_but_empty() {
		List<OrtSokResultat> resultat = ortSok(ortLanQuery, "grannäsudden", "stockholms län");
		assertThat(resultat.size(), is(0));
	}

	@Test
	void getOrt_coordinate() {
		List<OrtSokResultat> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(ortQuery, "stockholmsmyren").
				then().
				assertThat().
				statusCode(200).
				extract().as(OrtSokResultat[].class));

		double lastX = resultat.get(resultat.size()-1).getX();
		double lastY = resultat.get(resultat.size()-1).getY();

		int firstLength = resultat.size();
		resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(ortCoordinateQuery, "stockholmsmyren", lastX, lastY).
				then().
				assertThat().
				statusCode(200).
				extract().as(OrtSokResultat[].class));

		double firstX = resultat.get(0).getX();
		double firstY = resultat.get(0).getY();

		assertThat(resultat.size(), is(firstLength));
		assertThat(lastX, is(firstX));
		assertThat(lastY, is(firstY));
	}

	@Test
	void getOrt_auth_fail() {
		given().
				header(HttpHeaders.AUTHORIZATION, "").
				when().
				get(ortQuery, "LULEÅ").
				then().
				assertThat().
				statusCode(500);
	}
}
