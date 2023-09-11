package se.metria.xplore.samrad.security.config;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@KeycloakConfiguration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@DependsOn("keycloakConfigResolver")
public class SecConfig extends KeycloakWebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth){
        KeycloakAuthenticationProvider authenticationProvider = new KeycloakAuthenticationProvider();
        authenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/kunder/**", "/actuator/health").permitAll()
                .anyRequest().authenticated();

    }

    @Bean
    WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
          
            @Override
            public void addCorsMappings( CorsRegistry registry){
                registry.addMapping("/*")
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowCredentials(true);
            }
        };
    }
}

