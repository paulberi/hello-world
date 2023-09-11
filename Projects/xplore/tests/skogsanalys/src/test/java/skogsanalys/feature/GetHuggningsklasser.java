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

public class GetHuggningsklasser {
	// Används som returvärde från tjänsten
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Huggningsklasser {
		public double medel;
		public int referensAr;
		public boolean lov;
		public double andelUngskog_0_6;
		public double andelGallringsskog_6_15;
		public double andelSlutavverkningsskog_15;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class SkogligRequest {
		public Integer delomrade;
		public String fastighet;
		public String wkt;
		public double[] bbox;
	}

	private final LoginSession session;
	private final String huggningsklasserUrl;

	private Huggningsklasser response;

	public GetHuggningsklasser(Config config, LoginSession session) {
		this.session = session;
		this.huggningsklasserUrl = config.getProperty("baseUrl") + "/skog/huggningsklasser";
	}

	@När("jag hämtar huggningsklasser med polygonen {string}")
	public void jag_hämtar_huggningsklasser_med_polygonen(String wkt) {
		SkogligRequest request = new SkogligRequest();
		request.wkt = wkt;

		response = given()
				.filters(new RequestLoggingFilter())
				.header("Authorization", "Bearer " + session.getAccessToken())
				.header(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString())
				.header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
				.body(request)
				.post(huggningsklasserUrl)
				.then()
				.statusCode(200)
				.extract().as(Huggningsklasser.class);
	}

	@Så("ska huggningsklasser vara komplett")
	public void ska_huggningsklasser_vara_komplett() {
		assertNotNull(response);
		assertNotNull(response.medel);
		assertNotNull(response.referensAr);
		assertNotNull(response.lov);
		assertNotNull(response.andelUngskog_0_6);
		assertNotNull(response.andelGallringsskog_6_15);
		assertNotNull(response.andelSlutavverkningsskog_15);
	}
}
