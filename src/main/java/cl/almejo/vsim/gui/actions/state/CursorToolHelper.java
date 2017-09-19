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

	private Selection selection;
	private BufferedImage preview;
	private int deltaY;
	private int deltaX;

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
		if (selection == null) {
			Selection selection = circuit.findSelection(x, y);

			if (selection == null) {
				Draggable draggable = circuit.findDraggable(x, y);
				if (draggable != null) {
					circuit.setSelection(draggable);
				}
				selection = circuit.findSelection(x, y);

			}
			if (selection != null) {
				this.selection = selection;
				deltaX = x - selection.getX();
				deltaY = y - selection.getY();
				preview = this.selection.getImage();
			}
			return;
		}
		if (preview != null) {
			circuit.drawDragPreview(x - deltaX, y - deltaY, preview);
		}

	}

	@Override
	public void mouseUp(SimWindow window, MouseEvent event) {
		if (selection != null) {
			window.getCircuit().undoableDragSelection(selection
					, window.getCanvas().toCircuitCoordinatesX(event.getX()) - deltaX
					, window.getCanvas().toCircuitCoordinatesY(event.getY()) - deltaY);
			selection = null;
			preview = null;
			window.getCircuit().clearDragPreview();
		}
	}
}
