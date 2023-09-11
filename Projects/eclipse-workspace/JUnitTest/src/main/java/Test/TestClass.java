package Test;

import org.junit.jupiter.api.DisplayName;

import TestObject.DecimalToRoman;
import TestObject.NumberTooLargeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;


public class TestClass {
	
	DecimalToRoman converter= new DecimalToRoman();
	
	@Test
	@DisplayName("Convert from Decimal to Roman")
	public void TestDecimalToRoman() throws NumberTooLargeException {
	//	assert(false);
		assertEquals("V", converter.getRoman(5));
		assertEquals("X", converter.getRoman(10));
		assertEquals("I", converter.getRoman(1));
	}
	@Test
	@DisplayName("Number too large")
	public void testNumberTooLarge() {
		assertThrows(NumberTooLargeException.class, () ->{
			converter.getRoman(8);
		});
	}

}
