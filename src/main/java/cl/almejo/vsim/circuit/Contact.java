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

import java.util.LinkedList;
import java.util.List;

public class Contact extends Point {

	OptimisticList<PinGatePar> _pins = new OptimisticList<PinGatePar>();

	private int _conectionMask;

	private Pin _guidePin;

	public Contact(int x, int y) {
		super(x, y);
	}

	public void addPin(int pinId, Gate gate) {
		PinGatePar pinGatePar = new PinGatePar(pinId, gate);
		if (!_pins.contains(pinGatePar)) {
			_pins.add(pinGatePar);
		}
	}

	public void removePin(byte pinId, Gate gate) {
		_pins.remove(new PinGatePar(pinId, gate));
	}

	public List<Pin> getPins() {

		List<Pin> pins = new LinkedList<Pin>();
		List<PinGatePar> list = _pins.elements();

		for (PinGatePar pinGatePar : list) {
			pins.add(pinGatePar.getGate().getPin(pinGatePar.getPinId()));
		}

		return pins;
	}

	public void setGuidePin(Pin guidePin) {
		_guidePin = guidePin;
	}

	public Pin getGuidePin() {
		return _guidePin;
	}

	public boolean hasPins() {
		return !_pins.isEmpty();
	}

	public boolean isMiddlePoint() {
		return _conectionMask == (Constants.NORTH | Constants.SOUTH) || _conectionMask == (Constants.WEST | Constants.EAST);
	}

	public boolean isConnected(byte direction) {
		return (_conectionMask & direction) != 0;
	}

	public void connect(byte direction) {
		_conectionMask = (byte) _conectionMask | direction;
	}

	public boolean isNotConnected() {
		return _conectionMask == 0;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + (isConnected(Constants.NORTH) ? "N" : "-") + (isConnected(Constants.EAST) ? "E" : "-")
				+ (isConnected(Constants.SOUTH) ? "S" : "-") + (isConnected(Constants.WEST) ? "W" : "-") + ") [" + printPins() + "]";
	}

	private String printPins() {
		String ret = "";
		int i = 0;
		for (PinGatePar pinGate : _pins.elements()) {
			if (i > 0) {
				ret += ", ";
			}
			ret += pinGate.getGate() + "-" + pinGate.getPinId();
			i++;
		}
		return ret;
	}

	public void disconnect(Contact contact) {
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

	public void disconnect(byte direction) {
		_conectionMask = (byte) _conectionMask & ~direction;
	}

}
