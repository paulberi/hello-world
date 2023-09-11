package commandoPattern;

public class WhiteSpaceRemoval implements StringProcessingCommand{

	@Override
	public String process(String str) {
		// TODO Auto-generated method stub
		String str1=str.replaceAll("\\s", "");
		return str1;
	}

}
