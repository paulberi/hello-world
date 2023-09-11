package se.metria.matdatabas.service.definitionmatningstyp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.entity.DefinitionMatningstypEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefinitionMatningstyp {
	private Integer id;
	private Short matobjektTyp;
	private Berakningstyp berakningstyp;
	private String namn;
	private String storhet;
	private String enhet;
	private Short decimaler;
	private String beraknadStorhet;
	private String beraknadEnhet;
	private String beskrivning;
	private Boolean automatiskInrapportering;
	private Boolean automatiskGranskning;
	private Boolean andringsbar;
	private Short graftyp;

	public static DefinitionMatningstyp fromEntity(DefinitionMatningstypEntity entity) {
		return DefinitionMatningstyp.builder()
				.id(entity.getId())
				.matobjektTyp(entity.getMatobjektTyp())
				.berakningstyp(entity.getBerakningstyp())
				.namn(entity.getNamn())
				.storhet(entity.getStorhet())
				.enhet(entity.getEnhet())
				.decimaler(entity.getDecimaler())
				.beraknadStorhet(entity.getBeraknadStorhet())
				.beraknadEnhet(entity.getBeraknadEnhet())
				.beskrivning(entity.getBeskrivning())
				.automatiskInrapportering(entity.getAutomatiskInrapportering())
				.automatiskGranskning(entity.getAutomatiskGranskning())
				.andringsbar(entity.getAndringsbar())
				.graftyp(entity.getGraftyp())
				.build();
	}
}
