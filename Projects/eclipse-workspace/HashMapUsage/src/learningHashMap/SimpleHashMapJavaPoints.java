package learningHashMap;

import java.util.HashMap;
import java.util.Map;

public class SimpleHashMapJavaPoints {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<Integer,String> map=new HashMap<Integer,String>();
		map.put(1, "Amit");
		map.put(2, "Vijay");
		map.put(3, "Rahul");
		map.put(4, "Gaurav");
		System.out.println(map);
		
		for(Map.Entry m: map.entrySet()) {
			System.out.println(m.getKey()+":"+m.getValue());
		}

	}

}
