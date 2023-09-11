package io.brains;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class MathUtilsTest {

	@Test
	void test() {
		MathUtils mathUtils= new MathUtils();
		int expected=2;
		int actual =mathUtils.add(1, 1);
		assertEquals(expected, actual);
		
		
		System.out.println("this test ran");
		//fail("Not yet implemented");
	}

}
