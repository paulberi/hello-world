package se.metria.markhandlaggning.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class ResourceServerConfig extends KeycloakWebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                // CSRF-skydd är påslaget som standard och bra att ha på publika webbsidor som använder sessionsbaserad
                // authentisering. Vi använder oauth i bearer headers istället, (observera att sessionshaneringen måste
                // slås av för att det ska vara säkert, se nedan.
                .csrf().disable();

        http
                // Vi vill inte att det skapas sessioner därför slås det av här.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                .and()
                .authorizeRequests()
                /* .antMatchers("/api/**").authenticated() TODO: Kolla om vi inte kan ha detta så man inte har icke-authenticerade anrop av misstag*/
                .anyRequest().permitAll();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)     throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }
    @Bean
    @Override
    protected SessionAuthenticationStrategy
    sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }
    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}
