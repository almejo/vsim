/**
 *
 * vsim
 *
 * Created on Aug 1, 2013
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

	public PinEvent(Pin pin, Scheduler scheduler) {
		super(scheduler);
		_pin = pin;
	}

	@Override
	public void happen() {
		_pin.setOutValue(_pin.getProgrammedValue());
		System.out.println(_pin + " changed to " + _pin.getOutValue());
		_pin.updateValues();
	}
}
