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

	private List<Contact> reconnect(Contact contact) {
		List<Contact> contacts = new LinkedList<Contact>();
		getAllContactsAttached(contact, contacts);
		return reconnect(contacts);
	}

	private List<Contact> reconnect(List<Contact> contacts) {
		List<Pin> pins = reconnectContactPins(contacts);

		Pin first = null;

		if (pins.size() > 0) {
			first = (Pin) pins.get(0);
		}

		setGuidePin(first, contacts);
		return contacts;
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

	private List<Contact> getAllContactsAttached(Contact contact, List<Contact> contacts) {
		if (contact == null) {
			return contacts;
		}

		if (contacts.contains(contact)) {
			return contacts;
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

		return contacts;
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

		if (resultH.getPrevious() != null && ((Contact) resultH.getPrevious()).isConnected(Constants.EAST)) {
			((Contact) resultH.getHit()).connect(Constants.WEST);
		}

		if (resultH.getNext() != null && ((Contact) resultH.getNext()).isConnected(Constants.WEST)) {
			((Contact) resultH.getHit()).connect(Constants.EAST);
		}

		if (resultV.getPrevious() != null && ((Contact) resultV.getPrevious()).isConnected(Constants.NORTH)) {
			((Contact) resultH.getHit()).connect(Constants.SOUTH);
		}

		if (resultV.getNext() != null && ((Contact) resultV.getNext()).isConnected(Constants.SOUTH)) {
			((Contact) resultH.getHit()).connect(Constants.NORTH);
		}
	}

	public void connect(int x1, int y1, int x2, int y2) {
		if (x1 == x2 && y1 == y2) {
			return;
		}

		if (x1 != x2 && y1 != y2) {
			return;
		}

		if (x1 == x2) {
			connectVertical(poke(x1, y1), poke(x2, y2));
		} else {
			connectHorizontal(poke(x1, y1), poke(x2, y2));
		}
	}

	private void connectHorizontal(Contact contact1, Contact contact2) {
		Contact contactA, contactB;

		if (contact1 == null || contact2 == null)
			return;

		if (contact1._x < contact2._x) {
			contactA = contact1;
			contactB = contact2;
		} else {
			contactA = contact2;
			contactB = contact1;
		}

		contactA.connect(Constants.EAST);
		contactB.connect(Constants.WEST);

		List<Contact> contacts = reconnectInnerContactsVerticaly(contactA, contactB);

		contacts.add(contactA);
		contacts.add(contactB);
		testForDelete(contacts);
	}

	private void connectVertical(Contact contact1, Contact contact2) {
		Contact contactA, contactB;

		if (contact1 == null || contact2 == null)
			return;

		if (contact1._y < contact2._y) {
			contactA = contact1;
			contactB = contact2;
		} else {
			contactA = contact2;
			contactB = contact1;
		}

		contactA.connect(Constants.NORTH);
		contactB.connect(Constants.SOUTH);

		List<Contact> contacts = reconnectInnerContactsVerticaly(contactA, contactB);

		contacts.add(contactA);
		contacts.add(contactB);
		testForDelete(contacts);
	}

	private void testForDelete(List<Contact> contacts) {
		for (Contact contact : contacts) {
			testForDelete(contact);
		}
	}

	private void testForDelete(Contact contact) {
		if (contact.hasPins()) {
			return;
		}

		if (contact.isMiddlePoint() || !contact.isConnected()) {
			_matrix.remove(contact);
		}
	}

	private List<Contact> reconnectInnerContactsVerticaly(Contact contactA, Contact contactB) {
		List<Contact> contacts = ((List<Contact>) _matrix.getBetween(contactA, contactB));

		for (Contact contact : contacts) {
			contact.connect(Constants.NORTH);
			contact.connect(Constants.SOUTH);
		}
		return reconnect(getAllContactsAttached(contactA, new LinkedList<Contact>()));
	}
}
