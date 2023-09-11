package adapterPatternOvning;

import java.util.Random;

public class RandomTextGenerator implements TextGenerator{

	@Override
	public void createString(int length, Logger logger) {
		// TODO Auto-generated method stub
		String result="";
		for(int i=0; i<length;i++) {
			result+=new Random().nextInt(10);
		}
		logger.logger(result);
		System.out.println(result);
	}

}
