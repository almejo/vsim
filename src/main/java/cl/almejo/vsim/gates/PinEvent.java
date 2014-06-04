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


package cl.almejo.vsim.gates;

import cl.almejo.vsim.simulation.Scheduler;
import cl.almejo.vsim.simulation.SimulationEvent;

public class PinEvent extends SimulationEvent {

	private Pin _pin;
	private int _pinId;


	public PinEvent(Pin pin, Scheduler scheduler, int pinId) {
		super(scheduler);
		_pinId = pinId;
		_pin = pin;
	}

	@Override
	public void happen() {
		_pin.setOutValue(_pin.getProgrammedValue());
		_pin.updateValues();
	}
}
