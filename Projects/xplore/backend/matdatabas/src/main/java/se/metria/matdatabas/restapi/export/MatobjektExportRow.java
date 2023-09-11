package se.metria.matdatabas.restapi.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.jooq.Record;
import se.metria.matdatabas.common.ValueFormatter;
import se.metria.matdatabas.db.tables.DefinitionMatningstyp;
import se.metria.matdatabas.db.tables.Matningstyp;
import se.metria.matdatabas.db.tables.Matobjekt;
import se.metria.matdatabas.db.tables.MatobjektTyp;
import se.metria.matdatabas.service.matningstyp.dto.MatningstypMatobjekt;

import java.math.BigDecimal;
import java.text.Format;

import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;
import static se.metria.matdatabas.db.tables.MatobjektTyp.MATOBJEKT_TYP;

@Data
@JsonPropertyOrder(value = {"matobjekt", "fastighet", "lage", "posN", "posE", "matobjektTyp", "matningstyp", "storhet", "berakningReferensniva", "maxPejlbartDjup"})
public class MatobjektExportRow {
	@JsonProperty("Mätobjekt")
	private String matobjekt;
	@JsonProperty("Fastighet")
	private String fastighet;
	@JsonProperty("Läge")
	private String lage;
	@JsonProperty("N-position")
	private String posN;
	@JsonProperty("E-position")
	private String posE;
	@JsonProperty("Mätobjektstyp")
	private String matobjektTyp;
	@JsonProperty("Mätningstyp")
	private String matningstyp;
	@JsonProperty("Storhet")
	private String storhet;
	@JsonProperty("Referensnivå (m)")
	private String berakningReferensniva;
	@JsonProperty("Max pejlbart djup (m)")
	private String maxPejlbartDjup;

	public MatobjektExportRow(Record source, ValueFormatter formatter) {
		matobjekt = source.get(MATOBJEKT.NAMN);
		matningstyp = source.get(DEFINITION_MATNINGSTYP.NAMN);
		fastighet = source.get(MATOBJEKT.FASTIGHET);
		lage = source.get(MATOBJEKT.LAGE);
		posN = formatter.formatCoordinate(source.get(MATOBJEKT.POS_N));
		posE = formatter.formatCoordinate(source.get(MATOBJEKT.POS_E));
		matobjektTyp = source.get(MATOBJEKT_TYP.NAMN);
		storhet = source.get(DEFINITION_MATNINGSTYP.STORHET);
		berakningReferensniva = formatter.formatMatvarde(source.get(MATNINGSTYP.BERAKNING_REFERENSNIVA));
		maxPejlbartDjup = formatter.formatMatvarde(source.get(MATNINGSTYP.MAX_PEJLBART_DJUP));
	}
}
