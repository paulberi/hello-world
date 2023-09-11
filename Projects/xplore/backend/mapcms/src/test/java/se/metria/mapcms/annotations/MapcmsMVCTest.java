package se.metria.mapcms.annotations;


import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import se.metria.mapcms.security.config.SecConfig;
import se.metria.mapcms.security.mock.MockWebSecurityConfig;

import javax.validation.OverridesAttribute;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/***
 * från markkoll
 * **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(MockWebSecurityConfig.class)
@WebMvcTest(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecConfig.class}),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MockWebSecurityConfig.class})
)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public @interface MapcmsMVCTest {

    @OverridesAttribute(constraint=WebMvcTest.class, name="controllers")
    Class<?>[] controllers();
}
