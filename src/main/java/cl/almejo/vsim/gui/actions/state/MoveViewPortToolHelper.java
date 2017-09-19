package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class MoveViewPortToolHelper extends ActionToolHelper {
	private int lastX;
	private int lastY;

	private int accumulatedX;
	private int accumulatedY;

	@Override
	public void mouseDown(SimWindow window, MouseEvent event) {
		lastX = event.getX();
		lastY = event.getY();
		accumulatedX = 0;
		accumulatedY = 0;

		window.getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		window.getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseDoubleClicked(SimWindow window, MouseEvent event) {
		int x = window.getCanvas().toCircuitCoordinatesX(event.getX());
		int y = window.getCanvas().toCircuitCoordinatesY(event.getY());
		window.getCanvas().centerViewportTo(x, y);
	}

	@Override
	public void mouseDragged(SimWindow window, MouseEvent event) {
		int deltaX = event.getX() - lastX;
		int deltaY = event.getY() - lastY;

		accumulatedX += deltaX;
		accumulatedY += deltaY;

		int toMoveX = 0;
		int toMoveY = 0;
		if (Math.abs(accumulatedX) > Math.abs(Circuit.GRID_SIZE)) {
			int rest = accumulatedX - accumulatedX / Circuit.GRID_SIZE * Circuit.GRID_SIZE;
			toMoveX = accumulatedX - rest;
			accumulatedX = rest;
		}
		if (Math.abs(accumulatedY) > Circuit.GRID_SIZE) {
			int rest = accumulatedY - accumulatedY / Circuit.GRID_SIZE * Circuit.GRID_SIZE;
			toMoveY = accumulatedY - rest;
			accumulatedY = rest;
		}
		window.getCanvas().moveViewport((int) (toMoveX / window.getCanvas().getZoom()), (int) (toMoveY / window.getCanvas().getZoom()));
		lastX = event.getX();
		lastY = event.getY();
	}
}
