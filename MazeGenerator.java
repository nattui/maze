package maze;

import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This program generates mazes and solves them using Breadth-first Search and
 * Depth-first Search algorithms
 * 
 * @author Nhat Nguyen
 * @author Jasmine Mai
 */

public class MazeGenerator {

	public static void main(String[] args) {
		while (true) {
			// User Input for Maze Size
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			int size = 0;

			do {
				System.out.print("Input the size for the maze: ");
				while (!scan.hasNextInt()) {
					System.out.println("Needs a valid integer for maze size (3 or Higher)");
					System.out.print("Input the size for the maze: ");
					scan.next();
				}
				size = scan.nextInt();
			} while (size <= 2);

			System.out.println("-------------------------------------------------------------------");

			// Constructs a new 2D array
			String[][] maze2D = maze2D(size);
			// print2D(maze2D);
			// System.out.println("Generated 2D Array of Size " + size + "x" + size);
			// System.out.println();

			// Prints out new maze as 2D array
			String[][] mazeGenerated = generator(maze2D);
			// print2D(mazeGenerated);
			// System.out.println("Generated Maze as 2D Array");
			// System.out.println();

			// Prints the string representation of maze
			System.out.println(convert2D(mazeGenerated));
			System.out.println("String Representation of Generated " + size + "x" + size + " Maze");
			System.out.println();

			// Delete the Hash symbol in the maze
			mazeGenerated = emptyHash(mazeGenerated);
			// print2D(mazeGenerated);
			// System.out.println("Empty Generated Maze");
			// System.out.println();

			// Depth First Search
			String[][] mazeDFS = DFS(clone(mazeGenerated));
			print2D(mazeDFS);
			System.out.println("DFS Maze as 2D Array");

			// String representation of DFS
			System.out.println();
			System.out.println(convert2D(mazeDFS));
			System.out.println("String representation of DFS Maze");
			System.out.println();

			// Breasth First Search
			String[][] mazeBFS = BFS(clone(mazeGenerated));
			print2D(mazeBFS);
			System.out.println("BFS Maze as 2D Array");
			System.out.println();
			
			// String representation of DFS
			System.out.println(convert2D(mazeBFS));
			System.out.println("String representation of BFS Maze");
			System.out.println();

			// Queue<Cell> q = new LinkedList<Cell>();
			// q.add(new Cell(1, 1));
			// q.add(new Cell(1, 2));
			// q.add(new Cell(2, 1));
			// q.add(new Cell(2, 2));
			// System.out.println("Elements of queue: " + q);
			// System.out.println("Queue removed: " + q.remove());
			// System.out.println("Head of the queue: " + q.peek());
			// System.out.println("Elements of queue: " + q);

			// ArrayList<String> direction = new ArrayList<>();
			// Collections.addAll(direction, "NORTH", "EAST", "SOUTH", "WEST");
			// direction.add("FAKE");
			// System.out.println("Stack removed: " + direction.remove(direction.size() -
			// 1));

			System.out.println();
			// System.out.println("Press anything to repeat the program.");
			// Object repeat = scan.next();
			// //System.out.println("Press anything to repeat the program.");
		}
	}

	// Prints the 2D array into the console
	public static void print2D(String[][] array2D) {
		for (String[] row : array2D) {
			System.out.println(Arrays.toString(row));
		}
	}

	/**
	 * Clones 2D array to new 2D array
	 * 
	 * @param maze2D
	 *            is the matrix that represents the maze in 2D array
	 * @return clone2D The copy of the matrix that represents the maze in 2D array
	 */
	public static String[][] clone(String[][] maze2D) {
		String[][] clone2D = new String[maze2D.length][maze2D.length];
		for (int columnIndex = 0; columnIndex < maze2D.length; columnIndex++) {
			for (int rowIndex = 0; rowIndex < maze2D.length; rowIndex++) {
				clone2D[columnIndex][rowIndex] = maze2D[columnIndex][rowIndex];
			}
		}
		return clone2D;
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

	// Finds valid spot to move for maze generation
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
				// System.out.println("Do not go NORTH because that cell is not enclosed by
				// walls");
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
				// System.out.println("Do not go EAST because that cell is not enclosed by
				// walls");
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
				// System.out.println("Do not go SOUTH because that cell is not enclosed by
				// walls");
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
				// System.out.println("Do not go WEST because that cell is not enclosed by
				// walls");
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
				} else if (maze2D[columnIndex][rowIndex] == " " && columnIndex % 2 == 1 && rowIndex % 2 == 0) {
					// Spacing for the wall
					maze = maze + " ";
				} else if (maze2D[columnIndex][rowIndex] == " ") {
					// Spacing for the cell
					maze = maze + "   ";
				} else {
					maze = maze + " " + maze2D[columnIndex][rowIndex] + " ";
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
		maze2D[1][1] = "0";

		while (visitedCells < totalCells) {
			// Generates a unique direction
			ArrayList<String> direction = new ArrayList<>();
			Collections.addAll(direction, "NORTH", "EAST", "SOUTH", "WEST");
			Collections.shuffle(direction);

			// Finds a valid spot on the 2D array
			String random = DFSValid(maze2D, current, direction);
			// System.out.println("The FINAL DIRECTION: " + random);

			if (random == "BACKTRACK") {
				// path.remove(0);
				current = location.pop();
				continue;
			}

			current = DFSMove(maze2D, current, random, visitedCells);
			visitedCells = visitedCells + 1;
			location.push(current);

			if (current.getx() == size - 1 && current.gety() == size - 1) {
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

		// When the size of the list is 0, return "BACKTRACK"
		if (direction.size() == 0) {
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = " ";
			return "BACKTRACK";
		}

		String random = direction.remove(0);

		if (random == "NORTH") {
			if (current.gety() - 1 < 0) {
				// System.out.println("Do not go NORTH because outside of range of the 2D
				// array");
				return DFSValid(maze2D, current, direction);
			}
			if (maze2D[y - 1][x] != " ") {
				// System.out.println("Do not go NORTH because there is a wall");
				return DFSValid(maze2D, current, direction);
			}
		} else if (random == "EAST") {
			if (current.getx() + 1 >= size) {
				// System.out.println("Do not go EAST because outside of range of the 2D
				// array");
				return DFSValid(maze2D, current, direction);
			}
			if (maze2D[y][x + 1] != " ") {
				// System.out.println("Do not go EAST because there is a wall");
				return DFSValid(maze2D, current, direction);
			}
		} else if (random == "SOUTH") {
			if (current.gety() + 1 >= size) {
				// System.out.println("Do not go SOUTH because outside of range of the 2D
				// array");
				return DFSValid(maze2D, current, direction);
			}
			if (maze2D[y + 1][x] != " ") {
				// System.out.println("Do not go SOUTH because there is a wall");
				return DFSValid(maze2D, current, direction);
			}
		} else if (random == "WEST") {
			if (current.getx() - 1 < 0) {
				// System.out.println("Do not go WEST because outside of range of the 2D
				// array");
				return DFSValid(maze2D, current, direction);
			}
			if (maze2D[y][x - 1] != " ") {
				// System.out.println("Do not go WEST because there is a wall");
				return DFSValid(maze2D, current, direction);
			}
		}
		return random;
	}

	// Move DFS location
	public static Cell DFSMove(String[][] maze2D, Cell current, String random, int count) {

		// Prints out the coordinates of the current cell object
		// System.out.println(" X-coordinate: " + current.getx() + ", Y-coordinate: " +
		// current.gety());

		String path = Integer.toString(count % 10);

		if (random == "NORTH") {
			// NORTH and delete wall from bottom from next cell
			current.setNext(new Cell(current.getx(), current.gety() - 1));
			current = current.getNext();
			// Breaks the bottom wall from next cell
			maze2D[2 * current.gety() + 2][2 * current.getx() + 1] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = path;
		} else if (random == "EAST") {
			// EAST and delete wall from left from next cell
			current.setNext(new Cell(current.getx() + 1, current.gety()));
			current = current.getNext();
			// Breaks the left wall from next cell
			maze2D[2 * current.gety() + 1][2 * current.getx()] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = path;
		} else if (random == "SOUTH") {
			// SOUTH and delete wall from top from next cell
			current.setNext(new Cell(current.getx(), current.gety() + 1));
			current = current.getNext();
			// Breaks the top wall from next cell
			maze2D[2 * current.gety()][2 * current.getx() + 1] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = path;
		} else if (random == "WEST") {
			// WEST and delete wall from right from next cell
			current.setNext(new Cell(current.getx() - 1, current.gety()));
			current = current.getNext();
			// Breaks the right wall from next cell
			maze2D[2 * current.gety() + 1][2 * current.getx() + 2] = "#";

			// DEBUGGING: Visualizing
			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = path;
		}

		// DEBUGGING: Printing maze at each step
		// System.out.println("NEW X-coordinate: " + current.getx() + ", NEW
		// Y-coordinate: " + current.gety());
		// print2D(maze2D);
		// System.out.println();

		return current;

	}

	// Breadth first Search
	public static String[][] BFS(String[][] maze2D) {
		Queue<Cell> neighborQueue = new LinkedList<Cell>();

		int size = (maze2D.length - 1) / 2;
		int totalCells = size * size;
		int visitedCells = 1;
		Cell current = new Cell(0, 0);
		neighborQueue.add(current);
		maze2D[1][1] = "0";

		while (visitedCells < totalCells) {
			ArrayList<String> direction = new ArrayList<>();

			direction = BFSValid(maze2D, current);
			current = BFSMove(maze2D, current, neighborQueue, direction, visitedCells);
			visitedCells = visitedCells + 1;
			
			if (current.getx() == size - 1 && current.gety() == size - 1) {
				return maze2D;
			}
		}
		return maze2D;
	}

	// Checks if BFS is valid
	public static ArrayList<String> BFSValid(String[][] maze2D, Cell current) {
		int size = (maze2D.length - 1) / 2;
		int x = 2 * current.getx() + 1;
		int y = 2 * current.gety() + 1;

		// Generates a unique direction
		ArrayList<String> direction = new ArrayList<>();
		Collections.addAll(direction, "NORTH", "EAST", "SOUTH", "WEST");

		// System.out.println("BEGINNING: " + direction);

		// Remove NORTH
		if (current.gety() - 1 < 0) {
			// System.out.println("Do not go NORTH because outside of range of the 2D array");
			direction.remove("NORTH");
		} else if (maze2D[y - 1][x] != " " || maze2D[y - 2][x] != " ") {
			// System.out.println("Do not go NORTH because there is a wall");
			direction.remove("NORTH");
		}
		// Remove EAST
		if (current.getx() + 1 >= size) {
			// System.out.println("Do not go EAST because outside of range of the 2D array");
			direction.remove("EAST");
		} else if (maze2D[y][x + 1] != " " || maze2D[y][x + 2] != " ") {
			// System.out.println("Do not go EAST because there is a wall");
			direction.remove("EAST");
		}
		// Remove SOUTH
		if (current.gety() + 1 >= size) {
			// System.out.println("Do not go SOUTH because outside of range of the 2D array");
			direction.remove("SOUTH");
		} else if (maze2D[y + 1][x] != " " || maze2D[y + 2][x] != " ") {
			// System.out.println("Do not go SOUTH because there is a wall");
			direction.remove("SOUTH");
		}
		// Remove WEST
		if (current.getx() - 1 < 0) {
			// System.out.println("Do not go WEST because outside of range of the 2D array");
			direction.remove("WEST");
		} else if (maze2D[y][x - 1] != " " || maze2D[y][x - 2] != " ") {
			// System.out.println("Do not go WEST because there is a wall");
			direction.remove("WEST");
		}

		Collections.shuffle(direction);
		// System.out.println(direction);

		return direction;
	}

	// Move BFS location
	public static Cell BFSMove(String[][] maze2D, Cell current, Queue<Cell> neighborQueue, ArrayList<String> direction,
			int count) {

		String path = Integer.toString(count % 10);

		while (direction.size() > 0) {
			// System.out.println("Enters while loop");

			String random = direction.remove(0);
			if (random == "NORTH") {
				// System.out.println("Removes NORTH");
				neighborQueue.add(new Cell(current.getx(), current.gety() - 1));
			} else if (random == "EAST") {
				// System.out.println("Removes EAST");
				neighborQueue.add(new Cell(current.getx() + 1, current.gety()));
			} else if (random == "SOUTH") {
				// System.out.println("Removes SOUTH");
				neighborQueue.add(new Cell(current.getx(), current.gety() + 1));
			} else if (random == "WEST") {
				// System.out.println("Removes WEST");
				neighborQueue.add(new Cell(current.getx() - 1, current.gety()));
			}
		}

		// System.out.println("Elements: " + neighborQueue);
		// System.out.println();

		neighborQueue.remove();
		current = neighborQueue.peek();
		maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = path;

		return current;
	}

}
