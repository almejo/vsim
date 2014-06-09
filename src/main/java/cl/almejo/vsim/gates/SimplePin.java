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


public class SimplePin extends Pin {
	public SimplePin(Gate gate, Scheduler scheduler, int pindId) {
		super(gate, scheduler, pindId);
	}

	@Override
	public void hasChanged() {

	}
}
