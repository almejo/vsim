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


package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.Pin;
import cl.almejo.vsim.gui.ColorScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Protoboard {

	private static final Logger LOGGER = LoggerFactory.getLogger(Protoboard.class);

	private Matrix<Contact> _matrix = new Matrix<Contact>();
	private boolean _drawConnectPreview;
	private int _connectionStartX;
	private int _connectionStartY;
	private int _connectionEndX;
	private int _connectionEndY;
	private Rectangle _extent;

	public Protoboard() {
	}

	public void addPin(int pinId, Gate gate, int x, int y) {
		Contact contact = poke(x, y);
		contact.addPin(pinId, gate);
		reconnect(contact);
		updateExtent();
	}

	public void removePin(byte pinId, Gate gate, int x, int y) {
		Contact contact = poke(x, y);

		contact.removePin(pinId, gate);

		reconnect(contact);
		testForDelete(contact);
		updateExtent();
	}

	private void updateExtent() {
		if (_matrix.getXCoords().isEmpty()) {
			_extent = null;
		}
		List<Integer> listX = toList(_matrix.getXCoords());
		List<Integer> listY = toList(_matrix.getYCoords());

		_extent = new Rectangle(listX.get(0)
				, listY.get(0)
				, listX.get(listX.size() - 1) - listX.get(0)
				, listY.get(listY.size() - 1) - listY.get(0));
		LOGGER.debug("Extent: " + _extent);
	}

	private List<Integer> toList(Set<Integer> coords) {
		List<Integer> list = new LinkedList<Integer>();
		list.addAll(coords);
		Collections.sort(list);
		return list;
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

	public List<Contact> getAllContactsAttached(Contact contact, List<Contact> contacts) {
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

	public Contact peek(int x, int y) {
		FindResult<Contact> result = _matrix.findHorizontal(x, y);
		LOGGER.debug("peek " + new Point(x, y) + " ===> " + result.toString());
		return result.getHit();
	}

	private Contact poke(int x, int y) {
		FindResult<Contact> result = _matrix.findHorizontal(x, y);
		LOGGER.debug("poke " + new Point(x, y) + " ===> " + result.toString());
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
		updateExtent();
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

		if (contact.isMiddlePoint() || contact.isNotConnected()) {
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

	public void paint(Graphics2D graphics, Rectangle rectangle) {
		Color color = graphics.getColor();
		for (Integer x : _matrix.getXCoords()) {
			Contact previous = null;
			List<Contact> verticalContacts = _matrix.getVerticalContacts(x);
			for (Contact contact : verticalContacts) {
				graphics.setColor(getContactColor(contact));
				graphics.fillOval(contact.getX() - 3, contact.getY() - 3, 6, 6);
				if (previous != null && contact.isConnected(Constants.SOUTH)) {
					if (rectangle.contains(contact.getX(), contact.getY())
							|| rectangle.contains(previous.getX(), previous.getY())) {
						graphics.drawLine(previous.getX(), previous.getY(), contact.getX(), contact.getY());
					}
				}
				previous = contact;
			}
		}

		for (Integer y : _matrix.getYCoords()) {
			Contact previous = null;
			List<Contact> verticalContacts = _matrix.getHorizontalContacts(y);
			for (Contact contact : verticalContacts) {
				graphics.setColor(getContactColor(contact));
				if (previous != null && contact.isConnected(Constants.WEST)) {
					if (rectangle.contains(contact.getX(), contact.getY())
							|| rectangle.contains(previous.getX(), previous.getY())) {
						graphics.drawLine(previous.getX(), previous.getY(), contact.getX(), contact.getY());
					}
				}
				previous = contact;
			}
		}
		drawConnectPreview(graphics);
		graphics.setColor(color);
	}

	private Color getContactColor(Contact contact) {
		if (contact.getGuidePin() != null) {
			return ColorScheme.getColor(contact.getGuidePin());
		}
		return ColorScheme.getGround();
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
			if (yi < yf) {
				Contact first = findFirst(Constants.NORTH, _matrix.findVertical(xi, yi));
				Contact last = findLast(Constants.SOUTH, _matrix.findVertical(xf, yf));
				return findConnections(Constants.NORTH, _matrix.getSublist(first, last));
			}
			Contact first = findFirst(Constants.NORTH, _matrix.findVertical(xf, yf));
			Contact last = findLast(Constants.SOUTH, _matrix.findVertical(xi, yi));
			return findConnections(Constants.NORTH, _matrix.getSublist(first, last));
		}
		if (xi < xf) {
			Contact first = findFirst(Constants.EAST, _matrix.findHorizontal(xi, yi));
			Contact last = findLast(Constants.WEST, _matrix.findHorizontal(xf, yf));
			return findConnections(Constants.EAST, _matrix.getSublist(first, last));
		}
		Contact first = findFirst(Constants.EAST, _matrix.findHorizontal(xf, yf));
		Contact last = findLast(Constants.WEST, _matrix.findHorizontal(xi, yi));
		return findConnections(Constants.EAST, _matrix.getSublist(first, last));
	}

	private Contact findLast(byte direction, FindResult<Contact> result) {
		Contact hit = null;
		for (Contact contact : result.list()) {
			if (contact != null && contact.isConnected(direction)) {
				hit = contact;
			}
		}
		return hit;
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
		Contact contactA;
		Contact contactB;

		if (xi == xf) {
			if (yi < yf) {
				contactA = findFirst(Constants.NORTH, _matrix.findVertical(xi, yi));
				contactB = findLast(Constants.SOUTH, _matrix.findVertical(xf, yf));
			} else {
				contactA = findFirst(Constants.NORTH, _matrix.findVertical(xf, yf));
				contactB = findLast(Constants.SOUTH, _matrix.findVertical(xi, yi));
			}
		} else {
			if (xi < xf) {
				contactA = findFirst(Constants.EAST, _matrix.findHorizontal(xi, yi));
				contactB = findLast(Constants.WEST, _matrix.findHorizontal(xf, yf));
			} else {
				contactA = findFirst(Constants.EAST, _matrix.findHorizontal(xf, yf));
				contactB = findLast(Constants.WEST, _matrix.findHorizontal(xi, yi));
			}
		}

		if (contactA != null && contactB != null) {
			disconnect(_matrix.getBetweenIncluded(contactA, contactB));
		}
	}

	private void disconnect(List<Contact> contacts) {
		for (int i = 0; i < contacts.size() - 1; i++) {
			disconnect(contacts.get(i).getX(), contacts.get(i).getY(), contacts.get(i + 1).getX(), contacts.get(i + 1).getY());
		}
	}

	public void disconnect(int xi, int yi, int xf, int yf) {
		disconnect(poke(xi, yi), poke(xf, yf));
		updateExtent();
	}

	public void setDrawConnectPreview(boolean draw) {
		_drawConnectPreview = draw;
	}

	public void setConnectPreview(int xi, int yi, int xf, int yf) {
		_connectionStartX = xi;
		_connectionStartY = yi;
		_connectionEndX = xf;
		_connectionEndY = yf;
	}

	public void drawConnectPreview(Graphics2D graphics) {
		if (!_drawConnectPreview) {
			return;
		}

		int xi = Circuit.gridTrunc(_connectionStartX);
		int yi = Circuit.gridTrunc(_connectionStartY);
		int xf = Circuit.gridTrunc(_connectionEndX);
		int yf = Circuit.gridTrunc(_connectionEndY);

		if (xi == xf && yi == yf) {
			return;
		}

		MarchingAnts.drawLines(graphics, drawConnectionLine(xi, yi, xf, yf));
		graphics.fillOval(xi - 3, yi - 3, 6, 6);
		graphics.fillOval(xf - 3, yf - 3, 6, 6);
	}

	private List<Point[]> drawConnectionLine(int xi, int yi, int xf, int yf) {
		List<Point[]> points = new LinkedList<Point[]>();
		if (yi != yf) {
			points.add(new Point[]{new Point(xi, yf), new Point(xi, yi)});
		}
		if (xi != xf) {
			points.add(new Point[]{new Point(xf, yf), new Point(xi, yf)});
		}
		return points;
	}

	public List<Connection<Contact>> getAllConnections() {
		List<Connection<Contact>> list = new LinkedList<Connection<Contact>>();
		for (Integer y : _matrix.getYCoords()) {
			Contact previous = null;
			List<Contact> verticalContacts = _matrix.getHorizontalContacts(y);
			for (Contact contact : verticalContacts) {
				if (previous != null && contact.isConnected(Constants.WEST)) {
					list.add(new Connection<Contact>(previous, contact));
				}
				previous = contact;
			}
		}

		for (Integer x : _matrix.getXCoords()) {
			Contact previous = null;
			List<Contact> verticalContacts = _matrix.getVerticalContacts(x);
			for (Contact contact : verticalContacts) {
				if (previous != null && contact.isConnected(Constants.SOUTH)) {
					list.add(new Connection<Contact>(previous, contact));
				}
				previous = contact;
			}
		}
		return list;
	}

	public Rectangle getExtent() {
		return _extent;
	}
}
