package adapterPatternOvning;

import java.util.Date;

public class LoggerAdapter implements Logger{

	@Override
	public void logger(String str) {
		// TODO Auto-generated method stub
		CandidateLogger candidateLogger= new CandidateLogger();
		candidateLogger.log(str, new Date());
	}
	

}
