package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Selection;
import cl.almejo.vsim.gui.Draggable;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */

public class CursorToolHelper extends ActionToolHelper {

	private Selection _selection;
	private BufferedImage _preview;
	private int _deltaY;
	private int _deltaX;

	@Override
	public Object mouseClicked(SimWindow window, MouseEvent event) {
		int x = window.getCanvas().toCircuitCoordinatesX(event.getX());
		int y = window.getCanvas().toCircuitCoordinatesY(event.getY());

		return checkSelection(window, event, x, y);
	}

	@Override
	public void mouseDragged(SimWindow window, MouseEvent event) {
		Circuit circuit = window.getCircuit();
		int x = window.getCanvas().toCircuitCoordinatesX(event.getX());
		int y = window.getCanvas().toCircuitCoordinatesY(event.getY());
		if (_selection == null) {
			Selection selection = circuit.findSelection(x, y);

			if (selection == null) {
				Draggable draggable = circuit.findDraggable(x, y);
				if (draggable != null) {
					circuit.setSelection(draggable);
				}
				selection = circuit.findSelection(x, y);

			}
			if (selection != null) {
				_selection = selection;
				_deltaX = x - selection.getX();
				_deltaY = y - selection.getY();
				_preview = _selection.getImage();
			}
			return;
		}
		if (_preview != null) {
			circuit.drawDragPreview(x - _deltaX, y - _deltaY, _preview);
		}

	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		if (_selection != null) {
			window.getCircuit().undoableDragSelection(_selection
					, window.getCanvas().toCircuitCoordinatesX(event.getX()) - _deltaX
					, window.getCanvas().toCircuitCoordinatesY(event.getY()) - _deltaY);
			_selection = null;
			_preview = null;
			window.getCircuit().clearDragPreview();
		}
	}
}
