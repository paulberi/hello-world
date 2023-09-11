package se.metria.markkoll.config;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.client.RestTemplate;
import se.metria.markkoll.openapi.dokument_converter.api.PdfApi;
import se.metria.markkoll.openapi.dokument_converter.client.ApiClient;
import se.metria.markkoll.util.FileHttpMessageConverter;
import se.metria.xplore.geoserveradmin.service.GeoserverAdminService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Import({ModelMapperConfiguration.class, WebMvcConfiguration.class})
@EnableAsync
@EnableScheduling
public class MarkkollConfiguration {
    @Value("${dokument-converter.url}")
    String dokumentConverterUrl;

    @Bean
    public GeoserverAdminService geoserverService() {
        return new GeoserverAdminService();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(FullNameKeycloak());
    }

    @Bean
    public DelegatingSecurityContextExecutor taskExecutor() {
        /* Med en DelegatingSecurityContextExecutor så kan vi skicka vår SecurityContext vidare till andra trådar. */
        SimpleAsyncTaskExecutor delegateExecutor = new SimpleAsyncTaskExecutor();
        return new DelegatingSecurityContextExecutor(delegateExecutor);
    }

    @Bean
    public MessageDigest sha256Digest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256");
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public RestTemplate restTemplate() {
        var restTemplate = new RestTemplate();
        var messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(pdfMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

    @Bean
    public PdfApi pdfApi(RestTemplate restTemplate) {
        var apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(dokumentConverterUrl + "/api");
        return new PdfApi(apiClient);
    }

    private HttpMessageConverter<?> pdfMessageConverter() {
        var converter = new FileHttpMessageConverter();

        List<MediaType> supportedApplicationTypes = new ArrayList<>();
        supportedApplicationTypes.add(MediaType.APPLICATION_PDF);

        converter.setSupportedMediaTypes(supportedApplicationTypes);

        return converter;
    }

    String FullNameKeycloak() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "";
        }

        if (authentication.getPrincipal() instanceof User) {
            var user = (User)authentication.getPrincipal();
            return user.getUsername();
        }

        var principal = (Principal)authentication.getPrincipal();

        var keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;

        var accessToken = keycloakPrincipal.getKeycloakSecurityContext().getToken();

        return accessToken.getGivenName() + " " + accessToken.getFamilyName();
    }
}
