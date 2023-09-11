package se.metria.matdatabas.service.matrunda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import se.metria.matdatabas.service.matning.Matningsfelkod;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * This class represents a row in the generated excel document "fältprotokoll".
 */
@Data
@Builder
@AllArgsConstructor
public class RapporteringMatningstyp {
	private Integer matobjektId;
	private Integer matningstypId;
	private String matobjekt;
	private String matningstyp;
	private String berakningstyp;
	private Integer ordning;
	private String lage;
	private String fastighet;
	private String storhet;
	private String enhet;
	private Integer decimaler;
	private Boolean automatiskInrapportering;
	private String instrument;
	private String varde1;
	private String varde2;
	private String varde3;
	private Integer status1;
	private Integer status2;
	private Integer status3;
	private String felkod1;
	private String felkod2;
	private String felkod3;
	private String fixpunkt;
	private Double maxPejlbartDjup;
	private UUID bifogadBildId;
	private LocalDateTime senasteAvlastDatum;

	private RapporteringMatningstyp() {
	}

	public static RapporteringMatningstyp fromRows(List<Map<String, Object>> rows) {
		RapporteringMatningstyp matningstyp = new RapporteringMatningstyp();

		// All rows will have the same matningstyp, so we take the first one.
		matningstyp.setMatningstyp(rows.get(0));

		// For now we only care about the latest date.
		Timestamp avlast_datum = (Timestamp) rows.get(0).get("avlast_datum");
		if (avlast_datum != null) {
			matningstyp.setSenasteAvlastDatum(avlast_datum.toLocalDateTime());
		}

		int digits = matningstyp.getDecimaler();

		matningstyp.setMatning1(rows.get(0), digits);
		if (rows.size() > 1) matningstyp.setMatning2(rows.get(1), digits);
		if (rows.size() > 2) matningstyp.setMatning3(rows.get(2), digits);

		return matningstyp;
	}

	private boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}

	private boolean isError(String felkod) {
		return !isEmpty(felkod) && !felkod.equals(Matningsfelkod.OK);
	}

	private String formatNumber(Double number, int digits) {
		Locale sv = Locale.forLanguageTag("sv");
		NumberFormat numberFormat = NumberFormat.getInstance(sv);
		numberFormat.setMaximumFractionDigits(digits);
		numberFormat.setMinimumFractionDigits(digits);
		return numberFormat.format(number);
	}

	private String getVarde(Map<String, Object> row, int digits) {
		var felkod = (String) row.get("felkod");
		if (isError(felkod)) {
			return felkod;
		} else {
			var avlast_varde = (Double) row.get("avlast_varde");
			return avlast_varde == null ? "" : formatNumber(avlast_varde, digits);
		}
	}

	private void setMatning1(Map<String, Object> row, int digits) {
		varde1 = getVarde(row, digits);
		status1 = (Integer) row.get("status");
		felkod1 = (String) row.get("felkod");
	}

	private void setMatning2(Map<String, Object> row, int digits) {
		varde2 = getVarde(row, digits);
		status2 = (Integer) row.get("status");
		felkod2 = (String) row.get("felkod");
	}

	private void setMatning3(Map<String, Object> row, int digits) {
		varde3 = getVarde(row, digits);
		status3 = (Integer) row.get("status");
		felkod3 = (String) row.get("felkod");
	}

	private void setMatningstyp(Map<String, Object> row) {
		matobjektId = (Integer) row.get("matobjekt_id");
		matningstypId = (Integer) row.get("matningstyp_id");
		matobjekt = (String) row.get("matobjekt");
		matningstyp = (String) row.get("matningstyp");
		berakningstyp = (String) row.get("berakningstyp");
		ordning = (Integer) row.get("ordning");
		lage = (String) row.get("lage");
		fastighet = (String) row.get("fastighet");
		storhet = (String) row.get("storhet");
		enhet = (String) row.get("enhet");
		decimaler = (Integer) row.get("decimaler");
		automatiskInrapportering = (Boolean) row.get("automatisk_inrapportering");
		instrument = (String) row.get("instrument");

		fixpunkt = (String) row.get("fixpunkt");
		maxPejlbartDjup = (Double) row.get("max_pejlbart_djup");

		bifogadBildId = (UUID) row.get("bifogad_bild_id");
	}

	/**
	 * Formatted version, used in excel document.
	 */
	public String getFixpunktLabel() {
		if (!isEmpty(fixpunkt)) {
			return "Fixpunkt: " + fixpunkt;
		} else {
			return "";
		}
	}

	/**
	 * Formatted version, used in excel document.
	 */
	public String getMaxPejlbartDjupLabel() {
		if (maxPejlbartDjup != null) {
			return "MPD: " + formatNumber(maxPejlbartDjup, 2) + " m";
		} else {
			return "";
		}
	}
}
