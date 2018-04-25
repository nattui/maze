package sjsu.nguyen.cs146.project2;

import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;

public class MazeGenerator {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.print("Input a number: ");
		int size = scan.nextInt();
		System.out.println("The size: " + size);

		// Prints the string representation of the maze
		System.out.println();
		System.out.print(maze(size));
		System.out.println();

		String[][] maze2D = maze2D(size);

		// Prints the 2D Array of the maze
		for (String[] row : maze2D) {
			System.out.println(Arrays.toString(row));
		}

		System.out.println();
		System.out.println("DEBUGGED: " + maze2D[2][1]);
		System.out.println();

		// Stack Example
		Stack<Cell> location = new Stack<Cell>();
		location.push(new Cell(1, 1));
		Cell v = location.pop();

		System.out.println();
		System.out.println("X-coordinate: " + v.getx() + ", Y-coordinate: " + v.gety());
		System.out.println("isVisited: " + v.isVisited());

		// 0 is NORTH
		// 1 is EAST
		// 2 is SOUTH
		// 3 is WEST

		// Prints 4 unique random numbers in the range 0 to 3
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);
		// Prints the every element in the list
		for (int elements : list) {
			System.out.println("The element: " + elements);
		}

		System.out.println("The size: " + list.size());

		int removed = list.remove(0);

		// Prints the every element in the list
		for (int elements : list) {
			System.out.println("Element: " + elements);
		}

		// Direction Unique Random
		ArrayList<String> direction = new ArrayList<>();
		Collections.addAll(direction, "NORTH", "EAST", "SOUTH", "WEST");
		Collections.shuffle(direction);
		for (String elements : direction) {
			System.out.println("DIRECTION: " + elements);
		}

		String dir = direction.remove(0);
		System.out.println(dir == "NORTH");
		System.out.println(dir == "SOUTH");
		System.out.println(dir == "EAST");
		System.out.println(dir == "WEST");

		System.out.println("Removed: " + removed);
		System.out.println();

		// Prints out new maze
		String[][] newMaze = generator(maze2D, size);

		for (String[] row : newMaze) {
			System.out.println(Arrays.toString(row));
		}
	}

	// String representation of maze
	public static String maze(int size) {
		String maze = "";
		// 2D Array
		for (int rowIndex = 0; rowIndex < (2 * size + 1); rowIndex++) {
			for (int columnIndex = 0; columnIndex < (2 * size + 1); columnIndex++) {
				// Start of maze
				if (rowIndex == 0 && columnIndex == 1) {
					maze = maze + "   ";
					// End of maze
				} else if (rowIndex == 2 * size && columnIndex == 2 * size - 1) {
					maze = maze + "   ";
					// Row is even
				} else if (rowIndex % 2 == 0) {
					// Column is even
					if (columnIndex % 2 == 0) {
						maze = maze + "+";
						// Column is odd
					} else {
						maze = maze + "---";
					}
					// Row is odd
				} else {
					// Column is even
					if (columnIndex % 2 == 0) {
						maze = maze + "|";
						// Column is odd
					} else {
						maze = maze + "   ";
					}
				}

				// When columnIndex is at end, makes new line
				if (columnIndex == (2 * size)) {
					maze = maze + System.lineSeparator();
				}
			}
		}

		return maze;
	}

	// Converts maze into a 2D array
	public static String[][] maze2D(int size) {
		String[][] maze2D = new String[2 * size + 1][2 * size + 1];
		// 2D Array
		for (int rowIndex = 0; rowIndex < (2 * size + 1); rowIndex++) {
			for (int columnIndex = 0; columnIndex < (2 * size + 1); columnIndex++) {
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

	// Generator
	public static String[][] generator(String[][] maze2D, int size) {

		// Line Separator
		System.out.println("-------------------------------------------------------------------");
		

		Stack<Cell> location = new Stack<Cell>();
		int totalCells = size * size;
		int visitedCells = 1;
		Cell current = new Cell(0, 0);

		System.out.println("SIZE: " + size);
		System.out.println("VISITED CELLS: " + visitedCells);
		System.out.println();
		// NEED EDIT
		while (visitedCells < totalCells) {

			// Generate unique random integers in range 0 to 3
			ArrayList<String> direction = new ArrayList<>();
			Collections.addAll(direction, "NORTH", "EAST", "SOUTH", "WEST");
			Collections.shuffle(direction);

			String random = validSpot(maze2D, size, current, direction);

			if (random == "BACKTRACK") {
				
				// DEBUGGING: Prints BACKTRACKING
				System.out.println("\t PROCESSS: " + random);
				
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
	public static String validSpot(String[][] maze2D, int size, Cell current, ArrayList<String> direction) {
		int x = 2 * current.getx() + 1;
		int y = 2 * current.gety() + 1;

		// When the size of the list is 0, return -1
		if (direction.size() == 0) {
			return "BACKTRACK";
		}

		String random = direction.remove(0);

		// DEBUGGING: Prints current direction
		System.out.println("DIRECTION: " + random);

		if (random == "NORTH") {
			if (current.gety() - 1 < 0) {
				System.out.println("Do not go NORTH because outside of range of the 2D array");
				return validSpot(maze2D, size, current, direction);
			}
			if (!((maze2D[y - 3][x] == "-" && maze2D[y - 1][x] == "-")
					&& (maze2D[y - 2][x - 1] == "|" && maze2D[y - 2][x + 1] == "|"))) {
				System.out.println("Do not go NORTH because that cell is enclosed by walls");
				return validSpot(maze2D, size, current, direction);
			}
		} else if (random == "EAST") {
			if (current.getx() + 1 >= size) {
				System.out.println("Do not go EAST because outside of range of the 2D array");
				return validSpot(maze2D, size, current, direction);
			}
			if (!((maze2D[y + 1][x + 2] == "-" && maze2D[y - 1][x + 2] == "-")
					&& (maze2D[y][x + 1] == "|" && maze2D[y][x + 3] == "|"))) {
				System.out.println("Do not go EAST because that cell is enclosed by walls");
				return validSpot(maze2D, size, current, direction);
			}
		} else if (random == "SOUTH") {
			if (current.gety() + 1 >= size) {
				System.out.println("Do not go SOUTH because outside of range of the 2D array");
				return validSpot(maze2D, size, current, direction);
			}
			if (!((maze2D[y + 1][x] == "-" && maze2D[y + 3][x] == "-")
					&& (maze2D[y + 2][x - 1] == "|" && maze2D[y + 2][x + 1] == "|"))) {
				System.out.println("Do not go SOUTH because that cell is enclosed by walls");
				return validSpot(maze2D, size, current, direction);
			}
		} else if (random == "WEST") {
			if (current.getx() - 1 < 0) {
				System.out.println("Do not go WEST because outside of range of the 2D array");
				return validSpot(maze2D, size, current, direction);
			}
			if (!((maze2D[y - 1][x - 2] == "-" && maze2D[y + 1][x - 2] == "-")
					&& (maze2D[y][x - 3] == "|" && maze2D[y][x - 1] == "|"))) {
				System.out.println("Do not go WEST because that cell is enclosed by walls");
				return validSpot(maze2D, size, current, direction);
			}
		}
		return random;
	}

	// Moves the next cell and breaks the wall in between
	public static Cell move(String[][] maze2D, Cell current, String random) {

		// Prints out the coordinates of the current cell object
		System.out.println("    X-coordinate: " + current.getx() + ",     Y-coordinate: " + current.gety());

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

		System.out.println("NEW X-coordinate: " + current.getx() + ", NEW Y-coordinate: " + current.gety());
		for (String[] row : maze2D) {
			System.out.println(Arrays.toString(row));
		}
		System.out.println();
		return current;
	}
}
