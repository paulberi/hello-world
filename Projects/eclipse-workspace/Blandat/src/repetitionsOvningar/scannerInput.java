package repetitionsOvningar;

import java.text.DecimalFormat;
import java.util.Scanner;

public class scannerInput {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		Scanner input=new Scanner(System.in);
		
		System.out.println("Matta in två siffror");
		double userInput= input.nextDouble();
		double userInput2= input.nextInt();
		
		double average= userInput/userInput2;
		
		System.out.printf("Du skrev "+userInput +" och "+userInput2+ " their sum is %.2f",average);
		}catch(Exception e) {
			System.out.println(e);
		}
	}

}
