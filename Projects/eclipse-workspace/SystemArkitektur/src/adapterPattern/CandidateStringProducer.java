package adapterPattern;

import java.util.Random;

public class CandidateStringProducer implements CandidateInterface{
	
	public String makeString(int length) {
		String result="";
		for(int i=0;i<length;i++) {
			result+=new Random().nextInt(10);
		}
		return result;
	}

}
