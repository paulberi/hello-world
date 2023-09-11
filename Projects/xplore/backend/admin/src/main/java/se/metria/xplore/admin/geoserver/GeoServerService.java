package se.metria.xplore.admin.geoserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeoServerService {
    private final GeoServerProperties properties;
    private final GeoServerRESTManager manager;
    private final Logger logger = LoggerFactory.getLogger(GeoServerService.class);

    public GeoServerService(GeoServerProperties properties) throws GeoServerException, MalformedURLException {
        this.properties = properties;

        if (properties.getUrl() != null) {
            this.manager = new GeoServerRESTManager(new URL(properties.getUrl()), properties.getUsername(), properties.getPassword());
            ValidateGeoServer();
        } else {
            this.manager = null;
        }
    }

    /**
     * Publish all available layers from an existing datastore.
     *
     * @param  workspace    geoserver workspace
     * @param  datastore    geoserver datastore
     * @param  srs          coordinate system
     * @param  defaultStyle default style
     *
     * @throws IOException if response from GeoServer cannot be parsed.
     * @throws GeoServerException if feature types cannot be retrieved.
     *
     * @return list of published layers
     */
    public List<String> PublishAllLayers(String workspace, String datastore, String srs, String defaultStyle)
            throws GeoServerException, IOException {
        ValidateGeoServer();
        ValidateWorkspaceExists(workspace);
        ValidateDatastoreExists(workspace, datastore);

        GSLayerEncoder layerEncoder = LayerEncoder(defaultStyle);

        List<String> publishedLayers = new ArrayList<>();
        for (String layer: GetAvailableFeatureTypes(workspace, datastore)) {
            GSFeatureTypeEncoder featureTypeEncoder = FeatureTypeEncoder(layer, srs);

            boolean published = manager.getPublisher().publishDBLayer(workspace, datastore, featureTypeEncoder,
                    layerEncoder);
            if (!published) {
                logger.warn("Could not publish layer " + layer + ". workspace:" + workspace +
                        " datastore:" + datastore + " srs:" + srs + " style:" + defaultStyle);
            } else {
                publishedLayers.add(workspace + ":" + layer);
            }
        }

        return publishedLayers;
    }

    /**
     * Set default style for all layers in a workspace.
     *
     * @param  workspace    geoserver workspace
     * @param  defaultStyle default style
     *
     * @throws GeoServerException if workspace does not exist.
     *
     * @return list of layers with the new default style
     */
    public List<String> SetStyleWorkspace(String workspace, String defaultStyle) throws GeoServerException {
        ValidateGeoServer();
        ValidateWorkspaceExists(workspace);

        List<String> configuredLayers = new ArrayList<>();
        for (String layer: manager.getReader().getFeatureTypes(workspace).getNames()) {
            GSLayerEncoder layerEncoder = LayerEncoder(defaultStyle);

            boolean configured = manager.getPublisher().configureLayer(workspace, layer, layerEncoder);
            if (!configured) {
                logger.warn("Could not set style for layer " + layer + ". workspace:" + workspace
                        + " style:" + defaultStyle);
            } else {
                configuredLayers.add(workspace + ":" + layer);
            }
        }

        return configuredLayers;
    }

    /**
     * Set default style for all layers in a layer group.
     *
     * @param  workspace    geoserver workspace
     * @param  layerGroup   layer group
     * @param  defaultStyle defaultStyle
     *
     * @throws GeoServerException if layers cannot be retrieved.
     *
     * @return list of layers with the new default style
     */
    public List<String> SetStyleLayerGroup(String workspace, String layerGroup, String defaultStyle)
            throws GeoServerException {
        ValidateGeoServer();
        ValidateWorkspaceExists(workspace);
        ValidateLayerGroupExists(workspace, layerGroup);

        GSLayerEncoder layerEncoder = LayerEncoder(defaultStyle);

        List<String> layerList = manager.getReader()
                .getLayerGroup(workspace, layerGroup)
                .getPublishedList()
                .getNames()
                .stream()
                .map(this::OmitWorkspaceName)
                .collect(Collectors.toList());

        List<String> configuredLayers = new ArrayList<>();
        for (String layer: layerList) {
            boolean configured = manager.getPublisher().configureLayer(workspace, layer, layerEncoder);
            if (!configured) {
                logger.warn("Could not set style for layer " + layer + ". workspace:" + workspace
                        + " layer group:" + layerGroup + " style:" + defaultStyle);
            } else {
                configuredLayers.add(layer);
            }
        }

        return configuredLayers;
    }

    /**
     * Enabled/disable tile caching for all layers in a workspace.
     *
     * @param  workspace    geoserver workspace
     * @param  enabled      caching enabled/disabled
     *
     * @throws GeoServerException if layers cannot be retrieved.
     *
     * @return list of layers that have been set.
     */
    public List<String> ToggleCachingWorkspace(String workspace, boolean enabled) throws GeoServerException {
        ValidateGeoServer();
        ValidateWorkspaceExists(workspace);

        List<String> layersSet = new ArrayList<>();
        for (String layer: manager.getReader().getFeatureTypes(workspace).getNames()) {
            try {
                SetCachingLayer(workspace + ":" + layer, enabled);
                layersSet.add(workspace + ":" + layer);
            } catch (IOException | GeoServerException e) {
                logger.warn(e.toString());
            }
        }

        return layersSet;
    }

    /**
     * Enabled/disable tile caching for all layers in a layer group.
     *
     * @param  workspace    geoserver workspace
     * @param  layerGroup   layer group
     * @param  enabled      caching enabled/disabled
     *
     * @throws GeoServerException if layers cannot be retrieved.
     *
     * @return list of layers that have been set.
     */
    public List<String> ToggleCachingLayerGroup(String workspace, String layerGroup, boolean enabled)
            throws GeoServerException {
        ValidateGeoServer();
        ValidateWorkspaceExists(workspace);
        ValidateLayerGroupExists(workspace, layerGroup);

        List<String> layersSet = new ArrayList<>();
        for (String layer: manager.getReader().getLayerGroup(workspace, layerGroup).getPublishedList().getNames()) {
            try {
                SetCachingLayer(layer, enabled);
                layersSet.add(layer);
            } catch (IOException | GeoServerException e) {
                logger.warn(e.toString());
            }
        }

        return layersSet;
    }

    private void SetCachingLayer(String layer, boolean enabled) throws IOException, GeoServerException {
        Document doc = GetCachedLayerXML(layer);

        boolean enabledSet = false;
        for (Node n = doc.getDocumentElement().getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeName().equals("enabled")) {
                n.setTextContent(enabled ? "true" : "false");
                enabledSet = true;
            }
        }

        if (!enabledSet) {
            throw new GeoServerException("Could not find 'enabled' field in cached layer");
        }

        PutCachedLayerXML(layer, doc);
    }

    // The Geoserver REST Manager does not support the GeoWebCache API, so it's up to us to do the heavy lifting
    private Document GetCachedLayerXML(String layer) throws GeoServerException, IOException {
        String url = properties.getUrl() + "/gwc/rest/layers/" + layer;

        HttpClient client = HttpClient();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("content-type", "application/json");
        httpGet.setHeader("accept", "application/xml");

        HttpResponse res = client.execute(httpGet);
        int status = res.getStatusLine().getStatusCode();
        if (status != 200) {
            throw new GeoServerException("Could not GET cached layer " + layer + ". Status code: " + status);
        }

        HttpEntity entity = res.getEntity();

        return StringToXMLDoc(EntityUtils.toString(entity));
    }

    // The Geoserver REST Manager does not support the GeoWebCache API, so it's up to us to do the heavy lifting
    private void PutCachedLayerXML(String layer, Document doc) throws GeoServerException, IOException {
        String url = properties.getUrl() + "/gwc/rest/layers/" + layer;
        String content = XMLDocToString(doc);

        HttpClient client = HttpClient();

        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("content-type", "application/xml");
        httpPut.setHeader("accept", "application/json");
        HttpEntity entity = new StringEntity(content);
        httpPut.setEntity(entity);

        HttpResponse res = client.execute(httpPut);
        int status = res.getStatusLine().getStatusCode();
        if (status != 200) {
            throw new GeoServerException("Could not PUT layer " + layer + ". Status code: " + status);
        }
    }

    /* The Geoserver REST Manager does not support retrieving feature types that haven't been published yet, so we have
     * to do it ourselves */
    private List<String> GetAvailableFeatureTypes(String workspace, String datastore)
            throws GeoServerException, IOException {
        String url = properties.getUrl() + "/rest/workspaces/" + workspace + "/datastores/" +
                datastore + "/featuretypes?list=available";

        HttpClient client = HttpClient();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("content-type", "application/json");
        httpGet.setHeader("accept", "application/json");

        HttpResponse res = client.execute(httpGet);
        int status = res.getStatusLine().getStatusCode();
        if (status != 200) {
            throw new GeoServerException("Could not GET feature types for workspace:" + workspace + " datastore:" +
                    datastore + " status code:" + status);
        }

        return ParseFeatureTypesJson(res);
    }

    private Document StringToXMLDoc(String content) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            return dBuilder.parse(new InputSource(new StringReader(content)));
        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException(e);
        }
    }

    private String XMLDocToString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    private List<String> ParseFeatureTypesJson(HttpResponse response) throws IOException {
        String json = EntityUtils.toString(response.getEntity());

        // we're only interested in the array contained within the JSON object. Throw away the rest of the string
        json = json.substring(json.indexOf('['), json.indexOf(']') + 1);
        return Arrays.asList(new ObjectMapper().readValue(json, String[].class));
    }

    //When you fetch layers from a layer group, the layer name is prefixed with "<workspace name>:", which we don't want
    private String OmitWorkspaceName(String layer) {
        int index = layer.indexOf(':');

        return index == -1 ? layer : layer.substring(index + 1);
    }

    private HttpClient HttpClient() {
        CredentialsProvider provider = CredentialsProvider(properties.getUsername(), properties.getPassword());
        return HttpClientBuilder.create()
                                .setDefaultCredentialsProvider(provider)
                                .build();
    }

    private GSFeatureTypeEncoder FeatureTypeEncoder(String featureType, String srs) {
        GSFeatureTypeEncoder featureTypeEncoder = new GSFeatureTypeEncoder();
        featureTypeEncoder.setSRS(srs);
        featureTypeEncoder.setName(featureType);

        return featureTypeEncoder;
    }

    private CredentialsProvider CredentialsProvider(String username, String password) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials(username, password);
        provider.setCredentials(AuthScope.ANY, credentials);

        return provider;
    }

    private GSLayerEncoder LayerEncoder(String style) {
        GSLayerEncoder layerEncoder = new GSLayerEncoder();
        layerEncoder.setDefaultStyle(style);

        return layerEncoder;
    }

    private void ValidateGeoServer() throws GeoServerException {
        if (!this.manager.getReader().existGeoserver()) {
            throw new GeoServerException("Could not connect to " + properties.getUrl() +
                    ". user:" + properties.getUsername() +
                    " pw:" + properties.getPassword());
        }
    }
    private void ValidateWorkspaceExists(String workspace) throws GeoServerException {
        if (!manager.getReader().existsWorkspace(workspace)) {
            throw new GeoServerException("Workspace does not exist: " + workspace);
        }
    }

    private void ValidateDatastoreExists(String workspace, String datastore) throws GeoServerException {
        if (!manager.getReader().existsDatastore(workspace, datastore)) {
            throw new GeoServerException("No datastore " + datastore + " in workspace " + workspace);
        }
    }

    private void ValidateLayerGroupExists(String workspace, String layerGroup) throws GeoServerException {
        if (!manager.getReader().existsLayerGroup(workspace, layerGroup)) {
            throw new GeoServerException("No layer group " + layerGroup + " in workspace " + workspace);
        }
    }
}
