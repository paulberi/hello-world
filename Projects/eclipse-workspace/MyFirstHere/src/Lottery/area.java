package Lottery;

import java.util.Scanner;

public class area {

	public static void main(String args[]) {
		//arean för en fyrkant är a*a om varje sida har ländgen a
		//omkretsen är a+a+a+a eller 4a
		double w=0;double y = 0; double area;
		double omkretsen;
			System.out.println("Räkna arean och omkretsen för en fyrkant");
			System.out.println("Mata in följande data");
			Scanner input= new Scanner(System.in);
			System.out.println("Length = ");
			w=input.nextInt();
			System.out.println("width = ");
			y=input.nextInt();
			area=square(w,y);
			int x = 0;
			int z = 0;
			omkretsen=square(w,w,y,y);
			System.out.println(area);
			System.out.println(omkretsen);
	
		}
		
	private static double square(double w, double y) {
		return w*y;
	}
	private static double square(double a, double b, double y, double y2) {
		return (a+a+b+b);
		
	}

}
