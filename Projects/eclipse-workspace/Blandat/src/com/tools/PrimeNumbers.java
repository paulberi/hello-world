package com.tools;

public class PrimeNumbers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int num = 0;
		int x=0;
		int i = 0;
		int count = 0;
		while(num<100) {
			boolean isPrime=true;
			for (i=2; i<=num/2;i++) {
				if(num%i==0) {
					isPrime=false;
					break;
				}
			}
			if(isPrime &&num!=0&& num!=1) {
				System.out.println(num+" ");
				x++;
			}
			num++;
		}
		System.out.println( "number is "+x);
	}

}
