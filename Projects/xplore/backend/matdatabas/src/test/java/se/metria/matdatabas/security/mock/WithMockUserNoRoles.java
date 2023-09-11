package se.metria.matdatabas.security.mock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithMockUser;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="noroles", authorities = {})
public @interface WithMockUserNoRoles {

}
