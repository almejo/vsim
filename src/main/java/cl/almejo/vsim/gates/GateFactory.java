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

public class GateFactory {

	private static final int[][] BEHAVIOR_AND;

	private static final int[][] BEHAVIOR_OR;


	static {
		BEHAVIOR_AND = new int[3][3];
		BEHAVIOR_OR = new int[3][3];
		BEHAVIOR_OR[0][0] = -1;
		BEHAVIOR_OR[0][1] = 0;
		BEHAVIOR_OR[0][2] = 1;
		BEHAVIOR_OR[1][0] = 0;
		BEHAVIOR_OR[1][1] = 0;
		BEHAVIOR_OR[1][2] = 1;
		BEHAVIOR_OR[2][0] = 1;
		BEHAVIOR_OR[2][1] = 1;
		BEHAVIOR_OR[2][2] = 1;

		BEHAVIOR_AND[0][0] = -1;
		BEHAVIOR_AND[0][1] = 0;
		BEHAVIOR_AND[0][2] = 1;
		BEHAVIOR_AND[1][0] = 0;
		BEHAVIOR_AND[1][1] = 0;
		BEHAVIOR_AND[1][2] = 0;
		BEHAVIOR_AND[2][0] = 1;
		BEHAVIOR_AND[2][1] = 0;
		BEHAVIOR_AND[2][2] = 1;

	}

	public static IconGate getInstance(int gateIndex, Circuit circuit) {
		switch (gateIndex) {
			case Gate.CLOCK:
				ClockDescriptor descriptor = new ClockDescriptor();
				return new IconGate(circuit.getNextGateId(), new Clock(circuit, new ClockParams(1000, 1000), descriptor));
			case Gate.AND2:
				AssociateveGateParameters parameters = new AssociateveGateParameters(1, 2, BEHAVIOR_AND);
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, parameters, new AssociativeGateDescriptor((parameters))));
			case Gate.AND3:
				parameters = new AssociateveGateParameters(1, 3, BEHAVIOR_AND);
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, parameters, new AssociativeGateDescriptor((parameters))));
			case Gate.AND4:
				parameters = new AssociateveGateParameters(1, 4, BEHAVIOR_AND);
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, parameters, new AssociativeGateDescriptor((parameters))));
			case Gate.NOT:
				return new IconGate(circuit.getNextGateId(), new Not(circuit, new NotParams(1), new NotDescriptor()));
			default:
				break;
		}
		return null;
	}
}
