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


package cl.almejo.vsim.gates;

import cl.almejo.vsim.simulation.Scheduler;

public class TristatePin extends Pin {

	public TristatePin(Tristate tristate, Scheduler scheduler, int pinId) {
		super(tristate, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		if (getPinId() == 0) {
			byte newVal = _gate.getPin(2).getInValue() == Constants.ON ? getInValue() : Constants.THREE_STATE;
			_gate.getPin(1).program(newVal, _gate.getParamameters().getDelay());
		}
	}
}
