package se.metria.matdatabas.service.matrunda.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import se.metria.matdatabas.service.common.Auditable;
import se.metria.matdatabas.service.common.ChangeLog;
import se.metria.matdatabas.service.matrunda.dto.EditMatrunda;

@Data
@Entity
@Table(name = "matrunda")
@SequenceGenerator(name = "matrunda_id_seq", sequenceName = "matrunda_id_seq", allocationSize = 1)
public class MatrundaEntity extends Auditable {

	@Id
	@GeneratedValue(generator = "matrunda_id_seq")
	private Integer id;

	@NotNull
	private Boolean aktiv;

	@Size(max = 500)
	private String beskrivning;

	@NotNull
	@Size(max = 60)
	private String namn;
	
	@Formula("(SELECT COUNT(*) FROM matdatabas.matrunda_matningstyp mm WHERE mm.matrunda_id = id)")
	private Integer antalMatobjekt;

	@ElementCollection
	@CollectionTable(name = "matrunda_matningstyp", joinColumns = @JoinColumn(name = "matrunda_id"))
	private Set<MatrundaMatningstypEntity> matrundaMatningstyper = new HashSet<>();

	@Transient
	private ChangeLog changeLog = new ChangeLog(true);

	public MatrundaEntity() {
	}

	public MatrundaEntity(EditMatrunda edit) {
		update(edit);
	}

	public void update(EditMatrunda edit) {
		setNamn(edit.getNamn());
		setAktiv(edit.getAktiv());
		setBeskrivning(edit.getBeskrivning());

		if (edit.getMatningstyper() != null) {
			var members = edit.getMatningstyper().stream().map(member -> new MatrundaMatningstypEntity(member.getMatningstypId(), member.getOrdning())).collect(Collectors.toSet());
			setMatrundaMatningstyper(members);
		}
	}

	public void setAktiv(Boolean aktiv) {
		changeLog.evaluatePropertyChange("Namn", this.aktiv, aktiv);
		this.aktiv = aktiv;
	}

	public void setBeskrivning(String beskrivning) {
		changeLog.evaluatePropertyChange("Beskrivning", this.beskrivning, beskrivning);
		this.beskrivning = beskrivning;
	}

	public void setNamn(String namn) {
		changeLog.evaluatePropertyChange("Namn", this.namn, namn);
		this.namn = namn;
	}

	public void setMatrundaMatningstyper(Set<MatrundaMatningstypEntity> matrundaMatningstyper) {
		if (!changeLog.evaluateSizeDiff("Valda mätningstyper", this.matrundaMatningstyper, matrundaMatningstyper)) {
			changeLog.evaluatePropertyChange("Ordningen på mätningstyperna", this.matrundaMatningstyper, matrundaMatningstyper);
		}
		this.matrundaMatningstyper = matrundaMatningstyper;
	}

	public void removeMatningstyp(Integer matningstypId) {
		var sizeBefore = matrundaMatningstyper.size();
		matrundaMatningstyper.removeIf(member -> member.getMatningstypId().equals(matningstypId));
		changeLog.evaluateSizeDiff("Valda mätningstyper", sizeBefore, matrundaMatningstyper.size());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MatrundaEntity)) return false;
		MatrundaEntity that = (MatrundaEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
