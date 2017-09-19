package cl.almejo.vsim.circuit.commands;

import cl.almejo.vsim.circuit.Configurable;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class RotateCounterClockwiseCommand implements Command {

	private final Configurable configurable;

	public RotateCounterClockwiseCommand(Configurable configurable) {
		this.configurable = configurable;
	}

	@Override
	public boolean apply() {
		configurable.rotateCounterClockwise();
		return true;
	}

	@Override
	public void unDo() {
		configurable.rotateClockwise();
	}
}
