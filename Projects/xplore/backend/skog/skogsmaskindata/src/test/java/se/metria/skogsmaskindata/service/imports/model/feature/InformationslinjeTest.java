package se.metria.skogsmaskindata.service.imports.model.feature;

import org.locationtech.jts.geom.*;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class InformationslinjeTest {

	private GeometryFactory getGeometryFactory() {
		return new GeometryFactory(new PrecisionModel(), 3006);
	}

	@Test
	void fromSimpleFeature() {
		GeometryFactory gm = getGeometryFactory();

		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName("Informationslinje");
		typeBuilder.add("Company", String.class);
		typeBuilder.add("geom", LineString.class);

		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
		builder.add("SAAB");
		Coordinate[] lineString = new Coordinate[]{
				new Coordinate(3, 4),
				new Coordinate(4, 5),
				new Coordinate(3, 5),
				new Coordinate(3, 4)
		};
		builder.add(gm.createLineString(lineString));

		Informationslinje i = Informationslinje.fromSimpleFeature(builder.buildFeature("testid"));
		assertNotNull(i);
		assertNotNull(i.getGeometry());
	}
}
