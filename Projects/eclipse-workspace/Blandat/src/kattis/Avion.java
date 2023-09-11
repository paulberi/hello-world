package kattis;

import java.util.ArrayList;
import java.util.Scanner;

public class Avion {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> positionsList=new ArrayList<>();
		
		Scanner in=new Scanner(System.in);
		
		for(int i=0;i<5; i++) {
			String userInput=in.nextLine();
			
			if(userInput.contains("FBI")) {
				positionsList.add(String.valueOf(i+1));
			}
		}
		String result="";
		
		if(positionsList.isEmpty()) {
			System.out.println("HE GOT AWAY!");
		}else {
			for(String position :positionsList) {
				result +=position+" ";
			}
			System.out.println(result.trim());
		}

	}

}
