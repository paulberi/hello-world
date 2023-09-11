package se.metria.markkoll.security.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@TestConfiguration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(jsr250Enabled = true, proxyTargetClass = true)
@Order(1)
public class MockWebSecurityConfig extends WebSecurityConfigurerAdapter {

}
