package se.metria.markkoll.service.intrang.converters.featurestointrangset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.util.trimble.HarjeanTrimbleMotsvarighetsfilCSVReader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrimbleHarjeanCSVReaderTest {

    @BeforeEach
    void beforeEach() {
    }

    @Test
    void så_ska_data_läsas_från_csv_filen() throws IOException {

        // Given

        // When
        var rows =  HarjeanTrimbleMotsvarighetsfilCSVReader.ReadCSVFile();

        // Then
        assertNotNull(rows);
        assertEquals(268, rows.size());
    }
}
