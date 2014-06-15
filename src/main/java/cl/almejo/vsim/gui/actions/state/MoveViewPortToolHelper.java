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

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MoveViewPortToolHelper extends ActionToolHelper {
	private int _lastX;
	private int _lastY;

	private int _accumulatedX;
	private int _accumulatedY;

	@Override
	public void mouseDown(SimWindow window, MouseEvent event) {
		_lastX = event.getX();
		_lastY = event.getY();
		_accumulatedX = 0;
		_accumulatedY = 0;

		window.getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		window.getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseDoubleClicked(SimWindow window, MouseEvent event) {
		window.getCanvas().centerViewportTo(window.getCanvas().toCircuitCoordinatesX(event.getX()), window.getCanvas().toCircuitCoordinatesY(event.getY()));
	}

	public void mouseDragged(SimWindow window, MouseEvent event) {
		int deltaX = event.getX() - _lastX;
		int deltaY = event.getY() - _lastY;

		_accumulatedX += deltaX;
		_accumulatedY += deltaY;

		int toMoveX = 0;
		int toMoveY = 0;
		if (Math.abs(_accumulatedX) > Math.abs(Circuit.GRIDSIZE)) {
			int rest = _accumulatedX - (_accumulatedX / Circuit.GRIDSIZE) * Circuit.GRIDSIZE;
			toMoveX = _accumulatedX - rest;
			_accumulatedX = rest;
		}
		if (Math.abs(_accumulatedY) > Circuit.GRIDSIZE) {
			int rest = _accumulatedY - (_accumulatedY / Circuit.GRIDSIZE) * Circuit.GRIDSIZE;
			toMoveY = _accumulatedY - rest;
			_accumulatedY = rest;
		}
		window.getCanvas().moveViewport((int) (toMoveX / window.getCanvas().getZoom()), (int) (toMoveY / window.getCanvas().getZoom()));
		_lastX = event.getX();
		_lastY = event.getY();
	}
}
