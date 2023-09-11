package se.metria.matdatabas.service.matningstyp.dto;

import lombok.Data;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.TableField;

import javax.persistence.Column;
import java.util.Optional;

import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;

@Data
public class MatningstypAntalMatningar {
	private Integer matningstypId;
	private String matningstypNamn;
	private String matningstypStorhet;
	private Integer matobjektId;
	private String matobjektNamn;
	private String matobjektFastighet;
	private String matobjektLage;
	private Integer	antalMatningar;

	public MatningstypAntalMatningar(Record source) {
		matningstypId = source.get(MATNINGSTYP.ID);
		matningstypNamn = source.get(DEFINITION_MATNINGSTYP.NAMN);
		matningstypStorhet = source.get(DEFINITION_MATNINGSTYP.STORHET);
		matobjektId = source.get(MATOBJEKT.ID);
		matobjektNamn = source.get(MATOBJEKT.NAMN);
		matobjektFastighet = source.get(MATOBJEKT.FASTIGHET);
		matobjektLage = source.get(MATOBJEKT.LAGE);
		antalMatningar = (Integer) source.getValue("antal_matningar");
	}
}
