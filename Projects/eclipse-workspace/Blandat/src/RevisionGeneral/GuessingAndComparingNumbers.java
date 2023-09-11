package RevisionGeneral;

import java.util.Random;
import java.util.Scanner;


/**
 * @author paulberinyuylukong
 *
 */
/**
 * @author paulberinyuylukong
 *
 */
public class GuessingAndComparingNumbers {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Random rand=new Random();
		Scanner in=new Scanner(System.in);
		int x;
		
		for (x=0;x<3;x++) {
			
			System.out.println("choose a number between 1 and 100");

			int randNumber=rand.nextInt(100-1)+1;
			System.out.println(randNumber);
			
			int choosenNumber=in.nextInt();
			
			if(choosenNumber!=randNumber && x!=2) {
				System.out.println("Try again");
				
			}if(choosenNumber==randNumber) {
				System.out.println("Congrats you won");
				break;
			}if(x==2) {
				System.out.println("You lost! EXPLODE");
				
			}
			
		}
		
	}

}
