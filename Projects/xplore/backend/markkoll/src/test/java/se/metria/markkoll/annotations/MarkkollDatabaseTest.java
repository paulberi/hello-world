package se.metria.markkoll.annotations;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import se.metria.markkoll.DataJPATestConfiguration;
import se.metria.markkoll.util.TestAuditing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MarkkollServiceTest
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { DataJPATestConfiguration.class }))
@FlywayTest
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
@Import(TestAuditing.class)
public @interface MarkkollDatabaseTest {
}
