package gitLabAndTestsPractice;

public class Main {
	
	static StringProducer producer;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		producer= new StringProducer();
		
		String producedString=producer.generatingString(70);
		producer.reverseString(producedString);
	}

}
