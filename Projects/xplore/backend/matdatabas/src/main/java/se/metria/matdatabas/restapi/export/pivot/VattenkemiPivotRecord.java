package se.metria.matdatabas.restapi.export.pivot;

import org.jooq.Record;
import se.metria.matdatabas.common.ValueFormatter;

import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;

public class VattenkemiPivotRecord implements CsvPivotRecord {
	private Record record;
	private ValueFormatter formatter;

	public VattenkemiPivotRecord(Record record, ValueFormatter formatter) {
		this.record = record;
		this.formatter = formatter;

	}

	@Override
	public String groupByKey() {
		return formatter.formatDateTime(record.get(MATNING.AVLAST_DATUM));
	}

	@Override
	public Integer id() {
		return record.get(MATNINGSTYP.ID);
	}

	/**
	 * Used to write values when there is no record for this row
	 * @return three empty values
	 */
	@Override
	public String empty() {
		return ";;";
	}

	@Override
	public String values() {
		return String.format("%s;%s;%s",
				formatter.formatDetektionsomrade(record.get(MATNING.INOM_DETEKTIONSOMRADE)),
				formatter.formatMatvarde(record.get(MATNING.AVLAST_VARDE)),
				record.get(MATNINGSTYP.ENHET));
	}

	@Override
	public String columns() {
		return record.get(MATOBJEKT.NAMN) + " - " + record.get(DEFINITION_MATNINGSTYP.NAMN) + ";;";
	}
}
