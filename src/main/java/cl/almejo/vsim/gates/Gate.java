/**
 *
 * vsim
 *
 * Created on Aug 1, 2013
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;

public class Gate {

	public static final int CLOCK = 0;
	public static final int AND2 = 1;
	public static final int AND3 = 2;
	public static final int NOT = 3;

	protected Pin[] _pins;
	protected Circuit _circuit;
	protected GateParameters _parameters;
	private GateDescriptor _gateDescriptor;

	public Gate(Circuit circuit, GateParameters params, GateDescriptor gateDescriptor) {
		_circuit = circuit;
		_parameters = params;
		_gateDescriptor = gateDescriptor;
	}

	public GateParameters getParams() {
		return _parameters;
	}

	public Pin getPin(int i) {
		return _pins[i];
	}

	public GateDescriptor getGateDescriptor() {
		return _gateDescriptor;
	}

	public int getPinCount() {
		return _pins.length;
	}

}
