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

	private final Circuit circuit;
	private final IconGate iconGate;
	private final int x;
	private final int y;

	public RemoveGateCommand(Circuit circuit, IconGate iconGate) {
		this.circuit = circuit;
		this.iconGate = iconGate;
		x = (int) this.iconGate.getX();
		y = (int) this.iconGate.getY();
	}

	@Override
	public boolean apply() {
		circuit.remove(iconGate);
		return true;
	}

	@Override
	public void unDo() {
		circuit.add(iconGate, x, y);
	}
}
