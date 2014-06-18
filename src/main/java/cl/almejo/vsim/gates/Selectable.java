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

package cl.almejo.vsim.gates;

import java.awt.*;

public interface Selectable {
	public void select();

	public void deselect();

	public boolean isSelected();

	public boolean contains(int x, int y);

	public void drawPreview(Graphics2D graphics2D, double x, double y);
}
