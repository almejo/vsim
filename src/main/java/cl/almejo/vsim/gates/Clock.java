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
class Clock extends Gate {

	Clock(Circuit circuit, GateParameters params, GateDescriptor descriptor) {
		super(circuit, params, descriptor);
		pins = new Pin[1];
		pins[0] = new ClockPin(this, circuit.getScheduler(), 0);
		pins[0].hasChanged();
	}

}
