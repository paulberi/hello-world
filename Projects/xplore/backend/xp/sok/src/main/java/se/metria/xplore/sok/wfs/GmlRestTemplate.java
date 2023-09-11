package se.metria.xplore.sok.wfs;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class GmlRestTemplate extends RestTemplate {

    /*
     * Behövs eftersom man annars får följande när xml-filen parsas:
     * Invalid mime type "text/xml; subtype=gml/3.1.1": Invalid token character '/' in token "gml/3.1.1"
     */
    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback callback, final ResponseExtractor<T> responseExtractor) throws RestClientException {
        return super.doExecute(url, method, callback, response -> {
            String contentType = response.getHeaders().getFirst("Content-Type");
            if (contentType.startsWith("text/xml")) response.getHeaders().setContentType(MediaType.TEXT_XML);
            return responseExtractor.extractData(response);
        });
    }
}

