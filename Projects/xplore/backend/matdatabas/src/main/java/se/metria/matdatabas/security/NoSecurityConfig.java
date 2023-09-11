package se.metria.matdatabas.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static se.metria.matdatabas.security.MatdatabasRole.getRoles;

@Configuration
@ConditionalOnProperty(
		name = "se.metria.matdatabas.webSecurity",
		havingValue = "fake")
@EnableWebSecurity
public class NoSecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	@RequestScope
	public MatdatabasUser getMatdatabasUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (MatdatabasUser) auth.getDetails();
	}

	private MatdatabasUser admin() {
		return new MatdatabasUser("test-admin",
				"{noop}pass",
				true,
				true,
				true,
				true,
				getRoles(2).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
				"Admin",
				"TestCompany",
				null,
				666666, LocalDateTime.now());
	}

	private MatdatabasUser handlaggare() {
		return new MatdatabasUser("test-handlaggare",
				"{noop}pass",
				true,
				true,
				true,
				true,
				getRoles(1).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
				"Tillståndshandläggare",
				"TestCompany",
				null,
				666667, LocalDateTime.now());
	}

	private MatdatabasUser rapportor() {
		return new MatdatabasUser("test-rapportor",
				"{noop}pass",
				true,
				true,
				true,
				true,
				getRoles(0).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
				"Mätrapportör",
				"TestCompany",
				null,
				666668, LocalDateTime.now());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**").authenticated()
				.and().httpBasic();
		http.csrf().disable();
	}

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return authentication -> {
			String username = (String) authentication.getPrincipal();
			String password = (String) authentication.getCredentials();

			if (!password.equals("pass")) {
				throw new BadCredentialsException("Bad credentials");
			}

			MatdatabasUser matdatabasUser;

			switch (username) {
				case "test-admin": matdatabasUser = admin(); break;
				case "test-handlaggare": matdatabasUser = handlaggare(); break;
				case "test-rapportor": matdatabasUser = rapportor(); break;
				default: throw new BadCredentialsException("Bad credentials");
			}

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(matdatabasUser.getUsername(),
						"", matdatabasUser.getAuthorities());
			token.setDetails(matdatabasUser);

			return token;
		};
	}
}
