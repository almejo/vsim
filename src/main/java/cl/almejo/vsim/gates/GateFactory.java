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

	private final static ClockDescriptor CLOCK_DESCRIPTOR;
	private final static NotDescriptor NOT_DESCRIPTOR;
	private final static AssociativeGateDescriptor ASSOC_AND2_DESCRIPTOR;
	private final static AssociativeGateDescriptor ASSOC_AND3_DESCRIPTOR;
	private final static AssociativeGateDescriptor ASSOC_AND4_DESCRIPTOR;

	private final static AssociativeGateDescriptor ASSOC_OR2_DESCRIPTOR;
	private final static AssociativeGateDescriptor ASSOC_OR3_DESCRIPTOR;
	private final static AssociativeGateDescriptor ASSOC_OR4_DESCRIPTOR;

	private static final FlipFlopDataDescriptor FLIP_FLOP_DATA_DESCRIPTOR;

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

		CLOCK_DESCRIPTOR = new ClockDescriptor();
		ASSOC_AND2_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 2, BEHAVIOR_AND, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_AND));
		ASSOC_AND3_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 3, BEHAVIOR_AND, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_AND));
		ASSOC_AND4_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 4, BEHAVIOR_AND, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_AND));

		ASSOC_OR2_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 2, BEHAVIOR_OR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_OR));
		ASSOC_OR3_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 3, BEHAVIOR_OR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_OR));
		ASSOC_OR4_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 4, BEHAVIOR_OR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_OR));
		NOT_DESCRIPTOR = new NotDescriptor();

		FLIP_FLOP_DATA_DESCRIPTOR = new FlipFlopDataDescriptor(new FlipFlopDataParameters(1));
	}

	public static IconGate getInstance(int gateIndex, Circuit circuit) {
		try {
			switch (gateIndex) {
				case Gate.CLOCK:
					return new IconGate(circuit.getNextGateId(), new Clock(circuit, new ClockParams(1000, 1000), CLOCK_DESCRIPTOR));
				case Gate.AND2:
					return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_AND2_DESCRIPTOR.getParameters().clone(), ASSOC_AND2_DESCRIPTOR));
				case Gate.AND3:
					return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_AND3_DESCRIPTOR.getParameters().clone(), ASSOC_AND3_DESCRIPTOR));
				case Gate.AND4:
					return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_AND4_DESCRIPTOR.getParameters().clone(), ASSOC_AND4_DESCRIPTOR));
				case Gate.OR2:
					return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_OR2_DESCRIPTOR.getParameters().clone(), ASSOC_OR2_DESCRIPTOR));
				case Gate.OR3:
					return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_OR3_DESCRIPTOR.getParameters().clone(), ASSOC_OR3_DESCRIPTOR));
				case Gate.OR4:
					return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_OR4_DESCRIPTOR.getParameters().clone(), ASSOC_OR4_DESCRIPTOR));
				case Gate.NOT:
					return new IconGate(circuit.getNextGateId(), new Not(circuit, new NotParams(1), NOT_DESCRIPTOR));
				case Gate.FLIP_FLOP_DATA:
					return new IconGate(circuit.getNextGateId(), new FlipFlopData(circuit, FLIP_FLOP_DATA_DESCRIPTOR.getParameters().clone(), FLIP_FLOP_DATA_DESCRIPTOR));
				default:
					break;
			}
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return null;
	}
}
