package se.metria.skogsmaskindata;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
public class SkogsmaskindataApplication {

	public static void main(String[] args) {
		XploreTracing.StartTracing();
		SpringApplication.run(SkogsmaskindataApplication.class, args);
	}

	@Bean
	public GeometryFactory getGeometryFactory() {
		return new GeometryFactory(new PrecisionModel(), 3006);
	}
}
