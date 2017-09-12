package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gui.Draggable;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public interface Selection {

	void add(Draggable selectable);

	void remove(Draggable selectable);

	void clear();

	BufferedImage getImage();

	List<Draggable> getDraggables();

	int getX();

	int getY();
}
