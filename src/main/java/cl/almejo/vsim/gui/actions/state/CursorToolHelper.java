package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.SelectedGates;
import cl.almejo.vsim.circuit.Selection;
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
	private int initX;
	private int initY;
	private int deltaY;
	private int deltaX;

	@Override
	public void mouseDown(SimWindow window, MouseEvent event) {
		initX = window.getCanvas().toCircuitCoordinatesX(event.getX());
		initY = window.getCanvas().toCircuitCoordinatesY(event.getY());
		System.out.println("down: " + initX + " " + initY);
		Selection selection = window.getCircuit().findSelectedArea(initX, initY);
		if (selection == null) {
			SelectedGates selectedGates = window.getCircuit().findSelectedGates(initX, initY);
			if (selectedGates == null) {
				window.getCircuit().clearSelectedArea();
				window.getCircuit().startSelectedArea(initX, initY);
			}
		}
	}

	@Override
	public Object mouseClicked(SimWindow window, MouseEvent event) {
//		int x = window.getCanvas().toCircuitCoordinatesX(event.getX());
//		int y = window.getCanvas().toCircuitCoordinatesY(event.getY());
//		System.out.println("clicked: " + x + " " + y);
//		return checkSelection(window, event, x, y);
		return null;
	}

	@Override
	public void mouseDragged(SimWindow window, MouseEvent event) {
		Circuit circuit = window.getCircuit();
		int x = window.getCanvas().toCircuitCoordinatesX(event.getX());
		int y = window.getCanvas().toCircuitCoordinatesY(event.getY());
		Selection selection = window.getCircuit().findSelectedArea(x, y);
		if (selection == null) {
			SelectedGates selectedGates = window.getCircuit().findSelectedGates(x, y);
			if (selectedGates == null) {
				window.getCircuit().endSelectedArea(x, y);
			}
		}
//		if (selection == null) {
//
//			Selection selection = circuit.findSelectedArea(x, y);
//
//			if (selection == null) {
//				Selectable draggable = circuit.findDraggable(x, y);
//				if (draggable != null) {
//					circuit.setSelectedGates(draggable);
//				}
//				selection = circuit.findSelection(x, y);
//
//			}
//			if (selection != null) {
//				this.selection = selection;
//				deltaX = x - selection.getX();
//				deltaY = y - selection.getY();
//				preview = this.selection.getImage();
//			}
//			return;
//		}
//		if (preview != null) {
//			circuit.drawDragPreview(x - deltaX, y - deltaY, preview);
//		}
		System.out.println("drag: " + x + " " + y);
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
		System.out.println("up");
	}
}
