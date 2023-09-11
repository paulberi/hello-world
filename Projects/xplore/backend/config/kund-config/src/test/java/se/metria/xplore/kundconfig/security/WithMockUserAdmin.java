package se.metria.xplore.kundconfig.security;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="admin_api", roles = {KundConfigRole.ADMIN_API})
public @interface WithMockUserAdmin {
}
