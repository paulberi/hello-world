package se.metria.skogsmaskindata.service.imports.model.feature;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProvytaTest {

	private GeometryFactory getGeometryFactory() {
		return new GeometryFactory(new PrecisionModel(), 3006);
	}

	@Test
	void fromSimpleFeature() {
		GeometryFactory gm = getGeometryFactory();

		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName("Provyta");
		typeBuilder.add("Nr", String.class);
		typeBuilder.add("geom", Point.class);

		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
		builder.add("12");
		builder.add(gm.createPoint(new Coordinate(3, 4)));

		Provyta provyta = Provyta.fromSimpleFeature(builder.buildFeature("testid"));
		assertNotNull(provyta);
		assertNotNull(provyta.getGeometry());
	}
}
