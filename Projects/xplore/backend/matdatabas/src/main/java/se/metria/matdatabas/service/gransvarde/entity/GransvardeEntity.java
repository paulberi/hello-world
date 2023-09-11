package se.metria.matdatabas.service.gransvarde.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import se.metria.matdatabas.service.common.Auditable;
import se.metria.matdatabas.service.gransvarde.command.SaveGransvarde;

import javax.persistence.*;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "gransvarde")
@SequenceGenerator(name = "gransvarde_id_seq", sequenceName = "gransvarde_id_seq", allocationSize = 1)
public class GransvardeEntity extends Auditable {
	@Id
	@GeneratedValue(generator = "gransvarde_id_seq")
	private Integer id;
	@Column(name = "typ_av_kontroll")
	private Short typAvKontroll;

	@Column(name = "gransvarde")
	private Double gransvarde;

	@Column(name = "larm_till_anvandargrupp_id")
	private Integer larmTillAnvandargruppId;

	@Column(name = "larmniva_id", nullable = false)
	private Integer larmnivaId;

	@Column(name = "matningstyp_id", nullable = false)
	private Integer matningstypId;

	@Column(name = "aktiv", nullable = false)
	private Boolean aktiv;

	public GransvardeEntity(SaveGransvarde saveGransvarde) {
		save(saveGransvarde);
	}

	public void save(SaveGransvarde saveGransvarde) {
		setTypAvKontroll(saveGransvarde.getTypAvKontroll());
		setGransvarde(saveGransvarde.getGransvarde());
		setLarmTillAnvandargruppId(saveGransvarde.getLarmTillAnvandargruppId());
		setLarmnivaId(saveGransvarde.getLarmnivaId());
		setMatningstypId(saveGransvarde.getMatningstypId());
		setAktiv(saveGransvarde.getAktiv());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GransvardeEntity)) return false;
		GransvardeEntity that = (GransvardeEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
