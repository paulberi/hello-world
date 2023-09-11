package se.metria.matdatabas.service.larm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.larm.entity.LarmnivaEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Larmniva {
	private Integer id;
	private String namn;
	private String beskrivning;

	public static Larmniva fromEntity(LarmnivaEntity entity) {
		return Larmniva.builder()
				.id(entity.getId())
				.namn(entity.getNamn())
				.beskrivning(entity.getBeskrivning())
				.build();
	}
}
