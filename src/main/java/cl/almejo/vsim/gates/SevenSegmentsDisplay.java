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
class SevenSegmentsDisplay extends Gate {

	SevenSegmentsDisplay(Circuit circuit, GateParameters parameters, SevenSegmentsDisplayDescriptor descriptor) {
		super(circuit, parameters, descriptor);

		pins = new SevenSegmentsDisplayPin[descriptor.getPinCount()];
		for (int pinId = 0; pinId < pins.length; pinId++) {
			pins[pinId] = new SevenSegmentsDisplayPin(this, circuit.getScheduler(), pinId);
		}
	}
}
