package cl.almejo.vsim.circuit;

/**
 * Created by alejo on 5/4/14.
 */
public class CircuitEvent {

	private final Circuit _circuit;

	public CircuitEvent(Circuit circuit) {
		_circuit = circuit;
	}


	public Circuit getCircuit() {
		return _circuit;
	}
}
