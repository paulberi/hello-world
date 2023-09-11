package se.metria.finfo.util;

import org.junit.jupiter.api.Test;
import se.metria.finfo.data.SamfallighetTestdata;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastighetsinformationToSamfallighetEntityMapperTest {
    @Test
    void test() throws Exception {
        // Given
        var mapper = new FastighetsinformationToSamfallighetEntityMapper();
        var fastighetsinformation = XmlUtil.parseFastighetsinformation("samfallighet.xml");

        // When
        var samfallighet = mapper.map(fastighetsinformation);

        // Then
        assertEquals(SamfallighetTestdata.samfallighetEntity().toString(), samfallighet.toString());
    }
}
