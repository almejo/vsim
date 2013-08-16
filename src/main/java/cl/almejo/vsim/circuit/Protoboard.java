/**
 *
 * vsim
 *
 * Created on Aug 15, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.circuit;

import java.util.LinkedList;
import java.util.List;

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.Pin;

public class Protoboard {
	private Matrix _matrix = new Matrix();

	public Protoboard() {
	}

	public void addPin(int pinId, Gate gate, int x, int y) {
		Contact contact = (Contact) poke(x, y);
		contact.addPin(pinId, gate);
		reconnect(contact);
	}

	private void reconnect(Contact contact) {
		List<Contact> contacts = new LinkedList<Contact>();
		getAllContactsAttached(contact, contacts);
		reconnect(contacts);
	}

	private void reconnect(List<Contact> contacts) {
		List<Pin> pins = reconnectContactPins(contacts);

		Pin first = null;

		if (pins.size() > 0) {
			first = (Pin) pins.get(0);
		}

		setGuidePin(first, contacts);
	}

	private void setGuidePin(Pin pin, List<Contact> contacts) {
		for (Contact contact : contacts) {
			contact.setGuidePin(pin);
		}
	}

	private List<Pin> reconnectContactPins(List<Contact> contacts) {
		List<Pin> pins = getContactPins(contacts);
		if (pins.size() > 0) {
			Pin first = pins.get(0);
			for (Pin pin : pins) {
				pin.disconnect();
				first.connect(pin);
			}
		}
		return pins;
	}

	private List<Pin> getContactPins(List<Contact> contacts) {
		List<Pin> pins = new LinkedList<Pin>();
		for (Contact contact : contacts) {
			pins.addAll(contact.getPins());
		}
		return pins;
	}

	private void getAllContactsAttached(Contact contact, List<Contact> contacts) {
		if (contact == null) {
			return;
		}

		if (contacts.contains(contact)) {
			return;
		}

		contacts.add(contact);

		FindResult resultH = _matrix.findHorizontal(contact._x, contact._y);

		FindResult resultV = _matrix.findVertical(contact._x, contact._y);

		if (contact.isConnected(Constants.EAST)) {
			getAllContactsAttached((Contact) resultH.getNext(), contacts);
		}
		if (contact.isConnected(Constants.WEST)) {
			getAllContactsAttached((Contact) resultH.getPrevious(), contacts);
		}
		if (contact.isConnected(Constants.NORTH)) {
			getAllContactsAttached((Contact) resultV.getNext(), contacts);
		}
		if (contact.isConnected(Constants.SOUTH)) {
			getAllContactsAttached((Contact) resultV.getPrevious(), contacts);
		}
	}

	private Contact poke(int x, int y) {
		System.out.println("buscando en " + new Point(x, y));
		FindResult result = _matrix.findHorizontal(x, y);
		System.out.println(result);
		if (result.getHit() != null) {
			return (Contact) result.getHit();
		}

		Contact contact = new Contact(x, y);
		_matrix.add(contact);
		connectToSorrounding(contact);
		return contact;
	}

	private void connectToSorrounding(Contact contact) {
		FindResult resultH = _matrix.findHorizontal(contact.getX(), contact.getY());

		FindResult resultV = _matrix.findVertical(contact.getX(), contact.getY());

		if (resultH.getPrevious() != null && resultH.getPrevious().isConnected(Constants.EAST)) {
			resultH.getHit().connect(Constants.WEST);
		}
		if (resultH.getNext() != null && resultH.getNext().isConnected(Constants.WEST)) {
			resultH.getHit().connect(Constants.EAST);
		}
		if (resultV.getPrevious() != null && resultV.getPrevious().isConnected(Constants.NORTH)) {
			resultH.getHit().connect(Constants.SOUTH);
		}
		if (resultV.getNext() != null && resultV.getNext().isConnected(Constants.SOUTH)) {
			resultH.getHit().connect(Constants.NORTH);
		}
	}

}
