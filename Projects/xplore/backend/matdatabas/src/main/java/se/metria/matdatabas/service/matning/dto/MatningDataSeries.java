package se.metria.matdatabas.service.matning.dto;

import lombok.Getter;
import org.jooq.Record;
import org.jooq.Result;
import se.metria.matdatabas.db.tables.records.MatningRecord;
import se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp;
import se.metria.matdatabas.service.matning.Matningstatus;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;

@Getter
public class MatningDataSeries {
	private Integer matobjektId;
	private Integer matningstypId;
	private String matobjektNamn;
	private String matningstypNamn;
	private String storhet;
	private String enhet;
	private Short decimaler;
	private String beraknadStorhet;
	private String beraknadEnhet;
	private String berakningstyp;
	private Short graftyp;
	private ArrayList<MatningData> data = new ArrayList<>();

	@Getter
	public static class MatningData {
		private final Long id;
		private final Short status;
		private final Long avlastDatum;
		private final Double varde;
		private final String felkod;

		public MatningData(Long id, Short status, LocalDateTime avlastDatum, Double varde, String felkod) {
			this.id = id;
			this.status = status;
			ZonedDateTime zdt = avlastDatum.atZone(ZoneOffset.systemDefault());
			this.avlastDatum = zdt.toInstant().toEpochMilli();
			this.varde = varde;
			this.felkod = felkod;
		}
	}

	public MatningDataSeries(Record record) {
		this.matobjektId = record.get(MATOBJEKT.ID);
		this.matningstypId = record.get(MATNINGSTYP.ID);
		this.matobjektNamn = record.get(MATOBJEKT.NAMN);
		this.matningstypNamn = record.get(DEFINITION_MATNINGSTYP.NAMN);
		this.storhet = record.get(DEFINITION_MATNINGSTYP.STORHET);
		this.enhet = record.get(DEFINITION_MATNINGSTYP.ENHET);
		this.decimaler = record.get(MATNINGSTYP.DECIMALER);
		this.beraknadStorhet = record.get(DEFINITION_MATNINGSTYP.BERAKNAD_STORHET);
		this.beraknadEnhet = record.get(DEFINITION_MATNINGSTYP.BERAKNAD_ENHET);
		this.berakningstyp = record.get(DEFINITION_MATNINGSTYP.BERAKNINGSTYP);
		this.graftyp = record.get(DEFINITION_MATNINGSTYP.GRAFTYP);
	}

	private boolean isBeraknad() {
		return beraknadStorhet != null && beraknadEnhet != null;
	}

	private boolean isSattning() {
		return Berakningstyp.SATTNING.name().equalsIgnoreCase(berakningstyp);
	}

	public void setData(Result<MatningRecord> rows) {
		setData(rows, null);
	}

	public void setData(Result<MatningRecord> rows, Double referensValue) {
		if (rows.isEmpty()) {
			return;
		}

		if (isSattning()) {
			var rowsSattning = rows.stream()
								   .filter(r -> r.getStatus() != Matningstatus.FEL)
								   .collect(Collectors.toList());
			this.data = computeDataSattning(rowsSattning, referensValue);
		} else if (isBeraknad()) {
			this.data = computeDataBeraknad(rows);
		} else {
			this.data = computeDataAvlast(rows);
		}
	}

	private ArrayList<MatningData> computeDataSattning(List<MatningRecord> rows, Double referensValue) {
		ArrayList<MatningData> data = new ArrayList<>();

		for (var row : rows) {
			Double varde = row.getAvlastVarde();

			// If we do not have a relative value just pick the first one.
			if (referensValue == null && varde != null) {
				referensValue = varde;
			}

			if (varde == null) {
				continue;
			}

			LocalDateTime avlastDatum = row.getAvlastDatum();
			Long id = row.getId();
			Short status = row.getStatus();
			String felkod = row.getFelkod();
			data.add(new MatningData(id, status, avlastDatum, varde - referensValue, felkod));
		}

		return data;
	}

	private ArrayList<MatningData> computeDataBeraknad(Result<MatningRecord> rows) {
		ArrayList<MatningData> data = new ArrayList<>();

		for (var row : rows) {
			LocalDateTime avlastDatum = row.getAvlastDatum();
			Long id = row.getId();
			Short status = row.getStatus();
			Double varde = row.getBeraknatVarde();
			String felkod = row.getFelkod();
			data.add(new MatningData(id, status, avlastDatum, varde, felkod));
		}

		return data;
	}

	private ArrayList<MatningData> computeDataAvlast(Result<MatningRecord> rows) {
		ArrayList<MatningData> data = new ArrayList<>();

		for (var row : rows) {
			Long id = row.getId();
			Short status = row.getStatus();
			LocalDateTime avlastDatum = row.getAvlastDatum();
			Double varde = row.getAvlastVarde();
			String felkod = row.getFelkod();
			data.add(new MatningData(id, status, avlastDatum, varde, felkod));
		}

		return data;
	}

	public String getYAxisLabel() {
		if (isBeraknad()) {
			return beraknadStorhet + " (" + beraknadEnhet + ")";
		} else if (storhet != null) {
			return storhet + " (" + enhet + ")";
		} else {
			return enhet;
		}
	}
}
