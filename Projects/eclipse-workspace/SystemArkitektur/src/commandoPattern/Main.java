package commandoPattern;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		StringProcessingPipeline spp=new StringProcessingPipeline();
		spp.addCommand(new VowelsToUpperCase());
		spp.addCommand(new VowelToWhiteSpace());
		spp.addCommand(new WhiteSpaceRemoval());
		
		String str="we are supposed to be friends";
		str=spp.execute(str);
		System.out.println(str);

	}

}
