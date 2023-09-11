package adapterPatternOvning;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RandomTextGenerator rtg=new RandomTextGenerator();
		rtg.createString(10, new CurrentLogger());
		
		//test of candidate
		RandomTextGenerator rtgTest=new RandomTextGenerator();
		rtgTest.createString(10, new LoggerAdapter());

	}

}
