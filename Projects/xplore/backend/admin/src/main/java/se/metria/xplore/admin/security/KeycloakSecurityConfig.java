package se.metria.xplore.admin.security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@ConditionalOnProperty(
        name = "se.metria.xplore.admin.webSecurity",
        havingValue = "keycloak",
        matchIfMissing = true)
@KeycloakConfiguration
@EnableGlobalMethodSecurity(jsr250Enabled = true, proxyTargetClass = true)
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        http
                // CSRF-skydd är påslaget som standard och bra att ha på publika webbsidor som använder sessionsbaserad
                // authentisering. Vi använder oauth i bearer headers istället
                .csrf().disable();

        http
                // Vi vill inte att det skapas sessioner om vi inte kör via en web-sida, därför slås det av här.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll();
    }
}
