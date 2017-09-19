package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.Pin;
import cl.almejo.vsim.gui.ColorScheme;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class Protoboard {

	private static final Logger LOGGER = LoggerFactory.getLogger(Protoboard.class);

	private final Matrix<Contact> matrix = new Matrix<>();
	private boolean drawConnectPreview;
	private int connectionStartX;
	private int connectionStartY;
	private int connectionEndX;
	private int connectionEndY;

	@Getter
	private Rectangle extent;

	Protoboard() {
	}

	void addPin(int pinId, Gate gate, int x, int y) {
		Contact contact = poke(x, y);
		contact.addPin(pinId, gate);
		reconnect(contact);
		updateExtent();
	}

	void removePin(byte pinId, Gate gate, int x, int y) {
		Contact contact = poke(x, y);

		contact.removePin(pinId, gate);

		reconnect(contact);
		testForDelete(contact);
		updateExtent();
	}

	private void updateExtent() {
		if (matrix.getXCoordinates().isEmpty()) {
			extent = null;
		}
		List<Integer> listX = toList(matrix.getXCoordinates());
		List<Integer> listY = toList(matrix.getYCoordinates());

		extent = new Rectangle(listX.get(0)
				, listY.get(0)
				, listX.get(listX.size() - 1) - listX.get(0)
				, listY.get(listY.size() - 1) - listY.get(0));
		LOGGER.debug("Extent: " + extent);
	}

	private List<Integer> toList(Set<Integer> coordinates) {
		List<Integer> list = new LinkedList<>();
		list.addAll(coordinates);
		Collections.sort(list);
		return list;
	}

	private List<Contact> reconnect(Contact contact) {
		return reconnect(getAllContactsAttached(contact, new LinkedList<>()));
	}

	private List<Contact> reconnect(List<Contact> contacts) {
		List<Pin> pins = reconnectContactPins(contacts);

		Pin first = null;

		if (!pins.isEmpty()) {
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
		if (!pins.isEmpty()) {
			Pin first = pins.get(0);
			for (Pin pin : pins) {
				pin.disconnect();
				first.connect(pin);
			}
		}
		return pins;
	}

	private List<Pin> getContactPins(List<Contact> contacts) {
		List<Pin> pins = new LinkedList<>();
		contacts.stream().map(Contact::getPins).forEach(pins::addAll);
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

		FindResult<Contact> resultH = matrix.findHorizontal(contact.x, contact.y);

		FindResult<Contact> resultV = matrix.findVertical(contact.x, contact.y);

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
		FindResult<Contact> result = matrix.findHorizontal(x, y);
		LOGGER.debug("poke " + new Point(x, y) + " ===> " + result);
		if (result.getHit() != null) {
			return result.getHit();
		}

		Contact contact = new Contact(x, y);
		matrix.add(contact);
		connectToSurrounding(contact);
		return contact;
	}

	private void connectToSurrounding(Contact contact) {
		FindResult<Contact> resultH = matrix.findHorizontal(contact.getX(), contact.getY());

		FindResult<Contact> resultV = matrix.findVertical(contact.getX(), contact.getY());

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

	void connect(int x1, int y1, int x2, int y2) {
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
		if (contact1 == null || contact2 == null)
			return;

		Contact contactA;
		Contact contactB;
		if (contact1.x < contact2.x) {
			contactA = contact1;
			contactB = contact2;
		} else {
			contactA = contact2;
			contactB = contact1;
		}

		contactA.connect(Constants.EAST);
		contactB.connect(Constants.WEST);

		List<Contact> contacts = reconnectInnerContactsHorizontally(contactA, contactB);

		contacts.add(contactA);
		contacts.add(contactB);
		testForDelete(contacts);
	}

	private void connectVertical(Contact contact1, Contact contact2) {
		if (contact1 == null || contact2 == null)
			return;

		Contact contactA;
		Contact contactB;
		if (contact1.y < contact2.y) {
			contactA = contact1;
			contactB = contact2;
		} else {
			contactA = contact2;
			contactB = contact1;
		}

		contactA.connect(Constants.NORTH);
		contactB.connect(Constants.SOUTH);

		List<Contact> contacts = reconnectInnerContactsVertically(contactA, contactB);

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
			matrix.remove(contact);
		}
	}

	private List<Contact> reconnectInnerContactsVertically(Contact contactA, Contact contactB) {

		matrix.getBetween(contactA, contactB).forEach(contact -> {
			contact.connect(Constants.NORTH);
			contact.connect(Constants.SOUTH);
		});
		return reconnect(getAllContactsAttached(contactA, new LinkedList<>()));
	}

	private List<Contact> reconnectInnerContactsHorizontally(Contact contactA, Contact contactB) {
		matrix.getBetween(contactA, contactB).forEach(contact -> {
			contact.connect(Constants.EAST);
			contact.connect(Constants.WEST);
		});
		return reconnect(getAllContactsAttached(contactA, new LinkedList<>()));
	}

	void paint(Graphics2D graphics, Rectangle rectangle) {
		Color color = graphics.getColor();
		for (Integer x : matrix.getXCoordinates()) {
			Contact previous = null;
			List<Contact> verticalContacts = matrix.getVerticalContacts(x);
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

		for (Integer y : matrix.getYCoordinates()) {
			Contact previous = null;
			List<Contact> verticalContacts = matrix.getHorizontalContacts(y);
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

	List<Connection<Contact>> findToDisconnect(int x, int y) {

		List<Connection<Contact>> connections = findConnections(Constants.EAST, matrix.findHorizontal(x, y).list());

		if (!connections.isEmpty()) {
			return connections;
		}

		return findConnections(Constants.NORTH, matrix.findVertical(x, y).list());
	}

	private void disconnect(Contact contactA, Contact contactB) {
		contactA.disconnect(contactB);
		contactB.disconnect(contactA);

		reconnect(contactA);
		reconnect(contactB);

		testForDelete(contactA);
		testForDelete(contactB);
	}

	private List<Connection<Contact>> findConnections(byte direction, List<Contact> contacts) {

		List<Connection<Contact>> connections = new LinkedList<>();
		Contact previous = null;
		for (Contact contact : contacts) {
			if (previous != null && previous.isConnected(direction)) {
				connections.add(new Connection<>(new Contact(previous.getX(), previous.getY()), new Contact(
						contact.getX(), contact.getY())));
			}
			previous = contact;
		}
		return connections;
	}

	List<Connection<Contact>> findBeforeConnect(int xi, int yi, int xf, int yf) {
		if (xi == xf) {
			if (yi < yf) {
				Contact first = findFirst(Constants.NORTH, matrix.findVertical(xi, yi));
				Contact last = findLast(Constants.SOUTH, matrix.findVertical(xf, yf));
				return findConnections(Constants.NORTH, matrix.getSublist(first, last));
			}
			Contact first = findFirst(Constants.NORTH, matrix.findVertical(xf, yf));
			Contact last = findLast(Constants.SOUTH, matrix.findVertical(xi, yi));
			return findConnections(Constants.NORTH, matrix.getSublist(first, last));
		}
		if (xi < xf) {
			Contact first = findFirst(Constants.EAST, matrix.findHorizontal(xi, yi));
			Contact last = findLast(Constants.WEST, matrix.findHorizontal(xf, yf));
			return findConnections(Constants.EAST, matrix.getSublist(first, last));
		}
		Contact first = findFirst(Constants.EAST, matrix.findHorizontal(xf, yf));
		Contact last = findLast(Constants.WEST, matrix.findHorizontal(xi, yi));
		return findConnections(Constants.EAST, matrix.getSublist(first, last));
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

	void disconnectBetween(int xi, int yi, int xf, int yf) {
		Contact contactA;
		Contact contactB;

		if (xi == xf) {
			if (yi < yf) {
				contactA = findFirst(Constants.NORTH, matrix.findVertical(xi, yi));
				contactB = findLast(Constants.SOUTH, matrix.findVertical(xf, yf));
			} else {
				contactA = findFirst(Constants.NORTH, matrix.findVertical(xf, yf));
				contactB = findLast(Constants.SOUTH, matrix.findVertical(xi, yi));
			}
		} else {
			if (xi < xf) {
				contactA = findFirst(Constants.EAST, matrix.findHorizontal(xi, yi));
				contactB = findLast(Constants.WEST, matrix.findHorizontal(xf, yf));
			} else {
				contactA = findFirst(Constants.EAST, matrix.findHorizontal(xf, yf));
				contactB = findLast(Constants.WEST, matrix.findHorizontal(xi, yi));
			}
		}

		if (contactA != null && contactB != null) {
			disconnect(contactA.getX(), contactA.getY(), contactB.getX(), contactB.getY());
		}
	}

	void disconnect(int xi, int yi, int xf, int yf) {
		disconnect(poke(xi, yi), poke(xf, yf));
		updateExtent();
	}

	void setDrawConnectPreview(boolean draw) {
		drawConnectPreview = draw;
	}

	void setConnectPreview(int xi, int yi, int xf, int yf) {
		connectionStartX = xi;
		connectionStartY = yi;
		connectionEndX = xf;
		connectionEndY = yf;
	}

	private void drawConnectPreview(Graphics2D graphics) {
		if (drawConnectPreview) {

			int xi = Circuit.gridTrunc(connectionStartX);
			int yi = Circuit.gridTrunc(connectionStartY);
			int xf = Circuit.gridTrunc(connectionEndX);
			int yf = Circuit.gridTrunc(connectionEndY);

			if (xi == xf && yi == yf) {
				return;
			}

			MarchingAnts.drawLines(graphics, drawConnectionLine(xi, yi, xf, yf));
			graphics.fillOval(xi - 3, yi - 3, 6, 6);
			graphics.fillOval(xf - 3, yf - 3, 6, 6);
		}
	}

	private List<Point[]> drawConnectionLine(int xi, int yi, int xf, int yf) {
		List<Point[]> points = new LinkedList<>();
		if (yi != yf) {
			points.add(new Point[]{new Point(xi, yf), new Point(xi, yi)});
		}
		if (xi != xf) {
			points.add(new Point[]{new Point(xf, yf), new Point(xi, yf)});
		}
		return points;
	}

	List<Connection<Contact>> getAllConnections() {
		List<Connection<Contact>> list = new LinkedList<>();
		for (Integer y : matrix.getYCoordinates()) {
			Contact previous = null;
			List<Contact> verticalContacts = matrix.getHorizontalContacts(y);
			for (Contact contact : verticalContacts) {
				if (previous != null && contact.isConnected(Constants.WEST)) {
					list.add(new Connection<>(previous, contact));
				}
				previous = contact;
			}
		}

		for (Integer x : matrix.getXCoordinates()) {
			Contact previous = null;
			List<Contact> verticalContacts = matrix.getVerticalContacts(x);
			for (Contact contact : verticalContacts) {
				if (previous != null && contact.isConnected(Constants.SOUTH)) {
					list.add(new Connection<>(previous, contact));
				}
				previous = contact;
			}
		}
		return list;
	}
}
