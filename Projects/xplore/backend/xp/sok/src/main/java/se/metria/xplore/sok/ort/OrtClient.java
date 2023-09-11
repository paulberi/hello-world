package se.metria.xplore.sok.ort;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.metria.xplore.sok.SokProperties;
import se.metria.xplore.sok.exceptions.GmlConversionException;
import se.metria.xplore.sok.ort.model.OrtRequest;
import se.metria.xplore.sok.ort.model.OrtSokResultat;
import se.metria.xplore.sok.util.FeatureUtil;
import se.metria.xplore.sok.util.GmlUtil;
import se.metria.xplore.sok.util.SokUtil;
import se.metria.xplore.sok.wfs.WfsClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrtClient {
	protected SokProperties properties;
	private final WfsClient wfsClient;

    private static final String DETALJTYP_TATORT = "BEBTÄTTX";

    public OrtClient(SokProperties properties, WfsClient wfsClient) {
        this.properties = properties;
        this.wfsClient = wfsClient;
    }

    public ResponseEntity<List<OrtSokResultat>> request(OrtRequest request) throws GmlConversionException {
        ResponseEntity<String> response = wfsClient.get(request);

        SimpleFeatureCollection sfc = GmlUtil.gml2sfc(response.getBody(), StandardCharsets.UTF_8);

        return SokUtil.createJsonResponse(mapOrtSokResult(sfc, request.getCoordinate()));
    }

    private List<OrtSokResultat> mapOrtSokResult(SimpleFeatureCollection sfc, Coordinate coordinate) {
        List<OrtSokResultat> resultat = new ArrayList<>();
        SimpleFeatureIterator iter = sfc.features();
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();

            OrtSokResultat ortSokResultat = new OrtSokResultat(
                    FeatureUtil.getAttribute(feature, "DETALJTYP", false),
                    FeatureUtil.getAttribute(feature, "ORTNAMN", true),
                    FeatureUtil.getAttribute(feature, "KOMMUNNAMN", true),
                    FeatureUtil.getAttribute(feature, "LAN", true),
                    FeatureUtil.getCoordinate(feature, "geom", true));

            if (!resultat.contains(ortSokResultat)) {
                resultat.add(ortSokResultat);
            }
        }
        iter.close();

        Point refPoint = (coordinate != null) ? FeatureUtil.createPoint(coordinate.getX(), coordinate.getY()) : null;
        resultat.sort((ort1, ort2) -> {
            boolean isTatortOrt1 = ort1.getTyp() != null && ort1.getTyp().equals(DETALJTYP_TATORT);
            boolean isTatortOrt2 = ort2.getTyp() != null && ort2.getTyp().equals(DETALJTYP_TATORT);

            if (isTatortOrt1 && !isTatortOrt2) {
                return -1;
            } else if (!isTatortOrt1 && isTatortOrt2) {
                return 1;
            } else if (refPoint != null) {
                // Sort by proximity
                Point point1 = FeatureUtil.createPoint(ort1.getX(), ort1.getY());
                Point point2 = FeatureUtil.createPoint(ort2.getX(), ort2.getY());
                return (int) (point1.distance(refPoint) - point2.distance(refPoint));
            }

            return 0;
        });

        return resultat;
    }
}
