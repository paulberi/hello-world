package adapterPatternOvning;

import java.util.Random;

public class CurrentLogger implements Logger{

	@Override
	public void logger(String str) {
		// TODO Auto-generated method stub
		System.out.println("Logged string "+str);
	}
}
