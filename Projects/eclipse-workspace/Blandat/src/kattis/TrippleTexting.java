package kattis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TrippleTexting {

	public static void main(String[] args) {
		Scanner in= new Scanner(System.in);
		
		ArrayList<String> splitWords=new ArrayList<>();
		String userInput= in.nextLine();
		
		int wordLength=userInput.length()/3;
		
		String firstWord=userInput.substring(0,wordLength);
		String secondWord=userInput.substring(wordLength,wordLength*2);
		String thirdWord=userInput.substring(wordLength*2,wordLength*3);
		splitWords.add(firstWord);
		splitWords.add(secondWord);
		splitWords.add(thirdWord);
		
		for(String word:splitWords) {
			int freq= Collections.frequency(splitWords, word);
			if(freq>1) {
				System.out.println(word);
				return;
			}
		}
		
	}
}
