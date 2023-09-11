package se.metria.matdatabas.security;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@ConditionalOnProperty(
        name = "se.metria.matdatabas.webSecurity",
        havingValue = "keycloak",
        matchIfMissing = true)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class KeycloakSecuritConfig extends KeycloakWebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, MatdatabasKeycloakAuthenticationProvider authenticationProvider) {
//        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
//        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
//        auth.authenticationProvider(keycloakAuthenticationProvider);
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
//        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
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
                /* .antMatchers("/api/**").authenticated()  TODO: Om det inte finns några anrop som är */
                .anyRequest().permitAll();
    }

    // Detta är från keycloaks dokumentation:
    //
    // Avoid double Filter bean registration
    // Spring Boot attempts to eagerly register filter beans with the web application context.
    // Therefore, when running the Keycloak Spring Security adapter in a Spring Boot environment,
    // it may be necessary to add FilterRegistrationBeans to your security configuration to prevent the
    // Keycloak filters from being registered twice.
    //
    // Utan detta gjordes t.ex. basic authentisering gjordes två gånger för varje sådant anrop. >:(
    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakAuthenticatedActionsFilterBean(
            KeycloakAuthenticatedActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakSecurityContextRequestFilterBean(
            KeycloakSecurityContextRequestFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    @RequestScope
    public MatdatabasUser getMatdatabasUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof MatdatabasKeycloakAuthenticationToken) {
            return ((MatdatabasKeycloakAuthenticationToken) auth).getMatdatabasUser();
        }

        return (MatdatabasUser) auth.getDetails();
    }

    @Bean
    @RequestScope
    public KeycloakSecurityContext getKeycloakSecurityContext() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        KeycloakAuthenticationToken authenticationToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
        return authenticationToken.getAccount().getKeycloakSecurityContext();
    }

    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }
}
