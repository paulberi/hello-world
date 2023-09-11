package se.metria.markkoll.service.indata;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.core.io.Resource;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.IndataTypDto;
import se.metria.markkoll.util.FileUtil;
import se.metria.markkoll.util.ShapeFileUtil;
import se.metria.xplore.maputils.GeometryUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class Indata {
    @NonNull
    private final Map<String, Collection<SimpleFeature>> featureMap;

    @NonNull
    private final IndataTypDto indataTyp;

    public static Indata fromZipFile(Resource zipFile, IndataTypDto indataTyp) throws MarkkollException {

        Path tmpPath = null;
        try(var zipInputStream = zipFile.getInputStream()) {

            tmpPath = FileUtil.createTempFolder();

            Set<File> files = FileUtil.unzipFiles(tmpPath, zipInputStream).stream()
                    .filter(file -> file.getName().endsWith(".shp"))
                    .collect(Collectors.toSet());

            if (files.isEmpty()) {
                throw new MarkkollException(MarkkollError.IMPORT_ERROR_SHAPE_FILE_MISSING);
            }

            var featureMap = new HashMap<String, Collection<SimpleFeature>>();
            for (var file: files) {
                Set<SimpleFeature> featureSet = ShapeFileUtil.getFeatures(file);

                for (var feature: featureSet) {
                    var geom = GeometryUtil.getFeatureGeometry(feature);
                    GeometryAttribute geometryAttribute = feature.getDefaultGeometryProperty();
                    geometryAttribute.setValue(geom);
                }

                featureMap.put(FilenameUtils.getBaseName(file.getName()), featureSet);
            }

            return new Indata(featureMap, indataTyp);
        }
        catch (IOException e) {
            throw new MarkkollException(MarkkollError.IMPORT_ERROR);
        } finally {
            FileUtil.deleteTempFolder(tmpPath);
        }
    }
}
