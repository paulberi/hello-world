package com.tools;

import java.util.Scanner;

public class Switch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("program to allocate grades to exams scores");
		Scanner input = new Scanner(System.in);
		System.out.println("what is your exam score:");
		int score = input.nextInt();

		switch (score / 10) {

		case 9:
			System.out.println("you have an A");
			break;
		case 8:
			System.out.println("you have an B");
			break;
		case 7:
			System.out.println("you have an C");
			break;
		case 6:
			System.out.println("you have an D");
			break;
		default:
			System.out.println("you have an F");
			break;
		}
	}

}
