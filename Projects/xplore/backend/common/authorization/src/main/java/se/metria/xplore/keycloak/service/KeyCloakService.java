package se.metria.xplore.keycloak.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import se.metria.xplore.keycloak.KeyCloakCommunicationException;
import se.metria.xplore.keycloak.KeyCloakProperties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Service
@Slf4j
@EnableConfigurationProperties(KeyCloakProperties.class)
public class KeyCloakService {
    ConcurrentHashMap<String,Token> tokenCache = new ConcurrentHashMap<>();
    Scheduler scheduler = Schedulers.elastic();
    WebClient webClientAdmin;
    WebClient webClientToken;
    WebClient webClientAdminToken;
    KeyCloakProperties keyCloakProperties;

    public KeyCloakService(KeyCloakProperties keyCloakProperties) {
        this.keyCloakProperties = keyCloakProperties;

        webClientAdmin = WebClient.builder()
                .baseUrl(keyCloakProperties.getAdminBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .filter(retryOn401Function())
                .build();

        webClientAdminToken = WebClient.builder()
                .baseUrl(keyCloakProperties.getAdminBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();

        webClientToken = WebClient.builder()
                .baseUrl(keyCloakProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();

    }

    public void deleteUser(String userId) {
        if (userId == null || userId.trim().equals("")) {
            throw new IllegalArgumentException("no username supplied");
        }

        Token accessToken = getKeyCloakAdminToken(false).block();

        ClientResponse exch = webClientAdmin.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/admin/realms/{realm}/users/" + userId)
                        .build(keyCloakProperties.getAppRealm()))
                .headers(h -> h.setBearerAuth(accessToken.getAccess_token()))
                .exchange()
                .block();

        HttpStatus statusCode = exch.statusCode();

        if (!statusCode.is2xxSuccessful()) {
            throw new KeyCloakCommunicationException("User deletion failed: " + statusCode);
        }
    }

    public void deleteUserByUsername(String username) {
        deleteUser(findUser(username)[0].getId());
    }

    public UserRepresentation[] findUser(String username) {
        if (username == null || username.trim().equals("")) {
            throw new IllegalArgumentException("no username supplied");
        }

        Token accessToken = getKeyCloakAdminToken(false).block();

        ClientResponse exch = webClientAdmin.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/admin/realms/{realm}/users")
                        .queryParam("username", username)
                        .build(keyCloakProperties.getAppRealm()))
                .headers(h -> h.setBearerAuth(accessToken.getAccess_token()))
                .exchange()
                .block();

        HttpStatus statusCode = exch.statusCode();

        if (!statusCode.is2xxSuccessful()) {
            throw new KeyCloakCommunicationException("Could not find users user: " + statusCode);
        }

        return exch.bodyToMono(UserRepresentation[].class).block();
    }


    public CreateUserResult createUser(String username, String firstName, String lastName, String password) {
        Token accessToken = getKeyCloakAdminToken(false).block();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(username);
        userRepresentation.setUsername(username);
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);
        userRepresentation.setEnabled(true);

        ClientResponse exch = webClientAdmin.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/admin/realms/{realm}/users")
                        .build(keyCloakProperties.getAppRealm()))
                .headers(h -> h.setBearerAuth(accessToken.getAccess_token()))
                .syncBody(userRepresentation)
                .exchange()
                .block();

        HttpStatus statusCode = exch.statusCode();

        exch.bodyToMono(Void.class).block();

        switch (statusCode) {
            case CREATED: {
                // Skapa ok, sätt lösenord

                if (password == null)
                    return CreateUserResult.USER_CREATED;

                String userApiUrl = exch.headers().header("Location").get(0);

                CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                credentialRepresentation.setTemporary(true);
                credentialRepresentation.setType("password");
                credentialRepresentation.setValue(password);

                ClientResponse resetPasswordResponse = webClientAdmin.put()
                        .uri(userApiUrl+"/reset-password")
                        .headers(h -> h.setBearerAuth(accessToken.getAccess_token()))
                        .bodyValue(credentialRepresentation)
                        .exchange()
                        .block();

                resetPasswordResponse.bodyToMono(Void.class).block();

                if (!resetPasswordResponse.statusCode().is2xxSuccessful()) {
                    throw new KeyCloakCommunicationException("Set password for new user failed!");
                }

                return CreateUserResult.USER_CREATED;
            }

            case CONFLICT:
                // Användaren fanns redan;
                return CreateUserResult.USER_EXISTS;

            default:
                // Något gick inte som det skulle!
                throw new KeyCloakCommunicationException("Error statuscode from KeyCloak: "+statusCode);
        }
    }

    public void createRealmRole(String roleName, String roleDescription) {
        log.info("Skapar realmrollen {} till realmen {}", roleName, keyCloakProperties.getAppRealm());
        var body = new HashMap<String, String>();
        body.put("name", roleName);
        body.put("description", roleDescription);

        var accessToken = getKeyCloakAdminToken(false).block();

        webClientAdmin.post()
            .uri(uriBuilder -> uriBuilder
                .path("/auth/admin/realms/{realm}/roles")
                .build(keyCloakProperties.getAppRealm()))
            .headers(h -> h.setBearerAuth(accessToken.getAccess_token()))
            .body(BodyInserters.fromValue(body))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void addRealmRoleToUser(String userName, String roleName) {
        log.info("Lägger till rollen {} i realmen {} till användaren {}", roleName, keyCloakProperties.getAppRealm(),
            userName);

        var roleId = getRealmRoleByName(roleName).getId();
        var userId = findUser(userName)[0].id;

        var roleRepresentation = new RoleRepresentation();
        roleRepresentation.setId(roleId);
        roleRepresentation.setClientRole(false);
        roleRepresentation.setComposite(false);
        roleRepresentation.setContainerId(keyCloakProperties.getAppRealm());
        roleRepresentation.setName(roleName);
        var lst = Arrays.asList(roleRepresentation);

        var accessToken = getKeyCloakAdminToken(false).block();

        var req = webClientAdmin.post()
            .uri(uriBuilder -> uriBuilder
                .path("/auth/admin/realms/{realm}/users/{user}/role-mappings/realm")
                .build(keyCloakProperties.getAppRealm(), userId))
            .headers(h -> h.setBearerAuth(accessToken.getAccess_token()))
            .syncBody(lst)
            .exchange()
            .block();

        if (!req.statusCode().is2xxSuccessful()) {
            var msg = req.bodyToMono(String.class);
            throw new KeyCloakCommunicationException(String.format("Add realm role %s to user %s failed: %s %s",
                roleName, userName, req.statusCode().toString(), msg));
        }
    }

    public void removeRealmRoleFromUser(String userName, String roleName) {
        log.info("Tar bort rollen {} i realmen {} från användaren {}", roleName, keyCloakProperties.getAppRealm(),
                userName);

        var roleId = getRealmRoleByName(roleName).getId();
        var userId = findUser(userName)[0].id;

        var roleRepresentation = new RoleRepresentation();
        roleRepresentation.setId(roleId);
        roleRepresentation.setClientRole(false);
        roleRepresentation.setComposite(false);
        roleRepresentation.setContainerId(keyCloakProperties.getAppRealm());
        roleRepresentation.setName(roleName);
        var lst = Arrays.asList(roleRepresentation);

        var accessToken = getKeyCloakAdminToken(false).block();

        // seems .delete() does not support body
        var req = webClientAdmin
                .method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/admin/realms/{realm}/users/{user}/role-mappings/realm")
                        .build(keyCloakProperties.getAppRealm(), userId))
                .headers(h -> h.setBearerAuth(accessToken.getAccess_token()))
                .syncBody(lst)
                .exchange()
                .block();

        if (!req.statusCode().is2xxSuccessful()) {
            var msg = req.bodyToMono(String.class);
            throw new KeyCloakCommunicationException(String.format("Removed realm role %s from user %s failed: %s %s",
                    roleName, userName, req.statusCode().toString(), msg));
        }
    }

    public RoleRepresentation getRealmRoleByName(String roleName) {
        var accessToken = getKeyCloakAdminToken(false).block();

        return webClientAdmin.get()
            .uri(uriBuilder -> uriBuilder
                .path("/auth/admin/realms/{realm}/roles/{role-name}")
                .build(keyCloakProperties.getAppRealm(), roleName))
            .headers(h -> h.setBearerAuth(accessToken.getAccess_token()))
            .retrieve()
            .bodyToMono(RoleRepresentation.class)
            .block();
    }

    private ExchangeFilterFunction retryOn401Function() {
        return (request, next) -> next.exchange(request)
                .flatMap((Function<ClientResponse, Mono<ClientResponse>>) clientResponse -> {
                    int statusCode = clientResponse.statusCode().value();
                    if (statusCode == 401) {
                        return getKeyCloakAdminToken(true)
                                .flatMap(token -> {
                                    ClientRequest.Builder requestBuilder = ClientRequest.from(request);
                                    requestBuilder.headers(headers -> {
                                        headers.clear();
                                        headers.addAll(request.headers());
                                        headers.setBearerAuth(token.getAccess_token());
                                    });

                                    ClientRequest retryRequest = requestBuilder.build();
                                    return next.exchange(retryRequest);

                                });
                    } else {
                        return Mono.just(clientResponse);
                    }
                });
    }



    public Mono<Token> getKeyCloakAdminToken(boolean updateToken) {
        if (!updateToken) {
            Token cachedLogin = tokenCache.get("token");

            if (cachedLogin != null) {
                return Mono.just(cachedLogin);
            }
        }

        Mono<Token> blockingWrapper = Mono.fromCallable(() -> {
            Token newToken = getNewToken(
                    webClientAdminToken,
                    keyCloakProperties.getAdminUser(),
                    keyCloakProperties.getAdminPassword(),
                    "admin-cli",
                    keyCloakProperties.getAdminRealm()
                    );

            tokenCache.put("token",newToken);

            return newToken;
        });

        blockingWrapper = blockingWrapper.subscribeOn(scheduler);

        return blockingWrapper;
    }

    public Token getNewBatchUserToken() {
        return getNewToken(
                keyCloakProperties.getBatchUser(),
                keyCloakProperties.getBatchPassword());
    }

    public Token getNewToken(String username, String password) {
        return getNewToken(
                webClientToken,
                username,
                password,
                keyCloakProperties.getAppClient(),
                keyCloakProperties.getAppRealm());
    }

    public Token getNewAdminToken(String username, String password, String clientId, String realm) {
        return getNewToken(webClientAdminToken, username, password, clientId, realm);
    }

    public Token getNewToken(WebClient webClient, String username, String password, String clientId, String realm) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "password");
        formData.add("username", username);
        formData.add("password", password);
        formData.add("client_id", clientId);

        Mono<ClientResponse> loginRequest = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/realms/{realm}/protocol/openid-connect/token")
                        .build(realm))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .syncBody(formData)
                .exchange();

        ClientResponse response = loginRequest.block();

        if (!response.statusCode().is2xxSuccessful()) {
            var msg = response.bodyToMono(String.class).block();

            throw new KeyCloakCommunicationException("Fetch "+username+" token from KeyCloak failed: " +
                response.statusCode() + " " + msg);
        }

        return response.bodyToMono((Token.class)).block();
    }

    public enum CreateUserResult {
        USER_CREATED,
        USER_EXISTS
    }

    public static class Token {
        String access_token;

        public Token() {
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }

    public static class UserRepresentation {
        String id;
        String email;
        String username;
        String firstName;
        String lastName;
        Boolean enabled;

        public UserRepresentation() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }

    @Data
    public static class RoleRepresentation {
        private UUID id;
        private boolean clientRole;
        private boolean composite;
        private String containerId;
        private String name;
    }

    public static class CredentialRepresentation {
        Boolean temporary;
        String type;
        String value;

        public CredentialRepresentation() {
        }

        public Boolean getTemporary() {
            return temporary;
        }

        public void setTemporary(Boolean temporary) {
            this.temporary = temporary;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
