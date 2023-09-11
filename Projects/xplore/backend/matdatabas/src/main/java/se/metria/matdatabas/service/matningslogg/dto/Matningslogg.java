package se.metria.matdatabas.service.matningslogg.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.matningslogg.entity.MatningsloggEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matningslogg {
	private String beskrivning;
	private Short handelse;
	private Integer loggatAvId;
	private LocalDateTime loggatDatum;
	private Long matningId;

	public static Matningslogg fromEntity(MatningsloggEntity entity) {
		return Matningslogg.builder()
				.beskrivning(entity.getBeskrivning())
				.handelse(entity.getHandelse())
				.loggatAvId(entity.getLoggatAvId())
				.loggatDatum(entity.getLoggatDatum())
				.matningId(entity.getMatningId())
				.build();
	}	
}
