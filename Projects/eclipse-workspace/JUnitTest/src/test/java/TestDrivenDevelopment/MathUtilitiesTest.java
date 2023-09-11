package TestDrivenDevelopment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.brains.MathUtils;
/**
 * To reduce dependency between tests, all variables that belong to a method
 * should be declared in that method
 * */
class MathUtilitiesTest {
	//MathUtilities mathUtils= new MathUtilities();
	//do not declare variables here or instantiate classes here
	// do that in the method that needs them to reduce dependency
	
	//assetEquals
	@Test
	void test() {
		MathUtilities mathUtils= new MathUtilities();
		assertEquals(314.1592653589793, mathUtils.computeCircleArea(10),"Should return right circle area");
		//fail("Not yet implemented");
	}
	
	//assertThrows
	//the best way to assure that you get a throw exception is to surround in a try catch block
	@Test
	void testDivide() {
		MathUtilities mathUtils= new MathUtilities();
		assertThrows(NullPointerException.class, () ->mathUtils.divide(1, 0), "divide by zero should throw");
		
		
	}

}
