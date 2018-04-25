package sjsu.nguyen.cs146.project2;

public class Cell {
	private Cell node;
	private int x;
	private int y;
	private boolean visited;
	
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
	public boolean isVisited() {
		return visited;
	}
	public void setVisited() {
		visited = true;
	}
}
