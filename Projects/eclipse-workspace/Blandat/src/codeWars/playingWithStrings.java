package codeWars;

import java.util.Scanner;

public class playingWithStrings {
	
	public static void main (String args[]) {
		
		countingCharacters(args);
		//numberPyramidTraining(args);
		
	}
	public static void countingCharacters(String args[]) {
		
		/*try {
			char c,ch;
			int j,k;
			System.out.println("mata in en string för att räkna frekvens för character");
			Scanner input=new Scanner(System.in);
			String str=input.nextLine();
			
			for (c='a';c<'z';c++) {
				
				k=0;
				for (j=0;j<str.length(); j++) {
					ch=str.charAt(j);
					if(ch==c) {
						k++;
					}
					
				}
				if(k>0) {
					System.out.print(c +"=" +k+"  ");
			
				}
			}
		
			
		}catch(Exception e) {
			System.out.println(e);
			
		}
		*/
		String str= "what holds love is not love but variety";
		int k, l = 0;
		for(char c='a'; c<='z'; c++) {
			k=0;
			for(int j=0; j<str.length();j++) {
				char ch=str.charAt(j);
				if(ch==c) {
					k++;
				}
				
			}
			if(k>0) {
				System.out.println(c+" appears "+k+ " times");
				
			}
		}
		
	}
	public static void numberPyramidTraining(String args[]) {
		/*int col = 1; int x=8;
		for(int i=8; i>0; i--) {
			for(int j=1;j<=i*2;j++) {
				System.out.print(" ");
			}
			int value=1;
			for (int k=1;k<=col-1; k++) {
				System.out.print(value+" ");
				value *=3;
			}
			for(int l=col;l>=1; l--) {
				System.out.print(value+" ");
				value /=3;
			}
			System.out.println("\n");
			col=col+1;
		}*/
		
		int j,i,k, col = 2;
		for (i=8; i>0; i--) {
			for(j=0; j<i*2;j++) {
				System.out.print("    ");
			}
			k=1;
			for (int x=1; x<=col-2; x++) {
				System.out.print(k+"\t");
				k *=3;
			}
			for (int y=col; y>1; y--) {
				System.out.print(k+"\t");
				k /=3;
			}
			System.out.println();
			col++;
		}
		
	}

}
