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

public class Led extends Gate {

	private byte _value = Constants.THREE_STATE;

	public Led(Circuit circuit, GateParameters parameters, GateDescriptor descriptor) {
		super(circuit, parameters, descriptor);
		_pins = new Pin[1];
		_pins[0] = new LedPin(this, circuit.getScheduler(), 0);
		_pins[0].hasChanged();
	}

	public void setValue(byte value) {
		_value = value;
	}

	public byte getValue() {
		return _value;
	}
}
