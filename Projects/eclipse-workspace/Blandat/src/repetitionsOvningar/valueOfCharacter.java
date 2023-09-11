package repetitionsOvningar;

import java.util.Scanner;

public class valueOfCharacter {
	public static void main(String args[]) {
		
		Scanner input= new Scanner(System.in);
		String str=input.next();
		for(int i=0;i<str.length(); i++) {
			char c=str.charAt(i);
			int ch=c;
			int castCh=(int)c;
			System.out.println("value of "+str+" = "+castCh);
		}
		
	}

}
