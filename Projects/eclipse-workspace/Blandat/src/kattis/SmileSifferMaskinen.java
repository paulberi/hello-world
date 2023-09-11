package kattis;

import java.util.Scanner;

public class SmileSifferMaskinen {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);

		String str = in.nextLine();
		for (int i = 0; i < str.length(); i++) {
			if((i+1)<str.length()) {

				if (str.charAt(i) == ':' && str.charAt(i + 1) == ')') {
					System.out.println(i);
				}
				if (str.charAt(i) == ';' && str.charAt(i + 1) == ')') {
					System.out.println(i);
				}
			}
			if((i+2)<str.length()) {

				if (str.charAt(i) == ':' && str.charAt(i + 1) == '-' && str.charAt(i + 2) == ')') {
					System.out.println(i);
				}
				if (str.charAt(i) == ';' && str.charAt(i + 1) == '-' && str.charAt(i + 2) == ')') {
					System.out.println(i);
				}
			}
		}
	}

}
