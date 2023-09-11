package javaTentan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class miniRaknare {

	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * miniräknare Jag ska använda if sats för uppgiften
		 */
		boolean hello = true;

		while (hello) {
			System.out.println("JAVA MINIRÄKNARE");

			double svar = 0;

			double[] tal; // jag gjörde användars input till en array för att kunna ta in mer en två tal

			tal = userInput();

			System.out.println("mata in en tecken från listan \n +,-,*,/,s,c,t,q");
			// s= sin, c =cos, t =tan, q= square
			// if sats för att räkna efter val av tecken

			char tecken = input.next().charAt(0);
			for (int i = 0; i < tal.length; i++) {
				if (tecken == ('+')) {
					svar = svar + tal[i];
				} else if (tecken == ('-')) {

					svar = tal[1] - tal[0];
				} else if (tecken == '*') {
					svar = tal[0] * tal[1];
				} else if (tecken == '/') {
					svar = tal[1] / tal[0];
				} else if (tecken == 's') {
					svar = Math.sin(tal[i]);
				} else if (tecken == 's') {
					svar = Math.cos(tal[0]);
				} else if (tecken == 's') {
					svar = Math.tan(tal[0]);
				} else if (tecken == 'q') {
					svar = tal[i] * tal[0];
				}
			}
			System.out.println(svar);

			String origin = ("/Users/paulberinyuylukong/Desktop/paul1.txt");
			/**
			 * file path där svaret ska lagras
			 */
			deleteMemory(origin); // här kallar vi method för att rensa origin(file path)
			sparaSvar(svar, origin); // här kallar vi metod för att skriva i file path
			System.out.println("skriv 1 för att avsluta och annan knapp för att fortsätta");
			int avsluta =input.nextInt();
			if(avsluta==1) {
				hello=false;
			}else {
				System.out.println("tack att du vill räkna vidare");
			}
		}
	}

	private static void sparaSvar(double svar, String origin) {
		// TODO Auto-generated method stub
		/*
		 * metod för att spara svar i filen
		 */
		try {
			FileWriter w = new FileWriter(origin);

			w.write("svar = "+svar);
			w.close();
		} catch (IOException e) {
			System.out.println(e);
			;
		}

	}

	private static void deleteMemory(String origin) {
		// TODO Auto-generated method stub

		/*
		 * metod för att rensa filen där vi ska lagra våras värde
		 */
		try {
			FileWriter fw = new FileWriter(origin);
			fw.write("");
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
			;
		}

	}

	private static double[] userInput() {
		// TODO Auto-generated method stub

		/*
		 * här tar vi in infor från användaren
		 */
		boolean input1 = true;
		double user = 0;
		int length = 0;

		while (input1) {
			System.out.println("Hur många tal \nNB Fler om du vill sumera \n"
					+ "NB bara två tal om multiplicera, dividera och göra en subtraction\n"
					+ "NB bara en tal om du vill kola sin, tan och cos sam sqaure");

			try {
				length = input.nextInt();

			} catch (InputMismatchException e) {
				System.err.println("Bara heltal tack");
				input.nextLine();
				continue;
			}
			input1 = false;
		}
		double number[] = new double[length];
		input1 = true;

		while (input1) {
			System.out.println("Mata in taler som motsvarar antanlet du ange längre up");
			for (int i = 0; i < number.length; i++) { // array som mojligt gör det att tar in mer än en värde
				try {
					number[i] = input.nextDouble();
				} catch (InputMismatchException e) {
					System.err.println("Bara tal tack");
					input.nextLine();
					i--;
					continue;
				}

			}
			input1 = false;
		}

		return number;
	}
}
