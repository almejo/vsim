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

public class LedPin extends Pin {

	public LedPin(Led led, Scheduler scheduler, int pinId) {
		super(led, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		((Led) _gate).setValue(getInValue());
	}
}
