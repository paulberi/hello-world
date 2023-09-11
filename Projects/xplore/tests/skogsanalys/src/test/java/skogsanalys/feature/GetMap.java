package skogsanalys.feature;

import common.WmsClient;
import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import io.restassured.response.Response;
import skogsanalys.shared.Config;

public class GetMap {
	private final WmsClient wmsClient;
	private final String wmsUrl;
	private final double[] bbox;

	private String layerToTest;
	private Response wmsResponse;

	public GetMap(Config config, WmsClient wmsClient) {
		this.wmsClient = wmsClient;
		this.wmsUrl = config.getProperty("baseUrl") + "/metria-maps/geoserver/wms";
		// Nånstans i Stockholmsområdet
		this.bbox = new double[]{6585232.640000001, 676277.1199999999, 6586379.5200000005, 677423.9999999999};
	}

	@Givet("att jag har tänt skiktet {string}")
	public void skikt_tant(String layerToTest) {
		this.layerToTest = layerToTest;
	}

	@När("panorerar runt i kartan")
	public void panorera_i_kartan() {
		// "Panorera" i kartan innebär här att hämta en bild :)
		wmsResponse = wmsClient.getMap(wmsUrl, layerToTest, bbox);
	}

	@När("jag försöker hämta ett kartskikt utan inloggning")
	public void hämta_utan_inloggning() {
		wmsResponse = wmsClient.getMap(wmsUrl, "MetraFastighetPlus", bbox);
	}

	@Så("ska jag se kartskiktet")
	public void rasterskikt_hämtat() {
		wmsResponse.then()
				.statusCode(200)
				.contentType("image/png");
	}

	@Så("ska jag få 401 tillbaka från servern")
	public void permission_denied() {
		wmsResponse.then()
				.statusCode(401);
	}
}
