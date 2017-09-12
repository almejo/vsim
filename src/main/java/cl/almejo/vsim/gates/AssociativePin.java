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
class AssociativePin extends Pin {
	AssociativePin(Gate gate, Scheduler scheduler, int pinId) {
		super(gate, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		int pinCount = _gate.getPinCount();
		AssociativeGateDescriptor descriptor = (AssociativeGateDescriptor) _gate.getGateDescriptor();

		int value = _gate.getPin(0).getInValue();

		for (int i = 1; i < pinCount - 1; i++) {
			value = descriptor.computeAssociativeVal(value, _gate.getPin(i).getInValue());
		}

		_gate.getPin(pinCount - 1).program((byte) value, ((GateParametersWithDelay) _gate.getParamameters()).getDelay());
	}

}
