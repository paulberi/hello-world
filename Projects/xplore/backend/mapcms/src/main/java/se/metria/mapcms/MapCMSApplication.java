package se.metria.mapcms;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import se.metria.mapcms.security.config.PathBasedConfigResolver;
import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
public class MapCMSApplication {

    public static void main(String[] args) {
        XploreTracing.StartTracing();
        SpringApplication.run(MapCMSApplication.class, args);
    }


    @Bean
    @ConditionalOnMissingBean(PathBasedConfigResolver.class)
    public KeycloakConfigResolver keycloakConfigResolver(){
        return new PathBasedConfigResolver();
    }
}