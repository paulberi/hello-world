package com.example.api.demoapi;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class BasicConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(encoder.encode("admin"))
                .roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //För att H2-console inloggningsformuläret ska fungera
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/exam/questions").permitAll()
                .antMatchers("/api/v1/exam/addquestion").permitAll()
                .antMatchers("/api/v1/exam//success/{student}").permitAll()
                .antMatchers(" /api/v1/exam/examresult").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}