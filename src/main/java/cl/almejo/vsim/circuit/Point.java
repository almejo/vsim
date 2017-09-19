package cl.almejo.vsim.circuit;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
@Accessors(prefix = "_")
@Getter
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

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (!(object instanceof Point)) {
			return false;
		}
		Point point = (Point) object;
		return point._x == _x && point._y == _y;
	}

}
