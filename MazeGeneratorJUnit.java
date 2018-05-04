package maze;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Scanner;

class MazeGeneratorJUnit {

	@Test
	void test() throws IOException {

		// Ask for the size of the maze
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
		scan.close();

		MazeGenerator maze1 = new MazeGenerator(size);
		MazeGenerator maze2 = new MazeGenerator(size);

		// Shows different unique random maze for same size
		assertNotEquals(maze1, maze2);

		// Check for the total number of visited cells. This must be equal to total cells
		assertEquals(maze1.visitedCells(size), maze2.visitedCells(size));

		System.out.println();
		System.out.println("======================");
		System.out.println("Program Completed!");
		System.out.println("======================");

	}

}
