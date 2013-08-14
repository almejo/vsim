/**
 *
 * vsim
 *
 * Created on Aug 14, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */
 
package cl.almejo.vsim.gates;

import cl.almejo.vsim.simulation.Scheduler;

public class AndPin extends Pin {
	public AndPin(And and, Scheduler scheduler, int pinId) {
		super(and, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		super.hasChanged();
		byte newValue = (byte) (((And)_gate).getPin(0).getInValue() & ((And)_gate).getPin(0).getInValue());
		((And)_gate).getPin(2).program(newValue, _gate.getParams().getDelay());
	}
}
