/**
 *
 * vsim
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.gui;

import java.awt.*;

public interface Draggable {

	public void beforeDrag();

	public void drag(int x, int y);

	public void afterDrag();

	int getOriginalX();

	int getOriginalY();

	public void select();

	public void deselect();

	public boolean isSelected();

	public boolean contains(int x, int y);

	public void drawPreview(Graphics2D graphics2D, double x, double y);
}
