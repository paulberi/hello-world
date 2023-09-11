package se.metria.matdatabas.service.kartlager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static se.metria.matdatabas.service.kartlager.GeoJson.isValidGeoJson;

class GeoJsonTest {

	@Test
	void testValid() {
		assertTrue(isValidGeoJson("{\"type\": \"FeatureCollection\",\"features\": []}"));
		assertTrue(isValidGeoJson("{\n" +
				"  \"type\": \"FeatureCollection\",\n" +
				"  \"features\": [\n" +
				"    {\n" +
				"      \"type\": \"Feature\",\n" +
				"      \"geometry\": {\n" +
				"        \"type\": \"Point\",\n" +
				"        \"coordinates\": [\n" +
				"          -146.26485498240464,\n" +
				"          72.13995584842043\n" +
				"        ]\n" +
				"      },\n" +
				"      \"properties\": {}\n" +
				"    }\n" +
				"  ]\n" +
				"}"));
		assertTrue(isValidGeoJson("{\n" +
				"          \"type\": \"LineString\",\n" +
				"          \"coordinates\": [\n" +
				"            [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]\n" +
				"            ]\n" +
				"          }"));
	}

	@Test
	void testInvalid() {
		assertFalse(isValidGeoJson("{\"type\": 4,\"features\": []}"));
		assertFalse(isValidGeoJson("{\"type\": \"FeatureKollection\",\"features\": []}"));
		assertFalse(isValidGeoJson("{\"type\": \"FeatureCollection\",\"features\": {}}"));
		assertFalse(isValidGeoJson("{\"type\": \"FeatureCollection\",\"features\": 3}"));
		assertFalse(isValidGeoJson("\"type\": \"FeatureCollection\",\"features\": []}"));
	}
}
