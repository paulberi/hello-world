package sequenceNumberProviderLazy;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Configuration instance=new Configuration(); 
		//Configuration config=new Configuration();// instance control
		//Configuration config1=new Configuration();//instance control
		
		for(int i=0;i<10;i++) {
			int queue=Configuration.getConfig().numbers();
			//int queue1=instance.numbers();
			//System.out.println(queue);
			System.out.println(queue);
		}

	}

}
