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

	/**
	 * Constructor for JUnit
	 */
	public MazeGenerator(int size) {
		System.out.println("-------------------------------------------------------------------");
		System.out.println();

		// Constructs a new 2D array
		String[][] maze2D = maze2D(size);

		// Prints out new maze as 2D array
		String[][] mazeGenerated = generator(maze2D);

		// Prints the string representation of maze
		System.out.println(convert2D(mazeGenerated));
		System.out.println("String Representation of Generated " + size + "x" + size + " Maze");

		// Delete the Hash symbol in the maze
		mazeGenerated = emptyHash(mazeGenerated);

		// Depth First Search
		String[][] mazeDFS = DFS(clone(mazeGenerated));
		System.out.println();

		// String representation of DFS
		System.out.println(convert2D(mazeDFS));
		System.out.println("String representation of DFS Maze");
		System.out.println();

		// Breadth First Search
		String[][] mazeBFS = BFS(clone(mazeGenerated));
		System.out.println(convert2D(mazeBFS));
		System.out.println("String representation of BFS Maze");
		System.out.println();

		// Creates an single path of the maze
		String[][] mazePath = backtrackingDelete(clone(mazeGenerated));
		emptyHash(mazePath);
		hashList(mazePath);

		// Prints the string representation of maze with path
		System.out.println(printPath(mazePath));
		System.out.println("Hash Single Path");
	}

	/**
	 * Prints the 2D matrix
	 * 
	 * @param array2D The 2D matrix that represents the maze
	 */
	public static void print2D(String[][] array2D) {
		for (String[] row : array2D) {
			System.out.println(Arrays.toString(row));
		}
	}

	/**
	 * Clones 2D array to new 2D array
	 * 
	 * @param maze2D
	 *            The 2D matrix that represents the maze
	 * @return clone2D The copy of the 2D matrix that represents the maze
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

	/**
	 * Creates an empty non-generated maze
	 * 
	 * @param size
	 *            The size of the maze
	 * @return maze2D The empty non-generated maze
	 */
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

	/**
	 * Creates a valid generated maze that has a path from the begining to end
	 * 
	 * @param maze2D
	 *            The empty non-generated maze
	 * @return maze2D The empty generated maze
	 */
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

	/**
	 * The valid spot returns all the valid spot given a cell location
	 * 
	 * @param maze2D
	 *            The maze from maze2D method
	 * @param current
	 *            The current located cell
	 * @param direction
	 *            The list of directions
	 * @return random The valid random direction
	 */
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

	/**
	 * Move the next cell and break the wall in between
	 * 
	 * @param maze2D
	 *            The maze from the maze2D
	 * @param current
	 *            The current located cell
	 * @param random
	 *            The valid random direction
	 * @return current The new current cell
	 */
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

	/**
	 * Returns the total amount of visited cells
	 * 
	 * @param size The size of 2D array
	 * @return visitedCell The total amount
	 */
	public int visitedCells(int size) {
		return size*size;
	}
	
	/**
	 * Converts 2D array maze to the string representation
	 * 
	 * @param maze2D
	 *            The maze that will be convert to string representation
	 * @return maze The string representation of the maze
	 */
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

	/**
	 * Delete all the hash symbols in the maze
	 * 
	 * @param maze2D
	 *            The maze with hash symbols
	 * @return maze2D The maze with deleted hash symbols
	 */
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

	/**
	 * Depth-first Search (DFS)
	 * 
	 * @param maze2D
	 *            The empty generated maze
	 * @return maze2D The DFS generated maze
	 */
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

	/**
	 * Checks if direction is valid in DFS
	 * 
	 * @param maze2D
	 *            The maze from DFS method
	 * @param current
	 *            The current located cell
	 * @param direction
	 *            The list of directions
	 * @return random The valid random direction
	 */
	public static String DFSValid(String[][] maze2D, Cell current, ArrayList<String> direction) {
		int size = (maze2D.length - 1) / 2;
		int x = 2 * current.getx() + 1;
		int y = 2 * current.gety() + 1;

		// When the size of the list is 0, return "BACKTRACK"
		if (direction.size() == 0) {
			// maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = " ";
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

	/**
	 * Move to the direction given in DFS
	 * 
	 * @param maze2D
	 *            The maze from DFS method
	 * @param current
	 *            The current located cell
	 * @param random
	 *            The valid random direction
	 * @param count
	 *            The number presented in each cell
	 * @return current The new current cell
	 */
	public static Cell DFSMove(String[][] maze2D, Cell current, String random, int count) {

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

	/**
	 * Breadth-first Search (BFS)
	 * 
	 * @param maze2D
	 *            The empty generated maze
	 * @return maze2D The BFS generated maze
	 */
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

	/**
	 * Checks if direction is valid in BFS
	 * 
	 * @param maze2D
	 *            The maze from DFS method
	 * @param current
	 *            The current located cell
	 * @return direction The valid random direction
	 */
	public static ArrayList<String> BFSValid(String[][] maze2D, Cell current) {
		int size = (maze2D.length - 1) / 2;
		int x = 2 * current.getx() + 1;
		int y = 2 * current.gety() + 1;

		// Generates a unique direction
		ArrayList<String> direction = new ArrayList<>();
		Collections.addAll(direction, "NORTH", "EAST", "SOUTH", "WEST");

		// Removes NORTH
		if (current.gety() - 1 < 0) {
			// System.out.println("Do not go NORTH because outside of range of the 2D
			// array");
			direction.remove("NORTH");
		} else if (maze2D[y - 1][x] != " " || maze2D[y - 2][x] != " ") {
			// System.out.println("Do not go NORTH because there is a wall");
			direction.remove("NORTH");
		}
		// Removes EAST
		if (current.getx() + 1 >= size) {
			// System.out.println("Do not go EAST because outside of range of the 2D
			// array");
			direction.remove("EAST");
		} else if (maze2D[y][x + 1] != " " || maze2D[y][x + 2] != " ") {
			// System.out.println("Do not go EAST because there is a wall");
			direction.remove("EAST");
		}
		// Removes SOUTH
		if (current.gety() + 1 >= size) {
			// System.out.println("Do not go SOUTH because outside of range of the 2D
			// array");
			direction.remove("SOUTH");
		} else if (maze2D[y + 1][x] != " " || maze2D[y + 2][x] != " ") {
			// System.out.println("Do not go SOUTH because there is a wall");
			direction.remove("SOUTH");
		}
		// Removes WEST
		if (current.getx() - 1 < 0) {
			// System.out.println("Do not go WEST because outside of range of the 2D
			// array");
			direction.remove("WEST");
		} else if (maze2D[y][x - 1] != " " || maze2D[y][x - 2] != " ") {
			// System.out.println("Do not go WEST because there is a wall");
			direction.remove("WEST");
		}

		Collections.shuffle(direction);

		return direction;
	}

	/**
	 * Move to the direction given in BFS
	 * 
	 * @param maze2D
	 *            The maze from BFS method
	 * @param current
	 *            The current located cell
	 * @param neighborQueue
	 *            The queue for all the neighbors
	 * @param direction
	 *            The valid random direction
	 * @param count
	 *            The number presented in each cell
	 * @return current The new current cell
	 */
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

	/**
	 * Deletes the numbers from the bracktracking
	 * 
	 * @param maze2D
	 *            The DFS or BFS maze
	 * @return maze2D The maze with a single path
	 */
	public static String[][] backtrackingDelete(String[][] maze2D) {
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
				maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = " ";
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

	/**
	 * Delete every other number not on path and convert path to hash
	 * 
	 * @param maze2D
	 *            The maze2D from after process through backtrackingDelete method
	 * @return path The list the cell locations
	 */
	public static ArrayList<Cell> hashList(String[][] maze2D) {
		int size = (maze2D.length - 1) / 2;
		ArrayList<Cell> path = new ArrayList<>();
		Cell current = new Cell(0, 0);
		path.add(current);

		while (current.getx() != size - 1 || current.gety() != size - 1) {

			// NORTH
			if (current.gety() - 1 < 0) {
				// Do Nothing
			} else if (maze2D[2 * current.gety()][2 * current.getx() + 1] == " "
					&& maze2D[2 * current.gety() - 1][2 * current.getx() + 1] != " "
					&& maze2D[2 * current.gety() - 1][2 * current.getx() + 1] != "#") {

				path.add(new Cell(current.getx(), current.gety() - 1));
				current.setNext(new Cell(current.getx(), current.gety() - 1));

			}

			// EAST
			if (current.getx() + 1 >= size) {
				// Do Nothing
			} else if (maze2D[2 * current.gety() + 1][2 * current.getx() + 2] == " "
					&& maze2D[2 * current.gety() + 1][2 * current.getx() + 3] != " "
					&& maze2D[2 * current.gety() + 1][2 * current.getx() + 3] != "#") {

				path.add(new Cell(current.getx() + 1, current.gety()));
				current.setNext(new Cell(current.getx() + 1, current.gety()));
			}

			// SOUTH
			if (current.gety() + 1 >= size) {
				// Do Nothing
			} else if (maze2D[2 * current.gety() + 2][2 * current.getx() + 1] == " "
					&& maze2D[2 * current.gety() + 3][2 * current.getx() + 1] != " "
					&& maze2D[2 * current.gety() + 3][2 * current.getx() + 1] != "#") {

				path.add(new Cell(current.getx(), current.gety() + 1));
				current.setNext(new Cell(current.getx(), current.gety() + 1));
			}

			// WEST
			if (current.getx() - 1 < 0) {
				// Do Nothing
			} else if (maze2D[2 * current.gety() + 1][2 * current.getx()] == " "
					&& maze2D[2 * current.gety() + 1][2 * current.getx() - 1] != " "
					&& maze2D[2 * current.gety() + 1][2 * current.getx() - 1] != "#") {

				path.add(new Cell(current.getx() - 1, current.gety()));
				current.setNext(new Cell(current.getx() - 1, current.gety()));
			}

			maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";
			current = current.getNext();
		}

		maze2D[2 * current.gety() + 1][2 * current.getx() + 1] = "#";

		// Deletes all the extra numbers
		for (int columnIndex = 0; columnIndex < maze2D.length; columnIndex++) {
			for (int rowIndex = 0; rowIndex < maze2D.length; rowIndex++) {
				if (!(maze2D[columnIndex][rowIndex] == "+" || maze2D[columnIndex][rowIndex] == "-"
						|| maze2D[columnIndex][rowIndex] == "|" || maze2D[columnIndex][rowIndex] == "#")) {
					maze2D[columnIndex][rowIndex] = " ";
				}
			}
		}

		return path;
	}

	/**
	 * Special printing method for hash path
	 * 
	 * @param maze2D
	 *            The maze2D from after process through hashList method
	 * @return maze The string representation of the maze
	 */
	public static String printPath(String[][] maze2D) {
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
						maze = maze + " # ";
					}
				} else if (maze2D[columnIndex][rowIndex] == "#" && columnIndex % 2 == 0) {
					// Hash symbol and column is even
					maze = maze + " # ";
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
	
	/**
	 * Main method for running the program and displaying to the console
	 */
	public static void main(String[] args) {
		// Repeat Program
		while (true) {
			System.out.println("-------------------------------------------------------------------");
			// User Input for Maze Size
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			int size = 0;

			do {
				System.out.print("Input the size for the maze: ");
				while (!scan.hasNextInt()) {
					// Repeat message when bad input
					System.out.println("Needs a valid integer for maze size (3 or Higher)");
					System.out.print("Input the size for the maze: ");
					scan.next();
				}
				size = scan.nextInt();
			} while (size <= 2);
			
			System.out.println();

			// Constructs a new 2D array
			String[][] maze2D = maze2D(size);

			// Prints out new maze as 2D array
			String[][] mazeGenerated = generator(maze2D);

			// Prints the string representation of maze
			System.out.println(convert2D(mazeGenerated));
			System.out.println("String Representation of Generated " + size + "x" + size + " Maze");

			// Delete the Hash symbol in the maze
			mazeGenerated = emptyHash(mazeGenerated);

			// Depth First Search
			String[][] mazeDFS = DFS(clone(mazeGenerated));
			System.out.println();

			// String representation of DFS
			System.out.println(convert2D(mazeDFS));
			System.out.println("String representation of DFS Maze");
			System.out.println();

			// Breadth First Search
			String[][] mazeBFS = BFS(clone(mazeGenerated));
			System.out.println(convert2D(mazeBFS));
			System.out.println("String representation of BFS Maze");
			System.out.println();

			// Creates an single path of the maze
			String[][] mazePath = backtrackingDelete(clone(mazeGenerated));
			emptyHash(mazePath);
			hashList(mazePath);

			// Prints the string representation of maze with path
			System.out.println(printPath(mazePath));
			System.out.println("Hash Single Path");
		}
	}
}
