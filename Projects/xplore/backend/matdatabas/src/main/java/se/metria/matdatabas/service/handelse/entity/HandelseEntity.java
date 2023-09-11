package se.metria.matdatabas.service.handelse.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import se.metria.matdatabas.service.handelse.dto.SaveHandelse;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "handelse")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "handelse_id_seq", sequenceName = "handelse_id_seq", allocationSize = 1)
public class HandelseEntity {

	@Id
	@GeneratedValue(generator = "handelse_id_seq")
	private Integer id;

	@NotNull
	private String benamning;

	private String beskrivning;

	@CreatedBy
	@Column(name="loggad_av_id")
	private Integer loggadAvId;

	@Formula("(select a.foretag from matdatabas.anvandare a where a.id = loggad_av_id)")
	private String loggadAv;
	
	@NotNull
	@Column(name="loggad_datum")
	private LocalDateTime loggadDatum;

	@NotNull
	@Column(name="matobjekt_id")
	private Integer matobjektId;

	@ElementCollection
	@CollectionTable(name = "handelse_bifogad_bild", joinColumns = @JoinColumn(name = "handelse_id"))
	@Column(name = "bifogad_fil_id")
	private Set<UUID> bifogadeBilderIds = new HashSet<>();

	public HandelseEntity(Integer matobjektId, SaveHandelse save) {
		setMatobjektId(matobjektId);
		save(save);
	}

	public void save(SaveHandelse save) {
		setBenamning(save.getBenamning());
		setBeskrivning(save.getBeskrivning());
		setBifogadeBilderIds(save.getBifogadeBilderIds());
		setLoggadDatum(save.getDatum());
	}

	public LocalDateTime getLoggadDatum() {
		return loggadDatum.truncatedTo(ChronoUnit.SECONDS);
	}

	public void setLoggadDatum(LocalDateTime loggadDatum) {
		this.loggadDatum = loggadDatum.truncatedTo(ChronoUnit.SECONDS);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof HandelseEntity)) return false;
		HandelseEntity that = (HandelseEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
