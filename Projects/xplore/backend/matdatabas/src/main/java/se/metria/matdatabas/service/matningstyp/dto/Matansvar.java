package se.metria.matdatabas.service.matningstyp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.matningstyp.entity.MatansvarEntity;
import se.metria.matdatabas.service.matningstyp.entity.MatningstypEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matansvar {
	private String matobjekt;
	private Integer matobjektId;
	private String matningstyp;
	private String fastighet;

	public static Matansvar fromEntity(MatansvarEntity entity) {
		return Matansvar.builder()
				.matobjekt(entity.getMatobjekt())
				.matobjektId(entity.getMatobjektId())
				.matningstyp(entity.getMatningstyp())
				.fastighet(entity.getFastighet())
				.build();
	}
}
