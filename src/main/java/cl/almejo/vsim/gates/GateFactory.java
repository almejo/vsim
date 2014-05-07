package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;

public class GateFactory {
	public static IconGate getInstnce(int gateIndex, Circuit circuit) {
		switch (gateIndex) {
			case Gate.CLOCK:
				ClockDescriptor descriptor = new ClockDescriptor();
				return new IconGate(circuit.getNextGateId(), new Clock(circuit, new ClockParams(1000, 1000), descriptor));
			case Gate.AND2:
			case Gate.AND3:
				return new IconGate(circuit.getNextGateId(), new And(circuit, new AndParams(1), new AndDescriptor()));
			case Gate.NOT:
				return new IconGate(circuit.getNextGateId(), new Not(circuit, new NotParams(1), new NotDescriptor()));
			default:
				break;
		}
		return null;
	}
}
