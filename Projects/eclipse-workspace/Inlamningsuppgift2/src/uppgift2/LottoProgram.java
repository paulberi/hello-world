package uppgift2;

import java.util.Random;
import java.util.Scanner;

public class LottoProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner input= new Scanner(System.in);
		Random rnd=new Random();
		
		System.out.println("Skriv ner en två digit nummer");
		int userInput= input.nextInt();
		
		int rndGeneratedNum=rnd.nextInt((99-10)+1)+10;
		System.out.println(rndGeneratedNum);
		
		//char c=(char)userInput;
		char[] C=String.valueOf(userInput).toCharArray();
		char[] C1=String.valueOf(rndGeneratedNum).toCharArray();
		
		if (userInput==rndGeneratedNum) {
			System.out.println("Gratis, du är nu 10 000kr Rikare.");
		}
		else if(C[0]==C1[1] && C[1]==C1[0] ) {
			System.out.println("Gratis, du har vunnit 5000kr");
		}
		else if(C[0]==C1[0]||C[1]==C1[1]||C[0]==C1[1]||C[1]==C1[0]) {
			System.out.println("Gratis, du har vunnit 1000kr");
		}
		else {
			System.out.println("Sorry, lycka till för nästa gång");
		}
	}
}
		
		
		

