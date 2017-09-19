package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class AssociativeGate extends Gate {

	AssociativeGate(Circuit circuit, GateParameters parameters, GateDescriptor descriptor) {
		super(circuit, parameters, descriptor);
		pins = new AssociativePin[descriptor.getPinCount()];
		for (int i = 0; i < descriptor.getPinCount(); i++) {
			pins[i] = new AssociativePin(this, circuit.getScheduler(), i);
		}
		pins[0].hasChanged();
	}
}

