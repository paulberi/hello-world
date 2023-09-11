package se.metria.markkoll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
public class MarkkollApplication {

    public static void main(String[] args) {
        XploreTracing.StartTracing();
        SpringApplication.run(MarkkollApplication.class, args);
    }
}
