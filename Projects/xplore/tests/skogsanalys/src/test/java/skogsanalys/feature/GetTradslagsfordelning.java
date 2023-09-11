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

public class GetTradslagsfordelning {
	// Används som returvärde från tjänsten
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Tradslagsfordelning {
		public double tallskogHa;
		public double granskogHa;
		public double barrblandskogHa;
		public double lovblandadBarrskogHa;
		public double triviallovHa;
		public double adellovHa;
		public double triviallovAdellovHa;
		public double skogHa;
		public double temporartEjSkogHa;
		public double skogVatmarkHa;
		public double oppenVatmarkHa;
		public double akermarkHa;
		public double ovrigOppenMarkHa;
		public double exploateradMarkHa;
		public double vattenHa;
		public double molnOklassatHa;
		public double Areal_raster_ha;
		public double Area_ha;
		public int referensAr;
		public String raster_status;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class SkogligRequest {
		public Integer delomrade;
		public String fastighet;
		public String wkt;
		public double[] bbox;
	}

	private final LoginSession session;
	private final String tradslagsfordelningUrl;

	private Tradslagsfordelning response;

	public GetTradslagsfordelning(Config config, LoginSession session) {
		this.session = session;
		this.tradslagsfordelningUrl = config.getProperty("baseUrl") + "/skog/nmd";
	}

	@När("jag hämtar trädslagsfördelning med polygonen {string}")
	public void jag_hämtar_trädslagsfördelning_med_en_polygon(String wkt) {
		SkogligRequest request = new SkogligRequest();
		request.wkt = wkt;

		response = given()
				.filters(new RequestLoggingFilter())
				.header("Authorization", "Bearer " + session.getAccessToken())
				.header(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString())
				.header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
				.body(request)
				.post(tradslagsfordelningUrl)
				.then()
				.statusCode(200)
				.extract().as(Tradslagsfordelning.class);
	}

	@Så("ska trädslagsfördelning vara komplett")
	public void ska_trädslagsfördelning_vara_komplett() {
		assertNotNull(response);
		assertNotNull(response.tallskogHa);
		assertNotNull(response.granskogHa);
		assertNotNull(response.barrblandskogHa);
		assertNotNull(response.lovblandadBarrskogHa);
		assertNotNull(response.triviallovHa);
		assertNotNull(response.adellovHa);
		assertNotNull(response.triviallovAdellovHa);
		assertNotNull(response.temporartEjSkogHa);
		assertNotNull(response.skogHa);
		assertNotNull(response.skogVatmarkHa);
		assertNotNull(response.oppenVatmarkHa);
		assertNotNull(response.akermarkHa);
		assertNotNull(response.ovrigOppenMarkHa);
		assertNotNull(response.exploateradMarkHa);
		assertNotNull(response.vattenHa);
		assertNotNull(response.molnOklassatHa);
		assertNotNull(response.Areal_raster_ha);
		assertNotNull(response.Area_ha);
		assertNotNull(response.referensAr);
		assertNotNull(response.raster_status);
	}
}
