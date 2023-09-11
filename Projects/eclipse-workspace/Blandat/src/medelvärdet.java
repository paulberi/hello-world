import java.util.Scanner;

public class medelvärdet {
	
	public static void main(String args[]) {
		System.out.println("write down three numbers of your choice");
		Scanner input=new Scanner(System.in);
		System.out.println("first input:");
		int a=input.nextInt();
		System.out.println("second input");
		int b=input.nextInt();
		System.out.println("third input");
		int c=input.nextInt();
		
		double medelvärdet=(a+b+c)/3;
		System.out.println("medelvärdet is = "+medelvärdet);
	}

}
