package se.metria.skogsmaskindata.service.imports.shapefile;

import org.locationtech.jts.geom.Geometry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.geotools.data.FileDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class ShapefileHandler {

	private Collection<File> files;
	private HashMap<ShapefileType, Set<SimpleFeature>> featuresForType = new HashMap<>();

	private boolean success;

	public ShapefileHandler(File dir) {
		this.files = FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}

	public void readFiles() throws ShapefileException {
		for (ShapefileType type : ShapefileType.values()) {
			readShapefileFeatures(type);
		}

		success = !this.files.stream().filter(f -> f.getName().startsWith("Result_G_")).
				collect(Collectors.toList()).isEmpty();
	}

	public boolean isSuccess() {
		return success;
	}

	public Set<SimpleFeature> getFeatures(ShapefileType type) {
		return featuresForType.get(type);
	}

	private void readShapefileFeatures(ShapefileType type) throws ShapefileException {
		FileDataStore fileDataStore = null;
		SimpleFeatureIterator iter = null;

		try {
			Set<SimpleFeature> features = new HashSet<>();

			List<File> shapefiles = getShapefile(type);

			if (shapefiles != null) {
				for (var shapefile: shapefiles) {
					fileDataStore = new ShapefileDataStore(shapefile.toURI().toURL());
					SimpleFeatureCollection sfc = fileDataStore.getFeatureSource().getFeatures();

					iter = sfc.features();
					while (iter.hasNext()) {
						SimpleFeature f = iter.next();
						// Allt måste sättas till SWEREF99 TM, annars godtar inte databasen geometrin
						((Geometry) f.getDefaultGeometry()).setSRID(3006);
						features.add(f);
					}

					iter.close();
					iter = null;

					fileDataStore.dispose();
					fileDataStore = null;
				}
			}

			this.featuresForType.put(type, features);
		} catch (IOException e) {
			throw new ShapefileException("Misslyckades läsa shapefile: " + type.getName());
		} finally {
			if (iter != null) {
				iter.close();
			}
			if (fileDataStore != null) {
				fileDataStore.dispose();
			}
		}
	}

	private List<File> getShapefile(ShapefileType type) throws ShapefileException {
		List<File> shapeFiles = files.stream().filter(
				file -> file.getName().matches(type.getRegex())).collect(Collectors.toList());

		if (shapeFiles.isEmpty() && type.isRequired()) {
			throw new ShapefileException("Shapefile saknas: " + type.getName());
		}

//		if (shapeFiles.size() > 1) {
//			throw new ShapefileException("Multipla shapefiler hittades för: " + type.getName());
//		}

		return !shapeFiles.isEmpty() ? shapeFiles : null;
	}
}
