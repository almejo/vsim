/**
 *
 * vsim
 *
 * Created on Aug 14, 2013
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

	public Not(Circuit circuit, GateParameters params, NotDescriptor notDescriptor) {
		super(circuit, params, notDescriptor);

		_pins = new NotPin[2];
		_pins[0] = new NotPin(this, circuit.getScheduler(), 0);
		_pins[1] = new NotPin(this, circuit.getScheduler(), 1);
	}
}
