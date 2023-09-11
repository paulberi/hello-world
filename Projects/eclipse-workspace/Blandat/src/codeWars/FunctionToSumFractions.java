package codeWars;

import java.util.Scanner;

public class FunctionToSumFractions {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		xxoo();
	
	}

	private static void xxoo() {
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
		System.out.println("make a sentence");
		String str= input.nextLine();
		int x=0, o=0;
		str=str.toLowerCase();
		boolean condition=true;
		for(int i=0; i<str.length();i++) {
			char ch=str.charAt(i);
			if(ch=='o') {
				o++;
			}else if(ch=='x') {
				x++;
			}
			if(x==o) {
				condition=true;
			}else {
				condition=false;
			}
		}
		System.out.println(condition);
	}

}
