package se.metria.markkoll.security.mock;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="admin", roles = {"null"})
public @interface WithMockUserMarkhandlaggare {
}
