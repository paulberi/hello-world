package commandoPattern;

public class VowelToWhiteSpace implements StringProcessingCommand{

	@Override
	public String process(String str) {
		// TODO Auto-generated method stub
		str=str.replace('a', ' ');
		str=str.replace('e', ' ');
		str=str.replace('i', ' ');
		str=str.replace('o', ' ');
		str=str.replace('u', ' ');
		return str;
	}
	

}
