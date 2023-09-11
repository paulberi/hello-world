package uppgift1;

import java.util.Scanner;

public class KonsonantOchVokaler {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		
		//Jag har gjort två gånger för att jag vill öva på arrays men om du kan kolla på frågor i kommentärer jag blir tacksam
		//Du ska bara utvärdera den första metod, den med if.
		sortVowelConsonantWithIf(args);
		//sortVowelConsonantInArray(args);
		

	}
	public static void sortVowelConsonantWithIf(String argas[]) {
		System.out.println("Ett program som räknar konsonanter och vokaler i en användar input");
		System.out.println("skriv ett mening");
		Scanner input = new Scanner(System.in);
		
		//nu tar vi in användar input med hjälp av scanner
		String user =input.nextLine();
		
		//för att  förenkla koden så vänder vi allt user input till lowercase
		user=user.toLowerCase();
		
		/*en fråga: hur skriver man en array på den if sätt näre. försökte en del men inget gick bra
		 * char[] user1=user.toCharArray();
		for(int i=0; i<user1.length; i++) {
			if(user1[i].equals'a'||user1[i].equals(e)||user1[]i=='i'||user1[i]==o||user1=='u'||user1=='å'||user1=='ä'||user1=='ö')
			på vilken sätt kan man skriva den if case så den funkar utan att a,e i osv är deklarerat if förväg om den funkar
		}*/
		
		int kcount=0, vcount=0, ucount=0;
		
		//vi vänder user till en char för att kunna hantera den som enskilda char 
		for(int i=0; i<user.length(); i++) {
			
			char user1=user.charAt(i);
			
			//här ska vi skaffa kondition för att räkna som vokal eller konsonant
			if(user1=='a'||user1=='e'||user1=='i'||user1=='o'||user1=='u'||user1=='å'||user1=='ä'||user1=='ö') {
				vcount++;
			}else if(user1>='a' && user1<='z') {
				kcount++;
			}else {
				ucount++;
				
			}
		}
		System.out.println(user+" har "+vcount+" vokaler");
		System.out.println(user+" har "+kcount+" konsonanter");
		System.out.println(user+" har "+ucount+" okänt karaktärer");
	}
	
	
	
	public static void sortVowelConsonantInArray(String args[]) {
		
		//alternative lösning med Array.
		Scanner input=new Scanner(System.in);
		System.out.println("Skriv en mening");
		String str=input.nextLine();
		str=str.toLowerCase();
		char[] user=str.toCharArray();
		
		int vcount=0,kcount=0,ucount=0;
			for(char c:user) {
			
			switch (c) {
			case 'a':case 'e':case 'i':case 'o':case 'u':case 'å':case 'ä':case 'ö':
			vcount++;
			
				break; // finns det en kortare sätt att skrava den
						// kollade case 'b','c','d' osv och de sa att det finns bara för java 14 och above men 
						//det gick inte fast att jag installerade den.
			case 'b':case'c':case'd':case'f':case'g': case'h':case'j':case'k':case'l':case'm':case'n':
			case 'p':case'q':case'r':case's':case't':case'v':case'w':case'x':case'y':case'z':
			kcount++;
			
				break;
				default:
					ucount++;

				
		}
			
			
		}
			System.out.print(vcount+" and "+kcount+" and "+ucount);
		
	}

}
