package maze;

/**
 * The Cell class keeps the location and set and get the next Cell
 * 
 * @author Nhat Nguyen
 * @author Jasmine Mai
 */
public class Cell {
	private Cell node;
	private int x;
	private int y;

	/**
	 * Constructor has the location of each Cell
	 * 
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x-coordinate
	 * 
	 * @return x The x-coordinate
	 */
	public int getx() {
		return x;
	}

	/**
	 * Gets the y-coordinate
	 * 
	 * @return y The y-coordinate
	 */
	public int gety() {
		return y;
	}

	/**
	 * Gets the next node
	 * 
	 * @return node The next node
	 */
	public Cell getNext() {
		return node;
	}

	/**
	 * Sets the next node
	 * 
	 * @param node The next node being set
	 */
	public void setNext(Cell node) {
		this.node = node;
	}

	/**
	 * String Representation of the Cell class 
	 */
	public String toString() {
		return "[" + x + ":" + y + "]";
	}
}
