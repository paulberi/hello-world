package com.tools;

import java.util.Scanner;
import java.util.regex.Pattern;

public class CountingAndReversingWords {
	
	static Pattern p=Pattern.compile(" ");
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
		System.out.println("write a sentence of your choice");
		String str= input.nextLine();
		
		String [] str1=p.split(str);
		String[] result=spinWord(str1);
		for(int i=0; i<result.length; i++) {
			System.out.println(result[i]);
		}
		
		
			
	}

	private static String[] spinWord(String[] str1) {
		// TODO Auto-generated method stub
		String[] splitWords = str1; //check the solution
		
		for (int i=0; i<splitWords.length; i++) {
			if(splitWords[i].length()>=5) {
				splitWords[i]=reverseWord(splitWords[i]);
			}
		}
		return splitWords;
	}

	private static String reverseWord(String str) {
		// TODO Auto-generated method stub
		
	
		
		String[] temp=p.split(str);
		String rev=" ";
		for (int i=0; i<temp.length; i++) {
			if(i==temp.length-1) {
				rev=temp[i]+rev;
			}else {
				rev=" "+temp[i]+rev;
				
			}
		}
		return rev;
		
	}

}
