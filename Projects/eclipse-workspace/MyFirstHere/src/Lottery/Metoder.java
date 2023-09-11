package Lottery;

import java.util.Scanner;

public class Metoder {
	public static void main(String[] args) {
		int fInput=0;
		int sInput=0;
		int maximum=0;
		while(fInput<2){
			System.out.println("Mata in ett nummer");
			Scanner input=new Scanner(System.in);
			System.out.println("sInput är ");
			sInput=input.nextInt();
			System.out.println("fInput är ");
			fInput=input.nextInt();
			maximum =max(sInput,fInput);
			System.out.println("The maximum number is " +maximum);
		}
	}
	private static int max(int a, int b) {
		int c;
		if (a>b) {
			c=a;// man jan bara skriva return a och sen return b;
		}else {
			c=b;
		}
		return c;
	}	
	}



