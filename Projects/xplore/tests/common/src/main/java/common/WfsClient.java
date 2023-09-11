package common;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class WfsClient {
	private final LoginSession session;

	public WfsClient(LoginSession session) {
        this.session = session;
	}

	public String getFeatures(String owsUrl, String featureType, int count) {
		RequestSpecification requestSpecification = given()
				.queryParam("service", "wfs")
				.queryParam("version", "1.1.0")
				.queryParam("request", "GetFeature")
				.queryParam("typeNames", featureType)
				.queryParam("maxFeatures", count);

		if (!session.isAnonymous()) {
			requestSpecification = requestSpecification.
					header("Authorization", "Bearer " + session.getAccessToken());
		}

		return requestSpecification
				.get(owsUrl)
				.asString();
	}

	public String writeTransaction(String owsUrl, String xml) {
		return given()
				.header("Authorization", "Bearer " + session.getAccessToken())
				.header("Content-Type", "application/xml")
				.body(xml)
				.post(owsUrl)
				.asString();
	}
}
