import java.util.Random;
import java.util.Scanner;

public class Lot2 {
	public static void main(String args[]) {
		int generatedNumber = 0, guessedNumber = 0, NumberOfGuesses = 0;
		int a = 0;
		int b = 0;
		
		
		System.out.println("Take a guess");
		//inputFromUser(guessedNumber);
		inputFromUser(generatedNumber);
		Random input=new Random();
		generatedNumber=input.nextInt(100);
		if(guessedNumber==generatedNumber) {
		     System.out.println("Bra jobbat");
			
		}else {
		     System.out.println("The number does not match, you have 2 more trials");
		     System.out.println("Take your next guess");
		     //inputFromUser(guessedNumber);
		     inputFromUser(a);//how can i just loop it and not have to import it each time
		}
		if(a==generatedNumber) {
		     System.out.println("Bra jobbat");
		
		}else {
		     System.out.println("The number does not match, you have 1 more trials");
		     System.out.println("Take your next guess");
		     //inputFromUser(guessedNumber);
		     inputFromUser(b);
     	}
		if(b==generatedNumber) {
		     System.out.println("Bra jobbat");
		}else {
		     System.out.println("The number does not match, the right number is " + generatedNumber);
		     System.out.println("Thank you and hope to have you back again");
		}
			
		
	}
	private static int inputFromUser(int input) {
		Scanner s=new Scanner(System.in);
		return input=s.nextInt();
	}

}
