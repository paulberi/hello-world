package Lottery;

import java.util.Random;
import java.util.Scanner;

public class LotOn100Num3Times {
	public static void main(String args[]) {
		int randomNumber =0;
		int guessedNumber=0;
		int failedAttempt=0;
		
		while(failedAttempt<3){
			System.out.println("Take a guess");
			Scanner input=new Scanner(System.in);
			guessedNumber=input.nextInt();
			Random randomGenerator=new Random();
			randomNumber =randomGenerator.nextInt(100);
			if(guessedNumber==randomNumber) {
				System.out.println("You try");
			}else {
				System.out.println("You no well");
				failedAttempt=failedAttempt+1;
			}
		}
		System.out.println("Try again next time");
		
	}

}
