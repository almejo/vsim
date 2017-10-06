package cl.almejo.vsim.gui;

import java.awt.*;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public interface Selectable {

	void beforeDrag();

	void drag(int x, int y);

	void afterDrag();

	int getOriginalX();

	int getOriginalY();

	void select();

	void deselect();

	boolean isSelected();

	boolean contains(int x, int y);

	void drawPreview(Graphics2D graphics2D, double x, double y);

	Rectangle getExtent();
}
