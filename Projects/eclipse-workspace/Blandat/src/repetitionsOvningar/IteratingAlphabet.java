package repetitionsOvningar;

import java.util.Scanner;

public class IteratingAlphabet {
	
	public static void main(String [] args) {
		
		String hello="how many people are here today";
		
		
		for(char c='a'; c<='z'; c++) {
			int ch = 0;
			for(int i=0; i<hello.length();i++) {
				char h=hello.charAt(i);
				if(c==h) {
					ch++;
				}
			}
			if(ch>0) {
				System.out.println(c +" finns " +ch+" gånger");
			}
		}
		
		//age of moose
		Scanner input= new Scanner(System.in);
		System.out.println(" write in the r value, from 0 to 20");
		int r=input.nextInt();
		while(r<0 || r>20) {
			System.out.println("value must be between 0 and 20 try again");
			r=input.nextInt();
		}
		
		System.out.println(" write in the l value from 0 to 20");
		int l=input.nextInt();
		while(l<0 || l>20) {
			System.out.println(" value must be between 0 and 20 try again");
			l=input.nextInt();
		}
		System.out.println("for the l and r values you have submitted the values bellow");
		System.out.println(l+"  "+r);
		
		if(r==l && l!=0 && r!=0) {
			System.out.println("Even "+ r);
		}
		if(r<l) {
			System.out.println("Odd "+l);
		}
		if(l<r) {
			System.out.println("Odd "+r);
		}
		if(l==0 && r==0) {
			System.out.println("Not a moose");
		}

	}

}
