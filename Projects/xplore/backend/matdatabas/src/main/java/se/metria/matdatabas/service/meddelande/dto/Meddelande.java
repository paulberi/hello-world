package se.metria.matdatabas.service.meddelande.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meddelande {
	private Integer id;
	private LocalDate datum;
	private String rubrik;
	private String url;
	private String meddelande;
	private LocalDateTime skapadDatum;
	private LocalDateTime andradDatum;
	private Integer andradAvId;
}
