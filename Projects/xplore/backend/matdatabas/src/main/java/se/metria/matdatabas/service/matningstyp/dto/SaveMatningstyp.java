package se.metria.matdatabas.service.matningstyp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveMatningstyp {
	private Integer matansvarigAnvandargruppId;
	private Short matintervallAntalGanger;
	private Short matintervallTidsenhet;
	private Short paminnelseDagar;
	private Boolean aktiv;
	private String instrument;
	private Boolean granskasAutomatiskt;
	private Double granskasMin;
	private Double granskasMax;
	private Double berakningKonstant;
	private Double berakningReferensniva;
	private Double maxPejlbartDjup;
	private String fixpunkt;
	private String enhet;
	private Short decimaler;
}
