package se.metria.matdatabas.service.meddelande.entity;

import se.metria.matdatabas.service.common.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "meddelanden_startsida")
@SequenceGenerator(name = "meddelande_seq", sequenceName = "meddelanden_startsida_id_seq", allocationSize = 1)
public class MeddelandeEntity extends Auditable {
	@Id
	@GeneratedValue(generator = "meddelande_seq")
	@Column(name = "id")
	private Integer id;

	private LocalDateTime datum;

	private String rubrik;

	private String url;

	private String meddelande;

	public MeddelandeEntity() {
	}

	public MeddelandeEntity(LocalDateTime datum, String rubrik, String url, String meddelande) {
		this.datum = datum;
		this.rubrik = rubrik;
		this.url = url;
		this.meddelande = meddelande;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getDatum() {
		return datum;
	}

	public void setDatum(LocalDateTime datum) {
		this.datum = datum;
	}

	public String getRubrik() {
		return rubrik;
	}

	public void setRubrik(String rubrik) {
		this.rubrik = rubrik;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMeddelande() {
		return meddelande;
	}

	public void setMeddelande(String meddelande) {
		this.meddelande = meddelande;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MeddelandeEntity that = (MeddelandeEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
