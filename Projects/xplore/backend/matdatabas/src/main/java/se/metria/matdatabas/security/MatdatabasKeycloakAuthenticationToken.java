package se.metria.matdatabas.security;

import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MatdatabasKeycloakAuthenticationToken extends KeycloakAuthenticationToken {
    private MatdatabasUser matdatabasUser;

    public MatdatabasKeycloakAuthenticationToken(KeycloakAccount account, boolean interactive) {
        super(account, interactive);
    }

    public MatdatabasKeycloakAuthenticationToken(KeycloakAccount account, boolean interactive, Collection<? extends GrantedAuthority> authorities) {
        super(account, interactive, authorities);
    }

    public MatdatabasUser getMatdatabasUser() {
        return matdatabasUser;
    }

    public void setMatdatabasUser(MatdatabasUser matdatabasUser) {
        this.matdatabasUser = matdatabasUser;
    }
}
