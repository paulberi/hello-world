package se.metria.xplore.samrad;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import se.metria.xplore.samrad.security.config.PathBasedConfigResolver;

@SpringBootApplication
@OpenAPIDefinition
public class SamradApplication {

    public static void main(String[] args) {
        SpringApplication.run(SamradApplication.class, args);
    }


    @Bean
    @ConditionalOnMissingBean(PathBasedConfigResolver.class)
    public KeycloakConfigResolver keycloakConfigResolver(){
        return new PathBasedConfigResolver();
    }

}