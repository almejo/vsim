package cl.almejo.vsim.circuit;

import lombok.Getter;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
@Getter
public class Point {
	protected int x;
	protected int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
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
		return point.x == x && point.y == y;
	}

}
