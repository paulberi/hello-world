package uppgift1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Main
{
	private static int        computer1Level;
	private static int        computer2Level;
	private static String     currentMarker;
	private static String     player1Marker;
	private static boolean    versusComputer;
	private static String[][] board;

	// These are all the possible winning lines
	private final static int[][][] winningLines = {{{0, 0}, {0, 1}, {0, 2}}, // Row 1
			{{1, 0}, {1, 1}, {1, 2}}, // Row 2
			{{2, 0}, {2, 1}, {2, 2}}, // Row 3
			{{0, 0}, {1, 0}, {2, 0}}, // Column 1
			{{0, 1}, {1, 1}, {2, 1}}, // Column 2
			{{0, 2}, {1, 2}, {2, 2}}, // Column 3
			{{0, 0}, {1, 1}, {2, 2}}, // Diagonal top-left-to-bottom-right
			{{0, 2}, {1, 1}, {2, 0}} // Diagonal top-right-to-bottom-left
	};

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);

		// Loops until the user wants to quit
		mainLoop :
		while (true)
		{
			// Main menu
			clearConsole();
			System.out.println("Välkommen till Tic-Tac-Console!\nVem spelar?");
			String[] mainMenuItems = {"Du mot datorn.", "Två spelare.", "Två slumpade datorer."};
			int      mainMenuInput = getMenuInput(input, mainMenuItems);
			versusComputer = mainMenuInput == 1;
			System.out.println();

			// Set up computer if versus human
			if (mainMenuInput == 1)
			{
				clearConsole();
				System.out.println("Du kommer att spela mot datorn, välj svårighetsgrad.");
				String[] computerMenuItems = {"Lätt.", "Medel.", "Svår."};
				computer1Level = getMenuInput(input, computerMenuItems);
			}

			// Loops until the user wants to go back to the main menu
			currentRulesLoop :
			while (true)
			{
				String[][] newBoard = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
				board = newBoard;
				String winner = "";
				Random rand   = new Random();

				// Set the markers (X or O) to player1, X always start
				player1Marker = String.valueOf("XO".charAt(rand.nextInt(2)));
				currentMarker = "X";

				// Get random computer difficulties if it is computer versus computer
				if (mainMenuInput == 3)
				{
					computer1Level = 1 + rand.nextInt(3);
					computer2Level = 1 + rand.nextInt(3);

					String[] difficultyString = {"lätt", "medium", "svår"};
					System.out.println("Dator X är " + difficultyString[computer1Level - 1] + ".");
					System.out.println("Dator O är " + difficultyString[computer2Level - 1] + ".");
				}

				// We have everything we need to start the game, is the user ready?
				System.out.println("\nTyck Enter för att starta..");
				input.nextLine();

				// Game loop
				while (winner.isBlank())
				{
					if (mainMenuInput == 2)
						humanTurn(input);
					else if (mainMenuInput == 1 && player1Marker.equals(currentMarker))
						humanTurn(input);
					else
					{
						computerTurn();

						// Update the board if it is computer versus computer
						if (mainMenuInput == 3)
							printBoard();
					}

					// Check if the game has ended
					winner = lookForWinner();

					// If not we check for a draw or continue the game
					if (winner.isBlank())
					{
						if (lookForDraw())
							break;

						// Switch player
						if (currentMarker == "X")
							currentMarker = "O";
						else
							currentMarker = "X";

						// Pause between rounds if it is computer versus computer
						if (mainMenuInput == 3)
						{
							System.out.print("\nTryck Enter för nästa runda..");
							input.nextLine();
						}

					}

				}

				// Game has ended, present the result
				printBoard();
				if (!winner.isBlank())
				{
					if (mainMenuInput != 3)
						System.out.println("\nVinnaren är korad och hade markören " + winner + ". "
								+ (winner.equals(player1Marker)
										? "Grattis, du vann!"
										: "Du förlorade.")
								+ "\nTack för att du spelade.");
					else
						System.out.println("\nVinnaren är korad och hade markören " + winner
								+ ", grattis!\nTack för att ni spelade.");
				}
				else
					System.out.println("\nMatchen slutade oavgjord.\nTack för att "
							+ (mainMenuInput != 2 ? "du" : "ni") + " spelade.");

				// End of game menu
				System.out.println("\nSpelet är slut.");
				String[] endOfGameMenuItems = {"Spela igen med samma regler.",
						"Återgå till huvudmenyn.", "Avsluta."};
				int      endOfGameMenuInput = getMenuInput(input, endOfGameMenuItems);
				switch (endOfGameMenuInput)
				{
					case 1 : // Continue the current rules loop
					{
						System.out.println();
						break;
					}
					case 2 : // Continue the main loop
					{
						break currentRulesLoop;
					}
					case 3 : // Break the main loop
					{
						break mainLoop;
					}
				}

			}

		}

		// Goodbye World
		input.close();
		clearConsole();
		System.out.println("Välkommen åter, hejdå!");
	}

	private static int getMenuInput(Scanner input, String[] choices)
	{
		// Present the options to the user
		int i = 1;
		for (String menuItem : choices)
		{
			System.out.println(" " + i + ". " + menuItem);
			i++;
		}

		// Loops until we get a valid option from the user
		while (true)
		{
			System.out.print("Ange ditt val: ");

			try
			{
				int choice = input.nextInt();
				if (choice >= 1 && choice <= choices.length)
				{
					input.nextLine();
					return choice;
				}
				else
					throw new Exception("");

			}
			catch (Exception e)
			{
				System.out.println("Var god ange den siffra som står innan ditt val.");
				input.nextLine();
			}

		}

	}

	private static void printBoard()
	{
		clearConsole();

		// Header
		System.out.println("     A   B   C");
		System.out.println("    --- --- ---");

		// Print one row at a time
		for (int row = 0; row < board.length; row++)
		{
			printLine(row);

			// Skip the bottom line
			if (row < board.length - 1)
				System.out.println("   |---|---|---|");
		}

	}

	private static void printLine(int row)
	{
		// Header
		System.out.print(" " + (row + 1) + " |");

		// Print one cell at a time
		for (String cell : board[row])
		{
			if (cell.isBlank())
				System.out.print("   ");
			else
				System.out.print(" " + cell + " ");

			System.out.print("|");
		}

		System.out.println();
	}

	private static void clearConsole()
	{
		// Push 50 empty lines to simulate a clearing of the console. Uses 50 \n instead of a loop for (minimal) optimization
		System.out.println(
				"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

	private static void humanTurn(Scanner input)
	{
		printBoard();
		System.out.println("\nDu är " + currentMarker + ".");
		int row = -1, col = -1;

		// Run until we have a valid answer from the player
		while (true)
		{
			try
			{
				System.out.print("Ange ditt drag (en bokstav och en siffra): ");
				String choice = input.nextLine().replace(" ", "");

				// We have already removed potential spaces so we only accept 2 characters in the input
				if (choice.length() == 2)
				{
					// Check if both of the characters are part of "ABC123"
					if ("ABC123".indexOf(choice.substring(0, 1).toUpperCase()) >= 0
							&& "ABC123".indexOf(choice.substring(1, 2).toUpperCase()) >= 0)
					{
						// If the first character is a digit and the second is a letter
						if (Character.isDigit(choice.charAt(0))
								&& Character.isLetter(choice.charAt(1)))
						{
							row = Integer.parseInt(choice.substring(0, 1)) - 1;
							col = "ABC".indexOf(choice.substring(1, 2).toUpperCase());
						}
						// If the first character is letter and the second is a digit
						else if (Character.isDigit(choice.charAt(1))
								&& Character.isLetter(choice.charAt(0)))
						{
							row = Integer.parseInt(choice.substring(1, 2)) - 1;
							col = "ABC".indexOf(choice.substring(0, 1).toUpperCase());
						}
						// The user has entered either two digits or two letters
						else
							throw new Exception("Du har inte angett en bokstav och en siffra.");

						// Check if the row and column are within the board limits, they should be since we checked for ABC123
						if (row >= 0 && row < board.length && col >= 0 && col < board[0].length)
						{
							// If the cell is free we finally have a valid answer
							if (cellIsFree(row, col))
								break;
							else
								throw new Exception("Cellen " + "ABC".charAt(col) + (row + 1)
										+ " är inte fri.");
						}
						else
							throw new Exception("Cellen finns inte.");

					}
					else
						throw new Exception(
								"Ange en bokstav (A, B eller C) och en siffra (1, 2 eller 3).");

				}
				else
					throw new Exception("Du har angett för många tecken.");

			}
			catch (Exception e)
			{
				System.out.println("Felaktigt drag. " + e.getMessage());
			}

		}

		// Finally, take the cell
		board[row][col] = currentMarker;
	}

	private static void computerTurn()
	{
		int row      = -1, col = -1;
		int cpuLevel = versusComputer || currentMarker == "X" ? computer1Level : computer2Level;

		if (cpuLevel == 1) // Easy, looks for a free cell randomly
		{
			int[] cell = computerRandomFreeCell();

			row = cell[0];
			col = cell[1];
		}
		else if (cpuLevel == 2) // Medium, follows a list of preferred moves
		{
			int[][] preferredMoves = {{1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2}, {0, 1}, {1, 0},
					{1, 2}, {2, 1}};

			for (int[] move : preferredMoves)
			{
				if (cellIsFree(move))
				{
					row = move[0];
					col = move[1];
					break;
				}

			}

		}
		else // Hard, try to think a little
		{
			int[]   cell    = {-1, -1};
			int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
			int[][] sides   = {{0, 1}, {1, 0}, {1, 2}, {2, 1}};
			Random  rand    = new Random();

			int round = 0;
			while (cell[0] == -1)
			{
				round++;

				switch (round)
				{
					case 1 : // If we can win, we play that move
					{
						List<int[]> winnableLines = computerFindWinningOrBlockingMoves(false,
								board);
						if (winnableLines.size() > 0)
							cell = winnableLines.get(rand.nextInt(winnableLines.size()));

						break;
					}
					case 2 : // If we can block a win, we play that move
					{
						List<int[]> losingLines = computerFindWinningOrBlockingMoves(true, board);
						if (losingLines.size() > 0)
							cell = losingLines.get(rand.nextInt(losingLines.size()));

						break;
					}
					case 3 : // Try to create a fork
					{
						List<int[]> cells = computerFindFork(true);
						if (cells.size() > 0)
							cell = cells.get(rand.nextInt(cells.size()));

						break;
					}
					case 4 : // Try to block a potential fork
					{
						List<int[]> cells = computerFindFork(false);
						if (cells.size() > 0)
						{
							if (cells.size() == 1)
								cell = cells.get(0);

							// We have multiple potential forks to block, so we have to play a move that forces the opponent to block us instead
							else
							{
								// Build a dummy board
								String[][] dummyBoard = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
								for (int y = 0; y < board.length; y++)
								{
									for (int x = 0; x < board[y].length; x++)
										dummyBoard[y][x] = board[y][x];
								}

								// Start with corners, shuffle them first
								List<int[]> c = new ArrayList<int[]>();
								c.addAll(Arrays.asList(corners));
								Collections.shuffle(c);
								c.toArray(corners);
								for (int[] corner : corners)
								{
									// Corner is taken, move on
									if (!dummyBoard[corner[0]][corner[1]].isBlank())
										continue;

									// Test if that corner gives us a winning move
									dummyBoard[corner[0]][corner[1]] = currentMarker;
									List<int[]> moves = computerFindWinningOrBlockingMoves(false,
											dummyBoard);

									if (moves.size() > 0)
									{
										cell = corner;
										break;
									}
									else
										dummyBoard[corner[0]][corner[1]] = board[corner[0]][corner[1]];

								}

								// If we haven't already found it we have to look at the sides
								if (cell[0] == -1)
								{
									// Shuffle them first
									List<int[]> s = new ArrayList<int[]>();
									s.addAll(Arrays.asList(sides));
									Collections.shuffle(s);
									s.toArray(sides);
									for (int[] side : sides)
									{
										// Side is taken, move on
										if (!dummyBoard[side[0]][side[1]].isBlank())
											continue;

										dummyBoard[side[0]][side[1]] = currentMarker;
										List<int[]> moves = computerFindWinningOrBlockingMoves(
												false, dummyBoard);

										if (moves.size() > 0)
										{
											cell = side;
											break;
										}
										else
											dummyBoard[side[0]][side[1]] = board[side[0]][side[1]];

									}

								}

							}

						}
						else
							break;

					}
					case 5 : // Take the center if it is free
					{
						int[] center = {1, 1};
						if (cellIsFree(center))
						{
							// If this is the second move and the opponent took a corner, let's not fall in that trap
							int moves = 0;
							for (String[] x : board)
							{
								for (String marker : x)
								{
									if (!marker.isBlank())
										moves++;
								}

							}

							// Player 1 didn't take the center, so don't take the center
							if (moves == 1)
								break;

							// If we have the first move let's spice it up with a corner move sometimes
							else if (moves == 0)
							{
								if (rand.nextInt(2) == 1)
									cell = center;
								else
									break;
							}
							else
								cell = center;
						}

						break;
					}
					case 6 : // Take an opposite corner
					{
						if (!board[0][0].isEmpty() && board[0][0] != currentMarker
								&& cellIsFree(corners[3]))
							cell = corners[3];
						else if (!board[2][2].isEmpty() && board[2][2] != currentMarker
								&& cellIsFree(corners[0]))
							cell = corners[0];
						else if (!board[0][2].isEmpty() && board[0][2] != currentMarker
								&& cellIsFree(corners[2]))
							cell = corners[2];
						else if (!board[2][0].isEmpty() && board[2][0] != currentMarker
								&& cellIsFree(corners[1]))
							cell = corners[1];

						break;
					}
					case 7 : // Take a free corner, randomize the order of them first for a little more dynamic game
					{
						List<int[]> c = new ArrayList<int[]>();
						c.addAll(Arrays.asList(corners));
						Collections.shuffle(c);
						c.toArray(corners);
						for (int[] corner : corners)
						{
							if (cellIsFree(corner))
							{
								cell = corner;
								break;
							}

						}

						break;
					}
					case 8 : // Take a free side, randomize the order of them first for a little more dynamic game
					{
						List<int[]> s = new ArrayList<int[]>();
						s.addAll(Arrays.asList(sides));
						Collections.shuffle(s);
						s.toArray(sides);
						for (int[] side : sides)
						{
							if (cellIsFree(side))
							{
								cell = side;
								break;
							}

						}

						break;
					}
				}

			}

			row = cell[0];
			col = cell[1];
		}

		// Finally, take the cell
		board[row][col] = currentMarker;
	}

	private static int[] computerRandomFreeCell()
	{
		Random rand = new Random();
		int    row, col;
		int[]  cell = {0, 0};

		// Randomize the row and column until we have a free cell
		while (true)
		{
			row = rand.nextInt(3);
			col = rand.nextInt(3);

			if (cellIsFree(row, col))
				break;
		}

		cell[0] = row;
		cell[1] = col;

		return cell;
	}

	private static List<int[]> computerFindWinningOrBlockingMoves(boolean blocking,
			String[][] dummyBoard)
	{
		List<int[]> winnableMoves = new ArrayList<int[]>();

		// Loop through all the possible winning lines
		lineLoop :
		for (int[][] line : winningLines)
		{
			int   count = 0;
			int[] free  = new int[2];

			for (int[] cell : line)
			{
				String marker = dummyBoard[cell[0]][cell[1]];

				if (!marker.isEmpty() && ((!blocking && marker == currentMarker)
						|| (blocking && marker != currentMarker)))
					count++;
				else if (!marker.isEmpty())
					continue lineLoop;
				else
					free = cell;
			}

			// If we have found a line of two add the free cell in that line to the list
			if (count == 2)
				winnableMoves.add(free);
		}

		return winnableMoves;
	}

	private static List<int[]> computerFindFork(boolean mine)
	{
		List<int[]> cells      = new ArrayList<int[]>();
		List<int[]> freeCells  = new ArrayList<int[]>();
		String[][]  dummyBoard = {{"", "", ""}, {"", "", ""}, {"", "", ""}};

		// Get all the empty cells and fill the dummy board with the board data
		for (int row = 0; row < board.length; row++)
		{
			for (int col = 0; col < board.length; col++)
			{
				dummyBoard[row][col] = board[row][col];

				if (board[row][col].isEmpty())
				{
					int[] c = {row, col};
					freeCells.add(c);
				}

			}

		}

		// Check for forks by dummying the board and placing a marker on every free cell and count the possible 2-lines
		for (int[] c : freeCells)
		{
			dummyBoard[c[0]][c[1]] = (mine ? currentMarker : "XO".replace(currentMarker, "")); // "mine" means that we're looking for a fork of our own
			List<int[]> lines = computerFindWinningOrBlockingMoves((mine ? false : true),
					dummyBoard);

			// If we have found a cell that can create or block a fork, add it to the list
			if (lines.size() >= 2)
				cells.add(c);

			// Reset the dummy cell
			dummyBoard[c[0]][c[1]] = board[c[0]][c[1]];
		}

		return cells;
	}

	private static String lookForWinner()
	{
		// Check all the predefined possible winning lines if a marker has captured it fully
		for (int[][] line : winningLines)
		{
			String found = ""; // The found marker
			int    count = 0;
			for (int[] cell : line)
			{
				String marker = board[cell[0]][cell[1]];

				if (marker.isEmpty())
					break;
				else if (found.isEmpty())
				{
					found = marker;
					count++;
				}
				else if (marker.equals(found))
					count++;
				else if (!marker.equals(found))
					break;
			}

			// If a marker has a full line, return the found marker (the winner)
			if (count == 3)
				return found;
		}

		return "";
	}

	private static boolean lookForDraw()
	{
		// Loop over all cells, or until we find a free one (which means it's not a draw)
		int filledCells = 0;
		rowLoop :
		for (int y = 0; y < board.length; y++)
		{
			for (int x = 0; x < board[0].length; x++)
			{
				if (!cellIsFree(x, y))
					filledCells++;
				else // We have a free cell, abort!
					break rowLoop;
			}

		}
		// If all cells are filled we have a draw
		if (filledCells == board.length * board[0].length)
			return true;
		return false;
	}

	private static boolean cellIsFree(int row, int col)
	{
		// The cell is free if it is empty
		if (board[row][col].isBlank())
			return true;
		return false;
	}

	// Overload so that we can send either cells or row + column
	private static boolean cellIsFree(int[] cell)
	{
		if (cell.length == 2)
			return cellIsFree(cell[0], cell[1]);
		return false;
	}
}
