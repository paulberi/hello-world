package se.metria.mapcms.security.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@TestConfiguration
@EnableWebSecurity
public class MockWebSecurityConfig  extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api/kunder/**", "/actuator/health").permitAll()
                .anyRequest().authenticated();
    }
}
