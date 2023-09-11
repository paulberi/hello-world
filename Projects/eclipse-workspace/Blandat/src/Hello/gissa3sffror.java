package Hello;

import java.util.Random;
import java.util.Scanner;

public class gissa3sffror {
	public static void main(String args[]) {
		//I am going to create two methods and try to get the second to do the work so I can 
		//execute it in the main method. wish me luck
		double guessedNumber = 0;
		int generatedNumber = 0, numberOfGuesses=0;
		while(numberOfGuesses<3) {
			
			System.out.println("pick a number");
			game(guessedNumber);
			
			//Random ran=new Random();
			//generatedNumber=ran.nextInt(100);
			generatedNumber=game();
			
			if(guessedNumber==generatedNumber) {
				System.out.println("gerated number is"+ generatedNumber+" Bra jobbat");
				
			}else {
				System.out.println("Generated Number is "+generatedNumber+" try again");
				numberOfGuesses=numberOfGuesses+1;
				
			}
			
		}
		System.out.println("Tack för idag, vi ses igen");
	}
	private static int game() {
		Random ran=new Random();
		return ran.nextInt(100);
		
	}
	private static double game(double input) {
		Scanner enter=new Scanner(System.in);
		return input=enter.nextInt();
		
	}

}
/*import java.util.Random;
import java.util.Scanner;

public class Lot2 {
	public static void main(String args[]) {
		int generatedNumber, guessedNumber = 0, NumberOfGuesses = 0;
		int a = 0;
		int b = 0;
		
		
		System.out.println("Take a guess");
		//inputFromUser(guessedNumber);
		Scanner s=new Scanner(System.in);
		guessedNumber=s.nextInt();
		Random input=new Random();
		generatedNumber=input.nextInt(100);
		if(guessedNumber==generatedNumber) {
		     System.out.println("Bra jobbat");
			
		}else {
		     System.out.println("The number does not match, you have 2 more tials");
		     System.out.println("Take your next guess");
		     //inputFromUser(guessedNumber);
		     a=s.nextInt();
		}
		if(a==generatedNumber) {
		     System.out.println("Bra jobbat");
		
		}else {
		     System.out.println("The number does not match, you have 1 more tials");
		     System.out.println("Take your next guess");
		     //inputFromUser(guessedNumber);
			 b=s.nextInt();
     	}
		if(b==generatedNumber) {
		     System.out.println("Bra jobbat");
		}else {
		     System.out.println("The number does not match, the right number is " + generatedNumber);
		     System.out.println("Thank you and hope to have you back again");
		}
			
		
	}
	/*private static int inputFromUser(int input) {
		Scanner s=new Scanner(System.in);
		return input=s.nextInt(100);
	}

}
*/
