package se.metria.markkoll.util;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class TestAuditing {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("Test Testsson");
    }
}