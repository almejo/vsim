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

public class Tristate extends Gate {

	public Tristate(Circuit circuit, GateParameters params, TristateDescriptor descriptor) {
		super(circuit, params, descriptor);

		_pins = new TristatePin[3];
		_pins[0] = new TristatePin(this, circuit.getScheduler(), 0);
		_pins[1] = new TristatePin(this, circuit.getScheduler(), 1);
		_pins[2] = new TristatePin(this, circuit.getScheduler(), 2);
	}
}
