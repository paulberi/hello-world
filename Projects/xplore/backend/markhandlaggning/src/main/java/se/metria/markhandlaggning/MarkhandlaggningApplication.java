package se.metria.markhandlaggning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import se.metria.xplore.tracing.XploreTracing;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(MarkhandlaggningProperties.class)
@ComponentScan("se.metria")
public class MarkhandlaggningApplication {

	public static void main(String[] args) {
		XploreTracing.StartTracing();
		SpringApplication.run(MarkhandlaggningApplication.class, args);
	}

}
