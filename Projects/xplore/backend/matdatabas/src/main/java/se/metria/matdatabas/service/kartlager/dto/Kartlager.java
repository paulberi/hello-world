package se.metria.matdatabas.service.kartlager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.db.tables.records.KartlagerFilRecord;
import se.metria.matdatabas.db.tables.records.KartlagerRecord;
import se.metria.matdatabas.service.kartlager.entity.KartlagerEntity;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kartlager implements Ordered {
	private Integer id;
	private String namn;
	private String grupp;
	private String beskrivning;
	private Short ordning;
	private Boolean visa;
	private LocalDateTime skapadDatum;
	private LocalDateTime andradDatum;
	private Integer andradAvId;
	private Boolean andringsbar;

	private List<Kartlagerfil> kartlagerfiler;

	public static Kartlager fromEntity(KartlagerEntity entity, boolean skipFiles) {
		var kartlager = new Kartlager();
		kartlager.id = entity.getId();
		kartlager.namn = entity.getNamn();
		kartlager.grupp = entity.getGrupp();
		kartlager.beskrivning = entity.getBeskrivning();
		kartlager.visa = entity.getVisa();
		kartlager.ordning = entity.getOrdning();
		kartlager.skapadDatum = entity.getSkapadDatum();
		kartlager.andradDatum = entity.getAndradDatum();
		kartlager.andradAvId = entity.getAndradAvId();
		kartlager.andringsbar = entity.getAndringsbar();
		if (!skipFiles) {
			kartlager.kartlagerfiler = entity.getKartlagerfiler().stream()
					.map(Kartlagerfil::fromEntity)
					.sorted(Comparator.comparing(Kartlagerfil::getFilnamn))
					.collect(Collectors.toList());
		}
		return kartlager;
	}


	@Override
	public int getOrder() {
		return ordning;
	}
}
