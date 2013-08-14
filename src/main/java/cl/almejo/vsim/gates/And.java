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

public class And extends Gate {

	public And(Circuit circuit, GateParameters params) {
		super(circuit, params);
		_pins = new AndPin[3];
		_pins[0] = new AndPin(this, circuit.getScheduler(), 0);
		_pins[1] = new AndPin(this, circuit.getScheduler(), 1);
		_pins[2] = new AndPin(this, circuit.getScheduler(), 2);
	}
}
