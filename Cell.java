package maze;

public class Cell {
	private Cell node;
	private int x;
	private int y;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getx() {
		return x;
	}
	public int gety() {
		return y;
	}
	public Cell getNext() {
		return node;
	}
	public void setNext(Cell node) {
		this.node = node;
	}
	public String toString() {
		return "[" + x + ":" + y + "]";
	}
}
