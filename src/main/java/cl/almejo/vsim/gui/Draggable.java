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

import java.awt.image.BufferedImage;

public interface Draggable {

	public void beforeDrag();

	public void dragging();

	public void afterDrag();

	public BufferedImage getImage();
}
