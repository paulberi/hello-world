package se.metria.markkoll;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import se.metria.markkoll.config.ModelMapperConfiguration;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
@Import(ModelMapperConfiguration.class)
public class DataJPATestConfiguration {
    @Bean
    public DSLContext create(DataSource datasource) throws Exception {
        /* Den datasource vi har här är _inte_ samma datasource som används av repositories i tester. Av den anledningen
        så får vi inte ut något av att testa JOOQ, tills man har lyckats åtgärda problemet */
        var url = datasource.getConnection().getMetaData().getURL();
        return DSL.using(datasource, SQLDialect.POSTGRES);
    }
}
