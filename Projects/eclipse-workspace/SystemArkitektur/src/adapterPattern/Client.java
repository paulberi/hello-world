package adapterPattern;

public class Client {
	
	private CurrentlyUsedInterface stringProducer;
	
	public Client(CurrentlyUsedInterface stringProducer) {
		this.stringProducer=stringProducer;
	}

	/**
	 * @return the stringProducer
	 */
	public String getString() {
		return stringProducer.createString();
	}

}
