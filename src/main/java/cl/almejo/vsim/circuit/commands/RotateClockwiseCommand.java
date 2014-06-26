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

import cl.almejo.vsim.circuit.Configurable;

public class RotateClockwiseCommand implements Command {

	private final Configurable _configurable;

	public RotateClockwiseCommand(Configurable configurable) {
		_configurable = configurable;
	}

	@Override
	public boolean apply() {
		_configurable.rotateClockwise();
		return true;
	}

	@Override
	public void unDo() {
		_configurable.rotateCounterClockwise();
	}
}
