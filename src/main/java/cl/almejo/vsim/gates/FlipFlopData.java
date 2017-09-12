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
class FlipFlopData extends Gate {
	FlipFlopData(Circuit circuit, GateParameters parameters, FlipFlopDataDescriptor descriptor) {
		super(circuit, parameters, descriptor);

		_pins = new FlipFlopDataPin[3];
		_pins[0] = new FlipFlopDataPin(this, circuit.getScheduler(), 0);
		_pins[1] = new FlipFlopDataPin(this, circuit.getScheduler(), 1);
		_pins[2] = new FlipFlopDataPin(this, circuit.getScheduler(), 2);
	}
}
