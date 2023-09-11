package repetitionsOvningar;

import java.util.Scanner;

public class VokalOchKonsonant {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//vocalKonsonant(args);
		//vocalEllerKonsonant(args);
		countingLetterFrequency(args);

	}

	private static void countingLetterFrequency(String[] args) {
		// TODO Auto-generated method stub
		Scanner input= new Scanner(System.in);
		System.out.println("skriv en mening");
		String str=input.nextLine();
		int[] ch=new int[26];
		int index_at;
		int k, j;
		char c;
		for(c='a'; c<='z';c++) {
			k=0;
			for(j=0; j<str.length(); j++) {
				char h=str.charAt(j);
				if(h==c) {
					k++;
				}
			}
			if(k>0) {
				System.out.println(c +" finns " +k+" gånger");
			}
		}
				
	}

	private static void vocalEllerKonsonant(String[] args) {
		// TODO Auto-generated method stub
		Scanner input= new Scanner(System.in);
		System.out.println("skriv en mening");
		String str=input.nextLine();
		
		str=str.toLowerCase();
		for(int i=0; i<str.length(); i++) {
			char ch=str.charAt(i);
			
			if(ch=='a'||ch=='e'||ch=='i'||ch=='0'||ch=='u'||ch=='å'||ch=='ö'||ch=='ä') {
				System.out.println("its a vowel");
			}if(ch>='a' && ch<='z'){
				System.out.println("its a consonant");
			}
			
		}
	}

	private static void vocalKonsonant(String[] args) {
		// TODO Auto-generated method stub
		Scanner input= new Scanner(System.in);
		System.out.println("skriv en mening");
		String str=input.nextLine();
		int vcount = 0, kcount = 0;
		str=str.toLowerCase();
		for(int i=0; i<str.length(); i++) {
			char ch=str.charAt(i);
			
			if(ch=='a'||ch=='e'||ch=='i'||ch=='0'||ch=='u'||ch=='å'||ch=='ö'||ch=='ä') {
				vcount=vcount+1;
			}else if(ch>='a' && ch<='z'){
				kcount=kcount+1;
			}
			
		}
		System.out.println(str+" har "+vcount+" vokaler");
		System.out.println( str+"har "+kcount+" konsonanter");
	}
	

}
