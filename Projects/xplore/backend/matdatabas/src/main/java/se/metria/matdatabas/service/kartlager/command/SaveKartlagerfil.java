package se.metria.matdatabas.service.kartlager.command;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveKartlagerfil {
	// Should be null for new files.
	private UUID id;

	private String stil;

	// Only used for new files.
	private String filnamn;
	private String fil;
}
