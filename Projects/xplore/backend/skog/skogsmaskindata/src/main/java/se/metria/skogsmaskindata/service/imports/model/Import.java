package se.metria.skogsmaskindata.service.imports.model;

import org.opengis.feature.simple.SimpleFeature;
import se.metria.skogsmaskindata.service.imports.model.feature.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@SequenceGenerator(name = "import_seq", sequenceName = "import_seq", allocationSize = 1)
public class Import {

	@Id
	@GeneratedValue(generator = "import_seq")
	private Long id;

	private String objektnummer;
	private String organisation;
	private OffsetDateTime enddate;
	private String pakettyp;

	@OneToOne(mappedBy = "parent", cascade = CascadeType.ALL)
	@NotNull
	private Importstatus status;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<Informationslinje> informationslinjer = new HashSet<>();

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<Informationspolygon> informationspolygoner = new HashSet<>();

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<Informationspunkt> informationspunkter = new HashSet<>();

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<Korspar> korspar = new HashSet<>();

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<KorsparForTransport> korsparForTransport = new HashSet<>();

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<Provyta> provytor = new HashSet<>();

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<Resultat> resultat = new HashSet<>();

	Import() {
	}

	public Import(String objektnummer, String organisation, OffsetDateTime enddate, String pakettyp) {
		this.objektnummer = objektnummer;
		this.organisation = organisation;
		this.enddate = enddate;
		this.pakettyp = pakettyp;

		setImportstatus(new Importstatus());
	}

	public Long getId() {
		return id;
	}

	public String getObjektnummer() {
		return objektnummer;
	}

	public String getOrganisation() {
		return organisation;
	}

	public OffsetDateTime getEnddate() {
		return enddate;
	}

	public String getPakettyp() {
		return pakettyp;
	}

	public Importstatus getStatus() { return status; }

	private void setImportstatus(Importstatus status) {
		status.setParent(this);
		this.status = status;
	}

	public Set<Informationslinje> getInformationslinjer() {
		return informationslinjer;
	}

	public Set<Informationspolygon> getInformationspolygoner() {
		return informationspolygoner;
	}

	public Set<Informationspunkt> getInformationspunkter() {
		return informationspunkter;
	}

	public Set<Korspar> getKorspar() {
		return korspar;
	}

	public Set<KorsparForTransport> getKorsparForTransport() {
		return korsparForTransport;
	}

	public Set<Provyta> getProvytor() {
		return provytor;
	}

	public Set<Resultat> getResultat() {
		return resultat;
	}

	public void addInformationslinjer(Set<SimpleFeature> features) {
		informationslinjer.addAll(Informationslinje.fromSimpleFeatures(features));
		addParent(informationslinjer);
	}

	public void addInformationspolygoner(Set<SimpleFeature> features) {
		informationspolygoner.addAll(Informationspolygon.fromSimpleFeatures(features));
		addParent(informationspolygoner);
	}

	public void addInformationspunkter(Set<SimpleFeature> features) {
		informationspunkter.addAll(Informationspunkt.fromSimpleFeatures(features));
		addParent(informationspunkter);
	}

	public void addKorspar(Set<SimpleFeature> features) {
		korspar.addAll(Korspar.fromSimpleFeatures(features));
		addParent(korspar);
	}

	public void addKorsparForTransporter(Set<SimpleFeature> features) {
		korsparForTransport.addAll(KorsparForTransport.fromSimpleFeatures(features));
		addParent(korsparForTransport);
	}

	public void addProvytor(Set<SimpleFeature> features) {
		provytor.addAll(Provyta.fromSimpleFeatures(features));
		addParent(provytor);
	}

	public void addResultat(Set<SimpleFeature> features, boolean godkanda) {
		resultat.addAll(Resultat.fromSimpleFeatures(features, godkanda));
		addParent(resultat);
	}

	public void setCompleted() {
		status.setCompleted();
	}

	public void setError(String cause) {
		status.setError(cause);
	}

	public boolean isError() {
		return status.isError();
	}

	public boolean isCompleted() {
		return status.isCompleted();
	}

	public static Import fromInfo(Info info) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
		OffsetDateTime date = LocalDateTime.parse(info.getEnddate(), formatter)
				.atOffset(ZoneOffset.UTC);

		return new Import(info.getObjectNumber(), info.getExecutor(), date, info.getPurpose());
	}

	private void addParent(Set<? extends ForestlinkFeature> features) {
		features.stream().forEach(f -> f.setParent(this));
	}
}
