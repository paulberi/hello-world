package se.metria.skogsmaskindata.service.imports.model.feature;

import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;

import javax.persistence.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static se.metria.skogsmaskindata.utils.FeatureUtils.*;

@Entity
@SequenceGenerator(name = "resultat_seq", sequenceName = "resultat_seq", allocationSize = 1)
public class Resultat extends ForestlinkFeature {
	@Id
	@GeneratedValue(generator = "resultat_seq")
	private Long id;

	private String name;
	private Double area;
	private Double perimeter;
	private Double length;
	private String status;

	@Column(length = 1000000)
	private MultiPolygon geom;

	public static Resultat fromSimpleFeature(SimpleFeature feature, boolean godkand) {
		Resultat resultat = new Resultat();
		resultat.name = getString(feature, "Name");
		resultat.area = getDouble(feature, "Area");
		resultat.perimeter = getDouble(feature, "Perimeter");
		resultat.length = getDouble(feature, "Length");

		resultat.status = godkand ? "G" : "U";

		resultat.geom = getMultiPolygon(feature);

		return resultat;
	}

	public static Set<Resultat> fromSimpleFeatures(Collection<SimpleFeature> features, boolean godkanda) {
		return features.stream().map((feature -> fromSimpleFeature(feature, godkanda))).collect(Collectors.toSet());
	}

	public MultiPolygon getGeometry() {
		return geom;
	}
}
