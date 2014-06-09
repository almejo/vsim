/**
 * vsim
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: alejo
 */
package cl.almejo.vsim.gates;

import cl.almejo.vsim.simulation.Scheduler;


public class SevenSegmentsDisplayPin extends Pin {
	public SevenSegmentsDisplayPin(Gate gate, Scheduler scheduler, int pindId) {
		super(gate, scheduler, pindId);
	}

	@Override
	public void hasChanged() {
		((SevenSegmentsDisplayParameters) _gate.getParamameters()).updateNumber((SevenSegmentsDisplay) _gate);
	}
}
