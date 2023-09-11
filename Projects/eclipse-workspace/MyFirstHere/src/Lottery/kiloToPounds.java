package Lottery;

import java.util.Scanner;

public class kiloToPounds {
	public static void main(String args[]) {
		double kilo;
		double pounds;
		System.out.println("mata in en siffra in kg");
		Scanner input= new Scanner(System.in);
	    kilo=input.nextDouble();
		pounds = paul(kilo);
		System.out.println("its worth "+ pounds+ "pounds");
			    

			}
	
	private static double paul(double kg) {
		double pound1=kg*2.204;
		return pound1;
		
	}
	public static void area(String args[]) {
		//arean för en fyrkant är a*a om varje sida har ländgen a
		//omkretsen är a+a+a+a eller 4a
		int w, x = 0, y, z = 0, area, omkretsen;
		System.out.println("Räkna arean och omkretsen för en fyrkant");
		System.out.println("Mata in följande data");
		Scanner input= new Scanner(System.in);
		System.out.println("Length = ");
		w=input.nextInt();
		w=x;
		System.out.println("width = ");
		y=input.nextInt();
		y=z;
		area=square(w,y);
		omkretsen=square(w,x,y,z);
		System.out.println("Arean = length*width = "+w+"*"+y+"="+area);
		
		
	}
	private static int square(int a, int b) {
		return a*b;
	}
	private static int square(int a, int b, int c, int d) {
		return a+b+c+d;
		
	}
}