package se.metria.xplore.sok.geofilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.metria.xplore.sok.exceptions.ExternalGeofilterException;
import se.metria.xplore.sok.exceptions.GeofilterException;
import se.metria.xplore.sok.exceptions.InternalGeofilterException;
import se.metria.xplore.sok.exceptions.GeofilterFailureType;
import se.metria.xplore.sok.geocode.model.MediumReplyExtent;
import se.metria.xplore.sok.util.FeatureUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeofilterClient {
    private final RestTemplate restTemplate;
    private final GeoJsonReader geoJsonReader;
    private final GeometryFactory geometryFactory;

    private final HashMap<String, GeometryCollection> masks = new HashMap<>();

    public GeofilterClient() {
        this.restTemplate = new RestTemplate();
        this.geometryFactory = new GeometryFactory();
        this.geoJsonReader = new GeoJsonReader(this.geometryFactory);
    }

    public List<MediumReplyExtent> geofilter(List<MediumReplyExtent> replies, String url, String kommun) throws GeofilterException {
        Geometry mask = getMask(url, kommun);
        return replies.stream().filter(i -> intersects(i, mask)).collect(Collectors.toList());
    }

    private boolean intersects(MediumReplyExtent reply, Geometry mask) {
        return this.geometryFactory.createPoint(new Coordinate(reply.getX(), reply.getY())).intersects(mask);
    }

    public void addMask(String url) throws GeofilterException {
        if (!masks.containsKey(url)) {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                if (response.getBody() == null) {
                    throw new InternalGeofilterException(GeofilterFailureType.EMPTY_MASK);
                }

                masks.put(url, geoJson2jts(response.getBody()));
            } catch (HttpClientErrorException e) {
                throw new InternalGeofilterException(GeofilterFailureType.MASK_DOWNLOAD);
            }
        }
    }

    private GeometryCollection geoJson2jts(String geoJson) throws GeofilterException {
        try {
            JsonNode json = new ObjectMapper().readTree(geoJson);
            Iterator<JsonNode> iterator = json.get("features").iterator();

            List<Geometry> geometries = new ArrayList<>();
            while (iterator.hasNext()) {
                JsonNode node = iterator.next();
                Geometry geometry = geoJsonReader.read(node.get("geometry").toString());

                String namn = node.path("properties").path("namn").asText();
                if (!namn.isEmpty() ) {
                    geometry.setUserData(namn);
                }

                geometries.add(geometry);
            }

            return FeatureUtil.createGeometryCollection(geometries);
        } catch (Exception e) {
            throw new InternalGeofilterException(GeofilterFailureType.MASK_CONVERSION);
        }
    }

    private GeometryCollection getMask(String url, String kommun) throws GeofilterException {
        GeometryCollection fullMask = masks.get(url);
        if (fullMask == null) {
            throw new ExternalGeofilterException(GeofilterFailureType.MISSING_MASK);
        }

        if (kommun != null) {
            List<Geometry> partialMask = new ArrayList<>();

            for (int i = 0; i < fullMask.getNumGeometries(); i++) {
                Geometry geometry = fullMask.getGeometryN(i);
                Object userData = geometry.getUserData();
                if (kommun.equals(userData)) {
                    partialMask.add(geometry);
                }
            }

            if (!partialMask.isEmpty()) {
                return FeatureUtil.createGeometryCollection(partialMask);
            }
        }

        return fullMask;
    }
}
