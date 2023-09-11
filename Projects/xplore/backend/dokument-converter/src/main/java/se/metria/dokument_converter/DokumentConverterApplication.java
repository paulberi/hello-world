package se.metria.dokument_converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
public class DokumentConverterApplication {

	public static void main(String[] args) {
		XploreTracing.StartTracing();
		SpringApplication.run(DokumentConverterApplication.class, args);
	}

}
