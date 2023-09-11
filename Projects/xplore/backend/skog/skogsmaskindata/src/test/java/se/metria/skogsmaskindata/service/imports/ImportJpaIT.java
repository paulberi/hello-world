package se.metria.skogsmaskindata.service.imports;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.skogsmaskindata.service.imports.model.Import;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ImportJpaIT {
	@Autowired
	private TestEntityManager entityManager;

	@Test
	void persist_import() {
		Import markberedning = new Import("1", "2", OffsetDateTime.now(), "markberedning");
		entityManager.persist(markberedning);
		assertNotNull(markberedning.getId());
	}

	@Test
	void persist_features() {
		Import markberedning = new Import("1", "2", OffsetDateTime.now(), "markberedning");
		entityManager.persist(markberedning);
		assertNotNull(markberedning.getId());

		markberedning.addProvytor(getProvyta());
		markberedning.addProvytor(getProvyta());
		markberedning.addProvytor(getProvyta());
		markberedning.setCompleted();

		markberedning = entityManager.merge(markberedning);
		assertTrue(markberedning.isCompleted());
	}

	private GeometryFactory getGeometryFactory() {
		return new GeometryFactory(new PrecisionModel(), 3006);
	}

	private Set<SimpleFeature> getProvyta() {
		GeometryFactory gm = getGeometryFactory();

		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName("Provyta");
		typeBuilder.add("Nr", String.class);
		typeBuilder.add("geom", Point.class);

		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
		builder.add("12");
		builder.add(gm.createPoint(new Coordinate(3, 4)));

		return new HashSet<SimpleFeature>() {{ add(builder.buildFeature("testid")); }};
	}
}
