package se.metria.xplore.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.xplore.configuration.exceptions.ConfigurationNotFoundException;
import se.metria.xplore.configuration.exceptions.ForbiddenException;
import se.metria.xplore.configuration.exceptions.InvalidConfigurationException;
import se.metria.xplore.configuration.model.AppConfigRestriction;
import se.metria.xplore.configuration.model.AppConfig;
import se.metria.xplore.configuration.model.AppConfigRequest;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AppConfigService {
    Logger logger = LoggerFactory.getLogger(AppConfigService.class);

    private final AppConfigProperties properties;
    private final EntityManager entityManager;
    private final ObjectMapper mapper;

    public AppConfigService(AppConfigProperties properties, EntityManager entityManager, ObjectMapper mapper) {
        this.properties = properties;
        this.entityManager = entityManager;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public AppConfig getAppConfig(AppConfigRequest appConfigRequest, AppConfigRestriction accessRequest) throws ConfigurationNotFoundException, ForbiddenException {

        AppConfig appConfig;

        appConfig = getAppConfigFromStorage(appConfigRequest);
        checkRestrictions(appConfig, accessRequest);

        entityManager.detach(appConfig); // So that no edits to appConfig is stored to the database

        try {
            processImports(appConfig);
        } catch (ConfigurationNotFoundException e) {
            throw new InvalidConfigurationException("Imported configuration not found for app \""+appConfigRequest.app+
                    "\", configuration \""+appConfigRequest.config+
                    "\". Missing imported configuration is \""+e.getAppConfigRequest().config +"\"",e);
        }

        return appConfig;
    }

    private void processImports(AppConfig appConfig) throws ConfigurationNotFoundException {
        JsonNode json = appConfig.getJson();

        if (json == null) {
            return;
        }

        HashMap<String, AppConfig> appConfigCache = new HashMap<>();
        Map<String, Set<String>> importLayersForGroups = new HashMap<>();
        Set<String> stylesToImport = new HashSet<>();
        Set<String> infoTemplatesToImport = new HashSet<>();
        Set<String> importedLayers = new HashSet<>();

        // Import groups

        var groups = json.get("groups");

        if (groups != null) {
            ((ObjectNode) json).set("groups", importGroups(appConfigCache, importLayersForGroups, groups));
        }

        if (json.has("layers")) {
            // Import individual layers

            importIndividualLayers(json, appConfigCache, importedLayers);

            // Import layers from group import

            importLayersFromGroupImport(json, appConfigCache, importLayersForGroups, importedLayers);

            // Scan for styles to import

            for (var layer : json.get("layers")) {
                if (layer.has("style")) {
                    var styleNode = layer.get("style");

                    if (styleNode.isTextual()) {
                        if (styleNode.asText().contains("@")) {
                            stylesToImport.add(styleNode.asText());
                        }
                    } else {
                        if (styleNode.has("styles")) {
                            for (var styleArrayNode : styleNode.get("styles")) {
                                if (styleArrayNode.asText().contains("@")) {
                                    stylesToImport.add(styleArrayNode.asText());
                                }
                            }
                        }
                    }
                }

                if (layer.has("selectionStyleDelomraden")) {
                    var styleNode = layer.get("selectionStyleDelomraden");

                    if (styleNode.isTextual()) {
                        if (styleNode.asText().contains("@")) {
                            stylesToImport.add(styleNode.asText());
                        }
                    } else {
                        if (styleNode.has("styles")) {
                            for (var styleArrayNode : styleNode.get("styles")) {
                                if (styleArrayNode.asText().contains("@")) {
                                    stylesToImport.add(styleArrayNode.asText());
                                }
                            }
                        }
                    }
                }

                if (layer.has("selectionStyleFeatures")) {
                    var styleNode = layer.get("selectionStyleFeatures");

                    if (styleNode.isTextual()) {
                        if (styleNode.asText().contains("@")) {
                            stylesToImport.add(styleNode.asText());
                        }
                    } else {
                        if (styleNode.has("styles")) {
                            for (var styleArrayNode : styleNode.get("styles")) {
                                if (styleArrayNode.asText().contains("@")) {
                                    stylesToImport.add(styleArrayNode.asText());
                                }
                            }
                        }
                    }
                }
            }

            // Scan for infoTemplates to import

            for (var layer : json.get("layers")) {
                if (layer.has("infoTemplate")) {
                    var templateNode = layer.get("infoTemplate");

                    if (templateNode.isTextual()) {
                        if (templateNode.asText().contains("@")) {
                            infoTemplatesToImport.add(templateNode.asText());
                        }
                    }
                }
            }

        }

        // Import styles

        var styles = (ObjectNode) json.get("styles");

        if (styles == null) {
            styles = mapper.createObjectNode();
            ((ObjectNode) json).set("styles", styles);
        }

        for (var styleToImport : stylesToImport) {
            String[] importArray = styleToImport.split("@");
            String importConfig = importArray[1];
            String importStyleName = importArray[0];

            var subConfig = appConfigCache.get(importConfig);

            if (subConfig == null) {
                subConfig = getAppConfigFromStorage(new AppConfigRequest(null, importConfig));
                entityManager.detach(subConfig); // So that no edits to appConfig is stored to the database
                appConfigCache.put(importConfig, subConfig);
            }

            var subStyles = subConfig.getJson().get("styles");

            if (subStyles != null) {
                var subStyle = subStyles.get(importStyleName);

                styles.set(styleToImport,subStyle);
            }
        }

        // Import infoTemplates

        var infoTemplates = (ObjectNode) json.get("infoTemplates");

        if (infoTemplates == null) {
            infoTemplates = mapper.createObjectNode();
            ((ObjectNode) json).set("infoTemplates", infoTemplates);
        }

        for (var templateToImport : infoTemplatesToImport) {
            String[] importArray = templateToImport.split("@");
            String importConfig = importArray[1];
            String importTemplateName = importArray[0];

            var subConfig = appConfigCache.get(importConfig);

            if (subConfig == null) {
                subConfig = getAppConfigFromStorage(new AppConfigRequest(null, importConfig));
                entityManager.detach(subConfig); // So that no edits to appConfig is stored to the database
                appConfigCache.put(importConfig, subConfig);
            }

            var subTemplates = subConfig.getJson().get("infoTemplates");

            if (subTemplates != null) {
                var subTemplate = subTemplates.get(importTemplateName);

                infoTemplates.set(templateToImport,subTemplate);
            }
        }
    }

    private void importLayersFromGroupImport(JsonNode json, HashMap<String, AppConfig> appConfigCache, Map<String, Set<String>> importLayersForGroups, Set<String> importedLayers) throws ConfigurationNotFoundException {
        var newLayersGr = mapper.createArrayNode();

        for (var layer : json.get("layers")) {
            if (layer.has("import")) {
                String importText = layer.get("import").asText();

                String[] importArray = importText.split("@");
                String importConfig = importArray[1];
                String importLayerId = importArray[0];

                var subConfig = appConfigCache.get(importConfig);

                if (subConfig == null) {
                    subConfig = getAppConfigFromStorage(new AppConfigRequest(null, importConfig));
                    entityManager.detach(subConfig); // So that no edits to appConfig is stored to the database
                    appConfigCache.put(importConfig, subConfig);
                }

                Set<String> groupsToImport = null;

                if (importLayerId.equals("")) {
                    groupsToImport = importLayersForGroups.get(importConfig);
                }

                if (groupsToImport != null && subConfig.getJson().has("layers")) {
                    for (JsonNode layerFromSubConfig : subConfig.getJson().get("layers")) {
                        JsonNode groupNode = layerFromSubConfig.get("group");

                        if (layerFromSubConfig.has("id")) {
                            if (importedLayers.contains(layerFromSubConfig.get("id").asText() + "@" + importConfig)) {
                                continue; // Layers already imported
                            }
                        }

                        if (groupNode != null) {
                            String groupName = groupNode.asText();

                            if (groupsToImport.contains(groupName)) {
                                ObjectNode newLayer = layerFromSubConfig.deepCopy();

                                rewriteFieldsInLayer(newLayer, importConfig);
                                importOverrideAttributes(layer, newLayer);
                                newLayersGr.add(newLayer);
                            }
                        }
                    }
                }
            } else {
                newLayersGr.add(layer);
            }
        }

        ((ObjectNode) json).set("layers", newLayersGr);
    }

    private void importIndividualLayers(JsonNode json, HashMap<String, AppConfig> appConfigCache, Set<String> importedLayers) throws ConfigurationNotFoundException {
        var newLayersInd = mapper.createArrayNode();

        for (var layer : json.get("layers")) {
            if (layer.has("import")) {
                String importText = layer.get("import").asText();

                String[] importArray = importText.split("@");
                String importConfig = importArray[1];
                String importLayerId = importArray[0];

                if (importLayerId == null || importLayerId.equals("")) {
                    newLayersInd.add(layer);
                    continue; //Handled later
                }

                var subConfig = appConfigCache.get(importConfig);

                if (subConfig == null) {
                    subConfig = getAppConfigFromStorage(new AppConfigRequest(null, importConfig));
                    entityManager.detach(subConfig); // So that no edits to appConfig is stored to the database
                    appConfigCache.put(importConfig, subConfig);
                }

                if (subConfig.getJson().has("layers")) {
                    for (JsonNode layerFromSubConfig : subConfig.getJson().get("layers")) {
                        JsonNode idNode = layerFromSubConfig.get("id");

                        if (idNode != null) {
                            String idName = idNode.asText();

                            if (importLayerId.equals(idName)) {
                                ObjectNode newLayer = layerFromSubConfig.deepCopy();

                                rewriteFieldsInLayer(newLayer,importConfig);
                                importOverrideAttributes(layer, newLayer);

                                newLayersInd.add(newLayer);

                                importedLayers.add(importLayerId + "@" + importConfig);
                            }
                        }
                    }
                }
            } else {
                newLayersInd.add(layer);
            }
        }

        ((ObjectNode) json).set("layers", newLayersInd);
    }

    private ArrayNode importGroups(HashMap<String, AppConfig> appConfigCache, Map<String, Set<String>> importLayersForGroups, JsonNode groups) throws ConfigurationNotFoundException {
        var newGroups = mapper.createArrayNode();

        for (var group : groups) {
            if (group.has("groups")) {
                ((ObjectNode) group).set("groups", importGroups(appConfigCache, importLayersForGroups, group.get("groups")));
            }

            if (group.has("import")) {
                String importText = group.get("import").asText();

                String[] importArray = importText.split("@");
                String importConfig = importArray[1];
                String importGroupName = importArray[0];

                var subConfig = appConfigCache.get(importConfig);

                if (subConfig == null) {
                    subConfig = getAppConfigFromStorage(new AppConfigRequest(null, importConfig));
                    entityManager.detach(subConfig); // So that no edits to appConfig is stored to the database
                    appConfigCache.put(importConfig, subConfig);
                }

                var subGroups = subConfig.getJson().get("groups");

                JsonNode importedGroup = findImportedGroup(importGroupName, subGroups);

                if (importedGroup != null) {
                    JsonNode newGroup = importedGroup.deepCopy();

                    fixNameInImportedGroup(importConfig, newGroup, importLayersForGroups);
                    newGroups.add(newGroup);
                }
            } else {
                newGroups.add(group);
            }
        }
        return newGroups;
    }

    private JsonNode findImportedGroup(String importGroupName, JsonNode subGroups) {
        for (var subGroup : subGroups) {
            if (importGroupName.equals(subGroup.get("name").asText())) {
                return subGroup;
            }

            if (subGroup.has("groups")) {
                var found = findImportedGroup(importGroupName, subGroup.get("groups"));

                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    private void rewriteFieldsInLayer(ObjectNode newLayer, String importConfig) {
        if (newLayer.has("group")) {
            newLayer.put("group", newLayer.get("group").asText() + "@" + importConfig);
        }

        if (newLayer.has("id")) {
            newLayer.put("id", newLayer.get("id").asText() + "@" + importConfig);
        }

        if (newLayer.has("infoTemplate")) {
            var templateNode = newLayer.get("infoTemplate");

            if (templateNode.isTextual() && !templateNode.asText().contains("@")) {
                newLayer.put("infoTemplate", templateNode.asText() + "@" + importConfig);
            }
        }

        if (newLayer.has("style")) {
            var styleNode = newLayer.get("style");

            if (styleNode.isTextual()) {
                if (!styleNode.asText().contains("@")) {
                    newLayer.put("style", styleNode.asText() + "@" + importConfig);
                }
            } else if (styleNode.isObject()) {
                if (styleNode.has("styles")) {
                    var styleArray = (ArrayNode) styleNode.get("styles");

                    for (int i=0; i<styleArray.size(); i++) {
                        if (!styleArray.get(i).asText().contains("@")) {
                            String newStyle = styleArray.get(i).asText() + "@" + importConfig;
                            styleArray.set(i,new TextNode(newStyle));
                        }
                    }
                }
            }
        }

        if (newLayer.has("selectionStyleFeatures")) {
            var styleNode = newLayer.get("selectionStyleFeatures");

            if (styleNode.isTextual()) {
                if (!styleNode.asText().contains("@")) {
                    newLayer.put("selectionStyleFeatures", styleNode.asText() + "@" + importConfig);
                }
            } else if (styleNode.isObject()) {
                if (styleNode.has("styles")) {
                    var styleArray = (ArrayNode) styleNode.get("styles");

                    for (int i=0; i<styleArray.size(); i++) {
                        if (!styleArray.get(i).asText().contains("@")) {
                            String newStyle = styleArray.get(i).asText() + "@" + importConfig;
                            styleArray.set(i, new TextNode(newStyle));
                        }
                    }
                }
            }
        }

        if (newLayer.has("selectionStyleDelomraden")) {
            var styleNode = newLayer.get("selectionStyleDelomraden");

            if (styleNode.isTextual()) {
                if (!styleNode.asText().contains("@")) {
                    newLayer.put("selectionStyleDelomraden", styleNode.asText() + "@" + importConfig);
                }
            } else if (styleNode.isObject()) {
                if (styleNode.has("styles")) {
                    var styleArray = (ArrayNode) styleNode.get("styles");

                    for (int i=0; i<styleArray.size(); i++) {
                        if (!styleArray.get(i).asText().contains("@")) {
                            String newStyle = styleArray.get(i).asText() + "@" + importConfig;
                            styleArray.set(i,new TextNode(newStyle));
                        }
                    }
                }
            }
        }
    }

    private void importOverrideAttributes(JsonNode importNode, ObjectNode newNode) {
        var iterator = importNode.fields();

        while (iterator.hasNext()) {
            var e = iterator.next();

            if (e.getKey().equals("import")) {
                continue;
            }

            newNode.set(e.getKey(),e.getValue());
        }
    }

    private void fixNameInImportedGroup(String importConfig, JsonNode group, Map<String, Set<String>> importLayers) {
        String name = group.get("name").asText();
        ((ObjectNode) group).put("name", name+"@"+importConfig);

        Set<String> importLayersList = importLayers.computeIfAbsent(importConfig, k -> new HashSet<>());

        importLayersList.add(name);

        var groups = group.get("groups");

        if (groups != null) {
            for (var g : groups) {
                fixNameInImportedGroup(importConfig,g, importLayers);
            }
        }
    }

    private AppConfig getAppConfigFromStorage(AppConfigRequest appConfigRequest) throws ConfigurationNotFoundException {
        AppConfig appConfig;
        if (properties.isUseConfigFromResource()) {
            appConfig = getAppConfigFromResources(appConfigRequest);
        } else {
            appConfig = getAppConfigFromDB(appConfigRequest);
        }
        return appConfig;
    }

    private AppConfig getAppConfigFromResources(AppConfigRequest appConfigRequest) throws ConfigurationNotFoundException {
        String json;
        try {
            InputStream is = TypeReference.class.getResourceAsStream("/config/" + appConfigRequest.config + ".json");

            if (is == null ) {
                throw new ConfigurationNotFoundException(appConfigRequest);
            }

            json = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AppConfig appConfig = new AppConfig();
        try {
            appConfig.setJson(mapper.readTree(json));
        } catch (JsonProcessingException e) {
            throw new InvalidConfigurationException("Invalid json in configuration file \""+
                    appConfigRequest.config + ".json\" " + e.getMessage());
        }
        return appConfig;
    }

    private AppConfig getAppConfigFromDB(AppConfigRequest appConfigRequest) throws ConfigurationNotFoundException {
        try {
            AppConfig appConfig;

            if (StringUtils.isNumeric(appConfigRequest.config)) {
                appConfig=entityManager.createQuery("select appConfig" +
                                " from AppConfig appConfig" +
                                " where appConfig.id = :id", AppConfig.class)
                        .setParameter("id", Long.parseLong(appConfigRequest.config))
                        .getSingleResult();
            } else {
                appConfig=entityManager.createQuery("select appConfig" +
                                " from AppConfig appConfig" +
                                " where appConfig.name = :name", AppConfig.class)
                        .setParameter("name", appConfigRequest.config)
                        .getSingleResult();
            }

            return appConfig;
        } catch (NoResultException e) {
            throw new ConfigurationNotFoundException(appConfigRequest);
        }
    }

    public boolean checkRestrictions(AppConfig appConfig, AppConfigRestriction userProfile) throws ForbiddenException {
        if (appConfig.getAppConfigRestrictions().isEmpty()) {
            return true;
        }

        for (AppConfigRestriction ar : appConfig.getAppConfigRestrictions()) {
            if (isNotRestricted(ar.realms, userProfile.realms) &&
                    isNotRestricted(ar.users, userProfile.users) &&
                    isNotRestricted(ar.clients, userProfile.clients) &&
                    isNotRestricted(ar.groups, userProfile.groups) &&
                    isNotRestricted(ar.roles, userProfile.roles)) {
                return true;
            }
        }

        throw new ForbiddenException();
    }

    private static boolean isNotRestricted(String[] appConfigParam, String[] userProfileParam) {
        if (appConfigParam == null) {
            return true;
        }

        if (userProfileParam == null) {
            return false;
        }

        return !Collections.disjoint(Arrays.asList(appConfigParam), Arrays.asList(userProfileParam));
    }
}
