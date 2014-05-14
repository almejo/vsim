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

public class Gate {

	public static final int CLOCK = 0;
	public static final int AND2 = 1;
	public static final int AND3 = 2;
	public static final int AND4 = 3;
	public static final int OR2 = 4;
	public static final int OR3 = 5;
	public static final int OR4 = 6;
	public static final int NOT = 10;
	public static final int FLIP_FLOP_DATA = 11;
	public static final int TRISTATE = 12;

	protected Pin[] _pins;
	protected Circuit _circuit;
	protected GateParameters _parameters;
	private GateDescriptor _gateDescriptor;

	public Gate(Circuit circuit, GateParameters parameters, GateDescriptor descriptor) {
		_circuit = circuit;
		_parameters = parameters;
		_gateDescriptor = descriptor;
	}

	public GateParameters getParamameters() {
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
