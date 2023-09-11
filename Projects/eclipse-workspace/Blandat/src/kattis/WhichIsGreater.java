package kattis;

import java.util.Scanner;

public class WhichIsGreater {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in =new Scanner(System.in);
		
		int first=in.nextInt();
		int second=in.nextInt();
		
		if(first<=second) {
			System.out.println(0);
		}else {
			System.out.println(1);
		}
	}

}
