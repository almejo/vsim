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
class TristatePin extends Pin {

	TristatePin(TriState tristate, Scheduler scheduler, int pinId) {
		super(tristate, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		if (getPinId() == 0) {
			byte newVal = gate.getPin(2).getInValue() == Constants.ON ? getInValue() : Constants.THREE_STATE;
			gate.getPin(1).program(newVal, ((GateParametersWithDelay) gate.getParameters()).getDelay());
		}
	}
}
