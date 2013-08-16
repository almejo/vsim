package cl.almejo.vsim.circuit;

public class Point {
	int _x;
	int _y;

	public Point(int x, int y) {
		_x = x;
		_y = y;
	}

	@Override
	public String toString() {
		return "(" + _x + ", " + _y + ")";
	}

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}
}
