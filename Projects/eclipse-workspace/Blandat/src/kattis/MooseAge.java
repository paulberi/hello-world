package kattis;

import java.util.Scanner;

public class MooseAge {
	public static void main(String [] args) {
		Scanner input= new Scanner(System.in);
		int r=input.nextInt();
		int l=input.nextInt();
		
		if(r==l && l!=0 && r!=0) {
			System.out.println("Even "+ r*2);
		}
		if(r<l) {
			System.out.println("Odd "+l*2);
		}
		if(l<r) {
			System.out.println("Odd "+r*2);
		}
		if(l==0 && r==0) {
			System.out.println("Not a moose");
		}
	}
}
