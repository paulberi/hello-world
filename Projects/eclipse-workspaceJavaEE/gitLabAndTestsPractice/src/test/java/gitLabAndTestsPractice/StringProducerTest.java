package gitLabAndTestsPractice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StringProducer tests")
public class StringProducerTest {

	private StringProducer producer = new StringProducer();
	
	@Test
    void generatingString() {
		String string=producer.generatingString(6);
		long totalCharacters = string.chars().filter(ch -> ch != ' ').count();
		
        assertEquals(totalCharacters, 6);
    }
	
	@Test
	void reverseString() {
		String string="Hello";
		String string1= producer.reverseString(string);
		String string2="olleH";
		assertEquals(string1,string2);
		
	}
	
	
	
}
