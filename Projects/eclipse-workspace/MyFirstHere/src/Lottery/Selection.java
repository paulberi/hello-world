package Lottery;

import java.util.Scanner;

public class Selection {
	public static void main(String args[]) {
		
		int x=0;
		int y=0;
		while(y<8) {
		System.out.println("Welcome to Mygames");
		System.out.println("Choose a number");
		
		Scanner input = new Scanner(System.in);
		double z = Math.random()*100-0;
		x=input.nextInt();
		if(z==x) {
			System.out.println("Good Job");
		}
		else {
			System.out.print("Try again next time");
			System.out.println("the answer was " + z);
			y=y+1;
		}
		
		}
		System.out.println("To play again, insert coin");
	}
	

}
