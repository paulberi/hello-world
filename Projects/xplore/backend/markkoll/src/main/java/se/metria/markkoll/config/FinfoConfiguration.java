package se.metria.markkoll.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import se.metria.markkoll.openapi.finfo.api.AgareApi;
import se.metria.markkoll.openapi.finfo.api.ImportJobApi;
import se.metria.markkoll.openapi.finfo.api.RegisterenhetApi;
import se.metria.markkoll.openapi.finfo.api.SamfallighetsforeningApi;

@Configuration
public class FinfoConfiguration {
    @Value("${finfo.url}")
    String finfoUrl;

    @Bean
    public AgareApi agareApi(RestTemplate restTemplate) {
        var apiClient = new se.metria.markkoll.openapi.finfo.client.ApiClient(restTemplate);
        apiClient.setBasePath(finfoUrl + "/api");
        return new AgareApi(apiClient);
    }

    @Bean
    public RegisterenhetApi registerenhetApi(RestTemplate restTemplate) {
        var apiClient = new se.metria.markkoll.openapi.finfo.client.ApiClient(restTemplate);
        apiClient.setBasePath(finfoUrl + "/api");
        return new RegisterenhetApi(apiClient);
    }

    @Bean
    public SamfallighetsforeningApi samfallighetsforeningApi(RestTemplate restTemplate) {
        var apiClient = new se.metria.markkoll.openapi.finfo.client.ApiClient(restTemplate);
        apiClient.setBasePath(finfoUrl + "/api");
        return new SamfallighetsforeningApi(apiClient);
    }

    @Bean
    public ImportJobApi importJobApi(RestTemplate restTemplate) {
        var apiClient = new se.metria.markkoll.openapi.finfo.client.ApiClient(restTemplate);
        apiClient.setBasePath(finfoUrl + "/api");
        return new ImportJobApi(apiClient);
    }
}
