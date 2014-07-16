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

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Template.class);

	public Template(Circuit circuit, GateParameters params, TemplateDescriptor descriptor) {
		super(circuit, params, descriptor);
		_pins = new Pin[descriptor.getPinCount()];
		for (int pinId = 0; pinId < _pins.length; pinId++) {
			_pins[pinId] = new SimplePin(this, circuit.getScheduler(), pinId);
		}
	}

	public Gate compile(Circuit circuit, Point[] points) throws CompilationProblem {
		List<Contact> contacts = getTerminalContacts(circuit, points);
		List<Point> positions = getTerminalPoints(circuit, points, getGateDescriptor());

		List<Gate> gates = findGates(contacts);
		LOGGER.debug("[compiling] Terminal contacts: " + contacts);
		LOGGER.debug("[compiling] Internal gates: " + gates);

		GateData[] datas = createGateData(gates);
		for (GateData data : datas) {
			LOGGER.debug("[compiling] Gate data: " + data);
		}
		int[][] outConnections = createOutConnectionsTable(contacts, gates);
		for (int[] array : outConnections) {
			LOGGER.debug("outconn: " + array[0] + ", " + array[1]);
		}
		for (Point position : positions) {
			LOGGER.debug("pinPosition: " + position);
		}
		return new EncapsulatedDescriptor(datas, outConnections, positions, getGateDescriptor().getSize().width).make(circuit, null);
	}

	public int[][] createOutConnectionsTable(List<Contact> contacts, List<Gate> gates) {

		List<Integer[]> list = new LinkedList<Integer[]>();

		for (Contact contact : contacts) {

			List<Contact> attachedContacts = _circuit.getProtoboard().getAllContactsAttached(contact, new LinkedList<Contact>());
			List<Pin> pins = new LinkedList<Pin>();
			for (Contact attachedContact : attachedContacts) {
				pins.addAll(attachedContact.getPins());
			}
			if (pins.size() > 0) {
				Pin pin = getNextNormalPin(gates, pins);
				if (pin != null) {
					list.add(new Integer[]{gates.indexOf(pin.getGate()), pin.getPinId()});
				}
			}
		}
		int[][] connections = new int[list.size()][2];
		int i = 0;
		for (Integer[] data : list) {
			connections[i++] = new int[]{data[0], data[1]};
		}
		return connections;
	}

	private Pin getNextNormalPin(List<Gate> gates, List<Pin> pins) {
		Pin pin = pins.get(0);
		Pin first = pin;
		while (!gates.contains(pin.getGate())) {
			pin = (Pin) pin.getNext();
			if (first == pin) {
				return null;
			}
		}
		return pin;
	}


	private GateData[] createGateData(List<Gate> gates) throws CompilationProblem {
		GateData[] data = new GateData[gates.size()];
		int i = 0;
		for (Gate gate : gates)
			try {
				GateData gateData = new GateData(gate.getGateDescriptor(), gate.getParamameters().clone(), gate.getPinCount());
				updateConnections(gateData, gate, gates);
				data[i++] = gateData;

			} catch (CloneNotSupportedException exception) {
				throw new CompilationProblem(exception);
			}
		return data;
	}

	private void updateConnections(GateData gateData, Gate gate, List<Gate> gates) {
		for (int pinId = 0; pinId < gate.getPinCount(); pinId++) {
			Pin pin = gate.getPin(pinId);
			Pin nextPin = (Pin) pin.getNext();
			if (nextPin.getGate() == gate) {
				LOGGER.debug("*Setting gate: " + gate + " pinId: " + pinId + " -------> [" + nextPin.getGate() + "[" + gates.indexOf(nextPin.getGate()) + "]: " + nextPin.getPinId() + "]");
				gateData.setGateAndPin(pinId, -1, -1);
			} else {
				LOGGER.debug("Setting gate: " + gate + " pinId: " + pinId + " -------> [" + nextPin.getGate() + "[" + gates.indexOf(nextPin.getGate()) + "]: " + nextPin.getPinId() + "]");
				gateData.setGateAndPin(pinId, gates.indexOf(nextPin.getGate()), nextPin.getPinId());
			}
		}
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
			if (isTerminalAndConnectedToSomething(contact)) {
				contacts.add(contact);
			}
		}
		return contacts;
	}

	private boolean isTerminalAndConnectedToSomething(Contact contact) {
		if (contact == null) {
			return false;
		}

		if (!contact.isTerminal()) {
			return false;
		}
		Pin pin = contact.getGuidePin();
		if (pin == null) {
			return false;
		}
		Pin first = pin;
		do {
			if (pin.getGate() != null && pin.getGate().isNormalGate()) {
				return true;
			}
			pin = (Pin) pin.getNext();
		} while (pin != first);
		return false;
	}

	private List<Point> getTerminalPoints(Circuit circuit, Point[] points, GateDescriptor descriptor) {
		List<Point> positions = new LinkedList<Point>();
		int pinId = 0;
		for (Point point : points) {
			Contact contact = circuit.peek(point.getX(), point.getY());
			if (isTerminalAndConnectedToSomething(contact)) {
				positions.add(descriptor.getPinPosition((byte) pinId));
			}
			pinId++;
		}
		return positions;
	}

}
