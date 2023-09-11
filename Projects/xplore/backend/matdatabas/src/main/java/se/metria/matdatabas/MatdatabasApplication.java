package se.metria.matdatabas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
public class MatdatabasApplication {

	public static void main(String[] args) {
		XploreTracing.StartTracing();
		SpringApplication.run(MatdatabasApplication.class, args);
	}
}
