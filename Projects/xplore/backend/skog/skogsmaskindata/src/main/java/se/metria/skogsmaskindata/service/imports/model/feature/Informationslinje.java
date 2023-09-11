package se.metria.skogsmaskindata.service.imports.model.feature;

import org.locationtech.jts.geom.MultiLineString;
import org.opengis.feature.simple.SimpleFeature;

import javax.persistence.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static se.metria.skogsmaskindata.utils.FeatureUtils.*;

@Entity
@Table(name = "informationslinjer")
@SequenceGenerator(name = "informationslinjer_seq", sequenceName = "informationslinjer_seq", allocationSize = 1)
public class Informationslinje extends ForestlinkFeature {
	@Id
	@GeneratedValue(generator = "informationslinjer_seq")
	private Long id;

	private String company;
	private String type;
	private String sort;
	private String essay;

	private MultiLineString geom;

	public static Informationslinje fromSimpleFeature(SimpleFeature feature) {
		Informationslinje resultat = new Informationslinje();
		resultat.company = getString(feature, "Company");
		resultat.type = getString(feature, "Type");
		resultat.sort = getString(feature, "Sort");
		resultat.essay = getString(feature, "Essay");

		resultat.geom = getMultiLineString(feature);

		return resultat;
	}

	public static Set<Informationslinje> fromSimpleFeatures(Collection<SimpleFeature> features) {
		return features.stream().map((feature -> fromSimpleFeature(feature))).collect(Collectors.toSet());
	}

	public MultiLineString getGeometry() {
		return geom;
	}
}
