package se.metria.markkoll.service.intrang.helper;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.core.io.Resource;
import org.springframework.util.FileSystemUtils;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.IntrangsSubtypDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;
import se.metria.markkoll.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static se.metria.xplore.maputils.GeometryUtil.getFeatureGeometry;

/**
 * Hjälpmetoder för att läsa geometrier från en Shapefile.
 */
@Slf4j
public class ShapeFilerHelper {

    public static Set<SimpleFeature> getFeaturesFromShapeFile(File shapeFile, String filterByOwner, String ownerAttribute) throws IOException {
        if (filterByOwner == null) {
            filterByOwner = "ALL";
        }
        if (ownerAttribute == null) {
            ownerAttribute = "ANY";
        }
        FileDataStore dataStore;
        FeatureSource<SimpleFeatureType, SimpleFeature> source;
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection;

        Set<SimpleFeature> features = new HashSet<>();

        dataStore = new ShapefileDataStore(shapeFile.toURI().toURL());
        source = dataStore.getFeatureSource();
        collection = source.getFeatures();
        try (FeatureIterator<SimpleFeature> featureIterator = collection.features()) {
            while (featureIterator.hasNext()) {
                SimpleFeature feature = featureIterator.next();
                if (filterByOwner.compareToIgnoreCase("ALL") == 0
                        || (feature.getAttribute(ownerAttribute) != null
                        && feature.getAttribute(ownerAttribute).toString().compareToIgnoreCase(filterByOwner) == 0)) {
                    features.add(feature);
                }
            }
        } finally {
            dataStore.dispose();
        }
        return features;
    }

    /**
     * Ta reda på typ av intrång. Hårt kodad mot datamodell för Shapefil från DpCom.
     */
    public static IntrangstypDto getIntrangType(SimpleFeature feature) {
        int dpOtype = ((Long) feature.getAttribute("dp_otype")).intValue();
        int dpCtype = ((Long) feature.getAttribute("dp_ctype")).intValue();

        if (dpOtype == 800027 && dpCtype == 80030) {
            return IntrangstypDto.MARKSKAP;
        } else if (dpOtype == 800000 && dpCtype == 80030) {
            return IntrangstypDto.BRUNN;
        } else if (dpOtype == 800003 && dpCtype == 80010) {
            return IntrangstypDto.STRAK;
        } else {
            return IntrangstypDto.OKAND;
        }
    }

    public static IntrangsSubtypDto getIntrangSubType(SimpleFeature feature) {
        int dpOtype = ((Long) feature.getAttribute("dp_otype")).intValue();
        int dpCtype = ((Long) feature.getAttribute("dp_ctype")).intValue();
        int dpSubtype = ((Long) feature.getAttribute("dp_subtype")).intValue();
        if (dpOtype == 800003 && dpCtype == 80010) {

            switch (dpSubtype) {
                case 29:
                case 25:
                    return IntrangsSubtypDto.LUFTSTRAK;
                default:
                    return IntrangsSubtypDto.MARKSTRAK;
            }
        } else {
            return IntrangsSubtypDto.NONE;
        }
    }

    public static Set<SimpleFeature> readFeaturesFromFile(Resource zipFile) {
        Path tempPath = null;

        //För att filtrera import från shapefilen, sätt filterByOwner till önskad
        //ledningsägare och onwerAttribute till det sttribut i shape-filen där det
        //är lagrat. Sätt filterByOwner till "ALL" för att importera allt.
        // TODO: Hantera det här via bättre "kund-specifika attribut",
        //       görs när vi tar hand om kundbegreppet i framtida version
        String filterByOwner = "SKANOVA";
        String ownerAttribute = "OWNER";

        Set<SimpleFeature> features = new HashSet<>();

        log.info("Laddar in geometrier från zipfil {}...", zipFile.getFilename());
        try (var zipInputStream = zipFile.getInputStream()) {
            tempPath = Files.createTempDirectory("");
            log.debug("Temporär katalog skapad: {}", tempPath);

            Set<File> files = FileUtil.unzipFiles(tempPath, zipInputStream).stream()
                    .filter(file -> file.getName().endsWith(".shp"))
                    .collect(Collectors.toSet());
            log.debug("Uppzippad fil innehåller {} shapefiler", files.size());

            if (files.isEmpty()) {
                throw new MarkkollException(MarkkollError.IMPORT_ERROR_SHAPE_FILE_MISSING);
            }

            for (File file : files) {
                features.addAll(ShapeFilerHelper.getFeaturesFromShapeFile(file, filterByOwner, ownerAttribute));
            }
        } catch (IOException e) {
            throw new MarkkollException(MarkkollError.IMPORT_ERROR);
        } finally {
            FileSystemUtils.deleteRecursively(tempPath.toFile());
            log.debug("Temporär katalog raderad: {}", tempPath);
        }

        log.info("Laddade in {} geometrier från zipfil {}", features.size(), zipFile.getFilename());
        return features;
    }

    public static Set<IntrangEntity> intrangFromFeatures(Set<SimpleFeature> features) {
        log.info("Importerar intrång...");

        Set<IntrangEntity> savedIntrangSet = new HashSet<>();

        for (SimpleFeature feature : features) {
            if (ShapeFilerHelper.getIntrangType(feature) != IntrangstypDto.OKAND) {
                IntrangEntity intrangEntity = IntrangEntity.builder()
                        .geom(getFeatureGeometry(feature))
                        .subtype(ShapeFilerHelper.getIntrangSubType(feature).toString())
                        .type(ShapeFilerHelper.getIntrangType(feature).toString())
                        .build();

                savedIntrangSet.add(intrangEntity);
            }
        }

        log.info("Importerade {} intrång från {} geometrier", savedIntrangSet.size(), features.size());

        return savedIntrangSet;
    }
}

