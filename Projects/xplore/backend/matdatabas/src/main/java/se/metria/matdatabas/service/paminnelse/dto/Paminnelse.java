package se.metria.matdatabas.service.paminnelse.dto;

import lombok.Data;
import org.jooq.Record;

import java.time.LocalDateTime;

import static se.metria.matdatabas.service.paminnelse.PaminnelseJooqFields.*;

@Data
public class Paminnelse {
	private Integer matningstypId;
	private String matningstypNamn;
	private Integer matobjektId;
	private String matobjektNamn;
	private String matobjektFastighet;
	private String matobjektLage;
	private String matobjektTyp;
	private LocalDateTime avlastDatum;
	private Short antalGanger;
	private Short tidsenhet;
	private Integer forsenadDagar;

	public Paminnelse(Record source) {
		matningstypId = (Integer) source.getValue(MATNINGSTYP_ID);
		matningstypNamn = (String) source.getValue(MATNINGSTYP_NAMN);
		matobjektId = (Integer) source.getValue(MATOBJEKT_ID);
		matobjektNamn = (String) source.getValue(MATOBJEKT_NAMN);
		matobjektFastighet = (String) source.getValue(MATOBJEKT_FASTIGHET);
		matobjektLage = (String) source.getValue(MATOBJEKT_LAGE);
		matobjektTyp = (String) source.getValue(MATOBJEKT_TYP_NAMN);
		avlastDatum = (LocalDateTime) source.getValue(AVLAST_DATUM);
		antalGanger = (Short) source.getValue(ANTAL_GANGER);
		tidsenhet = (Short) source.getValue(TIDSENHET);
		forsenadDagar = (Integer) source.getValue(FORSENAD_DAGAR);
	}
}
