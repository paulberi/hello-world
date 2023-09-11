package se.metria.matdatabas.service.larm.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import se.metria.matdatabas.service.handelse.dto.SaveHandelse;
import se.metria.matdatabas.service.larm.dto.SaveLarm;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "larm")
@SequenceGenerator(name = "larm_id_seq", sequenceName = "larm_id_seq", allocationSize = 1)
public class LarmEntity {
	@Id
	@GeneratedValue(generator = "larm_id_seq")
	private Long id;

	private Short status;
	private Integer matobjektId;
	private Long matningId;
	private Double varde;
	private Double gransvarde;
	private Integer gransvardeId;
	private LocalDateTime kvitteradDatum;
	private Integer kvitteradAvId;
	private Integer larmnivaId;
	private Integer anvandargruppId;
	private Short typAvKontroll;

	public LarmEntity(SaveLarm saveLarm) {
		save(saveLarm);
	}
	public void save(SaveLarm save) {
		setStatus(save.getStatus());
		setMatobjektId(save.getMatobjektId());
		setMatningId(save.getMatningId());
		setVarde(save.getVarde());
		setGransvarde(save.getGransvarde());
		setGransvardeId(save.getGransvardeId());
		setLarmnivaId(save.getLarmnivaId());
		setAnvandargruppId(save.getAnvandargruppId());
		setTypAvKontroll(save.getTypAvKontroll());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LarmEntity)) return false;
		LarmEntity that = (LarmEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
