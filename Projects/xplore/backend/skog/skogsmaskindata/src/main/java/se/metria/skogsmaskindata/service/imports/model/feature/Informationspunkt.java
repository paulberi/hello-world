package se.metria.skogsmaskindata.service.imports.model.feature;

import org.locationtech.jts.geom.MultiPoint;
import org.opengis.feature.simple.SimpleFeature;

import javax.persistence.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static se.metria.skogsmaskindata.utils.FeatureUtils.getMultiPoint;
import static se.metria.skogsmaskindata.utils.FeatureUtils.getString;

@Entity
@Table(name = "informationspunkter")
@SequenceGenerator(name = "informationspunkter_seq", sequenceName = "informationspunkter_seq", allocationSize = 1)
public class Informationspunkt extends ForestlinkFeature {
	@Id
	@GeneratedValue(generator = "informationspunkter_seq")
	private Long id;

	private String company;
	private String type;
	private String sort;
	private String essay;

	@Column(length = 1000000)
	private MultiPoint geom;

	public static Informationspunkt fromSimpleFeature(SimpleFeature feature) {
		Informationspunkt resultat = new Informationspunkt();
		resultat.company = getString(feature, "Company");
		resultat.type = getString(feature, "Type");
		resultat.sort = getString(feature, "Sort");
		resultat.essay = getString(feature, "Essay");

		resultat.geom = getMultiPoint(feature);

		return resultat;
	}

	public static Set<Informationspunkt> fromSimpleFeatures(Collection<SimpleFeature> features) {
		return features.stream().map((feature -> fromSimpleFeature(feature))).collect(Collectors.toSet());
	}

	public MultiPoint getGeometry() {
		return geom;
	}
}
