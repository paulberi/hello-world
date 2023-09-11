package se.metria.markkoll.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.metria.xplore.keycloak.KeyCloakProperties;
import se.metria.xplore.keycloak.service.KeyCloakService;

@Configuration
@EnableConfigurationProperties({KeyCloakProperties.class})
public class KeyCloakConfig {
    @Bean
    public KeyCloakService keyCloakService(KeyCloakProperties keyCloakProperties) {
        return new KeyCloakService(keyCloakProperties);
    }
}
