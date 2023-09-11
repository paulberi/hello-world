package se.metria.matdatabas.security.mock;

import org.springframework.security.test.context.support.WithMockUser;
import se.metria.matdatabas.security.MatdatabasRole;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="matrapportor", authorities = {MatdatabasRole.OBSERVATOR})
public @interface WithMockUserObservator {

}
