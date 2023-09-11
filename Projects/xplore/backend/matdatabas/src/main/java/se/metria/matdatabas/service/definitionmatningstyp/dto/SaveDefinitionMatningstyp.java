package se.metria.matdatabas.service.definitionmatningstyp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.definitionmatningstyp.entity.DefinitionMatningstypEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveDefinitionMatningstyp {
	private Short matobjektTyp;
	private String namn;
	private String enhet;
	private Short decimaler;
	private Boolean automatiskInrapportering;
	private Boolean automatiskGranskning;
	private String beskrivning;
	private Short graftyp;
}
