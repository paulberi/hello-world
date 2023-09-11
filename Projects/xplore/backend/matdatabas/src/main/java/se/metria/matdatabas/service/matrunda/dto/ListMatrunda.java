package se.metria.matdatabas.service.matrunda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.matrunda.entity.MatrundaEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListMatrunda {
	
	private Integer id;
	private Boolean aktiv;
	private String beskrivning;
	private String namn;
	private Integer antalMatobjekt;
	
	public static ListMatrunda fromEntity(MatrundaEntity matrunda) {
		return ListMatrunda.builder()
				.id(matrunda.getId())
				.aktiv(matrunda.getAktiv())
				.beskrivning(matrunda.getBeskrivning())
				.namn(matrunda.getNamn())
				.antalMatobjekt(matrunda.getAntalMatobjekt())
				.build();
	}
}
