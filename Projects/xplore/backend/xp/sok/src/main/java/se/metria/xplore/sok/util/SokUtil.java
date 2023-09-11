package se.metria.xplore.sok.util;

import org.apache.commons.text.WordUtils;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SokUtil {

    public static boolean isUUID(String query) {
        try {
            return query.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isUUIDArray(String query) {
        List<String> items = Arrays.asList(query.split("\\s*,\\s*"));
        if(items.size() == 0) {
            return false;
        }
        for (var item : items) {
            if(!isUUID(item)) {
                return false;
            }
        }
        return true;
    }


    public static boolean isExternid(String query) {
        return query.startsWith("externid:");
    }


    public static boolean isWKT(String wkt) {
        try {
            WKTReader wktReader = new WKTReader();
            wktReader.read(wkt);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static double clampDouble(Double value, Double min, Double max) {
        if (value > max) return max;
        if (value < min) return min;
        return value;
    }

    public static <T> ResponseEntity<T> createJsonResponse(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    public static String capitalizeFully(String s) {
        return WordUtils.capitalizeFully(s, ' ', '-');
    }
}
