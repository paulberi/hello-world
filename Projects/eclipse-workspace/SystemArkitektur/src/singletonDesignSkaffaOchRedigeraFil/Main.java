package singletonDesignSkaffaOchRedigeraFil;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration configuration=new Configuration();
		configuration.save("name","tom");
		System.out.println(configuration.read("name"));
		//firstOvningOmEnInstance instance=new firstOvningOmEnInstance();
		//instance.getInstance().read("Beri");
		//System.out.println(instance.getInstance().read("Beri"));
	//	arrayListVersion instance1=new arrayListVersion();
		//instance1.saveConfiguration();
		//String ok=instance1.read("Name Paul");
	//	System.out.println(ok);

	}

}
