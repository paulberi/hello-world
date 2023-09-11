package se.metria.matdatabas;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import se.metria.matdatabas.common.ProxyProperties;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.xplore.keycloak.KeyCloakProperties;
import se.metria.xplore.keycloak.service.KeyCloakService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableJpaRepositories
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider", auditorAwareRef = "auditorAware")
@EnableConfigurationProperties({KeyCloakProperties.class, ProxyProperties.class})
public class MatdatabasConfiguration {

	private final MatdatabasUser user;

	public MatdatabasConfiguration(MatdatabasUser user) {
		this.user = user;
	}

	@Bean
	public DateTimeProvider dateTimeProvider() {
		return () -> Optional.of(LocalDateTime.now());
	}
		
	@Bean
	public AuditorAware<Integer> auditorAware() {
		return () -> Optional.of(user.getId());
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public KeyCloakService keyCloakService(KeyCloakProperties keyCloakProperties) {
		return new KeyCloakService(keyCloakProperties);
	}

	@Bean
	public TemplateEngine emailTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		// Resolver for TEXT emails
		templateEngine.addTemplateResolver(textTemplateResolver());
		// Resolver for HTML emails (except the editable one)
		templateEngine.addTemplateResolver(htmlTemplateResolver());
		// Resolver for HTML editable emails (which will be treated as a String)
		templateEngine.addTemplateResolver(stringTemplateResolver());
		// Message source, internationalization specific to emails
		templateEngine.setTemplateEngineMessageSource(emailMessageSource());
		templateEngine.addDialect(new Java8TimeDialect());
		return templateEngine;
	}

	private ITemplateResolver textTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(Integer.valueOf(1));
		templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
		templateResolver.setPrefix("/mail/");
		templateResolver.setSuffix(".txt");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	private ITemplateResolver htmlTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(Integer.valueOf(2));
		templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
		templateResolver.setPrefix("/mail/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	private ITemplateResolver stringTemplateResolver() {
		final StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setOrder(Integer.valueOf(3));
		// No resolvable pattern, will simply process as a String template everything not previously matched
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	@Bean
	public ResourceBundleMessageSource emailMessageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("mail/MailMessages");
		return messageSource;
	}
}
