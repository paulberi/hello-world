package se.metria.xplore.sok;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

public abstract class SokControllerBaseE2E {

	protected final String auth = "Basic c2tvZ3NhbmFseXM6TWV0cmlhMTgxMjEzSW50";

	@BeforeEach
	void setup() {
		RestAssured.port = 9005;
	}
}