package com.tools;

import java.util.Scanner;

public class SortIfNegativePostive {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		System.out.println("write down a number either + or-:");
		int user = input.nextInt();
		if (user < 0) {
			System.out.println("its a negative number");
		} else {
			System.out.println("its a positive number");
		}
	}

}
