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

public class AddGateCommand implements Command {

	private final Circuit circuit;
	private final IconGate iconGate;
	private final int x;
	private final int y;

	public AddGateCommand(Circuit circuit, IconGate iconGate, int x, int y) {
		this.circuit = circuit;
		this.iconGate = iconGate;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean apply() {
		circuit.add(iconGate, x, y);
		return true;
	}

	@Override
	public void unDo() {
		circuit.remove(iconGate);
	}
}