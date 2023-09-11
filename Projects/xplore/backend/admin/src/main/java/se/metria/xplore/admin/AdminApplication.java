package se.metria.xplore.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import se.metria.xplore.admin.geoserver.GeoServerProperties;
import se.metria.xplore.admin.keycloak.KeycloakFactory;
import se.metria.xplore.admin.keycloak.KeycloakProperties;
import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
@EnableConfigurationProperties({AdminApplicationProperties.class, KeycloakProperties.class, GeoServerProperties.class})
public class AdminApplication implements WebMvcConfigurer {

    @Bean
    public KeycloakFactory keycloakFactory() {
        return new KeycloakFactory();
    }

    public static void main(String[] args) {
        XploreTracing.StartTracing();
        SpringApplication.run(AdminApplication.class, args);
    }
}
