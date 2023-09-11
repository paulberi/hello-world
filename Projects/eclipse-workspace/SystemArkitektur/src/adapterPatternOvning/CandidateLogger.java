package adapterPatternOvning;

import java.util.Date;

public class CandidateLogger implements BestLogget {

	@Override
	public void log(String str, Date date) {
		// TODO Auto-generated method stub
		System.out.println("Logged String "+str+" at date "+date);
	}

}
