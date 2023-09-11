package Lottery;

import java.util.Scanner;

public class Tursd17 {
	public static void main(String args[]) {
		System.out.println("Write a number of your choice");
		Scanner input=new Scanner(System.in);
		int user=input.nextInt();
		if (user%2==0) {
			System.out.println("the number is even");
		}else {
			System.out.println("its an uneven number");
			
		}
	}

}
