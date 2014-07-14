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
import cl.almejo.vsim.circuit.Contact;
import cl.almejo.vsim.circuit.Point;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class Template extends Gate implements Compilable {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Circuit.class);

	public Template(Circuit circuit, GateParameters params, TemplateDescriptor descriptor) {
		super(circuit, params, descriptor);
		_pins = new Pin[descriptor.getPinCount()];
		for (int pinId = 0; pinId < _pins.length; pinId++) {
			_pins[pinId] = new SimplePin(this, circuit.getScheduler(), pinId);
		}
	}

	public Gate compile(Circuit circuit, Point[] points) throws CompilationProblem {
		List<Contact> contacts = getTerminalContacts(circuit, points);

		List<Gate> gates = findGates(contacts);
		LOGGER.debug("[compiling] Terminal contacts: " + contacts);
		LOGGER.debug("[compiling] Internal gates: " + gates);

		GateData[] datas = createGateData(gates);
		for(GateData data: datas) {
			LOGGER.debug("[compiling] Gate data: " + data);
		}

		return null;
	}

	private GateData[] createGateData(List<Gate> gates) throws CompilationProblem {
		GateData[] data = new GateData[gates.size()];
		int i = 0;
		for (Gate gate : gates) {
			try {
				data[i++] = new GateData(gate.getGateDescriptor(), gate.getParamameters().clone(), gate.getPinCount());
			} catch (CloneNotSupportedException exception) {
				throw new CompilationProblem(exception);
			}
		}
		return data;
	}

	private List<Gate> findGates(List<Contact> contacts) {
		List<Gate> gates = new LinkedList<Gate>();
		List<Gate> debugGates = new LinkedList<Gate>();
		for (Contact contact : contacts) {
			Pin pin = contact.getGuidePin();
			if (pin != null) {
				findGatesNotIncluded(pin.getGate(), gates, debugGates);
			}
		}
		return gates;
	}

	private void findGatesNotIncluded(Gate gate, List<Gate> gates, List<Gate> debugGates) {
		if (gates.contains(gate) || debugGates.contains(gate)) {
			return;
		}

		if (gate.isNormalGate()) {
			gates.add(gate);
		} else {
			debugGates.add(gate);
		}
		for (int pinId = 0; pinId < gate.getPinCount(); pinId++) {
			addGatesAttachedToPin(gate, pinId, gates, debugGates);
		}
	}

	private void addGatesAttachedToPin(Gate gate, int pinId, List<Gate> gates, List<Gate> debugGates) {
		Pin pin = gate.getPin(pinId);
		Pin first = pin;
		do {
			findGatesNotIncluded(pin.getGate(), gates, debugGates);
			pin = (Pin) pin.getNext();
		} while (pin != first);
	}

	private List<Contact> getTerminalContacts(Circuit circuit, Point[] points) {
		List<Contact> contacts = new LinkedList<Contact>();
		for (Point point : points) {
			Contact contact = circuit.peek(point.getX(), point.getY());
			if (contact != null && contact.isTerminal()) {
				contacts.add(contact);
			}
		}
		return contacts;
	}
}
