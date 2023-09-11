package se.metria.xplore.kundconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
public class KundConfigApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        XploreTracing.StartTracing();
        SpringApplication.run(KundConfigApplication.class, args);
    }
}
