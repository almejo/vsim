/**
 *
 * vsim
 *
 * Created on Aug 25, 2013
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

public class AddGateCommand implements Command {

	private final Circuit _circuit;
	private final IconGate _iconGate;
	private final int _x;
	private final int _y;

	public AddGateCommand(Circuit circuit, IconGate iconGate, int x, int y) {
		_circuit = circuit;
		_iconGate = iconGate;
		_x = x;
		_y = y;
	}

	@Override
	public void apply() {
		_circuit.add(_iconGate, _x, _y);
	}

	@Override
	public void unDo() {
		_circuit.remove(_iconGate);
	}
}
