package se.metria.markkoll.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;

public class HttpUtil {
    public static HttpHeaders responseHeaders(String prefix, String suffix, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        var date = LocalDate.now().toString();
        String filename = prefix + "_" + date + suffix;
        headers.set("Content-Disposition", "inline;filename=" + filename);
        headers.setContentType(mediaType);

        return headers;
    }

    public static HttpHeaders responseHeaders(String filename, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "inline;filename=" + filename);
        headers.setContentType(mediaType);

        return headers;
    }

    public static String basicAuth(String username, String password) {
        var bytes = (username + ":" + password).getBytes(StandardCharsets.UTF_8);
        return "BASIC " + Base64.getEncoder().encodeToString(bytes);
    }
}
