package se.metria.matdatabas.restapi.export.pivot;

import org.jooq.Record;
import se.metria.matdatabas.common.ValueFormatter;

import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;

public class InlakagePivotRecord implements CsvPivotRecord {
	private Record record;
	private ValueFormatter formatter;

	public InlakagePivotRecord(Record record, ValueFormatter formatter) {
		this.record = record;
		this.formatter = formatter;
	}

	@Override
	public String groupByKey() {
		// The real date is LocalDateTime but for this export we want to group
		// by day, so we format it as a date and use that as the key.
		return formatter.formatDate(record.get(MATNING.AVLAST_DATUM));
	}

	@Override
	public Integer id() {
		return record.get(MATNINGSTYP.ID);
	}

	@Override
	public String empty() {
		return "";
	}

	@Override
	public String values() {
		if (record.get(MATNING.BERAKNAT_VARDE) != null) {
			return formatter.formatMatvarde(record.get(MATNING.BERAKNAT_VARDE));
		}
		return formatter.formatMatvarde(record.get(MATNING.AVLAST_VARDE));
	}

	@Override
	public String columns() {
		String storhet = record.get(DEFINITION_MATNINGSTYP.STORHET);

		String storhetStr = storhet != null ? " ("+storhet+")" : "";

		return record.get(MATOBJEKT.NAMN) + " - " + record.get(DEFINITION_MATNINGSTYP.NAMN) + storhetStr;
	}
}
