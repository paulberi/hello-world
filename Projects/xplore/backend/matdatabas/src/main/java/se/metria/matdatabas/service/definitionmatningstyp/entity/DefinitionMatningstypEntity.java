package se.metria.matdatabas.service.definitionmatningstyp.entity;

import lombok.Data;
import lombok.Getter;
import se.metria.matdatabas.service.common.Auditable;
import se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.dto.SaveDefinitionMatningstyp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Entity
@Table(name = "definition_matningstyp")
@SequenceGenerator(name = "definition_matningstyp_id_seq", sequenceName = "definition_matningstyp_id_seq", allocationSize = 1)
public class DefinitionMatningstypEntity extends Auditable {
	@Id
	@GeneratedValue(generator = "definition_matningstyp_id_seq")
	private Integer id;

	@NotNull
	@Column(name = "matobjekt_typ")
	private Short matobjektTyp;

	@Enumerated(EnumType.STRING)
	private Berakningstyp berakningstyp;

	@NotNull
	private String namn;

	private String storhet;

	@NotNull
	private String enhet;

	@NotNull
	private Short decimaler;

	@Column(name = "beraknad_storhet")
	private String beraknadStorhet;

	@Column(name = "beraknad_enhet")
	private String beraknadEnhet;

	@Column(name = "beraknad_decimaler")
	private Short beraknadDecimaler;

	@NotNull
	private String felkoder;

	@NotNull
	private Short graftyp;

	@NotNull
	@Column(name = "automatisk_inrapportering")
	private Boolean automatiskInrapportering;

	private String beskrivning;

	@NotNull
	private Boolean andringsbar;

	@NotNull
	@Column(name = "automatisk_granskning")
	private Boolean automatiskGranskning;

	public DefinitionMatningstypEntity() {
	}

	public DefinitionMatningstypEntity(SaveDefinitionMatningstyp save) {
		setAndringsbar(true);
		setFelkoder("Hinder, Annat fel");
		setGraftyp((short) 0);
		save(save);
	}

	public void save(SaveDefinitionMatningstyp save) {
		if (!getAndringsbar()) {
			throw new IllegalStateException("DefinitionMatningstyp is read-only");
		}
		setMatobjektTyp(save.getMatobjektTyp());
		setNamn(save.getNamn());
		setEnhet(save.getEnhet());
		setDecimaler(save.getDecimaler());
		setAutomatiskInrapportering(save.getAutomatiskInrapportering());
		setAutomatiskGranskning(save.getAutomatiskGranskning());
		setBeskrivning(save.getBeskrivning());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DefinitionMatningstypEntity)) return false;
		DefinitionMatningstypEntity that = (DefinitionMatningstypEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
