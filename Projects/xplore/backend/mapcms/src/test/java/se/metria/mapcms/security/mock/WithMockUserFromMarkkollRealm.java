package se.metria.mapcms.security.mock;

import org.springframework.security.test.context.support.WithMockUser;
import se.metria.mapcms.commons.keycloakroles.KeycloakRoles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="santa", roles = {KeycloakRoles.ADMIN_API_USER})
public @interface WithMockUserFromMarkkollRealm {
}
