package skogsanalys.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import common.LoginSession;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import io.restassured.filter.log.RequestLoggingFilter;
import skogsanalys.shared.Config;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GetAvverkningstatistik {
	// Används som returvärde från tjänsten
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Avverkningsomrade {
		public String avverkningsAr;
		public double areallHa;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Statistik {
		public String json_featuretype;
		public Avverkningsomrade[] Avverkningar;
		public String statusMessage;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class SkogligRequest {
		public Integer delomrade;
		public String fastighet;
		public String wkt;
		public double[] bbox;
	}

	private final LoginSession session;
	private final String avverkningstatistikUrl;

	private Statistik response;

	public GetAvverkningstatistik(Config config, LoginSession session) {
		this.session = session;
		this.avverkningstatistikUrl = config.getProperty("baseUrl") + "/skog/avverkningsstatistik";
	}

	@När("jag hämtar avverkningstatistik med polygonen {string}")
	public void jag_hämtar_avverkningstatistik_med_polygonen(String wkt) {
		SkogligRequest request = new SkogligRequest();
		request.wkt = wkt;

		response = given()
				.filters(new RequestLoggingFilter())
				.header("Authorization", "Bearer " + session.getAccessToken())
				.header(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString())
				.header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
				.body(request)
				.post(avverkningstatistikUrl)
				.then()
				.statusCode(200)
				.extract().as(Statistik.class);
	}

	@Så("ska avverkningstatistiken vara komplett")
	public void ska_avverkningstatistiken_vara_komplett() {
		assertNotNull(response);
		assertTrue(response.json_featuretype.equals("Avverkning"));
		assertNotNull(response.statusMessage);
		assertNotNull(response.Avverkningar);

		for (int i = 0; i < response.Avverkningar.length; i++) {
			assertNotNull(response.Avverkningar[i].avverkningsAr);
			assertNotNull(response.Avverkningar[i].areallHa);
		}
	}
}
