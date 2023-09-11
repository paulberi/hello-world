package kattis;

import java.util.Scanner;

public class HelloWorld {
	
	public static void main(String [] args) {
		Scanner in=new Scanner(System.in);
		String[] data=new String[5];
		for(int i=0; i<5; i++) {
			data[i]=in.nextLine();
		}
		//extract the number
		int noDigits=data[0].length();
		int answer=0;
		for(int i=0; i<noDigits; i=i+4) {
			int currentDigit=convert(data[0].substring(i,i+3),
					data[1].substring(i,i+3),
					data[2].substring(i,i+3),
					data[3].substring(i,i+3),
					data[4].substring(i,i+3));
			if(currentDigit==-1) {
				System.out.println("BOOM!!");
				return;
			}
			answer=answer*10+currentDigit;
		}
		if(answer%6==0) {
			System.out.println("BEER!!");
		}else {
			System.out.println("BOOM!!");
		}
	}
	public static int convert(String a,String b,String c,String d,String e) {
		for (int i=0; i<image.length; i++) {
			//call the image array here
			if(a.equals(image[i][0])&&
					b.equals(image[i][1])&&
					c.equals(image[i][2])&&
					d.equals(image[i][3])&&
					e.equals(image[i][4])) {
				return i;
			}
		}
		return 0;
	}
	public static String[][] image= {
			{"***","* *","* *","* *","***"},
			{"  *","  *","  *","  *","  *"},
			{"***","  *","***","*  ","***"},
			{"***","  *","***","  *","***"},
			{"* *","* *","***","  *","  *"},
			{"***","*  ","***","  *","***"},
			{"***","*  ","***","* *","***"},
			{"***","  *","  *","  *","  *"},
			{"***","* *","***","* *","***"},
			{"***","* *","***","  *","***"}
	};

}
