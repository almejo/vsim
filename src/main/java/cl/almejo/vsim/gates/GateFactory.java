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
public class GateFactory {

	private static final int[][] BEHAVIOR_AND;
	private static final int[][] BEHAVIOR_OR;
	private static final int[][] BEHAVIOR_XOR;

	private static final ClockDescriptor CLOCK_DESCRIPTOR;
	private static final NotDescriptor NOT_DESCRIPTOR;
	private static final AssociativeGateDescriptor ASSOC_AND2_DESCRIPTOR;
	private static final AssociativeGateDescriptor ASSOC_AND3_DESCRIPTOR;
	private static final AssociativeGateDescriptor ASSOC_AND4_DESCRIPTOR;

	private static final AssociativeGateDescriptor ASSOC_OR2_DESCRIPTOR;
	private static final AssociativeGateDescriptor ASSOC_OR3_DESCRIPTOR;
	private static final AssociativeGateDescriptor ASSOC_OR4_DESCRIPTOR;

	private static final AssociativeGateDescriptor ASSOC_XOR2_DESCRIPTOR;
	private static final AssociativeGateDescriptor ASSOC_XOR3_DESCRIPTOR;
	private static final AssociativeGateDescriptor ASSOC_XOR4_DESCRIPTOR;

	private static final FlipFlopDataDescriptor FLIP_FLOP_DATA_DESCRIPTOR;

	private static final TriStateDescriptor TRI_STATE_DESCRIPTOR;
	private static final LedDescriptor LED_DESCRIPTOR;
	private static final TimeDiagramDescriptor TIME_DIAGRAM_DESCRIPTOR;
	private static final SwitchDescriptor SWITCH_DESCRIPTOR;
	private static final SevenSegmentsDisplayDescriptor SEVEN_SEGMENTS_DISPLAY_DESCRIPTOR;
	private static final SevenSegmentsDisplayDescriptor SEVEN_SEGMENTS_DISPLAY_DESCRIPTOR_DOUBLE;


	static {
		BEHAVIOR_AND = new int[3][3];
		BEHAVIOR_OR = new int[3][3];
		BEHAVIOR_XOR = new int[3][3];

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

		BEHAVIOR_XOR[0][0] = -1;
		BEHAVIOR_XOR[0][1] = 0;
		BEHAVIOR_XOR[0][2] = 1;
		BEHAVIOR_XOR[1][0] = 0;
		BEHAVIOR_XOR[1][1] = 0;
		BEHAVIOR_XOR[1][2] = 1;
		BEHAVIOR_XOR[2][0] = 1;
		BEHAVIOR_XOR[2][1] = 1;
		BEHAVIOR_XOR[2][2] = 0;

		CLOCK_DESCRIPTOR = new ClockDescriptor(new ClockParameters(1000, 1000), Gate.CLOCK);
		ASSOC_AND2_DESCRIPTOR = new AssociativeGateDescriptor(new AssociativeGateParameters(1, 2, BEHAVIOR_AND, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_AND), Gate.AND2);
		ASSOC_AND3_DESCRIPTOR = new AssociativeGateDescriptor(new AssociativeGateParameters(1, 3, BEHAVIOR_AND, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_AND), Gate.AND3);
		ASSOC_AND4_DESCRIPTOR = new AssociativeGateDescriptor(new AssociativeGateParameters(1, 4, BEHAVIOR_AND, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_AND), Gate.AND4);

		ASSOC_OR2_DESCRIPTOR = new AssociativeGateDescriptor(new AssociativeGateParameters(1, 2, BEHAVIOR_OR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_OR), Gate.OR2);
		ASSOC_OR3_DESCRIPTOR = new AssociativeGateDescriptor(new AssociativeGateParameters(1, 3, BEHAVIOR_OR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_OR), Gate.OR3);
		ASSOC_OR4_DESCRIPTOR = new AssociativeGateDescriptor(new AssociativeGateParameters(1, 4, BEHAVIOR_OR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_OR), Gate.OR4);

		ASSOC_XOR2_DESCRIPTOR = new AssociativeGateDescriptor(new AssociativeGateParameters(1, 2, BEHAVIOR_XOR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_XOR), Gate.XOR2);
		ASSOC_XOR3_DESCRIPTOR = new AssociativeGateDescriptor(new AssociativeGateParameters(1, 3, BEHAVIOR_XOR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_XOR), Gate.XOR3);
		ASSOC_XOR4_DESCRIPTOR = new AssociativeGateDescriptor(new AssociativeGateParameters(1, 4, BEHAVIOR_XOR, AssociativeGateDescriptor.ASSOCIATIVE_TYPE_XOR), Gate.XOR4);

		NOT_DESCRIPTOR = new NotDescriptor(new NotParameters(1), Gate.NOT);

		TRI_STATE_DESCRIPTOR = new TriStateDescriptor(new TriStateParameters(1), Gate.TRI_STATE);
		SEVEN_SEGMENTS_DISPLAY_DESCRIPTOR = new SevenSegmentsDisplayDescriptor(new SevenSegmentsDisplayParameters(4), Gate.SEVEN_SEGMENTS_DISPLAY);
		SEVEN_SEGMENTS_DISPLAY_DESCRIPTOR_DOUBLE = new SevenSegmentsDisplayDescriptor(new SevenSegmentsDisplayParameters(8), Gate.SEVEN_SEGMENTS_DISPLAY_DOUBLE);
		FLIP_FLOP_DATA_DESCRIPTOR = new FlipFlopDataDescriptor(new FlipFlopDataParameters(1), Gate.FLIP_FLOP_DATA);
		LED_DESCRIPTOR = new LedDescriptor(new LedParameters("Led"), Gate.LED);
		TIME_DIAGRAM_DESCRIPTOR = new TimeDiagramDescriptor(new TimeDiagramParameters(), Gate.TIME_DIAGRAM);
		SWITCH_DESCRIPTOR = new SwitchDescriptor(new SwitchParameters(Constants.ON), Gate.SWITCH);
	}

	public static IconGate getInstance(String gateIndex, Circuit circuit) {
		try {
			if (gateIndex.equalsIgnoreCase(Gate.CLOCK)) {
				return new IconGate(circuit.getNextGateId(), new Clock(circuit, CLOCK_DESCRIPTOR.getOriginalParameters().clone(), CLOCK_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.AND2)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_AND2_DESCRIPTOR.getOriginalParameters().clone(), ASSOC_AND2_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.AND3)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_AND3_DESCRIPTOR.getOriginalParameters().clone(), ASSOC_AND3_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.AND4)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_AND4_DESCRIPTOR.getOriginalParameters().clone(), ASSOC_AND4_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.OR2)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_OR2_DESCRIPTOR.getOriginalParameters().clone(), ASSOC_OR2_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.OR3)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_OR3_DESCRIPTOR.getOriginalParameters().clone(), ASSOC_OR3_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.OR4)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_OR4_DESCRIPTOR.getOriginalParameters().clone(), ASSOC_OR4_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.XOR2)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_XOR2_DESCRIPTOR.getOriginalParameters().clone(), ASSOC_XOR2_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.XOR3)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_XOR3_DESCRIPTOR.getOriginalParameters().clone(), ASSOC_XOR3_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.XOR4)) {
				return new IconGate(circuit.getNextGateId(), new AssociativeGate(circuit, ASSOC_XOR4_DESCRIPTOR.getOriginalParameters().clone(), ASSOC_XOR4_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.NOT)) {
				return new IconGate(circuit.getNextGateId(), new Not(circuit, NOT_DESCRIPTOR.getOriginalParameters().clone(), NOT_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.FLIP_FLOP_DATA)) {
				return new IconGate(circuit.getNextGateId(), new FlipFlopData(circuit, FLIP_FLOP_DATA_DESCRIPTOR.getOriginalParameters().clone(), FLIP_FLOP_DATA_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.TRI_STATE)) {
				return new IconGate(circuit.getNextGateId(), new TriState(circuit, TRI_STATE_DESCRIPTOR.getOriginalParameters().clone(), TRI_STATE_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.SEVEN_SEGMENTS_DISPLAY)) {
				return new IconGate(circuit.getNextGateId(), new SevenSegmentsDisplay(circuit, SEVEN_SEGMENTS_DISPLAY_DESCRIPTOR.getOriginalParameters().clone(), SEVEN_SEGMENTS_DISPLAY_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.SEVEN_SEGMENTS_DISPLAY_DOUBLE)) {
				return new IconGate(circuit.getNextGateId(), new SevenSegmentsDisplay(circuit, SEVEN_SEGMENTS_DISPLAY_DESCRIPTOR_DOUBLE.getOriginalParameters().clone(), SEVEN_SEGMENTS_DISPLAY_DESCRIPTOR_DOUBLE));
			}
			if (gateIndex.equalsIgnoreCase(Gate.LED)) {
				return new IconGate(circuit.getNextGateId(), new Led(circuit, LED_DESCRIPTOR.getOriginalParameters().clone(), LED_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.TIME_DIAGRAM)) {
				return new IconGate(circuit.getNextGateId(), new TimeDiagram(circuit, TIME_DIAGRAM_DESCRIPTOR.getOriginalParameters().clone(), TIME_DIAGRAM_DESCRIPTOR));
			}
			if (gateIndex.equalsIgnoreCase(Gate.SWITCH)) {
				return new IconGate(circuit.getNextGateId(), new Switch(circuit, SWITCH_DESCRIPTOR.getOriginalParameters().clone(), SWITCH_DESCRIPTOR));
			}
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return null;
	}
}
