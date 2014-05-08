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
import cl.almejo.vsim.gates.IconGate;

public class RemoveGateCommand implements Command {

	private final Circuit _circuit;
	private final IconGate _iconGate;
	private int _x = 0;
	private int _y = 0;

	public RemoveGateCommand(Circuit circuit, int x, int y) {
		_circuit = circuit;
		_iconGate = circuit.findIcon(x, y);
		if (_iconGate != null) {
			_x = (int) _iconGate.getX();
			_y = (int) _iconGate.getY();
		}
	}

	@Override
	public boolean apply() {
		if (_iconGate == null) {
			return false;
		}
		_circuit.remove(_iconGate);
		return true;
	}

	@Override
	public void unDo() {
		_circuit.add(_iconGate, _x, _y);
	}
}
