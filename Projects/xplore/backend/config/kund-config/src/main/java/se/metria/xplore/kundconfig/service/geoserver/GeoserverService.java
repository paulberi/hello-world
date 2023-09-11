package se.metria.xplore.kundconfig.service.geoserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.metria.xplore.kundconfig.security.KundConfigRole;
import se.metria.xplore.kundconfig.service.geoserver.model.FeatureTypeDetails;
import se.metria.xplore.kundconfig.service.geoserver.model.FeatureTypes;
import se.metria.xplore.kundconfig.service.geoserver.model.LayerGroups;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoserverService {
    @Value("${geoserver.url}")
    private String geoserverUrl;

    @Value("${geoserver.username}")
    private String username;

    @Value("${geoserver.password}")
    private String password;

    @Value("${geoserver.datastore}")
    private String datastore;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
            .enable(SerializationFeature.WRAP_ROOT_VALUE)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    private WebClient webClient = WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .build();

    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public void createRole(String roleName) {
        log.info("Skapar rollen " + roleName + " i Geoserver");
        webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "security/roles/role/{role}")
                .build(roleName))
            .headers(h -> h.setBasicAuth(username, password))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public void deleteRole(String roleName) {
        log.info("Tar bort rollen " + roleName + " i Geoserver");
        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(geoserverUrl + "security/roles/role/{role}")
                        .build(roleName))
                .headers(h -> h.setBasicAuth(username, password))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public List<FeatureTypeDetails>
    getFeatureTypes(String workspaceName) throws JsonProcessingException {
        log.debug("Hämtar featuretypes för workspace: {}, datastore: {}", workspaceName, datastore);
        var ftypes = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "workspaces/{workspaceName}/datastores/{storeName}/featuretypes.json")
                .build(workspaceName, datastore))
            .headers(h -> h.setBasicAuth(username, password))
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

    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public FeatureTypeDetails
    getFeatureType(String layer, String workspace) throws JsonProcessingException
    {
        log.debug("Hämtar featuretype {} för workspace: {}, datastore: {}", layer, workspace, datastore);
        var resp = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(geoserverUrl + "workspaces/{workspaceName}/datastores/{storeName}/featuretypes/{featureTypeName}.json")
                .build(workspace, datastore, layer))
            .headers(h -> h.setBasicAuth(username, password))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        var ftype = objectMapper.readValue(resp, FeatureTypeDetails.class);
        return ftype;
    }

    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public LayerGroups
    getLayerGroups(String workspaceName) throws JsonProcessingException {
        log.debug("Hämtar layer groups för workspace: {}, datastore: {}", workspaceName, datastore);
        var ftypes = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(geoserverUrl + "workspaces/{workspaceName}/layergroups.json")
                        .build(workspaceName))
                .headers(h -> h.setBasicAuth(username, password))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        var layerGroups = objectMapper.readValue(ftypes, LayerGroups.class);
        return layerGroups;
    }
}
