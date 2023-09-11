package com.tools;

import java.util.Scanner;

public class SumOfValues {
	public static void main(String args[]) {
		Scanner user=new Scanner(System.in);
		System.out.println("Mata in en siffra");
		int userInput=user.nextInt();
		
		int absInput=Math.abs(userInput);
		int sum=0;
		int i=0;
		
		while(absInput/Math.pow(10, i)>0) {
			sum+=calculator(userInput, i);
			i++;
			System.out.println(" sub sum is "+sum);
		}
		System.out.println("sum is "+sum);
	}

	private static int calculator(int a, int b) {
		// TODO Auto-generated method stub
		return (a%(int)Math.pow(10, b+1))/((int)Math.pow(10, b));
	}
}
