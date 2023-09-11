package se.metria.mapcms.commons.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * från httpUtils in Markkoll
 *
 */
public class HttpUtils {


    public static HttpHeaders setFilResponseHeaders(String filename, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "inline;filename=" + filename);
        headers.setContentType(mediaType);

        return headers;
    }
}
