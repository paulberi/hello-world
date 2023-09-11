package se.metria.skogsmaskindata.service.imports.model.feature;

import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;

import javax.persistence.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static se.metria.skogsmaskindata.utils.FeatureUtils.*;

@Entity
@Table(name = "informationspolygoner")
@SequenceGenerator(name = "informationspolygoner_seq", sequenceName = "informationspolygoner_seq", allocationSize = 1)
public class Informationspolygon extends ForestlinkFeature {
	@Id
	@GeneratedValue(generator = "informationspolygoner_seq")
	private Long id;

	private String company;
	private String type;
	private String sort;
	private String essay;

	@Column(length = 1000000)
	private MultiPolygon geom;

	public static Informationspolygon fromSimpleFeature(SimpleFeature feature) {
		Informationspolygon resultat = new Informationspolygon();
		resultat.company = getString(feature, "Company");
		resultat.type = getString(feature, "Type");
		resultat.sort = getString(feature, "Sort");
		resultat.essay = getString(feature, "Essay");

		resultat.geom = getMultiPolygon(feature);

		return resultat;
	}

	public static Set<Informationspolygon> fromSimpleFeatures(Collection<SimpleFeature> features) {
		return features.stream().map((feature -> fromSimpleFeature(feature))).collect(Collectors.toSet());
	}

	public MultiPolygon getGeometry() {
		return geom;
	}
}
