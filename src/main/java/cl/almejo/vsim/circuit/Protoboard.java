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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.Pin;

public class Protoboard {
	private Matrix<Contact> _matrix = new Matrix<Contact>();

	public void addPin(int pinId, Gate gate, int x, int y) {
		Contact contact = poke(x, y);
		contact.addPin(pinId, gate);
		reconnect(contact);
	}

	public void removePin(byte pinId, Gate gate, int x, int y) {
		Contact contact = poke(x, y);

		contact.removePin(pinId, gate);

		reconnect(contact);
		testForDelete(contact);
	}

	private List<Contact> reconnect(Contact contact) {
		return reconnect(getAllContactsAttached(contact, new LinkedList<Contact>()));
	}

	private List<Contact> reconnect(List<Contact> contacts) {
		List<Pin> pins = reconnectContactPins(contacts);

		Pin first = null;

		if (pins.size() > 0) {
			first = pins.get(0);
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

		FindResult<Contact> resultH = _matrix.findHorizontal(contact._x, contact._y);

		FindResult<Contact> resultV = _matrix.findVertical(contact._x, contact._y);

		if (contact.isConnected(Constants.EAST)) {
			getAllContactsAttached(resultH.getNext(), contacts);
		}
		if (contact.isConnected(Constants.WEST)) {
			getAllContactsAttached(resultH.getPrevious(), contacts);
		}
		if (contact.isConnected(Constants.NORTH)) {
			getAllContactsAttached(resultV.getNext(), contacts);
		}
		if (contact.isConnected(Constants.SOUTH)) {
			getAllContactsAttached(resultV.getPrevious(), contacts);
		}

		return contacts;
	}

	private Contact poke(int x, int y) {
		System.out.println("buscando en " + new Point(x, y));
		FindResult<Contact> result = _matrix.findHorizontal(x, y);
		System.out.println(result);
		if (result.getHit() != null) {
			return result.getHit();
		}

		Contact contact = new Contact(x, y);
		_matrix.add(contact);
		connectToSorrounding(contact);
		return contact;
	}

	private void connectToSorrounding(Contact contact) {
		FindResult<Contact> resultH = _matrix.findHorizontal(contact.getX(), contact.getY());

		FindResult<Contact> resultV = _matrix.findVertical(contact.getX(), contact.getY());

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

		List<Contact> contacts = reconnectInnerContactsHorizontaly(contactA, contactB);

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
		List<Contact> contacts = _matrix.getBetween(contactA, contactB);

		for (Contact contact : contacts) {
			contact.connect(Constants.NORTH);
			contact.connect(Constants.SOUTH);
		}
		return reconnect(getAllContactsAttached(contactA, new LinkedList<Contact>()));
	}

	private List<Contact> reconnectInnerContactsHorizontaly(Contact contactA, Contact contactB) {
		List<Contact> contacts = _matrix.getBetween(contactA, contactB);

		for (Contact contact : contacts) {
			contact.connect(Constants.EAST);
			contact.connect(Constants.WEST);
		}
		return reconnect(getAllContactsAttached(contactA, new LinkedList<Contact>()));
	}

	public void paint(Graphics2D g, Rectangle rectangle) {
		Color color = g.getColor();
		Set<Integer> coords = _matrix.getXCoords();

		Contact previous = null;
		for (Integer x : coords) {
			List<Contact> verticalContacts = _matrix.getVerticalContacts(x);
			for (Contact contact : verticalContacts) {
				g.setColor(getContactColor(contact));
				g.fillOval(contact.getX() - 3, contact.getY() - 3, 6, 6);
				if (previous != null && contact.isConnected(Constants.SOUTH)) {
					if (rectangle.contains(contact.getX(), contact.getY())
						|| rectangle.contains(previous.getX(), previous.getY())) {
						g.drawLine(previous.getX(), previous.getY(), contact.getX(), contact.getY());
					}
				}
				previous = contact;
			}
		}

		previous = null;
		coords = _matrix.getYCoords();
		for (Integer y : coords) {
			List<Contact> verticalContacts = _matrix.getHorizontalContacts(y);
			for (Contact contact : verticalContacts) {
				g.setColor(getContactColor(contact));
				if (previous != null && contact.isConnected(Constants.WEST)) {
					if (rectangle.contains(contact.getX(), contact.getY())
						|| rectangle.contains(previous.getX(), previous.getY())) {
						g.drawLine(previous.getX(), previous.getY(), contact.getX(), contact.getY());
					}
				}
				previous = contact;
			}
		}

		g.setColor(color);
	}

	private Color getContactColor(Contact contact) {
		if (contact.getGuidePin() != null) {
			return Constants.STATECOLORS.get(contact.getGuidePin().getInValue());
		}
		return Constants.STATECOLORS.get(Constants.THREE_STATE);
	}

	public List<Connection<Contact>> findToDisconnect(int x, int y) {

		List<Connection<Contact>> connections = findConnections(Constants.EAST, _matrix.findHorizontal(x, y).list());

		if (connections.size() > 0) {
			return connections;
		}

		return findConnections(Constants.NORTH, _matrix.findVertical(x, y).list());
	}

	public void disconnect(Contact contactA, Contact contactB) {
		contactA.disconnect(contactB);
		contactB.disconnect(contactA);

		reconnect(contactA);
		reconnect(contactB);

		testForDelete(contactA);
		testForDelete(contactB);
	}

	private List<Connection<Contact>> findConnections(byte direction, List<Contact> contacts) {

		List<Connection<Contact>> connections = new LinkedList<Connection<Contact>>();
		Contact previous = null;
		for (Contact contact : contacts) {
			if (previous != null && previous.isConnected(direction)) {
				connections.add(new Connection<Contact>(new Contact(previous.getX(), previous.getY()), new Contact(
					contact.getX(), contact.getY())));
			}
			previous = contact;
		}
		return connections;
	}

	public List<Connection<Contact>> findBeforeConnect(int xi, int yi, int xf, int yf) {

		if (xi == xf) {
			Contact first = findFirst(Constants.EAST, _matrix.findHorizontal(xi, yi));
			Contact last = findLast(Constants.WEST, _matrix.findHorizontal(xf, yf));
			return findConnections(Constants.EAST, _matrix.getBetween(first, last));
		}

		Contact first = findFirst(Constants.NORTH, _matrix.findVertical(xi, yi));
		Contact last = findLast(Constants.SOUTH, _matrix.findVertical(xf, yf));
		return findConnections(Constants.NORTH, _matrix.getBetween(first, last));
	}

	private Contact findLast(byte direction, FindResult<Contact> result) {
		Contact last = null;
		for (Contact contact : result.list()) {
			if (contact != null && contact.isConnected(direction)) {
				last = contact;
			}
		}
		return last;
	}

	private Contact findFirst(byte direction, FindResult<Contact> result) {
		for (Contact contact : result.list()) {
			if (contact != null && contact.isConnected(direction)) {
				return contact;
			}
		}
		return null;
	}

	public void disconnectBetween(int xi, int yi, int xf, int yf) {
		Contact contactA = null;
		Contact contactB = null;
		byte direction = 0;

		if (xi == xf) {
			if (yi < yf) {
				contactA = poke(xi, yi);
				contactB = poke(xf, yf);
			} else {
				contactA = poke(xf, yf);
				contactB = poke(xi, yi);
			}
			direction = Constants.NORTH;
		} else {
			if (xi < xf) {
				contactA = poke(xi, yi);
				contactB = poke(xf, yf);
			} else {
				contactA = poke(xf, yf);
				contactB = poke(xi, yi);
			}
			direction = Constants.EAST;
		}

		if (contactA == null || contactB == null) {
			return;
		}
		List<Contact> contacts = _matrix.getBetween(contactA, contactB);
		Contact previous = null;
		for (Contact contact : contacts) {
			if (previous != null && previous.isConnected(direction)) {
				disconnect(previous, contact);
			}
			previous = contact;
		}
	}

	public void disconnect(int xi, int yi, int xf, int yf) {
		disconnect(poke(xi, yi), poke(xf, yf));
	}
}
