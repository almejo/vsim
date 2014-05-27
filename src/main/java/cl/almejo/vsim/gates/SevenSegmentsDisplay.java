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

public class SevenSegmentsDisplay extends Gate {

	public SevenSegmentsDisplay(Circuit circuit, GateParameters parameters, SevenSegmentsDisplayDescriptor descriptor) {
		super(circuit, parameters, descriptor);

		_pins = new SevenSegmentsDisplayPin[descriptor.getPinCount()];
		for (int pindId = 0; pindId < _pins.length; pindId++) {
			_pins[pindId] = new SevenSegmentsDisplayPin(this, circuit.getScheduler(), pindId);
		}
	}
}
