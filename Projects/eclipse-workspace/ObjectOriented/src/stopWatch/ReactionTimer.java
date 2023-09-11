package stopWatch;

import java.util.Scanner;

public class ReactionTimer {
	public static void main(String [] args) throws InterruptedException {
		
		/**
		 * what this program does is it prints 3
		 * waits a second
		 * prints 2 abd waits a second
		 * prints 1 waits a second and the go
		 * You are expected to input something as
		 * fast as possible and press enter then it will tell you how long it took
		 * nice credit to Alex Lee
		 */
		 
		System.out.println("3");
		Thread.sleep(1000);
		System.out.println("2");
		Thread.sleep(1000);
		System.out.println("1");
		Thread.sleep(1000);
		
		long startTime=System.currentTimeMillis();
		
		System.out.println("GO !!!!!!!!");
		
		Scanner s=new Scanner(System.in);
		s.nextInt();
		
		long stopTime= System.currentTimeMillis();
		
		long reactionTime= stopTime-startTime;
		
		System.out.println(reactionTime+"ms");
	}

}
