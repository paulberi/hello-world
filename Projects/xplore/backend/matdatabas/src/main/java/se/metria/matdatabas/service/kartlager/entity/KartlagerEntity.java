package se.metria.matdatabas.service.kartlager.entity;

import lombok.Getter;
import lombok.Setter;
import se.metria.matdatabas.service.common.Auditable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "kartlager")
@SequenceGenerator(name = "kartlager_id_seq", sequenceName = "kartlager_id_seq", allocationSize = 1)
public class KartlagerEntity extends Auditable {

	@Id
	@GeneratedValue(generator = "kartlager_id_seq")
	private Integer id;
	private String namn;
	private String grupp;
	private Short ordning;
	private Boolean visa;
	private String beskrivning;
	private Boolean andringsbar;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<KartlagerfilEntity> kartlagerfiler = new HashSet<>();

	public KartlagerEntity() {
	}

	public KartlagerEntity(String namn, String grupp, Boolean visa, String beskrivning, Short ordning) {
		setNamn(namn);
		setGrupp(grupp);
		setVisa(visa);
		setBeskrivning(beskrivning);
		setOrdning(ordning);
		setAndringsbar(true);
	}

	public void setGrupp(String grupp) {
		this.grupp = "".equals(grupp) ? null : grupp;
	}

	public void addKartlagerfil(KartlagerfilEntity newKartlagerfil) {
		kartlagerfiler.add(newKartlagerfil);
		newKartlagerfil.setOwner(this);
	}
}
