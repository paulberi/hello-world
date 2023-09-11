package se.metria.xplore.sok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
@EnableConfigurationProperties(SokProperties.class)
public class SokApplication implements WebMvcConfigurer {

	public SokApplication() {
	}

	public static void main(String[] args) {
		XploreTracing.StartTracing();
		SpringApplication.run(SokApplication.class, args);
	}
}
