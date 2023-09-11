package se.metria.matdatabas.service.systemlogg.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@Table(name = "systemlogg")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "systemlogg_id_seq", sequenceName = "systemlogg_id_seq", allocationSize = 1)
public class SystemloggEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "systemlogg_id_seq")
	private Long id;

	private String beskrivning;

	private Short handelse;

	@CreatedBy
	@Column(name = "loggat_av_id")
	private Integer loggatAvId;
	
	@Formula("(select a.namn from matdatabas.anvandare a where a.id = loggat_av_id)")
	private String loggatAv;

	@CreatedDate
	@Column(name="loggat_datum")
	private LocalDateTime loggatDatum;

	public SystemloggEntity() {
	}
	
	public SystemloggEntity(String beskrivning, Short handelse) {
		super();
		this.beskrivning = beskrivning;
		this.handelse = handelse;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		var systemlogg = (SystemloggEntity) o;
		return Objects.equals(id, systemlogg.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}