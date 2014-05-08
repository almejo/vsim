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

public class Clock extends Gate {

	public Clock(Circuit circuit, GateParameters params, GateDescriptor descriptor) {
		super(circuit, params, descriptor);
		_pins = new Pin[1];
		_pins[0] = new ClockPin(this, circuit.getScheduler(), 0);
		_pins[0].hasChanged();
	}
}
