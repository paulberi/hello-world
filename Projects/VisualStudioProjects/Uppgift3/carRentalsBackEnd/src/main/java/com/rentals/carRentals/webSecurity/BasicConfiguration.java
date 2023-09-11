//package com.rentals.carRentals.webSecurity;
//
//import java.util.Arrays;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//@Configuration
//@EnableWebSecurity
//public class BasicConfiguration extends WebSecurityConfigurerAdapter {
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		auth.inMemoryAuthentication()
//		.withUser("user")
//		.password(encoder.encode("password"))
//		.roles("Customer").and()
//		.withUser("admin")
//		.password(encoder.encode("admin"))
//		.roles("Customer", "Admin");
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//				// För att H2-console inloggningsformuläret ska fungera
//				.csrf().disable().cors()
//				.and()
//				.authorizeRequests()
//                .antMatchers("/api/v1/allUserRentals/user/**").hasAnyRole("Admin","Customer")
//                .antMatchers("/api/v1/cars").permitAll()
//                .antMatchers("/api/v1/customers").permitAll()
//                .antMatchers("/api/v1/allUsers/postUser").permitAll()
//                .antMatchers("/api/v1/allUsers/**").hasAnyRole("Customer","Admin")
//                .antMatchers("/api/v1/cars/**").hasAnyRole("Customer","Admin")
//                .antMatchers("/api/v1/ordercar").hasAnyRole("Customer","Admin")
//                .antMatchers("/api/v1/updateorder/**").hasAnyRole("Customer","Admin")
//                .antMatchers("/api/v1/myorders/**").hasAnyAuthority("Customer","Admin")
//                .antMatchers("/api/v1/**").hasRole("Admin")
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers("http://127.0.0.1:8081/Angular/javaEE/CarRentalFrontEnd/home.html").permitAll()
//				.anyRequest()
//				.permitAll()
//				.and()
//				.httpBasic()
//				.and()
//				.formLogin()
//				.loginPage("http://127.0.0.1:8081/Angular/javaEE/CarRentalFrontEnd/home.html")
//				.usernameParameter("username")
//				.passwordParameter("password")
//				.permitAll();
//		// http.headers().frameOptions().disable();
//	}
//
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("*"));
//		configuration.setAllowCredentials(true);
//		configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
//				"Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control",
//				"Content-Type", "Authorization"));
//		configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}
//}