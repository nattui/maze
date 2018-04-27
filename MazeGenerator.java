package maze;

import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MazeGenerator {

	public static void main(String[] args) {
		@SuppressWarnings("resource")

		// User input a size for the maze
		Scanner scan = new Scanner(System.in);
		System.out.print("Input a number: ");
		int size = scan.nextInt();

		// Constructs a new 2D array
		String[][] maze2D = maze2D(size);
		print2D(maze2D);
		System.out.println("Generated 2D Array with size " + size);

		// Prints out new maze
		String[][] mazeGenerated = generator(maze2D);
		print2D(mazeGenerated);
		System.out.println("Generated Maze");

		// Prints the string representation of maze
		String mazeGeneratedStr = convert2D(mazeGenerated);
		System.out.println();
		System.out.println(mazeGeneratedStr);
		System.out.println("String representation of the generated maze");
		System.out.println();

		// Delete the Hash symbol in the maze
		mazeGenerated = emptyHash(mazeGenerated);
		print2D(mazeGenerated);
		System.out.println("Empty Generated Maze");
		System.out.println();
		
		// Depth First Search
		String[][] mazeDFS = DFS(mazeGenerated);
		print2D(mazeDFS);
		System.out.println("HELLO");
	}

	// Prints the 2D array into the console
	public static void print2D(String[][] array2D) {
		for (String[] row : array2D) {
			System.out.println(Arrays.toString(row));
		}
	}

	// Creates a 2D array with walls
	public static String[][] maze2D(int size) {
		String[][] maze2D = new String[2 * size + 1][2 * size + 1];
		// 2D Array
		for (int columnIndex = 0; columnIndex < (2 * size + 1); columnIndex++) {
			for (int rowIndex = 0; rowIndex < (2 * size + 1); rowIndex++) {
				// Start of maze
				if (rowIndex == 1 && columnIndex == 0) {
					maze2D[columnIndex][rowIndex] = "S";
					// End of maze
				} else if (rowIndex == 2 * size - 1 && columnIndex == 2 * size) {
					maze2D[columnIndex][rowIndex] = "E";
					// Row is even
				} else if (rowIndex % 2 == 0) {
					// Column is even
					if (columnIndex % 2 == 0) {
						maze2D[columnIndex][rowIndex] = "+";
						// Column is odd
					} else {
						maze2D[columnIndex][rowIndex] = "|";
					}
					// Row is odd
				} else {
					// Column is even
					if (columnIndex % 2 == 0) {
						maze2D[columnIndex][rowIndex] = "-";
						// Column is odd
					} else {
						maze2D[columnIndex][rowIndex] = "0";
					}
				}
			}
		}

		return maze2D;
	}

	// Generates a maze
	public static String[][] generator(String[][] maze2D) {

		// Line Separator
		System.out.println("-------------------------------------------------------------------");

		Stack<Cell> location = new Stack<Cell>();
		int size = (maze2D.length - 1) / 2;
		int totalCells = size * size;
		int visitedCells = 1;
		Cell current = new Cell(0, 0);

		System.out.println("SIZE: " + size);
		System.out.println("VISITED CELLS: " + visitedCells);
		System.out.println();

		while (visitedCells < totalCells) {

			// Generates a unique direction
			ArrayList<String> direction = new ArrayList<>();
			Collections.addAll(direction, "NORTH", "EAST", "SOUTH", "WEST");
			Collections.shuffle(direction);

			String random = validSpot(maze2D, current, direction);

			if (random == "BACKTRACK") {
				// // DEBUGGING: Prints BACKTRACKING
				// System.out.println("\t PROCESSS: " + random);

				current = location.pop();
				continue;
			}

			current = move(maze2D, current, random);
			visitedCells = visitedCells + 1;
			location.push(current);
		}

		return maze2D;
	}

	// Finds valid spot to move
	public static String validSpot(String[][] maze2D, Cell current, ArrayList<String> direction) {
		int size = (maze2D.length - 1) / 2;

		int x = 2 * current.getx() + 1;
		int y = 2 * current.gety() + 1;

		// When the size of the list is 0, return -1
		if (direction.size() == 0) {
			return "BACKTRACK";
		}

		String random = direction.remove(0);

		// // DEBUGGING: Prints current direction
		// System.out.println("DIRECTION: " + random);

		if (random == "NORTH") {
			if (current.gety() - 1 < 0) {
				// System.out.println("Do not go NORTH because outside of range of the 2D
				// array");
				return validSpot(maze2D, current, direction);
			}
			if ((maze2D[y - 3][x] == "#" || maze2D[y - 1][x] == "#")
					|| (maze2D[y - 2][x - 1] == "#" || maze2D[y - 2][x + 1] == "#")) {
				// System.out.println("Do not go NORTH because that cell is not enclosed by walls");
				return validSpot(maze2D, current, direction);
			}
		} else if (random == "EAST") {
			if (current.getx() + 1 >= size) {
				// System.out.println("Do not go EAST because outside of range of the 2D
				// array");
				return validSpot(maze2D, current, direction);
			}
			if (((maze2D[y + 1][x + 2] == "#" || maze2D[y - 1][x + 2] == "#")
					|| (maze2D[y][x + 1] == "#" || maze2D[y][x + 3] == "#"))) {
				// System.out.println("Do not go EAST because that cell is not enclosed by walls");
				return validSpot(maze2D, current, direction);
			}
		} else if (random == "SOUTH") {
			if (current.gety() + 1 >= size) {
				// System.out.println("Do not go SOUTH because outside of range of the 2D
				// array");
				return validSpot(maze2D, current, direction);
			}
			if (((maze2D[y + 1][x] == "#" || maze2D[y + 3][x] == "#")
					|| (maze2D[y + 2][x - 1] == "#" || maze2D[y + 2][x + 1] == "#"))) {
				// System.out.println("Do not go SOUTH because that cell is not enclosed by walls");
				return validSpot(maze2D, current, direction);
			}
		} else if (random == "WEST") {
			if (current.getx() - 1 < 0) {
				// System.out.println("Do not go WEST because outside of range of the 2D
				// array");
				return validSpot(maze2D, current, direction);
			}
			if (((maze2D[y - 1][x - 2] == "#" || maze2D[y + 1][x - 2] == "#")
					|| (maze2D[y][x - 3] == "#" || maze2D[y][x - 1] == "#"))) {
				// System.out.println("Do not go WEST because that cell is not enclosed by walls");
				return validSpot(maze2D, current, direction);
			}
		}
		return random;
	}

	// Moves the next cell and breaks the wall in between
	public static Cell move(String[][] maze2D, Cell current, String random) {

		// // Prints out the coordinates of the current cell object
		// System.out.println(" X-coordinate: " + current.getx() + ", Y-coordinate: " +
		// current.gety());

		maze2D[1][1] = "#";

		if (random == "NORTH") {
			// NORTH and delete wall from bottom from next cell
			current.setNext(new Cell(current.getx(), current.gety() - 1));
			current = current.getNext();
			// Breaks the bottom wall from next cell
			maze2D[2 * current.gety() + 2][2 * current.getx() + 1] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";
		} else if (random == "EAST") {
			// EAST and delete wall from left from next cell
			current.setNext(new Cell(current.getx() + 1, current.gety()));
			current = current.getNext();
			// Breaks the left wall from next cell
			maze2D[2 * current.gety() + 1][2 * current.getx()] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";
		} else if (random == "SOUTH") {
			// SOUTH and delete wall from top from next cell
			current.setNext(new Cell(current.getx(), current.gety() + 1));
			current = current.getNext();
			// Breaks the top wall from next cell
			maze2D[2 * current.gety()][2 * current.getx() + 1] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";
		} else if (random == "WEST") {
			// WEST and delete wall from right from next cell
			current.setNext(new Cell(current.getx() - 1, current.gety()));
			current = current.getNext();
			// Breaks the right wall from next cell
			maze2D[2 * current.gety() + 1][2 * current.getx() + 2] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";
		}

		// // DEBUGGING: Printing maze at each step
		// System.out.println("NEW X-coordinate: " + current.getx() + ", NEW
		// Y-coordinate: " + current.gety());
		// for (String[] row : maze2D) {
		// System.out.println(Arrays.toString(row));
		// }
		// System.out.println();

		return current;
	}

	// Converts 2D array maze to string representation
	public static String convert2D(String[][] maze2D) {
		String maze = "";
		int size = maze2D.length;
		for (int columnIndex = 0; columnIndex < size; columnIndex++) {
			for (int rowIndex = 0; rowIndex < size; rowIndex++) {
				if (maze2D[columnIndex][rowIndex] == "+") {
					maze = maze + "+";
				} else if (maze2D[columnIndex][rowIndex] == "-") {
					maze = maze + "---";
				} else if (maze2D[columnIndex][rowIndex] == "|") {
					maze = maze + "|";
				} else if (maze2D[columnIndex][rowIndex] == "#" && columnIndex % 2 == 1) {
					// Hash symbol and column is odd
					if (rowIndex % 2 == 0) {
						maze = maze + " ";
					} else if (rowIndex % 2 == 1) {
						maze = maze + "   ";
					}
				} else if (maze2D[columnIndex][rowIndex] == "#" && columnIndex % 2 == 0) {
					// Hash symbol and column is even
					maze = maze + "   ";
				} else if (maze2D[columnIndex][rowIndex] == "S" || maze2D[columnIndex][rowIndex] == "E") {
					maze = maze + "   ";
				}

				// When rowIndex is at end AND columnIndex is not at end, add a new line
				if (rowIndex == (size - 1) && columnIndex != (size - 1)) {
					maze = maze + System.lineSeparator();
				}
			}
		}
		return maze;
	}

	// Delete the Hash symbol in the maze
	public static String[][] emptyHash(String[][] maze2D) {
		int size = maze2D.length;
		for (int columnIndex = 0; columnIndex < size; columnIndex++) {
			for (int rowIndex = 0; rowIndex < size; rowIndex++) {
				if (maze2D[columnIndex][rowIndex] == "#") {
					maze2D[columnIndex][rowIndex] = " ";
				}
			}

		}
		return maze2D;
	}

	// Depth first Search
	public static String[][] DFS(String[][] maze2D) {
		Stack<Cell> location = new Stack<Cell>();
		int size = (maze2D.length - 1) / 2;
		int totalCells = size * size;
		int visitedCells = 1;
		Cell current = new Cell(0, 0);

		while (visitedCells < totalCells) {
			// Generates a unique direction
			ArrayList<String> direction = new ArrayList<>();
			Collections.addAll(direction, "NORTH", "EAST", "SOUTH", "WEST");
			Collections.shuffle(direction);

			// Finds a valid spot on the 2D array
			String random = validSpot(maze2D, current, direction);

			if (random == "BACKTRACK") {
				// // DEBUGGING: Prints BACKTRACKING
				// System.out.println("\t PROCESSS: " + random);
				current = location.pop();
				continue;
			}
			
			current = moveDFS(maze2D, current, random);
			visitedCells = visitedCells + 1;
			location.push(current);
			
			System.out.println(" X-coordinate: " + current.getx() + ", Y-coordinate: " + current.gety());
			if (current.getx() == size-1 && current.gety() == size-1) {
				return maze2D;
			}
		}

		return maze2D;
	}

	// Checks if DFS is valid
	public static String DFSValid(String[][] maze2D, Cell current, ArrayList<String> direction) {
		int size = (maze2D.length - 1) / 2;

		int x = 2 * current.getx() + 1;
		int y = 2 * current.gety() + 1;

		// When the size of the list is 0, return -1
		if (direction.size() == 0) {
			maze2D[y][x] = "@";
			return "BACKTRACK";
		}

		String random = direction.remove(0);

		// DEBUGGING: Prints current direction
		System.out.println("DIRECTION: " + random);

		if (random == "NORTH") {
			if (current.gety() - 1 < 0) {
				// System.out.println("Do not go NORTH because outside of range of the 2D
				// array");
				return validSpot(maze2D, current, direction);
			}
			if (maze2D[y-1][x] == " ") {
				return random;
			}
		} else if (random == "EAST") {
			if (current.getx() + 1 >= size) {
				// System.out.println("Do not go EAST because outside of range of the 2D
				// array");
				return validSpot(maze2D, current, direction);
			}
			if (maze2D[y][x+1] == " ") {
				return random;
			}
		} else if (random == "SOUTH") {
			if (current.gety() + 1 >= size) {
				// System.out.println("Do not go SOUTH because outside of range of the 2D
				// array");
				return validSpot(maze2D, current, direction);
			}
			if (maze2D[y+1][x] == " ") {
				return random;
			}
		} else if (random == "WEST") {
			if (current.getx() - 1 < 0) {
				// System.out.println("Do not go WEST because outside of range of the 2D
				// array");
				return validSpot(maze2D, current, direction);
			}
			if (maze2D[y][x-1] == " ") {
				return random;
			}
		}
		return random;
	}
	
	public static Cell moveDFS(String[][] maze2D, Cell current, String random) {

		// // Prints out the coordinates of the current cell object
		// System.out.println(" X-coordinate: " + current.getx() + ", Y-coordinate: " +
		// current.gety());

		maze2D[1][1] = "#";

		if (random == "NORTH") {
			// NORTH and delete wall from bottom from next cell
			current.setNext(new Cell(current.getx(), current.gety() - 1));
			current = current.getNext();
			// Breaks the bottom wall from next cell
			maze2D[2 * current.gety() + 2][2 * current.getx() + 1] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";
		} else if (random == "EAST") {
			// EAST and delete wall from left from next cell
			current.setNext(new Cell(current.getx() + 1, current.gety()));
			current = current.getNext();
			// Breaks the left wall from next cell
			maze2D[2 * current.gety() + 1][2 * current.getx()] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";
		} else if (random == "SOUTH") {
			// SOUTH and delete wall from top from next cell
			current.setNext(new Cell(current.getx(), current.gety() + 1));
			current = current.getNext();
			// Breaks the top wall from next cell
			maze2D[2 * current.gety()][2 * current.getx() + 1] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";
		} else if (random == "WEST") {
			// WEST and delete wall from right from next cell
			current.setNext(new Cell(current.getx() - 1, current.gety()));
			current = current.getNext();
			// Breaks the right wall from next cell
			maze2D[2 * current.gety() + 1][2 * current.getx() + 2] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";
		}

		// // DEBUGGING: Printing maze at each step
		// System.out.println("NEW X-coordinate: " + current.getx() + ", NEW
		// Y-coordinate: " + current.gety());
		// for (String[] row : maze2D) {
		// System.out.println(Arrays.toString(row));
		// }
		// System.out.println();

		return current;
	}
	
}
