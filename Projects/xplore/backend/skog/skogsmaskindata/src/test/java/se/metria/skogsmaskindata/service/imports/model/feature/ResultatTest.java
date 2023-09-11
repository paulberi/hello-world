package se.metria.skogsmaskindata.service.imports.model.feature;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResultatTest {

	private GeometryFactory getGeometryFactory() {
		return new GeometryFactory(new PrecisionModel(), 3006);
	}

	@Test
	void fromSimpleFeature() {
		GeometryFactory gm = getGeometryFactory();

		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName("Resultat");
		typeBuilder.add("Name", String.class);
		typeBuilder.add("geom", Polygon.class);

		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
		builder.add("12");
		Coordinate[] polygon = new Coordinate[]{
				new Coordinate(3, 4),
				new Coordinate(4, 5),
				new Coordinate(3, 5),
				new Coordinate(3, 4)
		};
		builder.add(gm.createPolygon(polygon));

		Resultat resultat = Resultat.fromSimpleFeature(builder.buildFeature("testid"), false);
		assertNotNull(resultat);
		assertNotNull(resultat.getGeometry());
	}
}
