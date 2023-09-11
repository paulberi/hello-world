package se.metria.xplore.sok.wfs;

import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.TracingRestTemplateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.metria.xplore.sok.SokProperties;
import se.metria.xplore.sok.common.BaseClient;

import java.nio.charset.StandardCharsets;

@Service
public class WfsClient extends BaseClient {

    private final RestTemplate restTemplate;

    public WfsClient(SokProperties properties,  @Autowired(required = false) Tracer tracer) {
        super(properties);
        this.restTemplate = new GmlRestTemplate();

        if (tracer != null) {
            // GmlRestTemplate är våran egna variant på RestTemplate, så vi får inte med tracingen automatiskt.
            TracingRestTemplateInterceptor traceInterceptor = new TracingRestTemplateInterceptor(tracer);
            this.restTemplate.getInterceptors().add(0, traceInterceptor);
        }

        this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public ResponseEntity<String> request(WfsRequest request) {
        if (request.getHttpMethod() == HttpMethod.POST) {
            return post(request);
        } else {
            return get(request);
        }
    }

    public ResponseEntity<String> get(WfsRequest request) {
        return restTemplate.exchange(
                this.properties.getWfsEndpoint() + "?" + request.getWfsQueryTemplate(),
                HttpMethod.GET,
                new HttpEntity<>(getHeaders()),
                String.class,
                request.getCqlFilter());
    }

    public ResponseEntity<String> post(WfsRequest request) {
        return restTemplate.exchange(
                this.properties.getWfsEndpoint(),
                HttpMethod.POST,
                new HttpEntity<>(request.getWfsQuery(), getHeaders(MediaType.APPLICATION_FORM_URLENCODED)),
                String.class);
    }
}
