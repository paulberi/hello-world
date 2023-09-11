package se.metria.xplore.geoserveradmin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.metria.xplore.geoserveradmin.service.model.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoserverAdminService {
    @Value("${geoserver.url}rest/")
    private String geoserverUrl;

    @Value("${geoserver.username}")
    private String authUsername;

    @Value("${geoserver.password}")
    private String authPassword;

    @Value("${geoserver.datastore}")
    private String datastore;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
        .enable(SerializationFeature.WRAP_ROOT_VALUE)
        .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    private WebClient webClient = WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .build();

    public void addRoleToUser(String roleName, String userName) {
        log.info("Lägger till rollen " + roleName + " till användaren " + userName + " i Geoserver");

        webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "security/roles/role/{role}/user/{user}")
                .build(roleName, userName))
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void createRole(String roleName) throws JsonProcessingException {
        if (getRoles().contains(roleName)) {
            log.info("Rollen '{}' existerar redan i Geoserver", roleName);
            return;
        }

        log.info("Skapar rollen " + roleName + " i Geoserver");

        webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "security/roles/role/{role}")
                .build(roleName))
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void createUser(String username, String password, boolean enabled) throws IOException {
        var userNames = getUsers().stream()
            .map(user -> user.getUserName())
            .collect(Collectors.toList());

        if (userNames.contains(username)) {
            log.info("Användaren '{}' existerar redan i Geoserver", username);
            return;
        }

        log.info("Skapar användaren " + username + " i Geoserver");

        var userBody = new HashMap<>();
        userBody.put("userName", username);
        userBody.put("password", password);
        userBody.put("enabled", enabled);
        var body = new HashMap<>();
        body.put("user", userBody);

        webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "security/usergroup/users")
                .build())
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .bodyValue(new ObjectMapper().writeValueAsString(body))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteRole(String roleName) {
        log.info("Tar bort rollen " + roleName + " i Geoserver");
        webClient.delete()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "security/roles/role/{role}")
                .build(roleName))
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteUser(String userName) {
        log.info("Tar bort användaren {} i Geoserver", userName);
        webClient.delete()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "security/usergroup/user/{user}")
                .build(userName))
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public List<FeatureTypeDetails>
    getFeatureTypes(String workspaceName) throws JsonProcessingException
    {
        log.debug("Hämtar featuretypes för workspace: {}, datastore: {}", workspaceName, datastore);
        var ftypes = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "workspaces/{workspaceName}/datastores/{storeName}/featuretypes.json")
                .build(workspaceName, datastore))
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        var ftypesObj = objectMapper.readValue(ftypes, FeatureTypes.class);

        var featureTypeDetails = new ArrayList<FeatureTypeDetails>();
        for (var ftype: ftypesObj.getFeatureType()) {
            featureTypeDetails.add(getFeatureType(ftype.getName(), workspaceName));
        }
        return featureTypeDetails;
    }

    public FeatureTypeDetails
    getFeatureType(String layer, String workspace) throws JsonProcessingException
    {
        log.debug("Hämtar featuretype {} för workspace: {}, datastore: {}", layer, workspace, datastore);
        var resp = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "workspaces/{workspaceName}/datastores/{storeName}/featuretypes/{featureTypeName}.json")
                .build(workspace, datastore, layer))
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        var ftype = objectMapper.readValue(resp, FeatureTypeDetails.class);
        return ftype;
    }

    public LayerGroups
    getLayerGroups(String workspaceName) throws JsonProcessingException {
        log.debug("Hämtar layer groups för workspace: {}, datastore: {}", workspaceName, datastore);
        var ftypes = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "workspaces/{workspaceName}/layergroups.json")
                .build(workspaceName))
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        var layerGroups = objectMapper.readValue(ftypes, LayerGroups.class);
        return layerGroups;
    }

    public List<String> getRoles() throws JsonProcessingException {
        var rolesString = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "security/roles")
                .build())
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .headers(h -> h.add("accept", MediaType.APPLICATION_JSON_VALUE))
            .retrieve()
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(10))
            .block();

        var roles = new ObjectMapper().readValue(rolesString, Roles.class);

        return roles.getRoles();
    }

    public List<User> getUsers() throws IOException {
        var usersString = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "security/usergroup/users")
                .build())
            .headers(h -> h.setBasicAuth(authUsername, authPassword))
            .headers(h -> h.add("accept", MediaType.APPLICATION_JSON_VALUE))
            .retrieve()
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(10))
            .block();

        var users = new ObjectMapper().readValue(usersString, Users.class);

        return users.getUsers();
    }
}
