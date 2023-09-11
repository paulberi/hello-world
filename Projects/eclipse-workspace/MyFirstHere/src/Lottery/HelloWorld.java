package Lottery;

import java.util.List;

public class HelloWorld {
	public static void main(String args[]) {
	
		
		int reverse = 0;
		int num=2345;
		while(num!=0) {
			reverse=reverse*10;
			reverse=reverse+num%10;
			num=num/10;
			
					
		}
		System.out.println(reverse);

	}
}