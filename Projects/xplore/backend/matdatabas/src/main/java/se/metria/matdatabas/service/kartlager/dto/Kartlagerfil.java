package se.metria.matdatabas.service.kartlager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.kartlager.entity.KartlagerfilEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kartlagerfil {
	private UUID id;
	private String filnamn;
	private String stil;
	private LocalDateTime skapadDatum;

	public static Kartlagerfil fromEntity(KartlagerfilEntity entity) {
		var kartlager = new Kartlagerfil();
		kartlager.id = entity.getId();
		kartlager.filnamn = entity.getFilnamn();
		kartlager.stil = entity.getStil();
		kartlager.skapadDatum = entity.getSkapadDatum();
		return kartlager;
	}
}
