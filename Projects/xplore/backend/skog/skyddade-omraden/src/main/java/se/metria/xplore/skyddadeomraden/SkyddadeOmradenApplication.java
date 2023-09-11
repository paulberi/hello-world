package se.metria.xplore.skyddadeomraden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import se.metria.xplore.authorization.JwtVerifyInterceptor;
import se.metria.xplore.tracing.XploreTracing;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(SkyddadeOmradenProperties.class)
@ComponentScan("se.metria.xplore")
public class SkyddadeOmradenApplication implements WebMvcConfigurer {
    private JwtVerifyInterceptor interceptor;

    public SkyddadeOmradenApplication(JwtVerifyInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }

    public static void main(String[] args) {
        XploreTracing.StartTracing();
        SpringApplication.run(SkyddadeOmradenApplication.class, args);
    }
}
