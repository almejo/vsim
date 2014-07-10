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

import java.util.LinkedList;
import java.util.List;

public class Template extends Gate implements Compilable {

	public Template(Circuit circuit, GateParameters params, TemplateDescriptor descriptor) {
		super(circuit, params, descriptor);
		_pins = new Pin[descriptor.getPinCount()];
		for (int pinId = 0; pinId < _pins.length; pinId++) {
			_pins[pinId] = new SimplePin(this, circuit.getScheduler(), pinId);
		}
	}

	public Gate compile(Circuit circuit, Point[] points) {
		List<Contact> contacts = getTerminalContacts(circuit, points);
		System.out.println(contacts);
//		FindResult<Contact> result = circuit.peek(x, y);
//		if (result.getHit() != null) {
//			return result.getHit();
//		}
		return null;
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
