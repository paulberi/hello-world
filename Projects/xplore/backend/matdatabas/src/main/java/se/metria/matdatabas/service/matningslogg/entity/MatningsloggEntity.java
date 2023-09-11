package se.metria.matdatabas.service.matningslogg.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@Table(name = "matningslogg")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name="matningslogg_id_seq", sequenceName="matningslogg_id_seq", allocationSize = 1)
public class MatningsloggEntity  {

	@Id
	@GeneratedValue(generator="matningslogg_id_seq")
	private Long id;

	@Size(max = 500)
	private String beskrivning;

	@NotNull
	private Short handelse;

	@CreatedBy
	@Column(name="loggat_av_id")
	private Integer loggatAvId;

	@NotNull
	@CreatedDate
	@Column(name="loggat_datum")
	private LocalDateTime loggatDatum;

	@NotNull
	@Column(name="matning_id")
	private Long matningId;
	
	public MatningsloggEntity() {
	}

	public MatningsloggEntity(@NotNull Long matningId, @NotNull Short handelse) {
		this.matningId = matningId;
		this.handelse = handelse;
	}
	
	public MatningsloggEntity(@NotNull Long matningId, @NotNull Short handelse, @Size(max = 500) String beskrivning) {
		this(matningId, handelse);
		this.beskrivning = beskrivning;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MatningsloggEntity)) return false;
		MatningsloggEntity that = (MatningsloggEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
