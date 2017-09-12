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
class SevenSegmentsDisplayPin extends Pin {
	SevenSegmentsDisplayPin(Gate gate, Scheduler scheduler, int pinId) {
		super(gate, scheduler, pinId);
	}

	@Override
	public void hasChanged() {
		((SevenSegmentsDisplayParameters) _gate.getParamameters()).updateNumber((SevenSegmentsDisplay) _gate);
	}
}
