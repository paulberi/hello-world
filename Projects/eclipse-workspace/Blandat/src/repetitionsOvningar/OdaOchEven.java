package repetitionsOvningar;

import java.util.Scanner;

public class OdaOchEven {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//oddAndEven(args);
		oddEvenWithTernary(args);

	}

	public static void oddAndEven(String[] args) {
		// TODO Auto-generated method stub
		Scanner input= new Scanner(System.in);
		System.out.println("mata in ett tal");
		int tal=input.nextInt();
		if(tal%2==0) {
			System.out.println("det är en jämt tal");
		}else {
			System.out.println("det är en udda tal");
		}
		
		
	}
	public static void oddEvenWithTernary(String[] args) {
		
		Scanner input= new Scanner(System.in);
		
		System.out.println("mata in en tal");
		int tal=input.nextInt();
		
		String result=(tal%2==0)?"even":"odd";
		System.out.println("det är en "+result+ " tal");
	
	}
	

}
