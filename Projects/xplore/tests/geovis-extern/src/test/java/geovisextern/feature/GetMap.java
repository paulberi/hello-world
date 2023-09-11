package geovisextern.feature;

import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Och;
import cucumber.api.java.sv.Så;
import geovisextern.shared.Config;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class GetMap {
	private final Config config;
	private final String wmtsUrl;

	private String wmtsPath;  // Innanför kommun
	private String imageType;
	private Response wmtsResponse;

	public GetMap(Config config) {
		this.config = config;
		this.wmtsUrl = config.getProperty("baseUrl") + config.getProperty("mapserverPath");
	}

	@Givet("att jag har tänt standardbakgrundsskiktet")
	public void standard_bakgrund_tänd() {
		wmtsPath = config.getProperty("standardBackground");
		imageType = "image/png";
	}

	@Givet("att jag har tänt det nedtonade bakgrundsskiktet")
	public void nedtonat_tänt() {
		wmtsPath = config.getProperty("nedtonadBackground");
		imageType = "image/png";
	}

	@Givet("att jag har tänt ortofotot")
	public void ortofoto_tänt() {
		wmtsPath = config.getProperty("ortoBackground");
		imageType = "image/jpeg";
	}

	@Och("att jag är inom kommungränserna")
	public void inom_kommungränserna() {
		// Vi skulle kunna hämta olika bilder beroende på om vi är innanför eller utanför
		// om vi vill testa att bilder utanför är transparenta eller vita. Men det strulade!!
	}

	@När("panorerar runt i kartan")
	public void panorera_i_kartan() {
		// "Panorera" i kartan innebär här att hämta en bild :)
		wmtsResponse = given().filters(new RequestLoggingFilter())
				.get(wmtsUrl + wmtsPath);
	}

	@Så("ska jag se en kartbild")
	public void kartbild_hämtad() {
		wmtsResponse.then()
				.statusCode(200)
				.contentType(imageType);
	}

}
