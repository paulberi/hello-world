package se.metria.matdatabas.service.matning.dto.csvImport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportError {
	
	public static final ImportError MATOBJEKT_MISSING = new ImportError("matobjekt", "Mätobjekt saknas.");
	public static final ImportError MATNINGSTYP_MISSING = new ImportError("matningstyp", "Mätningstyp saknas.");
	public static final ImportError MATNINGSTYP_STORHET_MISSING = new ImportError("matningstyp", "Flera mätningstyper med samma namn på mätobjektet, storhet måste anges.");
	public static final ImportError INSTUMENT_MISSING = new ImportError("matobjekt", "Mätobjekt saknas för instrument.");
	public static final ImportError INSTUMENT_TOMANY = new ImportError("matobjekt", "Det finns fler än ett mätobjekt för instrumentet.");
	public static final ImportError AVLASTDATUM_MISSING = new ImportError("avlastDatum", "Datum saknas.");
	public static final ImportError AVLASTDATUM_FORMAT = new ImportError("avlastDatum", "Datum har felaktigt format.");
	public static final ImportError AVLASTVARDE_MISSING = new ImportError("avlastVarde", "Värde saknas.");
	public static final ImportError AVLASTVARDE_FORMAT = new ImportError("avlastVarde", "Värde har felaktigt format.");
	public static final ImportError INOMDETEKTIONSOMRADE_VALUE = new ImportError("inomDetektionsomrade", "Detektionsgräns har felaktig symbol.");
	public static final ImportError FELKOD_VALUE = new ImportError("felkod", "Felkod har felaktigt värde.");
	public static final ImportError ENHET_AVLAST = new ImportError("enhetAvlast", "Felaktig enhet för uppmätt värde.");
	public static final ImportError ENHET_BERAKNAT = new ImportError("enhetBeraknat", "Felaktig enhet för beräknat värde.");
	public static final ImportError KOMMENTAR_LENGTH = new ImportError("kommentar", "Kommentar är för lång, max 250 tecken.");;
	public static final ImportError AVLASTVARDE_LESS_EQ_ZERO = new ImportError("avlastVarde", "Värde är mindre än eller lika med noll.");
	public static final ImportError AVLASTVARDE_LESS_ZERO = new ImportError("avlastVarde", "Värde är mindre än noll.");
	public static final ImportError EXISTS = new ImportError("", "Mätningen finns redan inrapporterad.");
	public static final ImportError VARDE_MISSING = new ImportError("varde", "Både uppmätt värde och beräknat värde saknas");
	public static final ImportError HINDER_AND_VARDE = new ImportError("felkod", "En mätning kan inte ha både en felkod och värden");
	
	private String property;
	private String error;
}
