package hashMapWithObjects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<Integer,Books> book=new HashMap<Integer,Books>();
		
		Books b1=new Books(101,"let us C","Yashwant kanetkar","BPB",8);
		Books b2=new Books(102,"Data communication","forouzan","Mc Graw Hill",4);
		Books b3=new Books(103,"Operating System","Galvin", "Wiley",6);
		
		book.put(1, b1);
		book.put(2, b2);
		book.put(3,b3);
		for(Entry<Integer, Books> m: book.entrySet()) {
			if(m.getKey()==1) {
				System.out.println(m);
				System.out.println("hello");
			}
			
			else if(m.getKey()==2) {
				System.out.println(m);
			}
			else {
				System.out.println(m);
			}
		}

	}

}
