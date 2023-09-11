package kattis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class RollCall1 {
	String firstname;
	String secondname;
	public RollCall1(String firstname,String secondname) {
		this.firstname=firstname;
		this.secondname=secondname;
	}
	public static void main(String[] args) {
		
		Scanner in=new Scanner(System.in);
		List<String> classList=new ArrayList<>();
		List<RollCall1> classList1=new ArrayList<>();
//		for(int i=0; i<6; i++) {
//			String userInput=in.nextLine();
//			classList.add(userInput);
//		}
		while(in.hasNext()) {
			String userInput=in.nextLine();
			classList.add(userInput);
		}
		for(String names:classList) {
			String[] splitStr=names.split("\\s+");
			for(int j=0; j<1; j++) {
				RollCall1 name=new RollCall1(splitStr[0],splitStr[1]);
				classList1.add(name);
			}
		}
		
		for(int i=0; i<classList1.size();i++) {
			for(int j=0; j<classList1.size(); j++) {
				Collections.sort(classList1, new Comparator<Object>(){

					@Override
					public int compare(Object o1, Object o2) {
						RollCall1 r1=(RollCall1) o1;
						RollCall1 r2=(RollCall1) o2;
						int res=r1.secondname.compareToIgnoreCase(r2.secondname);
						if(res!=0) {
							return res;
						}else {
							return r1.firstname.compareToIgnoreCase(r2.firstname);
						}
					}
					
				});
			}
		}
		int firstNameCount;
		for(RollCall1 name:classList1) {
			firstNameCount=0;
			for(RollCall1 name1:classList1) {
				if(name1.firstname.contains(name.firstname)) {
					firstNameCount++;
				}
			}if(firstNameCount>1) {
				System.out.println(name.firstname+" "+name.secondname);
			}else {
				System.out.println(name.firstname);
			}
		}
		in.close();

		
//		int firstNameCount;
//		for(String name:classList) {
//			firstNameCount=0;
//			int i = name.indexOf(' ');
//			String firstWord = name.substring(0, i);
//			String rest = name.substring(i);
//			for(String name1:classList) {
//				if(name1.contains(firstWord)) {
//					firstNameCount++;
//				}
//			}if(firstNameCount>1) {
//				System.out.println(firstWord+" "+rest);
//			}else {
//				System.out.println(firstWord);
//			}
//		}

//		Collections.sort(classList, (o1,o2) ->  o1.substring(o1.lastIndexOf(" ")+1)
//				.compareTo(o2.substring(o2.lastIndexOf(" ")+1)));
	}

}
