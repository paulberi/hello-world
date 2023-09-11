package se.metria.markkoll.annotations;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import se.metria.markkoll.config.ModelMapperConfiguration;
import se.metria.markkoll.security.config.ResourceServerConfig;
import se.metria.markkoll.config.WebMvcConfiguration;
import se.metria.markkoll.security.mock.MockWebSecurityConfig;

import javax.validation.OverridesAttribute;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({WebMvcConfiguration.class, ModelMapperConfiguration.class, MockWebSecurityConfig.class})
@WebMvcTest(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ResourceServerConfig.class}),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MockWebSecurityConfig.class})
)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public @interface MarkkollMVCTest {
    @OverridesAttribute(constraint=WebMvcTest.class, name="controllers")
    Class<?>[] controllers();
}
