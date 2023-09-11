package se.metria.matdatabas.service.systemlogg.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.systemlogg.entity.SystemloggEntity;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemloggEntry {

	private String beskrivning;
	private Short handelse;
	private Integer anvandarid;
	private LocalDateTime datum;

	public static SystemloggEntry fromEntity(SystemloggEntity entity) {
		return SystemloggEntry.builder()
				.anvandarid(entity.getLoggatAvId())
				.beskrivning(entity.getBeskrivning())
				.datum(entity.getLoggatDatum())
				.handelse(entity.getHandelse())
				.build();
	}
}