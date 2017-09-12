package cl.almejo.vsim.gates;

import cl.almejo.vsim.simulation.Scheduler;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public abstract class Pin extends Link {
	private final int _pinId;
	private byte _inValue = Constants.THREE_STATE;
	private byte _outValue = Constants.THREE_STATE;

	private byte _programedValue = Constants.THREE_STATE;

	private final PinEvent _pinEvent;
	public Gate _gate;

	public Pin(Gate gate, Scheduler scheduler, int pinId) {
		_gate = gate;
		_pinEvent = new PinEvent(this, scheduler, pinId);
		_pinId = pinId;
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

	void updateValues() {
		byte value = Constants.THREE_STATE;
		byte lineValue = Constants.THREE_STATE;
		Pin pin = this;
		do {
			if (pin.getOutValue() != Constants.THREE_STATE) {
				if (pin.getPinEvent().isProgrammed()) {
					value = pin.getOutValue();
				} else {
					if (lineValue != Constants.THREE_STATE && lineValue != pin.getOutValue()) {
						System.err.println("Corto-circuito!");
						break;
					}
					lineValue = pin.getOutValue();
				}
			}
			pin = (Pin) pin.getNext();
		} while (pin != this);

		byte lineVal = lineValue == Constants.THREE_STATE ? value : lineValue;

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

	public abstract void hasChanged();

	private void setInValue(byte value) {
		_inValue = value;
	}

	byte getProgrammedValue() {
		return _programedValue;
	}

	public byte getInValue() {
		return _inValue;
	}

	private PinEvent getPinEvent() {
		return _pinEvent;
	}

	private byte getOutValue() {
		return _outValue;
	}

	protected void program(byte value, int delay) {
		_programedValue = value;
		_pinEvent.schedule(delay);
	}

	void setOutValue(byte value) {
		_outValue = value;
	}

	public int getPinId() {
		return _pinId;
	}
}
