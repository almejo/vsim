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

public class Template extends Gate {

	public Template(Circuit circuit, GateParameters params, TemplateDescriptor descriptor) {
		super(circuit, params, descriptor);
		_pins = new Pin[descriptor.getPinCount()];
		for (int i = 0; i< _pins.length ; i++) {
			_pins[i] = new SimplePin(this, circuit.getScheduler(), i);
		}
	}
}
