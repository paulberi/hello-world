package se.metria.matdatabas.service.kartlager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.GeoJsonObject;

import java.io.IOException;
import java.util.Map;

public class GeoJson {

	public static boolean isValidGeoJson(String geoJson) {
		try {
			var mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.readValue(geoJson, GeoJsonObject.class);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
