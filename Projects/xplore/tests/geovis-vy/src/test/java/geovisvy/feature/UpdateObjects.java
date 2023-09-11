package geovisvy.feature;

import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import geovisvy.shared.Config;
import common.utils.TestUtils;
import common.WfsClient;

import java.io.IOException;

import static io.restassured.path.xml.XmlPath.from;
import static org.junit.Assert.assertEquals;

public class UpdateObjects {
	private final WfsClient wfsClient;
	private final String owsUrl;

	private String wfsXml;
	private String resultXml;

	public UpdateObjects(Config config, WfsClient wfsClient) {
        this.wfsClient = wfsClient;
		this.owsUrl = config.getProperty("baseUrl") + config.getProperty("owsPath");
	}

	@Givet("att det finns ett existerande verksamhetsobjekt")
	public void ett_existerande_verksamhetsobjekt() throws IOException {
		wfsXml = TestUtils.getResourceFileAsString("geovisvy/wfs/update_object.xml");
	}

	@När("jag försöker uppdatera objektet")
	public void uppdatera_objektet() {
		resultXml = wfsClient.writeTransaction(owsUrl, wfsXml);
	}

	@Så("ska uppdateringen lyckas")
	public void uppdateringen_lyckas() {
		int totalUpdated = from(resultXml).getInt("TransactionResponse.TransactionSummary.totalUpdated");
		assertEquals(1, totalUpdated);
	}

	@Så("ska jag få ett felmeddelande")
	public void ett_felmeddelande() {
		String root = from(resultXml).get().name();
		assertEquals("ExceptionReport", root);
	}
}
