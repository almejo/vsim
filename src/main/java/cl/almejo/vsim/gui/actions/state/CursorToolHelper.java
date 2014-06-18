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
import cl.almejo.vsim.circuit.Selection;
import cl.almejo.vsim.gui.Draggable;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CursorToolHelper extends ActionToolHelper {

	Selection _selection = null;
	private BufferedImage _preview;

	public Object mouseClicked(SimWindow window, MouseEvent event) {
		Circuit circuit = window.getCircuit();
		Draggable draggable = circuit.findDraggable(window.getCanvas().toCircuitCoordinatesX(event.getX())
				, window.getCanvas().toCircuitCoordinatesY(event.getY()));
		if (draggable != null) {
			if (event.isShiftDown()) {
				circuit.select(draggable);
			} else if (event.isControlDown()) {
				circuit.deselect(draggable);
			} else {
				circuit.clearSelection();
				circuit.select(draggable);
			}
		} else {
			circuit.clearSelection();
		}
		return draggable;
	}

	@Override
	public void mouseDragged(SimWindow window, MouseEvent event) {
		Circuit circuit = window.getCircuit();
		if (_selection == null) {
			Selection selection = circuit.findSelection(window.getCanvas().toCircuitCoordinatesX(event.getX())
					, window.getCanvas().toCircuitCoordinatesY(event.getY()));

			if (selection == null) {
				Draggable draggable = circuit.findDraggable(window.getCanvas().toCircuitCoordinatesX(event.getX())
						, window.getCanvas().toCircuitCoordinatesY(event.getY()));
				if (draggable != null) {
					circuit.setSelection(draggable);
				}
				selection = circuit.findSelection(window.getCanvas().toCircuitCoordinatesX(event.getX())
						, window.getCanvas().toCircuitCoordinatesY(event.getY()));

			}
			if (selection != null) {
				_selection = selection;
				_preview = _selection.getImage();
			}
			return;
		}
		if (_preview != null) {
			circuit.drawDragPreview(window.getCanvas().toCircuitCoordinatesX(event.getX())
					, window.getCanvas().toCircuitCoordinatesY(event.getY())
					, _preview);
		}

	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		if (_selection != null) {
			window.getCircuit().undoableDragSelection(_selection
					, window.getCanvas().toCircuitCoordinatesX(event.getX())
					, window.getCanvas().toCircuitCoordinatesY(event.getY()));
			_selection = null;
			_preview = null;
			window.getCircuit().clearDragPreview();
		}
	}
}
