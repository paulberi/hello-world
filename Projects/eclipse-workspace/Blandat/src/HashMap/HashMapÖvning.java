package HashMap;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HashMapÖvning {

	public static void main(String[] args) throws InterruptedException {

		HashMap<Integer, String> map = new HashMap<Integer, String>();

		map.put(1, "Paul");
		map.put(2, "Jane");
		map.put(3, "Gigi");
		map.put(4, "Esme");

		for (Integer m : map.keySet()) {
			if (m == 2) {
				System.out.println(map.get(m));
			}
			wait(1000);
			System.out.println(m+": "+map.get(m));
		}

		//System.out.println(map);
	}
	//method to cause a delay in milliseconds
	public static void wait(int ms)
	{
	    try
	    {
	        Thread.sleep(ms);
	    }
	    catch(InterruptedException ex)
	    {
	        Thread.currentThread().interrupt();
	    }
	}
}
