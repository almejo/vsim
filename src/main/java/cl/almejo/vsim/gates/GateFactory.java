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
	private static final TristateDescriptor TRISTATE_DESCRIPTOR;

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

		CLOCK_DESCRIPTOR = new ClockDescriptor(new ClockParameters(1000, 1000), Gate.CLOCK);
		ASSOC_AND2_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 2, BEHAVIOR_AND, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_AND), Gate.AND2);
		ASSOC_AND3_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 3, BEHAVIOR_AND, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_AND), Gate.AND3);
		ASSOC_AND4_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 4, BEHAVIOR_AND, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_AND), Gate.AND4);

		ASSOC_OR2_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 2, BEHAVIOR_OR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_OR), Gate.OR2);
		ASSOC_OR3_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 3, BEHAVIOR_OR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_OR), Gate.OR3);
		ASSOC_OR4_DESCRIPTOR = new AssociativeGateDescriptor(new AssociateveGateParameters(1, 4, BEHAVIOR_OR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_OR), Gate.OR4);
		NOT_DESCRIPTOR = new NotDescriptor(new NotParameters(1), Gate.NOT);

		TRISTATE_DESCRIPTOR = new TristateDescriptor(new TristateParameters(1), Gate.TRISTATE);

		FLIP_FLOP_DATA_DESCRIPTOR = new FlipFlopDataDescriptor(new FlipFlopDataParameters(1), Gate.FLIP_FLOP_DATA);
	}

	public static IconGate getInstance(String gateIndex, Circuit circuit) {
		try {
			if (gateIndex.equalsIgnoreCase(Gate.CLOCK)) {
				return new IconGate(circuit.getNextGateId(), new Clock(circuit, CLOCK_DESCRIPTOR.getParameters().clone(), CLOCK_DESCRIPTOR));
			} else if (gateIndex.equalsIgnoreCase(Gate.AND2)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_AND2_DESCRIPTOR.getParameters().clone(), ASSOC_AND2_DESCRIPTOR));
			} else if (gateIndex.equalsIgnoreCase(Gate.AND3)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_AND3_DESCRIPTOR.getParameters().clone(), ASSOC_AND3_DESCRIPTOR));
			} else if (gateIndex.equalsIgnoreCase(Gate.AND4)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_AND4_DESCRIPTOR.getParameters().clone(), ASSOC_AND4_DESCRIPTOR));
			} else if (gateIndex.equalsIgnoreCase(Gate.OR2)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_OR2_DESCRIPTOR.getParameters().clone(), ASSOC_OR2_DESCRIPTOR));
			} else if (gateIndex.equalsIgnoreCase(Gate.OR3)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_OR3_DESCRIPTOR.getParameters().clone(), ASSOC_OR3_DESCRIPTOR));
			} else if (gateIndex.equalsIgnoreCase(Gate.OR4)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_OR4_DESCRIPTOR.getParameters().clone(), ASSOC_OR4_DESCRIPTOR));
			} else if (gateIndex.equalsIgnoreCase(Gate.NOT)) {
				return new IconGate(circuit.getNextGateId(), new Not(circuit, NOT_DESCRIPTOR.getParameters().clone(), NOT_DESCRIPTOR));
			} else if (gateIndex.equalsIgnoreCase(Gate.FLIP_FLOP_DATA)) {
				return new IconGate(circuit.getNextGateId(), new FlipFlopData(circuit, FLIP_FLOP_DATA_DESCRIPTOR.getParameters().clone(), FLIP_FLOP_DATA_DESCRIPTOR));
			} else if (gateIndex.equalsIgnoreCase(Gate.TRISTATE)) {
				return new IconGate(circuit.getNextGateId(), new Tristate(circuit, TRISTATE_DESCRIPTOR.getParameters().clone(), TRISTATE_DESCRIPTOR));
			}
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return null;
	}
}
