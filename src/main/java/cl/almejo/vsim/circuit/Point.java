package cl.almejo.vsim.circuit;

public class Point {
	int _x;
	int _y;
	private int _conectionMask;

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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		Point p = (Point) obj;
		return p._x == _x && p._y == _y;
	}

	public boolean isConnected(byte direction) {
		return (_conectionMask & direction) != 0;
	}

	public void connect(byte direction) {
		_conectionMask = (byte) _conectionMask | direction;
	}
}
