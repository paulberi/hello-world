package adapterPattern;

public class StringProducerAdapter implements CurrentlyUsedInterface{

	@Override
	public String createString() {
		// TODO Auto-generated method stub
		CandidateStringProducer candidateStringProducer=new CandidateStringProducer();
		return candidateStringProducer.makeString(25);
	}

}
