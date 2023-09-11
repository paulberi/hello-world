package se.metria.matdatabas.service.anvandargrupp.entity;

import org.hibernate.annotations.Formula;
import se.metria.matdatabas.service.anvandare.model.AnvandareEntity;
import se.metria.matdatabas.service.common.Auditable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "anvandargrupp")
@SequenceGenerator(name = "anvandargrupp_id_seq", sequenceName = "anvandargrupp_id_seq", allocationSize = 1)
public class AnvandargruppEntity extends Auditable {

	@Id
	@GeneratedValue(generator = "anvandargrupp_id_seq")
	private Integer id;

	private String namn;

	private String beskrivning;

	@Formula("(SELECT COUNT(*) FROM matdatabas.anvandargrupp_anvandare aa WHERE aa.anvandargrupp_id = id)")
	private Integer anvandareCount;

	public AnvandargruppEntity() {
	}

	public AnvandargruppEntity(Integer id) {
		this.id = id;
	}

	public AnvandargruppEntity(String namn, String beskrivning) {
		this.namn = namn;
		this.beskrivning = beskrivning;
	}

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

	public String getBeskrivning() {
		return beskrivning;
	}

	public void setBeskrivning(String beskrivning) {
		this.beskrivning = beskrivning;
	}

	public Integer getAnvandareCount() {
		return anvandareCount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AnvandargruppEntity that = (AnvandargruppEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
