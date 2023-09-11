package se.metria.markkoll.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.metria.kundconfig.openapi.model.FastighetsokAuthDto;
import se.metria.kundconfig.openapi.model.MetriaMapsAuthDto;
import se.metria.markkoll.openapi.model.KundDto;
import se.metria.markkoll.service.admin.UserService;

import java.time.Duration;

@Service
public class KundConfigService {

    @Value("${kundconfig.url:}")
    String kundConfigUrl;

    private final WebClient webClient;

    public KundConfigService(WebClient.Builder webClientBuilder, UserService userService) {
        this.webClient = webClientBuilder.build();
        this.userService = userService;
    }

    @NonNull
    private final UserService userService;

    public MetriaMapsAuthDto getMetriaMapsAuth() {
        var kund = userService.getCurrentUser().getKundId();
        var url = String.format("%s/api/kund/%s/metria-maps", kundConfigUrl, kund);

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(MetriaMapsAuthDto.class)
                .timeout(Duration.ofSeconds(10))
                .block();
    }

    public FastighetsokAuthDto getFastighetsokAuth() {
        var kund = userService.getCurrentUser().getKundId();;
        var url = String.format("%s/api/kund/%s/fastighetsok", kundConfigUrl, kund);

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(FastighetsokAuthDto.class)
                .timeout(Duration.ofSeconds(10))
                .block();
    }

    public KundDto getKund(String kundId) {
        var url = String.format("%s/api/kund/%s", kundConfigUrl,kundId);

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(KundDto.class)
                .timeout(Duration.ofSeconds(10))
                .block();
    }
}
