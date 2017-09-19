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
class Not extends Gate {

	Not(Circuit circuit, GateParameters params, NotDescriptor notDescriptor) {
		super(circuit, params, notDescriptor);

		pins = new NotPin[2];
		pins[0] = new NotPin(this, circuit.getScheduler(), 0);
		pins[1] = new NotPin(this, circuit.getScheduler(), 1);
	}

}
