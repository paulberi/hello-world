package se.metria.xplore.fme;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Map;

@Component
@PropertySource("fme.properties")
@EnableConfigurationProperties(FmeProperties.class)
public class FmeClient<T, V> {

    private RestTemplateBuilder restTemplateBuilder;
    private FmeProperties properties;

    public FmeClient(RestTemplateBuilder restTemplateBuilder, FmeProperties properties) {
        this.restTemplateBuilder = restTemplateBuilder.requestFactory(HttpComponentsClientHttpRequestFactory.class);
        this.properties = properties;
    }

    public ResponseEntity<V> jobSubmitter(String script, T body, Class<V> clazz) {
        return jobSubmitter(script, body, clazz, null);
    }
    public ResponseEntity<V> jobSubmitter(String script, T body, Class<V> clazz, Map<String, String> params) {
        var restTemplate = restTemplateBuilder
                .basicAuthentication(this.properties.getUsername(), this.properties.getPassword())
                .setConnectTimeout(Duration.ofSeconds(this.properties.getConnectTimeout()))
                .setReadTimeout(Duration.ofSeconds(this.properties.getReadTimeout()))
                .build();

        var requestHeaders = new HttpHeaders();
        requestHeaders.set("Content-Type", "application/x-www-form-urlencoded");
        requestHeaders.set("Accept-Charset", "UTF-8");
        var requestEntity = new HttpEntity<>(body, requestHeaders);
        return handleJobResponse(restTemplate.exchange(this.createUrl(script, params), HttpMethod.POST, requestEntity, clazz));
    }

    public ResponseEntity<V> post(String script, T body, Class<V> clazz) {
        return post(script, body, clazz, null);
    }

    public ResponseEntity<V> post(String script, T body, Class<V> clazz, Map<String, String> params) {
        var restTemplate = restTemplateBuilder
                .basicAuthentication(this.properties.getUsername(), this.properties.getPassword())
                .setConnectTimeout(Duration.ofSeconds(this.properties.getConnectTimeout()))
                .setReadTimeout(Duration.ofSeconds(this.properties.getReadTimeout()))
                .errorHandler(new FmeResponseErrorHandler())
                .build();

        var requestHeaders = new HttpHeaders();
        requestHeaders.set("Content-Type", "application/x-www-form-urlencoded");
        requestHeaders.set("Accept-Charset", "UTF-8");
        var requestEntity = new HttpEntity<>(body, requestHeaders);

        return handleResponse(restTemplate.exchange(this.createUrl(script, params), HttpMethod.POST, requestEntity, clazz));
    }

    public ResponseEntity<V> get(String script, Class<V> clazz) {
        return get(script, clazz, null);
    }

    public ResponseEntity<V> get(String script, Class<V> clazz, Map<String, String> params) {
        var restTemplate = restTemplateBuilder.basicAuthentication(
                this.properties.getUsername(), this.properties.getPassword())
                .setConnectTimeout(Duration.ofSeconds(this.properties.getConnectTimeout()))
                .setReadTimeout(Duration.ofSeconds(this.properties.getReadTimeout()))
                .errorHandler(new FmeResponseErrorHandler())
                .build();

        return handleResponse(restTemplate.getForEntity(this.createUrl(script, params), clazz, params));
    }

    private ResponseEntity<V> handleResponse(ResponseEntity<V> responseEntity) {
        if (responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT))
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(((V[]) responseEntity.getBody())[0]);
        }
    }

    private ResponseEntity<V> handleJobResponse(ResponseEntity<V> responseEntity) {
        if (responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT))
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body((responseEntity.getBody()));
        }
    }

    private String createUrl(String script, Map<String, String> params) {

        var uriBuilder = UriComponentsBuilder.fromHttpUrl(this.properties.getUrl() + script)
                .queryParam("opt_responseformat", "json")
                .queryParam("opt_showresult", "false");

        if (params != null)
        {
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                uriBuilder.replaceQueryParam(entry.getKey(), entry.getValue());
            }
        }

        return uriBuilder.build(false).toUriString();
    }
}
