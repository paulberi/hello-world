package se.metria.matdatabas.service.handelse.dto;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.bifogadfil.dto.BifogadfilInfo;
import se.metria.matdatabas.service.handelse.entity.HandelseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Handelse {	
	private Integer id;
	private String benamning;
	private String beskrivning;
	private String foretag;
	private LocalDateTime datum;
	private List<BifogadfilInfo> bifogadebilder;

	public static Handelse fromEntity(HandelseEntity handelse, Map<UUID, BifogadfilInfo> bifogadfilMap) {
		return Handelse.builder()
				.id(handelse.getId())
				.foretag(handelse.getLoggadAv())
				.benamning(handelse.getBenamning())
				.beskrivning(handelse.getBeskrivning())
				.datum(handelse.getLoggadDatum())
				.bifogadebilder(handelse.getBifogadeBilderIds().stream()
						.map(bifogadfilMap::get)
						.sorted(comparing(BifogadfilInfo::getFilnamn))
						.collect(toList()))
				.build();
	}  
	  
}

