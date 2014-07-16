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

public class Encapsulated extends Gate {
	private Gate[] _gates;

	public Encapsulated(Circuit circuit, GateParameters parameters, GateDescriptor descriptor) {
		super(circuit, parameters, descriptor);
	}

	public void setGates(Gate[] gates) {
		_gates = gates;
	}

	public Gate[] getGates() {
		return _gates;
	}

	public void setPins(Pin[] pins) {
		_pins = pins;
	}
}
