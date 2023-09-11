package brandbranslekartan.feature;

import brandbranslekartan.shared.Config;
import common.WmsClient;
import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import io.restassured.response.Response;

import static io.restassured.RestAssured.head;

public class GetMapData {
	private final String bbkStorageUrl;

	private Response response;

	public GetMapData(Config config) {
		bbkStorageUrl = config.getProperty("baseUrl") + "/storage";
	}

	@När("jag klickar på kommunen {string} och datatypen {string} för kartan {string}")
	public void hämta_kommundata(String kommun, String datatyp, String karta) {
		// Gör dem till länkformatet
		kommun = kommun.toUpperCase();
		datatyp = datatyp.toLowerCase();
		if (karta.equals("skogsmark")) {
			response = head(bbkStorageUrl + "/zipfiler/skogsmark/kommuner/" + kommun + "_" + datatyp + ".zip");
		} else if (karta.equals("oppenmark")){
			response = head(bbkStorageUrl + "/zipfiler/oppenmark/kommuner/" + kommun + "_" + datatyp + ".zip");
		}
	}

	@När("jag klickar på länet {string} och datatyp {string} för kartan {string}")
	public void hämta_länsdata(String län, String datatyp, String karta) {
		// Gör dem till länkformatet
		län = län.toUpperCase();
		datatyp = datatyp.toLowerCase();
		if (karta.equals("skogsmark")) {
		  response = head(bbkStorageUrl + "/zipfiler/skogsmark/lan/" + län + "_" + datatyp + ".zip");
		} else if (karta.equals("oppenmark")){
			response = head(bbkStorageUrl + "/zipfiler/oppenmark/lan/" + län + "_" + datatyp + ".zip");
		}
	}

	@Så("ska jag få hem en zip-fil med datat")
	public void korrekt_zip_fil() {
		response.then()
				.statusCode(200)
				.contentType("application/zip");
	}
}

