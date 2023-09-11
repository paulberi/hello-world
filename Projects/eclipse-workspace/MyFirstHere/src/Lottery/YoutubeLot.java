package Lottery;

import java.util.Random;
import java.util.Scanner;

public class YoutubeLot {
	public static void main(String args[]) {
		int x=0;
		int y=0;
		int z=0;
		System.out.println("write a number");
		Scanner input=new Scanner(System.in);
		Random rand=new Random();
		y=rand.nextInt(100);
		while(z<4) {
			x=input.nextInt();
			if(x==y) {
				System.out.println("you are the man");
				
			}else {
				System.out.println("the right number was "+ y);
				System.out.println("try again");
				z=z+1;
			}
		}
		System.out.println("Welcome to new game");
}
}