package se.metria.markkoll.annotations;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.markkoll.config.ModelMapperConfiguration;
import se.metria.markkoll.PropertyOverrideContextInitializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ContextConfiguration(initializers = PropertyOverrideContextInitializer.class)
@Import(ModelMapperConfiguration.class)
public @interface MarkkollServiceTest {
}