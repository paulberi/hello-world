package kattis;

import java.util.ArrayList;
import java.util.Scanner;

public class JumboJavelin {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in=new Scanner(System.in);
		ArrayList<Integer> lengths=new ArrayList<>();
		int numberOfRods=in.nextInt();
		for(int i=0; i<numberOfRods; i++) {
			int lengthOfRod=in.nextInt();
			lengths.add(lengthOfRod);
		}
		int results=0;
		for(Integer one:lengths) {
			if(results==0) {
				results +=one;
			}else {
				results +=one-1;
			}
			
		}
		System.out.println(results);
	}

}
