package se.metria.matdatabas.restapi.export.pivot;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CsvPivotResponseWriter {
	private Charset charset = StandardCharsets.UTF_8;
	private String firstColumnName;

	public CsvPivotResponseWriter(String firstColumnName) {
		this.firstColumnName = firstColumnName;
	}

	/**
	 * @param columns      defines the set of columns (each record can produce multiple columns)
	 * @param data         data that will be grouped by date, where each date will correspond to a row
	 * @param target       where to write the CSV file
	 * @param writeUtf8Bom if a UTF-8 byte-order-mark should be included. MS Excel won't recognize the encoding
	 *                     as UTF-8 if a BOM is not present.
	 */
	public void write(List<? extends CsvPivotRecord> columns, List<? extends CsvPivotRecord> data, OutputStream target, boolean writeUtf8Bom) throws IOException {
		if (writeUtf8Bom) {
			target.write(0xEF);
			target.write(0xBB);
			target.write(0xBF);
		}

		writeColumns(columns, target);
		writeData(columns, data, target);
	}

	private void writeColumns(List<? extends CsvPivotRecord> columns, OutputStream target) throws IOException {
		write(firstColumnName, target);

		// Each record will provide 1 to n columns.
		for (CsvPivotRecord c : columns) {
			write(";", target);
			write(c.columns(), target);
		}

		write("\n", target);
	}

	/**
	 * The data will be group by the groupByKey. It must be sorted by the groupByKey.
	 * It will then be written to the stream in the order specified by the columns argument.
	 */
	private void writeData(List<? extends CsvPivotRecord> columns, List<? extends CsvPivotRecord> data, OutputStream target) throws IOException {
		// Group data by date and ID.
		Object groupByKey = null;
		Map<Object, Map<Object, CsvPivotRecord>> pivotTable = new TreeMap<>();
		for (CsvPivotRecord record : data) {
			Object currGroupByKey = record.groupByKey();
			Object recordId = record.id();

			if (!currGroupByKey.equals(groupByKey)) {
				groupByKey = currGroupByKey;
				pivotTable.put(groupByKey, new HashMap<>());
			}

			pivotTable.get(groupByKey).put(recordId, record);
		}

		// Write each row.
		// Each row has a key column and a set of n columns per record for that key.
		for (Object key : pivotTable.keySet()) {
			write(key.toString(), target);

			for (CsvPivotRecord columnRecord : columns) {
				write(";", target);
				Object id = columnRecord.id();
				var dataRecord = pivotTable.get(key).get(id);
				if (dataRecord == null) {
					write(columnRecord.empty(), target);
				} else {
					write(dataRecord.values(), target);
				}
			}

			write("\n", target);
		}
	}

	private void write(String s, OutputStream target) throws IOException {
		target.write(s.getBytes(charset));
	}
}
