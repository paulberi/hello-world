package se.metria.markkoll.service.geofence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import se.metria.markkoll.service.geofence.model.*;
import se.metria.xplore.geoserveradmin.service.GeoserverAdminService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeofenceService {
    @Value("${geoserver.url}/rest/")
    private String geoserverUrl;

    @Value("${geoserver.username}")
    private String username;

    @Value("${geoserver.password}")
    private String password;

    @Value("${geoserver.workspaces}")
    private String[] workspaces;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    @NonNull
    private final GeoserverAdminService geoserverAdminService;

    private WebClient webClient = WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .build();

    public List<Integer> resetAllRules(List<String> kundIds) throws JsonProcessingException {
        log.info("Återställer samtliga regler för {} kunder, Geofence", kundIds.size());

        deleteAllRules();

        var nextPrio = 0;
        var ruleIds = new ArrayList<Integer>();

        var adminRuleId = createRuleForAdmin(nextPrio++);
        ruleIds.add(adminRuleId);

        for (var kundId : kundIds) {
            var kundRuleIds = createRulesForKund(kundId, nextPrio);
            ruleIds.addAll(kundRuleIds);
            nextPrio += kundRuleIds.size();
        }

        var defaultRuleId = createDefaultRule(nextPrio);
        ruleIds.add(defaultRuleId);

        return ruleIds;
    }

    public List<Integer> createRulesForKund(String kundId) throws JsonProcessingException {

        log.info("Skapar nya regler för kund {}, Geofence", kundId);
        var prio = getMaxPriority() + 1;
        var ruleIds = createRulesForKund(kundId, prio);
        prio += ruleIds.size();

        // Se till så att default-regeln alltid ligger sist i prioritet
        var defaultRule = findDefaultRule();
        if(defaultRule.isPresent()) {
            var rule = defaultRule.get();
            rule.setPriority(prio);
            updateRule(rule);
        }
        else {
            createDefaultRule(prio);
        }

        return ruleIds;
    }

    public Integer deleteRulesForKund(String kundId) throws JsonProcessingException {
        log.info("Tar bort regler för kund {}, Geofence", kundId);

        var rules = getRules();
        var kundRules = rules
                .stream()
                .filter(p -> p.getRoleName() != null)
                .filter(rule -> rule.getRoleName().contains(kundId))
                .collect(Collectors.toList());

        for (var rule: kundRules) {
            deleteRule(rule.getId());
        }

        return kundRules.size();
    }

    private Integer deleteAllRules() throws JsonProcessingException {
        var rules = getRules();

        for (var rule: rules) {
            deleteRule(rule.getId());
        }

        return rules.size();
    }

    private Integer createRuleForAdmin(Integer prio) throws JsonProcessingException {
        var rule = buildBasicRule(Role.ADMIN.toString(), Access.ALLOW, prio);
        return createRule(rule);
    }

    private Integer createDefaultRule(Integer prio) throws JsonProcessingException {
        var rule = buildBasicRule(null, Access.DENY, prio);
        return createRule(rule);
    }

    private List<Integer> createRulesForKund(String kundId, Integer prio) throws JsonProcessingException {
        log.info("Skapar nya regler för kund {}, Geofence", kundId);

        var ruleIds = new ArrayList<Integer>();

        for (var workspace: workspaces) {
            var ftypes = geoserverAdminService.getFeatureTypes(workspace);
            for (var ftype : ftypes) {
                log.debug("Skapar ny regel för layer {} i workspace {}, Geofence", ftype.getName(), workspace);

                var rule = buildLayerRule(kundId,false, workspace, ftype.getName(), LayerType.VECTOR, Access.ALLOW, prio++);
                var id = createRule(rule);
                ruleIds.add(id);

                // Regel för extern användare för layer som innehåller "avtalsstatus"
                if (isAvtalsstatusLayer(ftype.getName())) {
                    var externalRule = buildLayerRule(kundId,true, workspace, ftype.getName(), LayerType.VECTOR, Access.ALLOW, prio++);
                    var externalRuleId = createRule(externalRule);
                    ruleIds.add(externalRuleId);
                }
            }
            var layerGroups = geoserverAdminService.getLayerGroups(workspace);
            for (var layerGroup : layerGroups.getLayerGroup()) {
                log.debug("Skapar ny regel för layer group {} i workspace {}, Geofence", layerGroup.getName(), workspace);

                var rule = buildLayerRule(kundId,false, workspace, layerGroup.getName(), LayerType.LAYERGROUP, Access.ALLOW, prio++);
                var id = createRule(rule);
                ruleIds.add(id);

                // Regel för extern användare för layer group som innehåller "avtalsstatus"
                if (isAvtalsstatusLayer(layerGroup.getName())) {
                    var externalRule = buildLayerRule(kundId,true, workspace, layerGroup.getName(), LayerType.LAYERGROUP, Access.ALLOW, prio++);
                    var externalRuleId = createRule(externalRule);
                    ruleIds.add(externalRuleId);
                }
            }
        }
        return ruleIds;
    }

    private Integer createRule(Rule rule) throws JsonProcessingException {
        var ruleJsonBody = objectMapper
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writeValueAsString(rule);

        log.debug(ruleJsonBody);
        var id = webClient.post()
            .uri(uriBuilder -> uriBuilder.path(geoserverUrl + "geofence/rules/").build())
            .headers(h -> h.setBasicAuth(username, password))
            .body(BodyInserters.fromValue(ruleJsonBody))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return Integer.parseInt(id);
    }

    private Integer updateRule(Rule rule) throws JsonProcessingException {
        var ruleJsonBody = objectMapper
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writeValueAsString(rule);

        log.debug(ruleJsonBody);

        var result = webClient.post()
                .uri(uriBuilder -> uriBuilder.path(geoserverUrl + "geofence/rules/id/{ruleId}").build(rule.getId()))
                .headers(h -> h.setBasicAuth(username, password))
                .body(BodyInserters.fromValue(ruleJsonBody))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return 1;
    }


    private Integer getMaxPriority() throws JsonProcessingException {
        var rules = getRules();
        var maxPrioRule= rules.stream().max(Comparator.comparingInt(Rule::getPriority));
        return  maxPrioRule.isEmpty() ? 0 : maxPrioRule.get().getPriority();
    }

    private Optional<Rule> findDefaultRule() throws JsonProcessingException {
        var rules = getRules();
        return rules
                .stream()
                .filter(p -> p.getRoleName() == null)
                .findFirst();
    }

    private List<Rule> getRules() throws JsonProcessingException {


        var result = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(geoserverUrl + "geofence/rules")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBasicAuth(username, password))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return objectMapper.readValue(result, RulesContainer.class).rules;
    }

    private void deleteRule(Integer ruleId) {
        var result = webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(geoserverUrl + "geofence/rules/id/{ruleId}")
                        .build(ruleId))
                .headers(h -> h.setBasicAuth(username, password))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private Rule buildBasicRule(String roleName, Access access, Integer priority) {
        var rule = new Rule();
        rule.setRoleName(roleName);
        rule.setAccess(access);
        rule.setPriority(priority);

        return rule;
    }

    private Rule buildLayerRule(String roleName, Boolean external, String workspace, String layerName, LayerType layerType, Access access, Integer priority) {

        var layerDetails = new LayerDetails();
        layerDetails.setCatalogMode(CatalogMode.CHALLENGE);
        // Det är endast layers som ska ha kund_id på sig, layer group behöver inte det.
        if (layerType == LayerType.VECTOR) {
            layerDetails.setCqlFilterRead(String.format("kund_id='%s'", roleName));
        }
        layerDetails.setLayerType(layerType);

        if (external) {
            roleName += ":EXTERN";
        }

        var rule = new Rule();
        rule.setRoleName(roleName);
        rule.setLayer(layerName);
        rule.setWorkspace(workspace);
        rule.setAccess(access);
        rule.setLayerDetails(layerDetails);
        rule.setPriority(priority);

        return rule;
    }

    private Boolean isAvtalsstatusLayer(String lager) {
        return lager.contains("avtalsstatus");
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class RulesContainer {
    public List<Rule> rules;
}

