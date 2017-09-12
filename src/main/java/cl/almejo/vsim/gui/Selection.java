package cl.almejo.vsim.gui;

import java.awt.image.BufferedImage;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public interface Selection {

	void add(Draggable Draggable);

	void remove(Draggable Draggable);

	void clear();

	BufferedImage getImage();
}
