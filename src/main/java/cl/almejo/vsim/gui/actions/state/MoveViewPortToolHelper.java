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

package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.gui.SimWindow;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MoveViewPortToolHelper extends ActionToolHelper {
	private int _lastX;
	private int _lastY;

	@Override
	public void mouseDown(SimWindow window, MouseEvent event) {
		_lastX = event.getX();
		_lastY = event.getY();
		window.getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		window.getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseDoubleClicked(SimWindow window, MouseEvent event) {

	}

	public void mouseDragged(SimWindow window, MouseEvent event) {
		System.out.println((event.getX() - _lastX) + " " + (_lastY - event.getY()));
		window.getCanvas().moveViewport(event.getX() - _lastX, event.getY() - _lastY);
		_lastX = event.getX();
		_lastY = event.getY();
	}
}
