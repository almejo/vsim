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
class TriState extends Gate {

	TriState(Circuit circuit, GateParameters parameters, TriStateDescriptor descriptor) {
		super(circuit, parameters, descriptor);
		pins = new TristatePin[3];
		for (int pinId = 0; pinId < 3; pinId++) {
			pins[pinId] = new TristatePin(this, circuit.getScheduler(), pinId);
		}
	}
}
