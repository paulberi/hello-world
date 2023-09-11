import java.util.HashMap;

public class DecimalToRomanConverter {
    HashMap<Integer, String> mapping = new HashMap<Integer, String>();

    public DecimalToRomanConverter() {
        mapping.put(1,"I");
        mapping.put(2,"II");
        mapping.put(3,"III");
        mapping.put(4,"IV");
        mapping.put(5,"V");
        mapping.put(6,"VI");
        mapping.put(7,"VII");
        mapping.put(8,"VIII");
        mapping.put(9,"IX");
        mapping.put(10,"X");
    }

    public String getRoman(int decimal) throws NumberTooLargeException {
        if(decimal>10) {
            throw new NumberTooLargeException("Decimal number " + decimal + " too large for this method.");
        }
        return mapping.get(decimal);
    }
}
