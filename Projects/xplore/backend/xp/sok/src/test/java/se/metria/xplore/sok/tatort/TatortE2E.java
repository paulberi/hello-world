package se.metria.xplore.sok.tatort;

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

class TatortE2E extends SokControllerBaseE2E {

	private final String tatortQuery = "/tatort?q={q}";
	private final String tatortKommunQuery = "/tatort?q={q}&k={k}";
	private final String tatortLanQuery = "/tatort?q={q}&l={l}";
	private final String tatortKommunLanQuery = "/tatort?q={q}&k={k}&l={l}";
	private final String tatortCoordinateQuery = "/tatort?q={q}&x={x}&y={y}";

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
	void getTatort_exact_success() {
		List<OrtSokResultat> resultat = ortSok(tatortQuery, "Stockholm");
		assertThat(resultat.size(), is(1));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", is("Stockholm"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("typ", is("BEBTÄTTX"))));
	}

	@Test
	void getTatort_many_partial_success() {
		List<OrtSokResultat> resultat = ortSok(tatortQuery, "stock");
		assertThat(resultat.size(), is(5));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", startsWith("Stock"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("typ", is("BEBTÄTTX"))));
	}

	@Test
	void getTatort_empty_response() {
		List<OrtSokResultat> resultat = ortSok(tatortQuery, "STOKCHOLM");
		assertThat(resultat.size(), is(0));
	}

	@Test
	void getTatort_success_with_kommun() {
		List<OrtSokResultat> resultat = ortSok(tatortKommunQuery, "sand", "Mullsjö");
		assertThat(resultat.size(), is(1));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", is("Sandhem"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("kommun", is("Mullsjö"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("typ", is("BEBTÄTTX"))));
	}

	@Test
	void getTatort_success_with_kommun_but_empty() {
		List<OrtSokResultat> resultat = ortSok(tatortKommunQuery, "stockholm", "luleå");
		assertThat(resultat.size(), is(0));
	}

	@Test
	void getTatort_success_with_lan() {
		List<OrtSokResultat> resultat = ortSok(tatortLanQuery, "sand", "Västra Gö");
		assertThat(resultat.size(), is(2));
		assertThat(resultat, hasItem(HasPropertyWithValue.hasProperty("ort", is("Sandhult"))));
		assertThat(resultat, hasItem(HasPropertyWithValue.hasProperty("ort", is("Sandared"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("lan", is("Västra Götalands Län"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("typ", is("BEBTÄTTX"))));
	}

	@Test
	void getTatort_success_with_lan_olika_kommuner() {
		List<OrtSokResultat> resultat = ortSok(tatortLanQuery, "Sand", "Gävleborgs LÄN");
		assertThat(resultat.size(), is(2));
		assertThat(resultat, hasItem(HasPropertyWithValue.hasProperty("ort", is("Sandviken"))));
		assertThat(resultat, hasItem(HasPropertyWithValue.hasProperty("ort", is("Sandarne"))));
		assertThat(resultat, hasItem(HasPropertyWithValue.hasProperty("kommun", is("Sandviken"))));
		assertThat(resultat, hasItem(HasPropertyWithValue.hasProperty("kommun", is("Söderhamn"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("lan", is("Gävleborgs Län"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("typ", is("BEBTÄTTX"))));
	}

	@Test
	void getTatort_success_with_lan_but_empty() {
		List<OrtSokResultat> resultat = ortSok(tatortLanQuery, "grannäsudden", "stockholms län");
		assertThat(resultat.size(), is(0));
	}

	@Test
	void getTatort_success_with_kommun_och_lan() {
		List<OrtSokResultat> resultat = ortSok(tatortKommunLanQuery, "Sand", "Söderhamn", "Gävleborgs LÄN");
		assertThat(resultat.size(), is(1));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("ort", is("Sandarne"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("kommun", is("Söderhamn"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("lan", is("Gävleborgs Län"))));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("typ", is("BEBTÄTTX"))));
	}


	@Test
	void getTatort_coordinate() {

		Double xSandslan = 641948.0;
		Double ySandslan = 6990293.0;
		Double xSandhem = 427012.0;
		Double ySandhem = 6427723.0;

		List<OrtSokResultat> resultat = ortSok(tatortCoordinateQuery, "Sand", xSandslan.toString(), ySandslan.toString());
		assertThat(resultat.get(0).getX(), is(xSandslan));
		assertThat(resultat.get(0).getY(), is(ySandslan));
		assertThat(resultat.get(0).getOrt(), is("Sandslån"));
		assertThat(resultat.get(1).getLan(), is("Gävleborgs Län"));
		assertThat(resultat.get(2).getLan(), is("Gävleborgs Län"));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("typ", is("BEBTÄTTX"))));

		int firstLength = resultat.size();

        resultat = ortSok(tatortCoordinateQuery, "Sand", xSandhem.toString(), ySandhem.toString());
		assertThat(resultat.get(0).getX(), is(xSandhem));
		assertThat(resultat.get(0).getY(), is(ySandhem));
		assertThat(resultat.get(resultat.size()-1).getOrt(), is("Sandslån"));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("typ", is("BEBTÄTTX"))));

		assertThat(resultat.size(), is(firstLength));
	}

	@Test
	void getTatort_auth_fail() {
		given().
				header(HttpHeaders.AUTHORIZATION, "").
				when().
				get(tatortQuery, "LULEÅ").
				then().
				assertThat().
				statusCode(500);
	}
}