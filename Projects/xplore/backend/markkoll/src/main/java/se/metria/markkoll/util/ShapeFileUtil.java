package se.metria.markkoll.util;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ShapeFileUtil {
    static ShapefileDataStore dataStore;

    public static Set<SimpleFeature> getFeatures(File shapeFile) {

        Set<SimpleFeature> features = new HashSet<>();
        FeatureIterator<SimpleFeature> featureIterator = null;
        try {
            dataStore = new ShapefileDataStore(shapeFile.toURI().toURL());
            dataStore.setCharset(StandardCharsets.UTF_8);
            FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource();
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();

            featureIterator = collection.features();

            while (featureIterator.hasNext()) {
                features.add(featureIterator.next());
            }
        }
        catch(Exception e) {
            log.error("Error vid parsning av features från "+shapeFile.getName()+": "+e.getMessage());
            throw new MarkkollException(MarkkollError.IMPORT_SHAPE_INVALID);
        }
        finally {
            if(featureIterator != null) {
                featureIterator.close();
            }
            dataStore.dispose();
        }

        log.info("Läste ut "+features.size()+" features från "+shapeFile.getName());
        return features;
    }

    public static Set<SimpleFeature> getFeatures(Set<File> shapeFiles) throws IOException {

        Set<SimpleFeature> features = new HashSet<>();

        for (File file : shapeFiles) {
            features.addAll(getFeatures(file));
        }

        return features;
    }


}
