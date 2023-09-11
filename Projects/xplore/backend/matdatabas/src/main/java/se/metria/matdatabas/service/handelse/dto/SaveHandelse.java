package se.metria.matdatabas.service.handelse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveHandelse {
	private String benamning;
	private String beskrivning;
	private LocalDateTime datum;
	private Set<UUID> bifogadeBilderIds;

}
