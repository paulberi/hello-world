package se.metria.xplore.kundconfig.config;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import se.metria.xplore.keycloak.KeyCloakProperties;
import se.metria.xplore.keycloak.service.KeyCloakService;

import java.security.Principal;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@EnableConfigurationProperties({KeyCloakProperties.class})
public class KundConfigConfiguration {
    @Bean
    public ModelMapper modelMapper() { return new ModelMapper();}

    @Bean
    public KeyCloakService keyCloakService(KeyCloakProperties keyCloakProperties) {
        return new KeyCloakService(keyCloakProperties);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(FullNameKeycloak());
    }

    String FullNameKeycloak() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

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
