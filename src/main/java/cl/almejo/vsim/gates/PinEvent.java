package cl.almejo.vsim.gates;

import cl.almejo.vsim.simulation.Scheduler;
import cl.almejo.vsim.simulation.SimulationEvent;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class PinEvent extends SimulationEvent {

	private final Pin pin;
	private final int pinId;


	PinEvent(Pin pin, Scheduler scheduler, int pinId) {
		super(scheduler);
		this.pinId = pinId;
		this.pin = pin;
	}

	@Override
	public void happen() {
		pin.setOutValue(pin.getProgrammedValue());
		pin.updateValues();
	}
}
