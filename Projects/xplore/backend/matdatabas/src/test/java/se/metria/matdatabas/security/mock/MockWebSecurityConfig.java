package se.metria.matdatabas.security.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import se.metria.matdatabas.security.ApiKeySecurityConfig;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class MockWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public ApiKeySecurityConfig apiKeySecurityConfig() {
		return new ApiKeySecurityConfig(null);
	}
}
