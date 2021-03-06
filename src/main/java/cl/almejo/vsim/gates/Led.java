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
class Led extends Gate {

	private byte value = Constants.THREE_STATE;

	Led(Circuit circuit, GateParameters parameters, GateDescriptor descriptor) {
		super(circuit, parameters, descriptor);
		pins = new Pin[1];
		pins[0] = new LedPin(this, circuit.getScheduler(), 0);
		pins[0].hasChanged();
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

}