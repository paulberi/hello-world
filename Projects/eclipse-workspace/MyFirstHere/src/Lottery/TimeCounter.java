package Lottery;

import java.util.Scanner;

public class TimeCounter {
	public static void main(String args[]) {
		/*Number of minutes in a day =60*24=1440
		 * 
		 */
		int m=0;
		Scanner input=new Scanner(System.in);
		System.out.println("Write in number of minutes");
		
		m=input.nextInt();
	    double d=(m/60/24);
		double years=(d/365);
			
		System.out.println(m+"minutes are equivalent to " + d +"days");	
		System.out.println(d+"days are equivalent to " + years + "years");
		
	}

}
