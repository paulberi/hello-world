package se.metria.xplore.castor.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
public class CastorIntegrationApplication {
	public static void main(String[] args) {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

		XploreTracing.StartTracing();

		SpringApplication.run(CastorIntegrationApplication.class, args);
	}
}
