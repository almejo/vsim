package cl.almejo.vsim.circuit;

import java.awt.*;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class Selection extends Rectangle {
	Selection() {
		setBounds(0, 0, 0, 0);
	}

	//	void add(Selectable selectable);
//
//	void remove(Selectable selectable);
//
	void clear() {
		setBounds(0, 0, 0, 0);
	}

	void setStart(int x, int y) {
		setLocation(x, y);
		setSize(0, 0);
	}

	void setEnd(int x, int y) {
		setSize((int) (x - getX()), (int) (y - getY()));
	}

//
//	BufferedImage getImage();
//
//	List<Selectable> getSelectables();
//
//	int getX();
//
//	int getY();
}
