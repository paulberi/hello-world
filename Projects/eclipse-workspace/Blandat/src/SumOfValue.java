import java.util.Scanner;

public class SumOfValue {
	public static void main(String args[]) {
		Scanner user=new Scanner(System.in);
		System.out.println("Mata in en siffra");
		int userInput=user.nextInt();
		
		int refInput=Math.abs(userInput);
		int sum=0;
		int i=0;
		
		while(refInput/Math.pow(10, i)>0) {
			sum+=calculator(userInput, i);
			i++;
		}
		System.out.println("sum is "+sum);
		/*Scanner input=new Scanner(System.in);
		System.out.println("write an integer:");
		int interger=input.nextInt();
		
		//calculations
		int integer=Math.abs(interger);
		int sum=0;
		int i=0;
		
		while(integer/Math.pow(10,i)>0) {
			sum+=getDigit(integer,i);
			i++;
		}
		System.out.println("Sum all digits in " + interger+" is "+sum);
	}

	private static int getDigit(int num, int power) {
		// TODO Auto-generated method stub
		return (num%(int)Math.pow(10, power+1))/(int)Math.pow(10, power);*/
	}

	private static int calculator(int a, int b) {
		// TODO Auto-generated method stub
		return (a%(int)Math.pow(10, b+1))/((int)Math.pow(10, b));
	}

}
