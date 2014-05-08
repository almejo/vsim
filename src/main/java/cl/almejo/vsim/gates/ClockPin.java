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

public class ClockPin extends Pin {

	class ClockEvent extends SimulationEvent {

		Clock _clock;

		public ClockEvent(Clock clock, Scheduler scheduler) {
			super(scheduler);
			_clock = clock;
		}

		public void happen() {
			ClockParams params = (ClockParams) _clock.getParamameters();
			int state = params.getState();
			schedule(state == Constants.OFF ? params.getTimeUp() : params.getTimeDown());

			_clock.getPin(0).hasChanged();

			params.setState((state == Constants.OFF ? Constants.ON : Constants.OFF));
		}
	}

	public ClockPin(Clock clock, Scheduler scheduler, int pinId) {
		super(clock, scheduler, pinId);
		ClockParams params = (ClockParams) clock.getParamameters();
		new ClockEvent(clock, scheduler).schedule(params.getTimeUp());
	}

	@Override
	public void hasChanged() {
		program(((ClockParams) _gate.getParamameters()).getState(), 0);
	}
}
