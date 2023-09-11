package se.metria.xplore.sok.geocode;

import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import se.metria.xplore.sok.SokControllerBaseE2E;
import se.metria.xplore.sok.geocode.model.MediumReplyExtent;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Every.everyItem;

class GeocodeE2E extends SokControllerBaseE2E {

	private final String geocodeQuery = "/geocode?q={q}";
	private final String geocodeKommunQuery = "/geocode?q={q}&k={k}";
	private final String geocodeCoordinateQuery = "/geocode?q={q}&x={x}&y={y}";
	private final String geocodeKommunCoordinateQuery = "/geocode?q={q}&x={x}&y={y}&k={k}";
	private final String geocodeKommunMaskQuery = "/geocode?q={q}&k={k}&m={m}";
	private final String geocodeMaskQuery = "/geocode?q={q}&&m={m}";

	private final String maskUrlAneby = "https://xplorefile01.prodstod.se/test/aneby_kommun.geojson";
	private final String maskUrlGotaKanalbolag = "https://xplorefile01.prodstod.se/test/götakanalbolag_kommun.geojson";

	@Test
	void getGeocode_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeQuery,"Kungsgatan 1").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body("size()", is(10));
	}

	@Test
	void getGeocode_full_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeQuery,"Storgatan 1a, 57831 Aneby, Sverige").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body("size()", is(1));
	}

	@Test
	void getGeocode_full_kommun_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunQuery,"Storgatan 1a, 57831 Aneby, Sverige", "aneby").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body("size()", is(1));
	}

	@Test
	void getGeocode_kommun_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunQuery, "Skolgatan", "Aneby").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body("size()", is(1));
	}

	@Test
	void getGeocode_success_empty_response() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunQuery, "Kungsgatan", "Aneby").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body("size()", is(0));
	}

	@Test
	void getGeocode_coordinate() {
		double xLulea = 829562;
		double yLulea = 7293601;

		List<MediumReplyExtent> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeCoordinateQuery, "storgatan 10", xLulea, yLulea).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.get(0).getAddress(), containsString("Storgatan 10"));
		assertThat(resultat.get(0).getAddress(), containsString("Luleå"));
		assertThat(resultat.get(1).getAddress(), containsString("Storgatan 10"));
		assertThat(resultat.get(1).getAddress(), containsString("Byske"));
	}

	@Test
	void getGeocode_coordinate_kommun() {
		double x = 488887;
		double y = 6410320;

		List<MediumReplyExtent> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunCoordinateQuery, "storgatan 1", x, y, "aneby").
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.get(0).getAddress(), containsString("Storgatan 1,"));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("address", containsString("Aneby,"))));
	}

	@Test
	void getGeocode_auth_fail() {
		given().
				header(HttpHeaders.AUTHORIZATION, "").
				when().
				get(geocodeQuery,"Kungsgatan 1").
				then().
				assertThat().
				statusCode(500);
	}

	@Test
	void getGeocode_fail_with_missing_mask() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunMaskQuery, "Slottsbacken 1", "Stockholm", "invalid_url").
				then().
				assertThat().
				statusCode(400);
	}

	@Test
	void getGeocode_mask_success() {
		List<MediumReplyExtent> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunMaskQuery, "Storgatan 1", "Aneby", maskUrlAneby).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(2));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("address", containsString("Aneby,"))));
	}

	@Test
	void getGeocode_mask_filtering_success() {
		List<MediumReplyExtent> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunQuery, "Storgatan 1", "Linköping").
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(3));
		assertThat(resultat, hasItem(HasPropertyWithValue.hasProperty("address", containsString("Lidköping,"))));

		resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunMaskQuery, "Storgatan 1", "Linköping", maskUrlGotaKanalbolag).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(2));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("address", containsString("Linköping,"))));
	}

	@Test
	void getGeocode_mask_multi_success() {
		List<MediumReplyExtent> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunMaskQuery, "Storgatan 1", "Karlsborg,Linköping,Mariestad,Motala,Norrköping,Söderköping,Töreboda,Laxå,Gullspång", maskUrlGotaKanalbolag).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(10));

		resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeKommunMaskQuery, "Storgatan 1", "Linköping,Gullspång", maskUrlGotaKanalbolag).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(10));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("address", anyOf(
				containsString("Linköping,"), containsString("Gullspång,")))));
	}

	@Test
	void getGeocode_mask_no_kommun_success() {
		List<MediumReplyExtent> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeQuery, "Storkgatan").
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(3));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("address", anyOf(
				containsString("Linköping,"), containsString("Hunnebostrand,"), containsString("Göteborg,")))));

		resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(geocodeMaskQuery, "Storkgatan", maskUrlGotaKanalbolag).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(1));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("address", containsString("Linköping,"))));
	}
}