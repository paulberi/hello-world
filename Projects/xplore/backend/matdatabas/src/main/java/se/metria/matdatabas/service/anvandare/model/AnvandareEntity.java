package se.metria.matdatabas.service.anvandare.model;

import se.metria.matdatabas.service.anvandare.behorighet.Behorighet;
import se.metria.matdatabas.service.anvandargrupp.entity.AnvandargruppEntity;
import se.metria.matdatabas.service.common.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "anvandare")
@SequenceGenerator(name = "anvandare_id_seq", sequenceName = "anvandare_id_seq", allocationSize = 1)
public class AnvandareEntity extends Auditable {

	@Id
	@GeneratedValue(generator = "anvandare_id_seq")
	private Integer id;

	private String namn;

	private String foretag;

	@Column(nullable = false)
	private Boolean aktiv;

	@Column(name = "inloggnings_namn", unique = true)
	private String inloggningsnamn;

	@Column(nullable = false)
	private Short behorighet;

	@Column(name = "default_kartlager_id")
	private Integer defaultKartlagerId;

	@Column(name = "skicka_epost", nullable = false)
	private Boolean skickaEpost;

	@Column(name = "e_post")
	private String epost;

	@Column(name = "inloggad_senast")
	private LocalDateTime inloggadSenast;

	@ManyToMany
	@JoinTable(
			name = "anvandargrupp_anvandare",
			joinColumns = @JoinColumn(name = "anvandar_id"),
			inverseJoinColumns = @JoinColumn(name = "anvandargrupp_id"))
	private Set<AnvandargruppEntity> anvandargrupper = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNamn() {
		return namn;
	}

	public void setNamn(String namn) {
		this.namn = namn;
	}

	public String getForetag() {
		return foretag;
	}

	public void setForetag(String foretag) {
		this.foretag = foretag;
	}

	public Boolean getAktiv() {
		return aktiv;
	}

	public void setAktiv(Boolean aktiv) {
		this.aktiv = aktiv;
	}

	public String getInloggningsnamn() {
		return inloggningsnamn;
	}

	public void setInloggningsnamn(String inloggningsnamn) {
		this.inloggningsnamn = inloggningsnamn;
	}

	public Short getBehorighet() {
		return behorighet;
	}

	public void setBehorighet(Short behorighet) {
		this.behorighet = behorighet;
	}

	public Integer getDefaultKartlagerId() {
		return defaultKartlagerId;
	}

	public void setDefaultKartlagerId(Integer defaultKartlagerId) {
		this.defaultKartlagerId = defaultKartlagerId;
	}

	public Boolean getSkickaEpost() {
		return skickaEpost;
	}

	public void setSkickaEpost(Boolean skickaEpost) {
		this.skickaEpost = skickaEpost;
	}

	public String getEpost() {
		return epost;
	}

	public void setEpost(String epost) {
		this.epost = epost;
	}

	public LocalDateTime getInloggadSenast() {
		return inloggadSenast;
	}

	public void setInloggadSenast(LocalDateTime inloggadSenast) {
		this.inloggadSenast = inloggadSenast;
	}

	public Set<AnvandargruppEntity> getAnvandargrupper() {
		return anvandargrupper;
	}

	public void setAnvandargrupper(Set<AnvandargruppEntity> anvandargrupper) {
		this.anvandargrupper = anvandargrupper;
	}

	@Transient
	public AnvandareEntity anonymize() {
		String anonymizedName = Behorighet.getNameById(this.behorighet) + this.getId();
		this.namn = anonymizedName;
		this.foretag = null;
		this.inloggningsnamn = anonymizedName;
		this.aktiv = false;
		this.epost = null;
		this.anvandargrupper = null;
		this.skickaEpost = false;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AnvandareEntity anvandare = (AnvandareEntity) o;
		return Objects.equals(id, anvandare.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
