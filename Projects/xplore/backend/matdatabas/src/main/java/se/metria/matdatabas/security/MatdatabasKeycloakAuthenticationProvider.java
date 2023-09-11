package se.metria.matdatabas.security;

import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class MatdatabasKeycloakAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(MatdatabasKeycloakAuthenticationProvider.class);

    private MatdatabasUserDetailsService matdatabasUserDetailsService;
    private SystemloggService systemloggService;
    private AnvandareService anvandareService;


    public MatdatabasKeycloakAuthenticationProvider(MatdatabasUserDetailsService matdatabasUserDetailsService, SystemloggService systemloggService, AnvandareService anvandareService) {
        this.matdatabasUserDetailsService = matdatabasUserDetailsService;
        this.systemloggService = systemloggService;
        this.anvandareService = anvandareService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken)authentication;

        SimpleKeycloakAccount simpleKeycloakAccount = (SimpleKeycloakAccount) token.getDetails();
        // token.getAccount().getRoles() för att hämta keycloak roller

        var userID = simpleKeycloakAccount.getPrincipal().getName();

        try {
            MatdatabasUser user = matdatabasUserDetailsService.getMatdatabasUser(userID);

            LocalDateTime nowMinusInloggadSenastInterval = LocalDateTime.now().minusHours(8);
            LocalDateTime nowMinusLogInterval = LocalDateTime.now().minusHours(24);
            LocalDateTime inloggadSenast = user.getInloggadSenast();

            if (inloggadSenast == null || inloggadSenast.isBefore(nowMinusInloggadSenastInterval)) {
                anvandareService.updateInloggadSenast(user.getId());
            }

            if (inloggadSenast == null || inloggadSenast.isBefore(nowMinusLogInterval)) {
                systemloggService.addHandelseUserLoggedIn(user.getId());
            }

            MatdatabasKeycloakAuthenticationToken newToken = new MatdatabasKeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), user.getAuthorities());

            newToken.setMatdatabasUser(user);

            return newToken;
        } catch (EmptyResultDataAccessException e) {
            // Användar finns inte i databasen men hade ett korrekt nyckel. Vi flejkar användaren intern men ger
            // den inte några roller. Om vi ger fel här så kommer det att bli ett 401 svar i rest anropet och då
            // kommer klienten bara att hämta en ny nycker och försöka igen. Nu blir det 403 istället.

            MatdatabasKeycloakAuthenticationToken newToken = new MatdatabasKeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), Collections.emptyList());

            MatdatabasUser user = new MatdatabasUser("-","-",false,false,
                    false,true,Collections.emptyList(),"-","-",null,-99, LocalDateTime.now());
            newToken.setMatdatabasUser(user);

            return newToken;
        }

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return KeycloakAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
