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

import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.Pin;

public class Contact extends Point {

	OptimisticList<PinGatePar> _pins = new OptimisticList<PinGatePar>();
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

	public void setPin(Pin pin) {
		_guidePin = pin;
	}
}
