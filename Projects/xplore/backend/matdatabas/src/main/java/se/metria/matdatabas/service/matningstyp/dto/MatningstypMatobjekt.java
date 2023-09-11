package se.metria.matdatabas.service.matningstyp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;
import static se.metria.matdatabas.db.tables.MatobjektTyp.MATOBJEKT_TYP;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatningstypMatobjekt {
	private Integer matningstypId;
	private String matningstypNamn;
	private String matningstypStorhet;
	private Boolean matningstypAktiv;

	private Integer matobjektId;
	private String matobjektNamn;
	private String matobjektTyp;
	private String matobjektFastighet;
	private String matobjektLage;
	private Boolean matobjektAktiv;

	private Boolean aktiv;

	private Integer ejGranskade;

	public MatningstypMatobjekt(Record source) {
		matningstypId = source.get(MATNINGSTYP.ID);
		matningstypNamn = source.get(DEFINITION_MATNINGSTYP.NAMN);
		matningstypStorhet = source.get(DEFINITION_MATNINGSTYP.STORHET);
		matningstypAktiv = source.get(MATNINGSTYP.AKTIV);
		matobjektId = source.get(MATOBJEKT.ID);
		matobjektNamn = source.get(MATOBJEKT.NAMN);
		matobjektTyp = source.get(MATOBJEKT_TYP.NAMN);
		matobjektFastighet = source.get(MATOBJEKT.FASTIGHET);
		matobjektLage = source.get(MATOBJEKT.LAGE);
		matobjektAktiv = source.get(MATOBJEKT.AKTIV);
		ejGranskade = (Integer) source.getValue("ej_granskade");
	}

	public Boolean getAktiv() {
		return matningstypAktiv && matobjektAktiv;
	}
}
