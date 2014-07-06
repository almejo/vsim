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

	public static final String CLOCK = "clock";
	public static final String AND2 = "and2";
	public static final String AND3 = "and3";
	public static final String AND4 = "and4";
	public static final String OR2 = "or2";
	public static final String OR3 = "or3";
	public static final String OR4 = "or4";
	public static final String XOR2 = "xor2";
	public static final String XOR3 = "xor3";
	public static final String XOR4 = "xor4";
	public static final String NOT = "not";
	public static final String FLIP_FLOP_DATA = "flip-flop-data";
	public static final String TRISTATE = "tristate";
	public static final String SEVEN_SEGMENTS_DISPLAY = "seven-segments-display";
	public static final String SEVEN_SEGMENTS_DISPLAY_DOUBLE = "seven-segments-display-double";
	public static final String LED = "led";
	public static final String TIME_DIAGRAM = "time-diagram";
	public static final String SWITCH = "switch";
	public static final String TEMPLATE= "template";

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

	public void parametersUpdated() {
	}

	public Circuit getCircuit() {
		return _circuit;
	}

}
