package se.metria.xplore.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import se.metria.xplore.authorization.JwtDecodeInterceptor;
import se.metria.xplore.tracing.XploreTracing;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(AppConfigProperties.class)
@ComponentScan("se.metria.xplore")
@EnableCaching
public class AppConfigApplication implements WebMvcConfigurer {
    private JwtDecodeInterceptor interceptor;

    public AppConfigApplication(JwtDecodeInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }

    public static void main(String[] args) {
        XploreTracing.StartTracing();
        SpringApplication.run(AppConfigApplication.class, args);
    }
}
