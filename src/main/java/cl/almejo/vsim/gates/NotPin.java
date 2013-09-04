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

public class NotPin extends Pin {

	public NotPin(Not not, Scheduler scheduler, int pinId) {
		super(not, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		_gate.getPin(1).program(newVal(_gate.getPin(0).getInValue()), _gate.getParams().getDelay());
	}

	private byte newVal(byte inValue) {
		return inValue == Constants.OFF ? Constants.ON : Constants.OFF;
	}
}
