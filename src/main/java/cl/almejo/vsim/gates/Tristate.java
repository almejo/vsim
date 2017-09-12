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
public class Tristate extends Gate {

	public Tristate(Circuit circuit, GateParameters parameters, TristateDescriptor descriptor) {
		super(circuit, parameters, descriptor);
		_pins = new TristatePin[3];
		for (int pinId = 0; pinId < 3; pinId++) {
			_pins[pinId] = new TristatePin(this, circuit.getScheduler(), pinId);
		}
	}
}
