package se.metria.matdatabas.restapi.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.jooq.Record;
import se.metria.matdatabas.common.ValueFormatter;
import se.metria.matdatabas.service.matning.Matningstatus;

import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;

@Data
@JsonPropertyOrder(value = {"matobjekt", "matningstyp","storhet", "avlastDatum", "inomDetektionsomrade", "avlastVarde", "enhetAvlast", "beraknatVarde", "enhetBeraknat", "felkod", "status", "kommentar"})
public class MatdataExportRow {
	@JsonProperty("Mätobjekt")
	private String matobjekt;
	@JsonProperty("Mätningstyp")
	private String matningstyp;
	@JsonProperty("Storhet")
	private String storhet;
	@JsonProperty("Datum och tid")
	private String avlastDatum;
	@JsonProperty("Över/under detektionsgräns")
	private String inomDetektionsomrade;
	@JsonProperty("Uppmätt värde")
	private String avlastVarde;
	@JsonProperty("Enhet (uppmätt värde)")
	private String enhetAvlast;
	@JsonProperty("Beräknat värde")
	private String beraknatVarde;
	@JsonProperty("Enhet (beräknat värde)")
	private String enhetBeraknat;
	@JsonProperty("Felkod")
	private String felkod;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("Kommentar")
	private String kommentar;

	public MatdataExportRow(Record source, ValueFormatter formatter) {
		matobjekt = source.get(MATOBJEKT.NAMN);
		matningstyp = source.get(DEFINITION_MATNINGSTYP.NAMN);
		storhet = source.get(DEFINITION_MATNINGSTYP.STORHET);
		avlastDatum = formatter.formatDateTime(source.get(MATNING.AVLAST_DATUM));
		inomDetektionsomrade = formatter.formatDetektionsomrade(source.get(MATNING.INOM_DETEKTIONSOMRADE));

		if (source.get(MATNING.BERAKNAT_VARDE) != null) {
			beraknatVarde = formatter.formatMatvarde(source.get(MATNING.BERAKNAT_VARDE));
			enhetBeraknat = source.get(DEFINITION_MATNINGSTYP.BERAKNAD_ENHET);
		}

		if (source.get(MATNING.AVLAST_VARDE) != null) {
			avlastVarde = formatter.formatMatvarde(source.get(MATNING.AVLAST_VARDE));
			enhetAvlast = source.get(MATNINGSTYP.ENHET);
		}

		felkod = source.get(MATNING.FELKOD);
		status = matningStatus(source.get(MATNING.STATUS));
		kommentar = source.get(MATNING.KOMMENTAR);
	}

	private String matningStatus(Short status) {
		switch (status) {
			case Matningstatus.EJGRANSKAT:
				return "Ej granskat";
			case Matningstatus.FEL:
				return "Fel";
			case Matningstatus.GODKANT:
				return "Godkänt";
			default:
				return "Okänd mätningstatus: " + status;
		}
	}
}
