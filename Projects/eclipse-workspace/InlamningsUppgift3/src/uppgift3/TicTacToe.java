package uppgift3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;
import java.util.List;

public class TicTacToe {
	static int playerPositions;
	static int cpuPositions;
	static char [][] ttt;
	static char symbol;
	static ArrayList<Integer> playerPos=new ArrayList<Integer>();
	static ArrayList<Integer> cpuPos=new ArrayList<Integer>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char[][] ttt= {
				{' ','|',' ','|',' '},
				{'-','+','-','+','-'},
				{' ','|',' ','|',' '},
				{'-','+','-','+','-'},
				{' ','|',' ','|',' '},
		};
		Scanner input=new Scanner(System.in);
		Random ran= new Random();
		gameLines(ttt);
		int moves = 0;
		String result = null;
		while(true) {
			
			cpuPositions=ran.nextInt(9)+1;
			while(cpuPos.contains(cpuPositions)||playerPos.contains(cpuPositions)) {
				System.out.println("position taken, run your sample again");
				cpuPositions= ran.nextInt(9)+1;
			}
			System.out.println(cpuPositions);
			placingChoicesOnTheboard(ttt,cpuPositions,"cpu");
			
			result=winningPositions();
			
			if(result.length()>0) {
				System.out.println(result);
				break;
			}	
			System.out.println("Choose a number from 1 through to 9:");
			playerPositions= input.nextInt();
			
			while(playerPos.contains(playerPositions)||cpuPos.contains(playerPositions)) {
				System.out.println("position taken try again from 1 through to 9");
				playerPositions= input.nextInt();
			}
			
			
			placingChoicesOnTheboard(ttt,playerPositions,"player");
		    result=winningPositions();
			if(result.length()>0) {
				System.out.println(result);
				break;
			}
			
		
		}
		
			System.out.println(moves);
			
			
			
	}
		
			
	private static String winningPositions() {
		// TODO Auto-generated method stub
		List row1=Arrays.asList(1,2,3);
		List row2=Arrays.asList(4,5,6);
		List row3=Arrays.asList(7,8,9);
		List col1=Arrays.asList(1,4,7);
		List col2=Arrays.asList(2,5,8);
		List col3=Arrays.asList(3,6,9);
		List cross1=Arrays.asList(1,5,9);
		List cross2=Arrays.asList(3,5,7);
		
		List<List>winners =new ArrayList<List>();
		winners.add(row1);
		winners.add(row2);
		winners.add(row3);
		winners.add(col1);
		winners.add(col2);
		winners.add(col3);
		winners.add(cross1);
		winners.add(cross2);
		
		for(List l:winners) {
			if(playerPos.containsAll(l)) {
				System.out.println( "Congratulations you won");
				
			}else if(cpuPos.containsAll(l)) {
				System.out.println("CPU won, try again next time");
			
			}else if(playerPos.size() +cpuPos.size()==9) {
				System.out.println( "Draw");
				
			}
		}
		
		
		return "";


	}

	

	private static char[] []gameLines(char [][]ttt) {
		// TODO Auto-generated method stub
		for (int i=0; i<ttt.length; i++) {
			for(int j=0; j<ttt.length; j++) {
				System.out.print(ttt[i][j]);
			}
			System.out.println();
		}
		return(ttt);
	}

	private static void placingChoicesOnTheboard(char [][]ttt, int pos, String user){
		// TODO Auto-generated method stub
		
		
		char symbol = ' ';
		if (user.equals("player")) {
			symbol='X';
			playerPos.add(pos);
		}else if(user.equals("cpu")) {
			symbol='O';
			cpuPos.add(pos);
		}
		
		if(pos==1) {
			ttt[0][0]=symbol;
			gameLines(ttt);
			
		}if(pos==2) {
			ttt[0][2]=symbol;
			gameLines(ttt);
	
		
		}if(pos==3) {
			ttt[0][4]=symbol;
			gameLines(ttt);
			
		}if(pos==4) {
			ttt[2][0]=symbol;
			gameLines(ttt);
		
		}if(pos==5) {
			ttt[2][2]=symbol;
			gameLines(ttt);
		
		}if(pos==6) {
			ttt[2][4]=symbol;
			gameLines(ttt);
		
		}if(pos==7) {
			ttt[4][0]=symbol;
			gameLines(ttt);
			
		}if(pos==8) {
			ttt[4][2]=symbol;
			gameLines(ttt);
		
		}if(pos==9) {
			ttt[4][4]=symbol;
			gameLines(ttt);
			
		}
		
	
		
		
	
	}
	
}
