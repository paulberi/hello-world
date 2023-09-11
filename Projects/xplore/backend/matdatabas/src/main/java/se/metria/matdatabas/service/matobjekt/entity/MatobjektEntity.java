package se.metria.matdatabas.service.matobjekt.entity;

import se.metria.matdatabas.service.common.Auditable;
import se.metria.matdatabas.service.matningstyp.entity.MatningstypEntity;
import se.metria.matdatabas.service.matobjektgrupp.entity.MatobjektgruppEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "matobjekt")
@SequenceGenerator(name = "matobjekt_id_seq", sequenceName = "matobjekt_id_seq", allocationSize = 1)
public class MatobjektEntity extends Auditable {
	@Id
	@GeneratedValue(generator = "matobjekt_id_seq")
	private Integer id;
	private Short typ;
	private String namn;
	private Boolean aktiv;
	private Boolean kontrollprogram;
	@Column(name = "pos_n")
	private BigDecimal posN;
	@Column(name = "pos_e")
	private BigDecimal posE;
	private String fastighet;
	private String lage;
	@Column(name = "bifogad_bild_id")
	private UUID bifogadBildId;

	@OneToMany
	@JoinColumn(name = "matobjekt_id", updatable = false, insertable = false)
	private Set<MatningstypEntity> matningstyper = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "grupp_matobjekt",
			joinColumns = @JoinColumn(name = "matobjekt_id"),
			inverseJoinColumns = @JoinColumn(name = "grupp_id"))
	private Set<MatobjektgruppEntity> matobjektgrupper = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "matobjekt_bifogat_dokument", joinColumns = @JoinColumn(name = "matobjekt_id"))
	@Column(name = "bifogad_fil_id")
	private Set<UUID> dokumentIds = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Short getTyp() {
		return typ;
	}

	public void setTyp(Short typ) {
		this.typ = typ;
	}

	public String getNamn() {
		return namn;
	}

	public void setNamn(String namn) {
		this.namn = namn;
	}

	public Boolean getAktiv() {
		return aktiv;
	}

	public void setAktiv(Boolean aktiv) {
		this.aktiv = aktiv;
	}

	public Boolean getKontrollprogram() {
		return kontrollprogram;
	}

	public void setKontrollprogram(Boolean kontrollprogram) {
		this.kontrollprogram = kontrollprogram;
	}

	public BigDecimal getPosN() {
		return posN;
	}

	public void setPosN(BigDecimal posN) {
		this.posN = posN;
	}

	public BigDecimal getPosE() {
		return posE;
	}

	public void setPosE(BigDecimal posE) {
		this.posE = posE;
	}

	public String getFastighet() {
		return fastighet;
	}

	public void setFastighet(String fastighet) {
		this.fastighet = fastighet;
	}

	public String getLage() {
		return lage;
	}

	public void setLage(String lage) {
		this.lage = lage;
	}

	public UUID getBifogadBildId() {
		return bifogadBildId;
	}

	public void setBifogadBildId(UUID bifogadBildId) {
		this.bifogadBildId = bifogadBildId;
	}

	public Set<MatobjektgruppEntity> getMatobjektgrupper() {
		return matobjektgrupper;
	}

	public void setMatobjektgrupper(Set<MatobjektgruppEntity> matobjektgrupper) {
		this.matobjektgrupper = matobjektgrupper;
	}

	public Set<UUID> getDokumentIds() {
		return dokumentIds;
	}

	public void setDokumentIds(Set<UUID> dokumentIds) {
		this.dokumentIds = dokumentIds;
	}
}
