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
import cl.almejo.vsim.gates.Selectable;
import cl.almejo.vsim.gui.Draggable;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CursorToolHelper extends ActionToolHelper {

	Draggable _dragging = null;
	private BufferedImage _preview;

	public Object mouseClicked(SimWindow window, MouseEvent event) {
		Circuit circuit = window.getCircuit();
		Selectable selectable = circuit.findSelectable(window.getCanvas().toCircuitCoordinatesX(event.getX())
				, window.getCanvas().toCircuitCoordinatesY(event.getY()));
		if (selectable != null) {
			if (event.isShiftDown()) {
				circuit.select(selectable);
			} else if (event.isControlDown()) {
				circuit.deselect(selectable);
			} else {
				circuit.clearSelection();
				circuit.select(selectable);
			}
		} else {
			circuit.clearSelection();
		}
		return selectable;
	}

	@Override
	public void mouseDragged(SimWindow window, MouseEvent event) {
		Circuit circuit = window.getCircuit();
		if (_dragging == null) {
			Draggable draggable = circuit.findDraggable(window.getCanvas().toCircuitCoordinatesX(event.getX())
					, window.getCanvas().toCircuitCoordinatesY(event.getY()));
			if (draggable != null) {
				_dragging = draggable;
				_preview = _dragging.getImage();
			}
		} else if (_preview != null) {
			circuit.drawDragPreview(window.getCanvas().toCircuitCoordinatesX(event.getX())
					, window.getCanvas().toCircuitCoordinatesY(event.getY())
					, _preview);
		}
		System.out.println(_dragging);
	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		if (_dragging != null) {
			_dragging = null;
			_preview = null;
			window.getCircuit().clearDragPreview();
		}
	}
}
