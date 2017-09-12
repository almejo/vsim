package cl.almejo.vsim.circuit.commands;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gates.IconGate;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class RemoveGateCommand implements Command {

	private final Circuit _circuit;
	private final IconGate _iconGate;
	private final int _x;
	private final int _y;

	public RemoveGateCommand(Circuit circuit, IconGate iconGate) {
		_circuit = circuit;
		_iconGate = iconGate;
		_x = (int) _iconGate.getX();
		_y = (int) _iconGate.getY();
	}

	@Override
	public boolean apply() {
		_circuit.remove(_iconGate);
		return true;
	}

	@Override
	public void unDo() {
		_circuit.add(_iconGate, _x, _y);
	}
}
