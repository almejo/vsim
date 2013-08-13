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

public class Clock extends Gate {

	public Clock(Scheduler scheduler, ClockParams params) {
		super(params);
		ClockPin clockPin = new ClockPin(this, scheduler);
		_pins = new Pin[1];
		_pins[0] = clockPin;
		clockPin.hasChanged();
	}


	
}
