package kattis;

import java.util.ArrayList;
import java.util.Scanner;

public class AutoriNames {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in=new Scanner(System.in);
		ArrayList<Character> capitals=new ArrayList<>();
		String chr=in.nextLine();
		
		for(int i=0; i<chr.length(); i++) {
			if (Character.isUpperCase(chr.charAt(i))) {
				capitals.add(chr.charAt(i));
			}
		}
		capitals.forEach(System.out::print);
	}

}
