package se.metria.skogsmaskindata.service.imports.model.feature;

import org.locationtech.jts.geom.MultiLineString;
import org.opengis.feature.simple.SimpleFeature;

import javax.persistence.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static se.metria.skogsmaskindata.utils.FeatureUtils.getMultiLineString;
import static se.metria.skogsmaskindata.utils.FeatureUtils.getString;

@Entity
@SequenceGenerator(name = "korspar_seq", sequenceName = "korspar_seq", allocationSize = 1)
public class Korspar extends ForestlinkFeature {
	@Id
	@GeneratedValue(generator = "korspar_seq")
	private Long id;

	private String name;
	private String activity;
	private String starttime;
	private String endtime;
	private String corr_status;
	private String agg_status;
	private String operator;

	@Column(length = 1000000)
	private MultiLineString geom;

	public static Korspar fromSimpleFeature(SimpleFeature feature) {
		Korspar resultat = new Korspar();
		resultat.name = getString(feature, "Name");
		resultat.activity = getString(feature, "Activity");
		resultat.starttime = getString(feature, "Starttime");
		resultat.endtime = getString(feature, "Endtime");
		resultat.corr_status = getString(feature, "CorrStatus");
		resultat.agg_status = getString(feature, "AggStatus");
		resultat.operator = getString(feature, "Operator");

		resultat.geom = getMultiLineString(feature);

		return resultat;
	}

	public static Set<Korspar> fromSimpleFeatures(Collection<SimpleFeature> features) {
		return features.stream().map((feature -> fromSimpleFeature(feature))).collect(Collectors.toSet());
	}

	public MultiLineString getGeometry() {
		return geom;
	}
}
