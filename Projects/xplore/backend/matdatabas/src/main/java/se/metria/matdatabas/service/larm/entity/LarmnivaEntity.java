package se.metria.matdatabas.service.larm.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import se.metria.matdatabas.service.common.Auditable;
import se.metria.matdatabas.service.larm.dto.SaveLarm;
import se.metria.matdatabas.service.larm.dto.SaveLarmniva;

import javax.persistence.*;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "larmniva")
@SequenceGenerator(name = "larmniva_id_seq", sequenceName = "larmniva_id_seq", allocationSize = 1)
public class LarmnivaEntity extends Auditable {
	@Id
	@GeneratedValue(generator = "larmniva_id_seq")
	private Integer id;
	private String namn;
	private String beskrivning;

	public LarmnivaEntity(SaveLarmniva saveLarmniva) {
		save(saveLarmniva);
	}
	public void save(SaveLarmniva saveLarmniva) {
		setNamn(saveLarmniva.getNamn());
		setBeskrivning(saveLarmniva.getBeskrivning());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LarmnivaEntity)) return false;
		LarmnivaEntity that = (LarmnivaEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
