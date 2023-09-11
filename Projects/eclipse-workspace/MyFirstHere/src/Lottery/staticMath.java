package Lottery;
import java.awt.List;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.Math;
import java.util.Random;
import java.util.Scanner;



public class staticMath {

	/*private static final String String = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a =Integer.parseInt(args(null));
		if (a==12 || a>=10 && a<=2) {
			System.out.println("Winter");
		}else if(a>=6 && a<=9) {
			System.out.println("Summer");
		}else if(a>=3 && a<=5) {
			System.out.println("Spring");
			
		}else{
			System.out.println("error");
		}
		

	}

	private static String args(String user) {
		// TODO Auto-generated method stub
			Scanner input = new Scanner(System.in);
			return user = input.next();*
	public static void main(String args[]) {
		String test="Det här är en lång sträng";
		System.out.println(test.length());
		String lang=test.substring(7,13);
		System.out.println(lang);Ä*
	
		
	}
	public static void main(String args[]) {
		System.out.println("Hejveijs i lingonskogen");
		System.out.println("Hejveijs i \nLingonskogen");
		
	}
	public static void main(String args[]) {
		int x;
		for(x=10; x>=0; x--) {
			System.out.println(x);
			
		}
	}
	public static void main(String args[]) {
		System.out.println("dags att borsta");
		System.out.println("lägg tandcreme på tandborsten");
		int borstadeTänder = 0;
		while(borstadeTänder<=32) {
			System.out.println("fortsätt att börsta, du har borstat "+borstadeTänder );
			borstadeTänder=borstadeTänder+1;
		}
		System.out.println("Bra jobbat");
	}
	
	public static void main(String args[]) {
		Scanner input=new Scanner(System.in);
		
		
		Random random=new Random();
		int generatedNumber=random.nextInt(100);
		int NumberOfTrials = 0;
		while(NumberOfTrials<3) {
			System.out.println("Guess a number");
			int guessedNumber=input.nextInt();
			if(generatedNumber==guessedNumber) {
				System.out.println("Bra jobbat");break;
				
			}else {
				System.out.println("Försök igen" +generatedNumber);
				NumberOfTrials=NumberOfTrials+1;
			}
			
		}
		System.out.println("Tack för att du besökte oss");
	}
	
	public static void main(String args[]) {
		Scanner input=new Scanner(System.in);
		System.out.println("Gissa två nummer");
		int num1=input.nextInt();
		int num2=input.nextInt();
		int max= myMethod(num1,num2);
		System.out.println("The maximum number of the two is "+max);
	}

	private static int myMethod(int num3, int num4) {
		// TODO Auto-generated method stub
		if(num3 > num4) {
			return num3;
		}else {
			return num4;
		}
		
	}
	public static void main(String args[]) {
		System.out.println("Mata in en siffra i kilo");
		Scanner input=new Scanner(System.in);
		double inKilo=input.nextDouble();
		double Convert=inPounds(inKilo);
		System.out.println(inKilo+"kg is equivalent to "+Convert);
	}

	private static Double inPounds(double kilo) {
		// TODO Auto-generated method stub
		double pounds;
		pounds=kilo*2.204;
		return pounds;
		
	}
	public static void main(String args[]) {
		System.out.println("Du har en fyrkant, bestämma hur lång varje sida ska vara");
		System.out.println("längd:");
		Scanner input= new Scanner(System.in);
		double längden=input.nextInt();
		System.out.println("bred:");
		double bred=input.nextInt();
		double arean=methodForArea(längden,bred);
		double omkretsen=methodForArea(längden,bred,längden,bred);
		System.out.println("arean = "+arean+ "omkretsen = "+omkretsen);
	}

	private static double methodForArea(double length, double width, double length1, double width1) {
		// TODO Auto-generated method stub

		double perimeters=(length+width+length1+width1);
		return perimeters;
	}

	private static Double methodForArea(double length, double width) {
		// TODO Auto-generated method stub
		double area = length*width;
		return area;
		
	}
	public static void main(String args[]) {
		
		
		String user="long";
		System.out.println(user.indexOf(4));
		
		
	}
	public static void main(String args[]) {

		List<Integer> list=List.of(5,1,4,2,3);
        for(Integer item:list){
            System.out.println(item);
            löpande
		}
		
	}

	
	public static void main(String args[]) {
		char continueloop='Y';
		while(continueloop=='Y') {
			System.out.println("enter Y to continue and N to quit:");
			continueloop=input.getLine().charAt(0);
		}
		*/
	public static void main(String args[]) {
		
		try {
			FileWriter fw=new FileWriter("D:\\documents\\java text.text");
			fw.write(65);
			fw.write("Hello welcome to java");
			
		}catch(Exception e) {
			System.out.println(e);
		}
		System.out.println("success...");
			
	}
	
	
	
	
	
	
	
	

}
