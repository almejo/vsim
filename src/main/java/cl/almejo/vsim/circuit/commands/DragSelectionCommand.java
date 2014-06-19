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

package cl.almejo.vsim.circuit.commands;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;
import cl.almejo.vsim.circuit.Selection;
import cl.almejo.vsim.gui.Draggable;

import java.util.LinkedList;
import java.util.List;

public class DragSelectionCommand implements Command {

	private final Circuit _circuit;
	private List<Draggable> _draggables;
	List<Point> origin = new LinkedList<Point>();
	List<Point> end = new LinkedList<Point>();

	public DragSelectionCommand(Circuit circuit, Selection selection, int xf, int yf) {
		_circuit = circuit;
		_draggables = selection.getDraggables();
		for (Draggable draggable : _draggables) {
			origin.add(new Point(draggable.getOriginalX(), draggable.getOriginalY()));
			end.add(new Point(xf + draggable.getOriginalX() - selection.getX(), yf + draggable.getOriginalY() - selection.getY()));
		}
	}

	@Override
	public boolean apply() {
		_circuit.drag(_draggables, end);
		_circuit.updateSelection();
		return true;
	}

	@Override
	public void unDo() {
		_circuit.drag(_draggables, origin);
		_circuit.updateSelection();
	}
}
