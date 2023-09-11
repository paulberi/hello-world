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

public class GetSkogligaGrunddata {
	// Används som returvärde från tjänsten
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class SkogligaGrunddata {
		public Double volSum;
		public Double volMedel;
		public Double hgvMedel;
		public Double gyMedel;
		public Double dgvMedel;
		public Double areaHa;
		public Double areaSkogHa;
		public Double areaProduktivHa;
		public Integer referensAr;
		public Boolean lov;
		public String referensArIntervall;
		public Double volSumExklAvv;
		public Double volMedelExklAvv;
		public Double hgvMedelExklAvv;
		public Double gyMedelExklAvv;
		public Double dgvMedelExklAvv;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class SkogligRequest {
		public Integer delomrade;
		public String fastighet;
		public String wkt;
		public double[] bbox;
	}

	private final LoginSession session;
	private final String skogligaGrunddataUrl;

	private SkogligaGrunddata response;

	public GetSkogligaGrunddata(Config config, LoginSession session) {
		this.session = session;
		this.skogligaGrunddataUrl = config.getProperty("baseUrl") + "/skog/skogliga-grunddata";
	}

	@När("jag hämtar skogliga grunddata med polygonen {string}")
	public void jag_hämtar_skogliga_grunddata_med_polygonen(String wkt) {
		SkogligRequest request = new SkogligRequest();
		request.wkt = wkt;

		response = given()
				.filters(new RequestLoggingFilter())
				.header("Authorization", "Bearer " + session.getAccessToken())
				.header(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString())
				.header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
				.body(request)
				.post(skogligaGrunddataUrl)
				.then()
				.statusCode(200)
				.extract().as(SkogligaGrunddata.class);
	}

	@Så("ska standardattributen vara satta, men inte exklAvv")
	public void ska_skogliga_grunddata_vara_komplett() {
		assertNotNull(response);

		assertNotNull(response.volSum);
		assertNotNull(response.volMedel);
		assertNotNull(response.hgvMedel);
		assertNotNull(response.gyMedel);
		assertNotNull(response.dgvMedel);
		assertNotNull(response.areaHa);
		assertNotNull(response.referensAr);
		assertNotNull(response.lov);
		assertNotNull(response.referensArIntervall);
	}
}
