package se.metria.matdatabas.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import se.metria.matdatabas.kolibri.ApiClient;
import se.metria.matdatabas.kolibri.api.DevicesApi;
import se.metria.matdatabas.kolibri.api.MeasurementsApi;

@Configuration
public class KolibriConfiguration {
    @Value("${importservice.kolibri.token}")
    private String kolibriToken;

    @Bean
    public DevicesApi kolibriDevicesApi(@Qualifier("proxyWebclientBuilder") WebClient.Builder webClientBuilder) {
        final ApiClient apiClient = new ApiClient(webClientBuilder.build(), null,null);
        apiClient.addDefaultHeader("userOid", kolibriToken);
        return new DevicesApi(apiClient);
    }

    @Bean
    public MeasurementsApi kolibriMeasuremntsApi(@Qualifier("proxyWebclientBuilder") WebClient.Builder webClientBuilder) {
        final ApiClient apiClient = new ApiClient(webClientBuilder.build(), null,null);
        apiClient.addDefaultHeader("userOid", kolibriToken);
        return new MeasurementsApi(apiClient);
    }
}
