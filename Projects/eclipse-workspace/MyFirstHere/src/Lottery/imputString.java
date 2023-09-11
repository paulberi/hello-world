package Lottery;

import java.util.Scanner;

public class imputString {
	public static void main(String args[]) {
		
		
		int input = 0;
		int reverse = 0;
		System.out.println("Mata in en siffra");
		Scanner in=new Scanner(System.in);
		input= in.nextInt();
		
		while(input!=0) {
			
			reverse=reverse*10;
			reverse=reverse+input%10;
			input=input/10;
			
		}
		System.out.println("reverse of "+input +" is "+reverse);
		
		/*String input ="llew eb llahs lla";
		char[] temparray= input.toCharArray();
		int left, right=0;
		right=temparray.length-1;
		for(left=0; left<right; left++, right--) {
			char temp = temparray[left];
			temparray[left]=temparray[right];
			temparray[right]=temp;
		}
		for(char c:temparray)
			System.out.print(c);
		System.out.println();
		/*I am going to turn this to a comment to try to reverse a string also
		but you have access to it once you access it
		int user1 = 0, input1 = 0;
		System.out.println("Mata in en siffra");
		input1=myMethod(input1);
		 /*reverse = reverse * 10;
	      reverse = reverse + n%10;
	      n = n/10;*/
		/*Scanner input=new Scanner(System.in);
		input1=input.nextInt();*/
		
		/*while (input1!=0) {
			user1=user1*10;
			user1=user1 + input1%10;
			input1=input1/10;
			
		}
		
		System.out.println("The reverse is = " +user1);
	}

	private static int myMethod(int user) {
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
		return user=input.nextInt();*/
	
	}
	

	/*private static int myMethod(int inpt) {
		// TODO Auto-generated method stub
		Scanner in=new Scanner(System.in);
		return inpt= in.nextInt();
	}*/
}