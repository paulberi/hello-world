package brandbranslekartan.feature;

import brandbranslekartan.shared.Config;
import common.WmsClient;
import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import io.restassured.response.Response;

import static io.restassured.RestAssured.get;

public class GetMap {
	private final WmsClient wmsClient;
	private final Config config;
	private final double[] bbox;

	private String layerToTest;
	private String wmsUrl;
	private String wmtsUrl;
	private Response response;

	public GetMap(Config config, WmsClient wmsClient) {
		this.wmsClient = wmsClient;
		this.config = config;
		// Nånstans i Stockholmsområdet
		this.bbox = new double[]{6585232.640000001, 676277.1199999999, 6586379.5200000005, 677423.9999999999};
	}

	@Givet("att jag har tänt skiktet {string} från källan {string}")
	public void skikt_tant(String layerToTest, String source) {
		this.layerToTest = layerToTest;
		switch (source) {
			case "Metria Maps":
				this.wmsUrl = config.getProperty("baseUrl") + "/metria-maps/geoserver/wms";
				break;
			case "LM ORTO":
				this.wmsUrl = config.getProperty("baseUrl") + "/lm-maps/ortofoto/wms/v1.3";
				break;
			case "LM TOPOWEBB":
				// Exakt tile är hårdkodad
				this.wmtsUrl = config.getProperty("baseUrl") + String.format("/lm-maps/topowebb/v1.1/wmts?layer=%s&style=default&tilematrixset=3006&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%%2Fpng&TileMatrix=6&TileCol=110&TileRow=95", this.layerToTest);
				break;
		}
	}

	@När("panorerar runt i kartan")
	public void panorera_i_kartan() {
		// "Panorera" i kartan innebär här att hämta en bild :)
		if (wmtsUrl != null) {
			response = get(wmtsUrl);
		} else if (wmsUrl != null) {
			response = wmsClient.getMap(wmsUrl, layerToTest, bbox);
		} else {
			throw new IllegalStateException("Ingen url satt");
		}
	}

	@Så("ska jag se kartskiktet")
	public void rasterskikt_hämtat() {
		response.then()
				.statusCode(200)
				.contentType("image/png");
	}
}
