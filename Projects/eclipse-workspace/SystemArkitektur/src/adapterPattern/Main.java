package adapterPattern;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringProducer stringProducer=new StringProducer();
		Client client=new Client(stringProducer);
		System.out.println(client.getString());
		
		//test of new StringProducer (adapted)
		StringProducerAdapter stringProducerAdapter=new StringProducerAdapter();
		Client clientTest =new Client(stringProducerAdapter);
		System.out.println(clientTest.getString());
	}

}
