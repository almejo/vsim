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

import cl.almejo.vsim.circuit.Circuit;

public class Not extends Gate {

	public Not(Circuit circuit, GateParameters parameters, NotDescriptor descriptor) {
		super(circuit, parameters, descriptor);

		_pins = new NotPin[2];
		for(int pinId = 0; pinId< 2; pinId++) {
			_pins[pinId] = new NotPin(this, circuit.getScheduler(), pinId);
		}
	}
}
