package se.metria.matdatabas.service.matningstyp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.definitionmatningstyp.dto.DefinitionMatningstyp;
import se.metria.matdatabas.service.matning.Matningsfelkod;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matningstyp.entity.MatningstypEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matningstyp {
	private Integer id;
	private Integer definitionMatningstypId;
	private Integer matansvarigAnvandargruppId;
	private Short matintervallAntalGanger;
	private Short matintervallTidsenhet;
	private Short paminnelseDagar;
	private String instrument;
	private Boolean granskasAutomatiskt;
	private Double granskasMin;
	private Double granskasMax;
	private Double berakningKonstant;
	private Double berakningReferensniva;
	private Double maxPejlbartDjup;
	private String fixpunkt;
	private String typ;
	private String enhet;
	private String storhet;
	private String beraknadEnhet;
	private String beraknadStorhet;
	private Short decimaler;
	private String senasteVarde;
	private LocalDateTime senasteVardeDatum;
	private Boolean aktiv;
	private Integer ejGranskade;
	private Integer matobjektId;

	public static Matningstyp fromEntity(MatningstypEntity entity) {
		return Matningstyp.builder()
				.id(entity.getId())
				.matansvarigAnvandargruppId(entity.getMatansvarigAnvandargruppId())
				.matintervallAntalGanger(entity.getMatintervall().getAntalGanger())
				.matintervallTidsenhet(entity.getMatintervall().getTidsenhet())
				.paminnelseDagar(entity.getPaminnelseDagar())
				.instrument(entity.getInstrument())
				.granskasAutomatiskt(entity.getGranskasAutomatiskt())
				.granskasMin(entity.getGranskasMin())
				.granskasMax(entity.getGranskasMax())
				.berakningKonstant(entity.getBerakningKonstant())
				.berakningReferensniva(entity.getBerakningReferensniva())
				.maxPejlbartDjup(entity.getMaxPejlbartDjup())
				.fixpunkt(entity.getFixpunkt())
				.typ(entity.getDefinitionMatningstyp().getNamn())
				.definitionMatningstypId(entity.getDefinitionMatningstyp().getId())
				.enhet(entity.getEnhet())
				.beraknadEnhet(entity.getDefinitionMatningstyp().getBeraknadEnhet())
				.storhet(entity.getDefinitionMatningstyp().getStorhet())
				.beraknadStorhet(entity.getDefinitionMatningstyp().getBeraknadStorhet())
				.decimaler(entity.getDecimaler())
				.aktiv(entity.getAktiv())
				.ejGranskade(entity.getEjGranskadeMatningar())
				.matobjektId(entity.getMatobjektId())
				.build();
	}

	public void setLatestMatning(Matning latest) {
		if (!latest.getFelkod().equals(Matningsfelkod.OK)) {
			senasteVarde = latest.getFelkod();
		} else if (latest.getAvlastVarde() != null) {
			senasteVarde = latest.getAvlastVarde().toString();
		} else {
			senasteVarde = null;
		}

		senasteVardeDatum = latest.getAvlastDatum();
	}
}
