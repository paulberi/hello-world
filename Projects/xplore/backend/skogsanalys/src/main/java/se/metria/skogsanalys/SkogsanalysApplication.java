package se.metria.skogsanalys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.metria.xplore.tracing.XploreTracing;

@SpringBootApplication
public class SkogsanalysApplication {
    public static void main(String[] args) {
        XploreTracing.StartTracing();
        SpringApplication.run(SkogsanalysApplication.class, args);
    }
}
