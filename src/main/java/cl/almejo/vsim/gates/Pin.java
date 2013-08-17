/**
 *
 * vsim
 *
 * Created on Aug 2, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.gates;

import cl.almejo.vsim.simulation.Scheduler;

public class Pin extends Link {
	protected byte _inValue = Constants.THREE_STATE;
	protected byte _outValue = Constants.THREE_STATE;

	protected byte _programedValue = Constants.THREE_STATE;

	PinEvent _pinEvent;
	public Gate _gate;

	public Pin(Gate gate, Scheduler scheduler, int pinId) {
		_gate = gate;
		_pinEvent = new PinEvent(this, scheduler, pinId);
	}

	public void connect(Link pin) {
		join(pin);
		updateValues();
	}

	public void disconnect() {
		Pin nextPin = (Pin) getNext();
		delete();
		nextPin.updateValues();
		updateValues();
	}

	public void updateValues() {
		byte value = Constants.THREE_STATE;
		byte lineValue = Constants.THREE_STATE;
		Pin pin = this;
		do {
			if (pin.getOutValue() != Constants.THREE_STATE) {
				if (!pin.getPinEvent().isProgrammed()) {
					if (lineValue != Constants.THREE_STATE && lineValue != pin.getOutValue()) {
						System.err.println("Corto-circuito!");
						break;
					}
					lineValue = pin.getOutValue();
				} else {
					value = pin.getOutValue();
				}
			}
			pin = (Pin) pin.getNext();
		} while (pin != this);

		byte lineVal = lineValue != Constants.THREE_STATE ? lineValue : value;

		pin = this;
		do {
			byte oldValue = pin.getInValue();
			pin.setInValue(lineVal);
			if (lineVal != oldValue && pin.getOutValue() == Constants.THREE_STATE
				&& pin.getProgrammedValue() == Constants.THREE_STATE) {
				pin.hasChanged();
			}

			pin = (Pin) pin.getNext();
		} while (pin != this);
	}

	public void hasChanged() {

	}

	public void setInValue(byte value) {
		_inValue = value;
	}

	public byte getProgrammedValue() {
		return _programedValue;
	}

	public byte getInValue() {
		return _inValue;
	}

	public PinEvent getPinEvent() {
		return _pinEvent;
	}

	public byte getOutValue() {
		return _outValue;
	}

	protected void program(byte value, int delay) {
		_programedValue = value;
		_pinEvent.schedule(delay);
	}

	public void setOutValue(byte value) {
		_outValue = value;
	}
}
