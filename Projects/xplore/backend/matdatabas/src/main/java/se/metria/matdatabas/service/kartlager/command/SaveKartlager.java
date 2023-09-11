package se.metria.matdatabas.service.kartlager.command;

import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveKartlager {
	private String namn;
	private String grupp;
	private Boolean visa;
	private String beskrivning;
	@Singular("kartlagerfil")
	private Set<SaveKartlagerfil> kartlagerfiler;
}
