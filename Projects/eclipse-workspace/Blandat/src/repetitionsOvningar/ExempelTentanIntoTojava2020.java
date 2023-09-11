package repetitionsOvningar;

import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ExempelTentanIntoTojava2020 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		boolean running=true;
		double average =0;
		String filePath="/Users/paulberinyuylukong/Desktop/paul1.txt";
		Scanner input =new Scanner(System.in);
		
		clearMemory(filePath);
		
		while(running) {
			double numbers[];
			numbers=numbersFromUser(input);
			average=calculateAverage(numbers);
			
			saveResult(average,filePath,numbers);
			
			System.out.println("medelvärdet är: "+average);
			
			System.out.println("Avsluta med 0, annars tryck valfri siffra för att fortsätta");
			if(input.nextInt()==0) {
				running=false;
			}
			
		}
		

	}

	private static void saveResult(double average, String filePath, double[] numbers) {
		// TODO Auto-generated method stub
		try {
			FileWriter fw= new FileWriter(filePath);
			for(double x:numbers) {
				fw.write(x+"\n");
			}
			fw.write("and average is: "+average);
			fw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	private static double calculateAverage(double[] numbers) {
		// TODO Auto-generated method stub
		double average=0;
		double total=0;
		int amount=numbers.length;
		
		for( double x:numbers) {
			total +=x;
		}
		average=total/amount;
	
		return average;
	}

	private static double[] numbersFromUser(Scanner input) {
		// TODO Auto-generated method stub
		boolean tryingToGetInput=true;
		int amount=0;
		
		while(tryingToGetInput) {
			System.out.println("Hur många är talen? (1-20)");
			
			try {
				amount=input.nextInt();
				
				
			}catch(InputMismatchException e) {
				System.err.println("Bara heltal tack");
				input.nextLine();
				continue;
			}
			if(amount<0 || amount>20) {
				System.out.println("Fel vid inmatning försök igen");
				continue;
			}
			tryingToGetInput=false;
		}
		double numbers[]= new double[amount];
		tryingToGetInput=true;
		
		while(tryingToGetInput) {
			System.out.println("Mata in dina tal, tryck enter efter varje inmatat tal");
			for (int i=0; i<numbers.length; i++) {
				try {
					numbers[i]=input.nextInt();
				}catch(InputMismatchException e) {
					System.err.println("Bara tal tack");
					input.nextLine();
					i--;
					continue;
				}
				
			}
			tryingToGetInput=false;
		}
		
		return numbers;
	}

	private static void clearMemory(String filePath) {
		// TODO Auto-generated method stub
		try {
			FileWriter fw= new FileWriter(filePath);
			fw.write("");
			fw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
/**
 * @param empties the file and make it ready for next input.
 * @param
 * testing how @param works
 */
