package se.metria.xplore.sok.kommun;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.metria.xplore.sok.exceptions.GeoJsonMergeException;
import se.metria.xplore.sok.kommun.model.KommunRequest;
import se.metria.xplore.sok.util.GeoJsonUtil;
import se.metria.xplore.sok.util.LanUtil;
import se.metria.xplore.sok.util.SokUtil;
import se.metria.xplore.sok.wfs.WfsClient;

@Service
public class KommunClient {
    private final WfsClient wfsClient;

    public KommunClient(WfsClient wfsClient) {
        this.wfsClient = wfsClient;
    }

    public ResponseEntity<String> request(KommunRequest request) throws GeoJsonMergeException {
        ResponseEntity<String> response = wfsClient.get(request);

        String geoJson = response.getBody();
		geoJson = GeoJsonUtil.mergeGeometries(geoJson, "kommunkod");
		geoJson = GeoJsonUtil.addMappedProperties(geoJson, "kommunnamn", "kommunnamn", SokUtil::capitalizeFully);
        geoJson = GeoJsonUtil.addMappedProperties(geoJson, "lankod", "lansnamn",  LanUtil::mapLanskodToLansnamn);

        return SokUtil.createJsonResponse(geoJson);
    }
}
