package se.metria.matdatabas.restapi.export;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

public class CsvResponseWriter<T> {
	CsvMapper mapper;
	CsvSchema schema;

	public CsvResponseWriter(Class<T> source) {
		mapper = new CsvMapper();
		schema = mapper
				.schemaFor(source)
				.withColumnSeparator(';')
				.withHeader();
	}

	/**
	 * Write the CSV file.
	 *
	 * @param source       a stream of objects representing each line
	 * @param target       where to write the CSV file
	 * @param writeUtf8Bom if a UTF-8 byte-order-mark should be included. MS Excel won't recognize the encoding
	 *                     as UTF-8 if a BOM is not present.
	 */
	public void write(Stream<T> source, OutputStream target, boolean writeUtf8Bom) throws IOException {
		if (writeUtf8Bom) {
			target.write(0xEF);
			target.write(0xBB);
			target.write(0xBF);
		}

		SequenceWriter sequenceWriter = mapper.writer(schema).writeValues(target);

		// The stream might have open resources if it comes from e.g. a Spring Data Repository
		// so we make sure it gets closed.
		try (source) {
			source.forEach(value -> {
				try {
					sequenceWriter.write(value);
				} catch (IOException e) {
					throw new RuntimeException("Error writing response", e);
				}
			});
		}
	}
}
