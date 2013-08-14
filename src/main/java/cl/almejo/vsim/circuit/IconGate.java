package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gates.Gate;

public class IconGate {
	
	Gate _gate;

	public IconGate(Gate gate) {
		_gate = gate;
	}
	
	public Gate getGate() {
		return _gate;
	}

	public void setGate(Gate gate) {
		_gate = gate;
	}

}
