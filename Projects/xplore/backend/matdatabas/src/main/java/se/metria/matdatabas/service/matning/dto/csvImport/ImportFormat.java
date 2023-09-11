package se.metria.matdatabas.service.matning.dto.csvImport;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a mapping from rows in a CSV file to an instance of ImportMatning.
 */
@EqualsAndHashCode
public class ImportFormat {

    @Getter
    @AllArgsConstructor
    private class CsvMapping {
        static final boolean REQUIRED = true;
        static final boolean OPTIONAL = false;

        private String header;
        private MappingFunction action;
        private boolean isRequired;
    }

    private List<CsvMapping> mappings;

    public static final ImportFormat STANDARD = formatStandard();
    public static final ImportFormat INSTRUMENT = formatInstrument();

    public ImportFormat() {
        mappings = new ArrayList<>();
    }

    public void addMappingRequired(String header, MappingFunction mapping) {
        mappings.add(new CsvMapping(header, mapping, CsvMapping.REQUIRED));
    }

    public void addMappingOptional(String header, MappingFunction mapping) {
        mappings.add(new CsvMapping(header, mapping, CsvMapping.OPTIONAL));
    }

    public List<ImportMatning> parse(InputStream stream) throws IllegalArgumentException, IOException {
        skipByteOrderMark(stream);

        var csvRecords = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withFirstRecordAsHeader()
                .withTrim()
                .parse(new InputStreamReader(stream, StandardCharsets.UTF_8.name()))
                .getRecords();

        validateHeaders(new ArrayList<>(csvRecords.get(0).toMap().keySet()));

        return csvRecords.stream()
                         .map(this::parseSingleRecord)
                         .collect(Collectors.toList());
    }

    private List<String> getAllHeaders() {
        return mappings.stream()
                .map(m -> m.getHeader())
                .collect(Collectors.toList());
    }

    private List<String> getRequiredHeaders() {
        return mappings.stream()
                .filter(m -> m.isRequired())
                .map(m -> m.getHeader())
                .collect(Collectors.toList());
    }

    private void skipByteOrderMark(InputStream stream) throws IOException {
        if (!skipByteOrderMarkUTF8(stream)) {
            skipByteOrderMarkUTF16(stream);
        }
    }

    private boolean skipByteOrderMarkUTF16(InputStream stream) throws IOException {
        stream.mark(2);

        byte b1 = (byte)stream.read();
        byte b2 = (byte)stream.read();

        if (b1 == (byte)0xFE && b2 == (byte)0xFF) {
            return true;
        } else {
            stream.reset();
            return false;
        }
    }

    private boolean skipByteOrderMarkUTF8(InputStream stream) throws IOException {
        stream.mark(3);

        byte b1 = (byte)stream.read();
        byte b2 = (byte)stream.read();
        byte b3 = (byte)stream.read();

        if (b1 == (byte)0xEF && b2 == (byte)0xBB && b3 == (byte)0xBF) {
            return true;
        } else {
            stream.reset();
            return false;
        }
    }

    private ImportMatning parseSingleRecord(CSVRecord record) throws IllegalArgumentException {
        var importMatning = ImportMatning.builder()
                .importFel(new ArrayList<>())
                .build();

        for (var mapping: this.mappings) {
            var header = mapping.getHeader();
            try {
                var value = record.get(header);
                mapping.getAction().map(importMatning, value);
            } catch (IllegalArgumentException e) {
                if (mapping.isRequired()) {
                    throw new IllegalArgumentException("Missing required field: " + mapping.getHeader());
                }
            }
        }

        return importMatning;
    }

    private void validateHeaders(List<String> fileHeaders) throws IllegalArgumentException {
        String errorMessage = "";

        var illegalHeaders = fileHeaders.stream()
                                        .filter(header -> !getAllHeaders().contains(header))
                                        .collect(Collectors.toList());
        if (illegalHeaders.size() > 0) {
            errorMessage += "Ogiltiga rubriker: " + String.join(", ", illegalHeaders);
        }

        var missingHeaders = getRequiredHeaders().stream()
                                                 .filter(header -> !fileHeaders.contains(header))
                                                 .collect(Collectors.toList());
        if (missingHeaders.size() > 0) {
            if (!errorMessage.isEmpty()) {
                errorMessage += "\n";
            }
            errorMessage += "Saknar nödvändiga rubriker: " + String.join(", ", missingHeaders);
        }

        if (missingHeaders.size() > 0 || illegalHeaders.size() > 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static ImportFormat formatStandard() {
        var format = new ImportFormat();

        format.addMappingOptional("Enhet (uppmätt värde)",      (matning, val) -> matning.setEnhetAvlast(val));
        format.addMappingOptional("Enhet (beräknat värde)",     (matning, val) -> matning.setEnhetBeraknat(val));
        format.addMappingRequired("Felkod",                     (matning, val) -> matning.setFelkod(val));
        format.addMappingRequired("Kommentar",                  (matning, val) -> matning.setKommentar(val));
        format.addMappingRequired("Mätningstyp",                (matning, val) -> matning.setMatningstyp(val));
        format.addMappingOptional("Storhet",                    (matning, val) -> matning.setStorhet(val));
        format.addMappingRequired("Datum och tid",              (matning, val) -> matning.setAvlastDatum(val));
        format.addMappingOptional("Uppmätt värde",              (matning, val) -> matning.setAvlastVarde(val));
        format.addMappingOptional("Beräknat värde",             (matning, val) -> matning.setBeraknatVarde(val));
        format.addMappingRequired("Mätningstyp",                (matning, val) -> matning.setMatningstyp(val));
        format.addMappingRequired("Mätobjekt",                  (matning, val) -> matning.setMatobjekt(val));
        format.addMappingRequired("Över/under detektionsgräns", (matning, val) -> matning.setInomDetektionsomrade(val));

        return format;
    }

    private static ImportFormat formatInstrument() {
        var format = new ImportFormat();

        format.addMappingRequired("Identitet",              (matning, val) -> matning.setInstrument(val));
        format.addMappingRequired("Datum och tid",          (matning, val) -> matning.setAvlastDatum(val));
        format.addMappingOptional("Uppmätt värde",          (matning, val) -> matning.setAvlastVarde(val));
        format.addMappingOptional("Beräknat värde",         (matning, val) -> matning.setBeraknatVarde(val));
        format.addMappingRequired("Felkod",                 (matning, val) -> matning.setFelkod(val));
        format.addMappingRequired("Kommentar",              (matning, val) -> matning.setKommentar(val));

        return format;
    }
}
