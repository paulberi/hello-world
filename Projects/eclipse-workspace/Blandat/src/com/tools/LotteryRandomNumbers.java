package com.tools;

import java.util.Random;
import java.util.Scanner;

public class LotteryRandomNumbers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random input = new Random();
		Scanner in = new Scanner(System.in);

		int choice = 0;
		while (choice < 3) {
			System.out.println("choose a number between 1 and 100");
			int userInput = in.nextInt();
			int cpuInput = input.nextInt(100) + 1;
			System.out.println("cpu " + cpuInput);

			if (userInput == cpuInput) {
				System.out.println("bra jobbat");

			} else {
				System.out.println("du är dumt");
				// System.out.println("choose a number between 1 and 100");
				// userInput=in.nextInt();

			}

			choice++;

		}
		System.out.println(choice + "too bad");
	}

}
