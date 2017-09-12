package cl.almejo.vsim.gates;

import cl.almejo.vsim.simulation.Scheduler;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class FlipFlopDataPin extends Pin {

	public FlipFlopDataPin(Gate gate, Scheduler scheduler, int pinId) {
		super(gate, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		if (getPinId() == 2 && getInValue() == 0) {
			_gate.getPin(1).program(_gate.getPin(0).getInValue(), ((GateParametersWithDelay) _gate.getParamameters()).getDelay());
		}
	}
}
