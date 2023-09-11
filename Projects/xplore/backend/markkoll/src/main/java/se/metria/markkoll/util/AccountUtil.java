package se.metria.markkoll.util;

import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class AccountUtil {

    public static final String MARKKOLL_SYSTEM_USER_FRIENDLY_NAME = "Markkoll";

    public static String getUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var keycloakPrincipal = (KeycloakPrincipal)authentication.getPrincipal();

        return keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }
}
