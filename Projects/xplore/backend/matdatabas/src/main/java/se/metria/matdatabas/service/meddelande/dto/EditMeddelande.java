package se.metria.matdatabas.service.meddelande.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditMeddelande {
	private LocalDate datum;
	private String rubrik;
	private String url;
	private String meddelande;
}
