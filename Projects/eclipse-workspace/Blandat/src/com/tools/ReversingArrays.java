package com.tools;

import java.util.Scanner;

public class ReversingArrays {
	public static void main(String[] args) {
		
		Scanner in=new Scanner(System.in);
		System.out.println("hur lång är Arrayen");
		int n= in.nextInt();
		int[] list=new int[n];
		
		System.out.println("skriv ner siffrorna för arrayen list");
		
		for(int i=0;i<n;i++) {
			list[i]=in.nextInt();
		}
		int[]result=reverse(list);
		
		for(int j:result) {
			System.out.print(j+",");
		}
	}
	private static int[] reverse(int[] list) {
		// TODO Auto-generated method stub
		int [] result=new int[list.length];
		for(int i=0,j=result.length-1;i<list.length; i++,j--) {
			result[j]=list[i];
		}
		return result;
	}

}
