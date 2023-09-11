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
public class Systemlogg {

	private String beskrivning;
	private Short handelse;
	private String anvandarnamn;
	private LocalDateTime datum;

	public static Systemlogg fromEntity(SystemloggEntity entity) {
		return Systemlogg.builder()
				.anvandarnamn(entity.getLoggatAv())
				.beskrivning(entity.getBeskrivning())
				.datum(entity.getLoggatDatum())
				.handelse(entity.getHandelse()).build();
	}
}