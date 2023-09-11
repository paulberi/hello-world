package commandoPattern;

public class VowelsToUpperCase implements StringProcessingCommand{

	@Override
	public String process(String str) {
		// TODO Auto-generated method stub
		
		str=str.replace('a', 'A');
		str=str.replace('e', 'E');
		str=str.replace('i', 'I');
		str=str.replace('o', 'O');
		str=str.replace('u', 'U');
		return str;
	}
	
	

}
