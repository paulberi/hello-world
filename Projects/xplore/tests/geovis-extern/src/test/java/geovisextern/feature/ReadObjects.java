package geovisextern.feature;

import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import common.WfsClient;
import geovisextern.shared.Config;

import java.util.List;

import static io.restassured.path.xml.XmlPath.from;
import static org.junit.Assert.assertTrue;

public class ReadObjects {
	private final Config config;
	private final WfsClient wfsClient;
	private final String owsUrl;

	private String featureType;
	private String result;

	public ReadObjects(Config config, WfsClient wfsClient) {
		this.config = config;
        this.wfsClient = wfsClient;
		this.owsUrl = config.getProperty("baseUrl") + config.getProperty("owsPath");
	}

	@Givet("att det finns ett läsbart lager")
	public void ett_läsbart_lager() {
		featureType = config.getProperty("wfsFeatureType");
	}

	@När("jag försöker läsa från lagret")
	public void läs_från_lagret() {
		result = wfsClient.getFeatures(owsUrl, featureType, 10);
	}

	@Så("ska jag få en lista med objekt")
	public void en_lista_med_objekt() {
		List<String> list = from(result).getList("FeatureCollection.featureMembers.karta_skola.@gml:id");
		assertTrue(!list.isEmpty());
	}
}
