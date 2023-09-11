package se.metria.xplore.sok.reversegeocode;

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

class ReverseGeocodeE2E extends SokControllerBaseE2E {

	private final String reverseGeocodeQuery = "/reverse-geocode?x={x}&y={y}";
	private final String reverseGeocodeMaskQuery = "/reverse-geocode?x={x}&y={y}&m={m}";

	private final String maskUrlAneby = "https://xplorefile01.prodstod.se/test/aneby_kommun.geojson";
	private final String maskUrlGotaKanalbolag = "https://xplorefile01.prodstod.se/test/götakanalbolag_kommun.geojson";

	@Test
	void getReverseGeocode_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(reverseGeocodeQuery, 488962.6, 6410260.92).
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body("size()", is(10));
	}

	@Test
	void getReverseGeocode_success_with_mask() {

		List<MediumReplyExtent> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(reverseGeocodeQuery, 479922.0, 6402497.0).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(10));
		assertThat(resultat, not(everyItem(HasPropertyWithValue.hasProperty("address", containsString("Aneby,")))));

		resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(reverseGeocodeMaskQuery, 479922.0, 6402497.0, maskUrlAneby).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(4));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("address", containsString("Aneby,"))));
	}

	@Test
	void getReverseGeocode_success_with_mask_but_empty() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(reverseGeocodeMaskQuery, 474844.16, 6410127.36, maskUrlAneby).
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body("size()", is(0));
	}

	@Test
	void getReverseGeocode_auth_fail() {
		given().
				header(HttpHeaders.AUTHORIZATION, "").
				when().
				get(reverseGeocodeQuery, 488962.6, 6410260.92).
				then().
				assertThat().
				statusCode(500);
	}


	@Test
	void getReverseGeocode_fail_with_unsupported_kommun() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(reverseGeocodeMaskQuery, 674772, 6580462, "invalid_url").
				then().
				assertThat().
				statusCode(400);
	}

	@Test
	void getReverseGeocode_success_with_multi_mask() {
		List<MediumReplyExtent> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(reverseGeocodeMaskQuery, 449248.0, 6538592.0, maskUrlGotaKanalbolag).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(10));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("address", containsString("Gullspång,"))));
	}

	@Test
	void getReverseGeocode_success_with_multi_mask_filtering() {
		List<MediumReplyExtent> resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(reverseGeocodeQuery, 561929.0, 6469822.0).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(10));
		assertThat(resultat, hasItem(HasPropertyWithValue.hasProperty("address", containsString("Åtvidaberg,"))));

		resultat = Arrays.asList(given().
				header(HttpHeaders.AUTHORIZATION, auth).
				when().
				get(reverseGeocodeMaskQuery, 561929.0, 6469822.0, maskUrlGotaKanalbolag).
				then().
				assertThat().
				statusCode(200).
				extract().as(MediumReplyExtent[].class));

		assertThat(resultat.size(), equalTo(3));
		assertThat(resultat, everyItem(HasPropertyWithValue.hasProperty("address", anyOf(
				containsString("Linköping,"), containsString("Söderköping,")))));
		assertThat(resultat, not(hasItem(HasPropertyWithValue.hasProperty("address", containsString("Åtvidaberg,")))));
	}
}