/**
 * vsim
 * <p/>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 */
package cl.almejo.vsim.gates;

import cl.almejo.vsim.simulation.Scheduler;

public class AssociativePin extends Pin {
	public AssociativePin(Gate gate, Scheduler scheduler, int pinId) {
		super(gate, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		int pincount = _gate.getPinCount();
		AssociativeGateDescriptor descriptor = (AssociativeGateDescriptor) _gate.getGateDescriptor();

		int value = _gate.getPin(0).getInValue();

		for (int i = 1; i < pincount - 1; i++) {
			value = descriptor.computeAssocVal(value, _gate.getPin(i).getInValue());
		}

		_gate.getPin(pincount - 1).program((byte) value, _gate.getParamameters().getDelay());
	}

}
