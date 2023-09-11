package se.metria.xplore.kundconfig.security;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="global_admin", roles = {KundConfigRole.ADMIN_API})
public @interface WithMockUserGlobalAdmin {
}
