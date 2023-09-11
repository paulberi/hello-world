package se.metria.xplore.kundconfig.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.metria.xplore.keycloak.KeyCloakCommunicationException;
import se.metria.xplore.keycloak.service.KeyCloakService;
import se.metria.xplore.kundconfig.openapi.model.MarkkollUserDto;
import se.metria.xplore.kundconfig.openapi.model.UserInfoDto;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MarkkollService {

    WebClient webClient;
    WebClient webClientToken;

    @Value("${markkoll.url:}")
    String markkollUrl;

    @Value("${markkoll.baseAuthUrl}")
    String baseUrlAdmin;

    @Value("${markkoll.realm}")
    String realm;

    @Value("${markkoll.keycloakClientName}")
    String keycloakClientName;

    @Value("${markkoll.systemuserUsername}")
    String systemuserUsername;

    @Value("${markkoll.systemuserPassword}")
    String systemuserPassword;

    public MarkkollService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        webClientToken = webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    public List<MarkkollUserDto> getMarkkollKundAdminsForKund(String kundId) {
        log.info("Hämtar kundadministratörer för kund: {}", kundId);
        var url = String.format("%s/api/admin/kund/%s/kundadmin", markkollUrl, kundId);

        var token = getNewSystemUserToken(webClientToken);

        return webClient
                .get()
                .uri(url)
                .headers(h -> h.setBearerAuth(token.getAccess_token()))
                .retrieve()
                .bodyToFlux(MarkkollUserDto.class)
                .timeout(Duration.ofSeconds(10))
                .collect(Collectors.toList())
                .block();
    }
    
    public List<MarkkollUserDto> getMarkkollUsersForKund(String kundId) {
        var url = String.format("%s/api/kund/%s/users", markkollUrl, kundId);

        var token = getNewSystemUserToken(webClientToken);

        return webClient
                .get()
                .uri(url)
                .headers(h -> h.setBearerAuth(token.getAccess_token()))
                .retrieve()
                .bodyToFlux(MarkkollUserDto.class)
                .timeout(Duration.ofSeconds(10))
                .collect(Collectors.toList())
                .block();
    }

    public Void createKundAdmin(String kundId, UserInfoDto userInfoDto) {
        var url = String.format("%s/api/admin/kund/%s/user/admin", markkollUrl, kundId);

        var token = getNewSystemUserToken(webClientToken);

        return webClient
                .post()
                .uri(url)
                .body(BodyInserters.fromValue(userInfoDto))
                .headers(h -> h.setBearerAuth(token.getAccess_token()))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Void deleteUser(String userId ) {
        var url = String.format("%s/api/user/%s", markkollUrl, userId);
        var token = getNewSystemUserToken(webClientToken);

        return webClient
                .delete()
                .uri(url)
                .headers(h -> h.setBearerAuth(token.getAccess_token()))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private KeyCloakService.Token getNewSystemUserToken(WebClient webClient) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        var url = String.format("%s/auth/realms/%s/protocol/openid-connect/token", baseUrlAdmin, realm);

        formData.add("grant_type", "password");
        formData.add("username", systemuserUsername);
        formData.add("password", systemuserPassword);
        formData.add("client_id", keycloakClientName);

        Mono<ClientResponse> loginRequest = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .syncBody(formData)
                .exchange();

        ClientResponse response = loginRequest.block();

        if (!response.statusCode().is2xxSuccessful()) {
            var msg = response.bodyToMono(String.class).block();

            throw new KeyCloakCommunicationException("Fetch "+systemuserUsername+" token from KeyCloak failed: " +
                    response.statusCode() + " " + msg);
        }

        return response.bodyToMono((KeyCloakService.Token.class)).block();
    }
}