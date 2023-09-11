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
@Table(name = "provytor")
@SequenceGenerator(name = "provytor_seq", sequenceName = "provytor_seq", allocationSize = 1)
public class Provyta extends ForestlinkFeature {
	@Id
	@GeneratedValue(generator = "provytor_seq")
	private Long id;

	private String nr;
	private String driver;
	private String marktyp;
	private String kommentar;
	private String h3;
	private String m3;
	private String t3;
	private String fyra;
	private String fem;
	private String sjuxsju;
	private String laagor;
	private String sm;
	private String lf;
	private String mp;
	private String x;
	private String y;
	private String z;
	private String fc_olcolor;
	private String fc_olsize;
	private String fc_style;
	private String fc_color;

	@Column(length = 1000000)
	private MultiPoint geom;

	public MultiPoint getGeometry() {
		return geom;
	}

	public static Provyta fromSimpleFeature(SimpleFeature feature) {
		Provyta provyta = new Provyta();

		provyta.nr = getString(feature, "Nr");
		provyta.driver = getString(feature, "Driver");
		provyta.marktyp = getString(feature, "MarkTyp");
		provyta.kommentar = getString(feature, "Kommentar");
		provyta.h3 = getString(feature, "H3");
		provyta.m3 = getString(feature, "M3");
		provyta.t3 = getString(feature, "T3");
		provyta.fyra = getString(feature, "Fyra");
		provyta.fem = getString(feature, "Fem");
		provyta.sjuxsju = getString(feature, "7x7");
		provyta.laagor = getString(feature, "Laagor");
		provyta.sm = getString(feature, "SM");
		provyta.lf = getString(feature, "LF");
		provyta.mp = getString(feature, "MP");
		provyta.x = getString(feature, "X");
		provyta.y = getString(feature, "Y");
		provyta.z = getString(feature, "Z");
		provyta.fc_olcolor = getString(feature, "FC_OlColor");
		provyta.fc_olsize = getString(feature, "FC_OlSize");
		provyta.fc_style = getString(feature, "FC_Style");
		provyta.fc_color = getString(feature, "FC_Color");

		provyta.geom = getMultiPoint(feature);

		return provyta;
	}

	public static Set<Provyta> fromSimpleFeatures(Collection<SimpleFeature> features) {
		return features.stream().map((feature -> fromSimpleFeature(feature))).collect(Collectors.toSet());
	}
}
