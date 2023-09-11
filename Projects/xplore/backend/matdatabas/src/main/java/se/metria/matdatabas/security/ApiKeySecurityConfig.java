
package se.metria.matdatabas.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.UUID;

@Configuration
@Order(1)
@EnableWebSecurity
@ConditionalOnExpression("{'keycloak', 'saml', 'fake'}.contains('${se.metria.matdatabas.webSecurity:saml}')")
public class ApiKeySecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(ApiKeySecurityConfig.class);

    private final MatdatabasUserDetailsService matdatabasUserDetailsService;

    private String apiKey;

    public ApiKeySecurityConfig(MatdatabasUserDetailsService matdatabasUserDetailsService) {
        this.matdatabasUserDetailsService = matdatabasUserDetailsService;

        apiKey = UUID.randomUUID().toString();

        logger.info("APIkey: "+apiKey);
    }

    public String getApiKey() {
        return apiKey;
    };

    public String getAuthEndpoint() {
       return "/api/systemAuth";
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/systemAuth").authorizeRequests().antMatchers("/**").authenticated()
                .and().httpBasic();
        http.csrf().disable();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return authentication -> {
            String user = (String) authentication.getPrincipal();
            String password = (String) authentication.getCredentials();

            if (user.equals("SYSTEM") && password.equals(apiKey)) {
                MatdatabasUser matdatabasUser = matdatabasUserDetailsService.getMatdatabasUser("SYSTEM");
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(matdatabasUser.getUsername(),
                        "", matdatabasUser.getAuthorities());
                token.setDetails(matdatabasUser);

                return token;
            } else {
                throw new BadCredentialsException("Bad credentials");
            }
        };
    }
}
