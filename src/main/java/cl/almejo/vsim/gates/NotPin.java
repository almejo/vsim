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
class NotPin extends Pin {

	NotPin(Not not, Scheduler scheduler, int pinId) {
		super(not, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		gate.getPin(1).program(newVal(gate.getPin(0).getInValue()), ((GateParametersWithDelay) gate.getParamameters()).getDelay());
	}

	private byte newVal(byte inValue) {
		return inValue == Constants.OFF ? Constants.ON : Constants.OFF;
	}
}
