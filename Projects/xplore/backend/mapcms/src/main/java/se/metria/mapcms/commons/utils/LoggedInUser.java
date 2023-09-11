package se.metria.mapcms.commons.utils;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

@Configuration
public class LoggedInUser {


    /**
     * Används för dialoger att sätta en dialogpart
     * **/
    public AccessToken ActiveUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        var principal = (Principal)authentication.getPrincipal();

        var keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;

        var accessToken = keycloakPrincipal.getKeycloakSecurityContext().getToken();

        return accessToken;
    }

}
