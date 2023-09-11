package kattis;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OddEcho {

	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		
		int numberOfWords=in.nextInt();
		List<String> wordList=new ArrayList<>();

		wordList.clear();
		for(int i=0; i<=numberOfWords; i++) {
			String word=in.nextLine();
			wordList.add(word);
		}
		for(String word:wordList) {
			if(wordList.indexOf(word)%2!=0) {
				System.out.println(word);
			}
		}
	}
}
