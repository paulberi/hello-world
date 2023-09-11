package se.metria.xplore.samrad.commons.config;


import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Clock;
import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Configuration
@RequiredArgsConstructor
public class SamradConfiguration {

    public String FullNameKeycloak() {
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

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(FullNameKeycloak());
    }

}
