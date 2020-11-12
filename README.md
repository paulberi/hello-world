package uppgift3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
	static char[][] board;
	static int playerPositions;
	static int cpuPositions;
	static ArrayList<Integer> cpuPos = new ArrayList<Integer>();
	static ArrayList<Integer> playerPos = new ArrayList<Integer>();
	static char symbol;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// tic tac toe game. First we build the gameboard using arrays.
		// then we identify the playable slots and attribute them to user choices
		// then we determine the winning combinations

		System.out.println("Welcome to Tic Tac Toe game");
		// *using two dimensional array to create the game board

		char[][] board = { { ' ', '|', ' ', '|', ' ' }, { '-', '+', '-', '+', '-' }, { ' ', '|', ' ', '|', ' ' },
				{ '-', '+', '-', '+', '-' }, { ' ', '|', ' ', '|', ' ' }, };
		// call of method to print out game board
		gameBoard(board);

		String result;
		while (true) {
			Scanner input = new Scanner(System.in);
			System.out.println("Write a number from 1 through to 9:");
			playerPositions = input.nextInt();
			while (playerPos.contains(playerPositions) || cpuPos.contains(playerPositions)) {
				System.out.println("place taken, make another choice");
				playerPositions = input.nextInt();
			}

			placingParticipantsChoicesOnBoard(board, playerPositions, "player");
			result = winningCombinations();

			if (result.length() > 1) {
				System.out.println(result);
				break;
			}

			Random ran = new Random();
			cpuPositions = ran.nextInt(9) + 1;
			while (playerPos.contains(cpuPositions) || cpuPos.contains(cpuPositions)) {
				System.out.println("place taken, make another choice");
				cpuPositions = ran.nextInt(9) + 1;
			}

			System.out.println(cpuPositions);

			placingParticipantsChoicesOnBoard(board, cpuPositions, "cpu");

			result = winningCombinations();

			if (result.length() > 1) {
				System.out.println(result);
				break;

			}
		}

	}

	private static String winningCombinations() {
		/*
		 * här vi identifierar de vinst combinationer och samla de i olicka list sen
		 * skaffar vi en array som har alla vinst positioner för att från där kan vi
		 * loppa igenom alla vinst combinationer för enkelt agvöra om det finns en vinst
		 */
		// TODO Auto-generated method stub
		List combination1 = Arrays.asList(1, 2, 3);
		List combination2 = Arrays.asList(4, 5, 6);
		List combination3 = Arrays.asList(7, 8, 9);
		List combination4 = Arrays.asList(1, 4, 7);
		List combination5 = Arrays.asList(2, 5, 8);
		List combination6 = Arrays.asList(3, 6, 9);
		List combination7 = Arrays.asList(1, 5, 9);
		List combination8 = Arrays.asList(3, 5, 7);

		List<List> winner = new ArrayList<List>();
		;
		winner.add(combination1);
		winner.add(combination2);
		winner.add(combination3);
		winner.add(combination4);
		winner.add(combination5);
		winner.add(combination6);
		winner.add(combination7);
		winner.add(combination8);

		for (List l : winner) {
			if (playerPos.containsAll(l)) {
				return "Grattis, du vann";
			} else if (cpuPos.containsAll(l)) {
				return "Obs! CPU won";
			} else if (playerPos.size() + cpuPos.size() == 9) {
				return "Draw";
				/*
				 * jag inträffade en problem här för att om det finns 8 positioner som är fyllda
				 * och den sista ska ge mig en winner combination, jag får en draw istället men
				 * förstår inte varför det gör så och hur jag kan lösa det efter som en draw är
				 * en else position och den ska börja från den första if.
				 * 
				 */
			}
		}
		return "";

	}

	private static void placingParticipantsChoicesOnBoard(char[][] board, int positions, String user) {
		// TODO Auto-generated method stub
		// här placerar vi user input på spel board och vi roppar in gameboard här också
		// for att
		// utskrifter på engång som vi kör
		symbol = ' ';

		if (user.equals("player")) {
			symbol = 'X';
			playerPos.add(positions);
		}
		if (user.equals("cpu")) {
			symbol = 'O';
			cpuPos.add(positions);
		}

		if (positions == 1) {
			board[0][0] = symbol;
			gameBoard(board);
		}
		if (positions == 2) {
			board[0][2] = symbol;
			gameBoard(board);
		}
		if (positions == 3) {
			board[0][4] = symbol;
			gameBoard(board);
		}
		if (positions == 4) {
			board[2][0] = symbol;
			gameBoard(board);
		}
		if (positions == 5) {
			board[2][2] = symbol;
			gameBoard(board);
		}
		if (positions == 6) {
			board[2][4] = symbol;
			gameBoard(board);
		}
		if (positions == 7) {
			board[4][0] = symbol;
			gameBoard(board);
		}
		if (positions == 8) {
			board[4][2] = symbol;
			gameBoard(board);
		}
		if (positions == 9) {
			board[4][4] = symbol;
			gameBoard(board);
		}

	}

	// method för game board
	private static void gameBoard(char[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}

}

