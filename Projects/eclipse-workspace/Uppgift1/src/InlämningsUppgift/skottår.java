package InlämningsUppgift;

import java.util.Scanner;

public class skottår {
	
	public static void main(String args[]) {
		
		System.out.println("Mata in et år för att kolla om den är ett skottår");
		
		Scanner input = new Scanner(System.in);
		
		int chosenYear = input.nextInt();
		
		if(chosenYear%4!=0) 
			System.out.println(chosenYear+" är inte ett skottår");
		    else
		    if(chosenYear%100!=0) 
			System.out.println(chosenYear +" är ett skottår");
		    else
		    if(chosenYear%400!=0) 
			System.out.println(chosenYear+" är inte ett skottår");
		    else
		    System.out.println(chosenYear +" är ett skottår");
		
	
	}

}
