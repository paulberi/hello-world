package se.metria.matdatabas.service.matningstyp.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import lombok.Data;
import se.metria.matdatabas.service.common.Auditable;
import se.metria.matdatabas.service.common.ChangeLog;
import se.metria.matdatabas.service.definitionmatningstyp.entity.DefinitionMatningstypEntity;
import se.metria.matdatabas.service.matning.entity.MatningEntity;
import se.metria.matdatabas.service.matningstyp.dto.SaveMatningstyp;
import se.metria.matdatabas.service.matobjekt.MatobjektConstants;

@Data
@Entity
@Table(name = "matningstyp")
@SequenceGenerator(name = "matningstyp_id_seq", sequenceName = "matningstyp_id_seq", allocationSize = 1)
public class MatningstypEntity extends Auditable {

	@Id
	@GeneratedValue(generator = "matningstyp_id_seq")
	private Integer id;

	@Embedded
	private Matintervall matintervall;

	@NotNull
	private Boolean aktiv;

	@NotNull
	@Column(name = "paminnelse_dagar")
	private Short paminnelseDagar;

	private String instrument;

	@NotNull
	@Column(name = "granskas_automatiskt")
	private Boolean granskasAutomatiskt;

	@Column(name = "granskas_min")
	private Double granskasMin;

	@Column(name = "granskas_max")
	private Double granskasMax;

	@Column(name = "berakning_konstant")
	private Double berakningKonstant;

	@Column(name = "berakning_referensniva")
	private Double berakningReferensniva;

	@Column(name = "max_pejlbart_djup")
	private Double maxPejlbartDjup;

	private String fixpunkt;

	@NotNull
	private String enhet;

	@NotNull
	private Short decimaler;

	@OneToMany
	@JoinColumn(name = "matningstyp_id")
	private Set<MatningEntity> matningar = new HashSet<>();

	@Formula("(select count(*) from matdatabas.matning m where m.matningstyp_id = id and m.status = 0)")
	private Integer ejGranskadeMatningar;

	@NotNull
	@Column(name = "matobjekt_id")
	private Integer matobjektId;

	@Formula("(select dm.namn from matdatabas.definition_matningstyp dm where dm.id = definition_matningstyp_id)")
	private String definitionMatningstypNamn;

	@ManyToOne
	@JoinColumn(name = "definition_matningstyp_id")
	private DefinitionMatningstypEntity definitionMatningstyp;

	@Column(name = "matansvarig_anvandargrupp_id")
	private Integer matansvarigAnvandargruppId;

	@Transient
	private ChangeLog changeLog = new ChangeLog(true);

	public MatningstypEntity() {
	}

	public MatningstypEntity(Integer matobjektId, SaveMatningstyp save, DefinitionMatningstypEntity definition) {
		setMatobjektId(matobjektId);
		setDefinitionMatningstyp(definition);

		copyDefaultValues(definition);

		update(save, definition);
	}

	public void update(SaveMatningstyp save, DefinitionMatningstypEntity definition) {
		updateGrunduppgifter(save);

		if (definition.getAutomatiskInrapportering()) {
			setInstrument(save.getInstrument());
		}
		if (definition.getAutomatiskGranskning()) {
			updateAutomatiskGranskning(save);
		} else {
			setGranskasAutomatiskt(false);
		}

		if (definition.getBerakningstyp() != null) {
			switch (definition.getBerakningstyp()) {
				case NIVA_VATTEN_LUFTTRYCK:
					updateNivaVattenLufttryck(save);
					break;
				case NIVA_NEDMATNING:
					updateNivaNedmatning(save);
					break;
				case NIVA_PORTRYCK:
					updateNivaPortryck(save);
					break;
				case SATTNING:
					updateSattning(save);
			}
		}

		if (definition.getMatobjektTyp() == MatobjektConstants.TYP_VATTENKEMI) {
			updateVattenkemi(save);
		}

	}

	private void copyDefaultValues(DefinitionMatningstypEntity definition) {
		setEnhet(definition.getEnhet());
		setDecimaler(definition.getDecimaler());
	}

	private void updateGrunduppgifter(SaveMatningstyp save) {
		setMatansvarigAnvandargruppId(save.getMatansvarigAnvandargruppId());
		setMatintervall(new Matintervall(save.getMatintervallTidsenhet(), save.getMatintervallAntalGanger()));
		setPaminnelseDagar(save.getPaminnelseDagar());
		setAktiv(save.getAktiv());
	}

	private void updateAutomatiskGranskning(SaveMatningstyp save) {
		setGranskasAutomatiskt(Objects.requireNonNull(save.getGranskasAutomatiskt(), "granskasAutomatiskt must not be null"));
		if (granskasAutomatiskt) {
			Objects.requireNonNull(save.getGranskasMin(), "granskasMin must not be null");
			Objects.requireNonNull(save.getGranskasMax(), "granskasMax must not be null");
		}
		setGranskasMin(save.getGranskasMin());
		setGranskasMax(save.getGranskasMax());
	}

	private void updateNivaVattenLufttryck(SaveMatningstyp save) {
		var oldBerakningKonstant = getBerakningKonstant();

		setBerakningKonstant(Objects.requireNonNull(save.getBerakningKonstant(), "berakningKonstant must not be null"));

		changeLog.evaluatePropertyChange("Konstant (kref, ber)", oldBerakningKonstant, getBerakningKonstant());
	}

	private void updateNivaNedmatning(SaveMatningstyp save) {
		var oldReferensniva = getBerakningReferensniva() + " " + getEnhet();
		var oldBerakningKonstant = getBerakningKonstant();

		setBerakningReferensniva(Objects.requireNonNull(save.getBerakningReferensniva(), "referensniva must not be null"));
		setBerakningKonstant(Objects.requireNonNull(save.getBerakningKonstant(), "berakningKonstant must not be null"));
		setMaxPejlbartDjup(Objects.requireNonNull(save.getMaxPejlbartDjup(), "maxPejlbartDjup must not be null"));

		changeLog.evaluatePropertyChange("Referensnivå (zref)", oldReferensniva, getBerakningReferensniva() + " " + getEnhet());
		changeLog.evaluatePropertyChange("Gradningskonstant (kgrad)", oldBerakningKonstant, getBerakningKonstant());
	}

	private void updateNivaPortryck(SaveMatningstyp save) {
		var oldReferensniva = getBerakningReferensniva() + " " + getEnhet();
		var oldBerakningKonstant = getBerakningKonstant();

		setBerakningReferensniva(Objects.requireNonNull(save.getBerakningReferensniva(), "referensniva must not be null"));
		setBerakningKonstant(Objects.requireNonNull(save.getBerakningKonstant(), "berakningKonstant must not be null"));

		changeLog.evaluatePropertyChange("Spetsnivå (zspets)", oldReferensniva, getBerakningReferensniva() + " " + getEnhet());
		changeLog.evaluatePropertyChange("Gradningskonstant (kgrad)", oldBerakningKonstant, getBerakningKonstant());
	}

	private void updateSattning(SaveMatningstyp save) {
		setFixpunkt(Objects.requireNonNull(save.getFixpunkt(), "fixpunkt must not be null"));
	}

	private void updateVattenkemi(SaveMatningstyp save) {
		setEnhet(Objects.requireNonNull(save.getEnhet(), "enhet must not be null"));
		setDecimaler(Objects.requireNonNull(save.getDecimaler(), "decimaler must not be null"));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MatningstypEntity)) return false;
		MatningstypEntity that = (MatningstypEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
