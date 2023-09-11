package com.rentals.carRentals.webSecurity;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfigurations extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsAuth();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() { 
	return 	new BCryptPasswordEncoder();
	
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		System.out.println("paul here checking USERDETAILS END 1" );
		
		DaoAuthenticationProvider authProvider= new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
		
	}
	  @Override
      public void configure(WebSecurity web) throws Exception {
          web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
      }
	
	   @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

			System.out.println("paul here checking USERDETAILS END 2" );
	        auth.authenticationProvider(authenticationProvider());
	    }
	   

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {

			System.out.println("paul here checking USERDETAILS END" );
	        http.csrf().disable()
	        		.cors()
	        		.and()
	                .authorizeRequests()
//	                .antMatchers("/api/v1/allUserRentals/user/**").hasAnyAuthority("Admin","Customer")
//	                .antMatchers("/api/v1/cars").permitAll()
//	                .antMatchers("/api/v1/customers").permitAll()
//	                .antMatchers("/api/v1/allUsers/postUser").permitAll()
//	                .antMatchers("/api/v1/allUsers/**").hasAnyAuthority("Customer","Admin")
//	                .antMatchers("/api/v1/cars/**").hasAnyAuthority("Customer","Admin")
//	                .antMatchers("/api/v1/ordercar").hasAnyAuthority("Customer","Admin")
//	                .antMatchers("/api/v1/updateorder/**").hasAnyAuthority("Customer","Admin")
//	                .antMatchers("/api/v1/myorders/**").hasAnyAuthority("Customer","Admin")
//	                .antMatchers("/api/v1/**").hasAuthority("Admin")
	                .antMatchers("/h2-console/**").permitAll()
	                .antMatchers("/api/v1/**").permitAll()
	                .anyRequest()
	                .authenticated()
	                .and()
	                .formLogin().permitAll()
	                .successHandler(successHandler)
	                .permitAll()
	                .and()
	                .logout()
	                .permitAll()
	                .logoutSuccessUrl("/");
	        http.headers().frameOptions().disable();

	    }
	    
	  	    @Bean
	  	    CorsConfigurationSource corsConfigurationSource() {
	  	        CorsConfiguration configuration = new CorsConfiguration();
	  	        configuration.setAllowedOrigins(Arrays.asList("*"));
	  	        configuration.setAllowCredentials(true);
	  	        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers","Access-Control-Allow-Origin","Access-Control-Request-Method", "Access-Control-Request-Headers","Origin","Cache-Control", "Content-Type", "Authorization"));
	  	        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
	  	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	  	        source.registerCorsConfiguration("/**", configuration);
	  	        return source;
	  	    }
	    
	}    
	  