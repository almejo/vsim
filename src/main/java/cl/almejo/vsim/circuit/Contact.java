package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.Pin;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class Contact extends Point {

	private final OptimisticList<PinGatePar> _pins = new OptimisticList<>();

	private int _connectionMask;

	private Pin _guidePin;

	Contact(int x, int y) {
		super(x, y);
	}

	void addPin(int pinId, Gate gate) {
		PinGatePar pinGatePar = new PinGatePar(pinId, gate);
		if (!_pins.contains(pinGatePar)) {
			_pins.add(pinGatePar);
		}
	}

	void removePin(byte pinId, Gate gate) {
		_pins.remove(new PinGatePar(pinId, gate));
	}

	List<Pin> getPins() {

		List<PinGatePar> list = _pins.elements();

		return list.stream().map(pinGatePar -> pinGatePar.getGate().getPin(pinGatePar.getPinId())).collect(Collectors.toCollection(LinkedList::new));
	}

	void setGuidePin(Pin guidePin) {
		_guidePin = guidePin;
	}

	Pin getGuidePin() {
		return _guidePin;
	}

	boolean hasPins() {
		return !_pins.isEmpty();
	}

	boolean isMiddlePoint() {
		return _connectionMask == (Constants.NORTH | Constants.SOUTH) || _connectionMask == (Constants.WEST | Constants.EAST);
	}

	boolean isConnected(byte direction) {
		return (_connectionMask & direction) != 0;
	}

	void connect(byte direction) {
		_connectionMask = (byte) _connectionMask | direction;
	}

	boolean isNotConnected() {
		return _connectionMask == 0;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + (isConnected(Constants.NORTH) ? "N" : "-") + (isConnected(Constants.EAST) ? "E" : "-")
				+ (isConnected(Constants.SOUTH) ? "S" : "-") + (isConnected(Constants.WEST) ? "W" : "-") + ") [" + printPins() + "]";
	}

	private String printPins() {
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (PinGatePar pinGate : _pins.elements()) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(pinGate.getGate()).append("-").append(pinGate.getPinId());
			i++;
		}
		return builder.toString();
	}

	void disconnect(Contact contact) {
		if (_x < contact.getX()) {
			disconnect(Constants.EAST);
		} else if (_x > contact.getX()) {
			disconnect(Constants.WEST);
		} else if (_y < contact.getY()) {
			disconnect(Constants.NORTH);
		} else if (_y > contact.getY()) {
			disconnect(Constants.SOUTH);
		}
	}

	private void disconnect(byte direction) {
		_connectionMask = (byte) _connectionMask & ~direction;
	}

}
