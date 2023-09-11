package common;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class WmsClient {
	private final LoginSession session;

	public WmsClient(LoginSession session) {
        this.session = session;
	}

	public Response getMap(String wmsUrl, String layer, double[] bbox) {
		RequestSpecification requestSpecification = given()
				.queryParam("service", "wms")
				.queryParam("version", "1.3.0")
				.queryParam("request", "GetMap")
				.queryParam("format", "image/png")
				.queryParam("transparent", "true")
				.queryParam("layers", layer)
				.queryParam("width", "512")
				.queryParam("height", "512")
				.queryParam("crs", "EPSG:3006")
				.queryParam("bbox", bbox[0] + "," + bbox[1] + "," + bbox[2] + "," + bbox[3]);

		if (!session.isAnonymous()) {
			requestSpecification = requestSpecification.
					header("Authorization", "Bearer " + session.getAccessToken());
		}

		return requestSpecification
				.get(wmsUrl);
	}
}
