package maze;

/**
 * @author Nhat Nguyen
 * @author Jasmine Mai
 */
public class Path {
	private int number;
	private String direction;

	public Path(int number, String direction) {
		this.setNumber(number);
		this.setDirection(direction);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
