package se.metria.matdatabas.service.common;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class Auditable extends BaseEntity<Integer> {

	@CreatedDate
	@Column(name = "skapad_datum")
	private LocalDateTime skapadDatum;

	@LastModifiedDate
	@Column(name = "andrad_datum")
	private LocalDateTime andradDatum;

	@LastModifiedBy
	@Column(name = "andrad_av_id")
	private Integer andradAvId;

	public LocalDateTime getSkapadDatum() {
		return skapadDatum;
	}

	public LocalDateTime getAndradDatum() {
		return andradDatum;
	}

	public Integer getAndradAvId() {
		return andradAvId;
	}
}
