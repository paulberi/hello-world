package se.metria.matdatabas.service.bifogadfil.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.bifogadfil.entity.BifogadfilInfoView;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BifogadfilInfo {
	
	private UUID id;
	private String filnamn;
	private String mimeTyp;
	private LocalDateTime skapadDatum;

	public static BifogadfilInfo fromView(BifogadfilInfoView bifogadfil) {
		return BifogadfilInfo.builder()
				.id(bifogadfil.getId())
				.filnamn(bifogadfil.getFilnamn())
				.mimeTyp(bifogadfil.getMimeTyp())
				.skapadDatum(bifogadfil.getSkapadDatum())
				.build();				
	}
}
