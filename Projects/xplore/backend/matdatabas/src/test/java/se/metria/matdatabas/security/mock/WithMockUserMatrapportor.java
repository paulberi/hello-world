package se.metria.matdatabas.security.mock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithMockUser;

import se.metria.matdatabas.security.MatdatabasRole;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="matrapportor", authorities = {MatdatabasRole.OBSERVATOR, MatdatabasRole.MATRAPPORTOR})
public @interface WithMockUserMatrapportor {

}
