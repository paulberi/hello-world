package se.metria.markkoll.annotations;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.markkoll.MarkkollTestConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;

/** Jag får inte igång tester som innehåller JOOQ (eller native queries för den delen) med @DataJpaTest. Däremot så
fungerar det om man kickar igång hela Springapplikationen. Det tar lite längre tid, men är bättre än att inte få igång
 tester alls. */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
@Import(MarkkollTestConfiguration.class)
public @interface MarkkollDatabaseTestJooq {
}
