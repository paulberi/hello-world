package kattis;

import java.util.Scanner;

public class FiftShadesOfPink {
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		
		int timesToRun=in.nextInt();
		
		int amount=0;
		for(int i=0; i<=timesToRun; i++) {
			String userInput=in.nextLine();
			if(userInput.toLowerCase().contains("rose") || userInput.toLowerCase().contains("pink")) {
				amount++;
			}
		}
		if(amount==0) {
			System.out.println("I must watch Star Wars with my daughter");
		}else {
			System.out.println(amount);
		}
	}
}
