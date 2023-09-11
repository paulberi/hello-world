import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDecimalToRomanConverter {

    DecimalToRomanConverter converter = new DecimalToRomanConverter();

    @Test
    @DisplayName("Convert from Decimal to Roman")
    public void TestDecimalToRoman() throws NumberTooLargeException {
        assertEquals("V", converter.getRoman(5));
        assertEquals("X", converter.getRoman(10));
        assertEquals("I", converter.getRoman(1));
    }

    @Test
    @DisplayName("Number too large")
    public void TestNumberToLage() {
        assertThrows(NumberTooLargeException.class, () -> {
            converter.getRoman(11);
        });
    }
}
