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
public class ClockPin extends Pin {

	static class ClockEvent extends SimulationEvent {

		Clock _clock;

		public ClockEvent(Clock clock, Scheduler scheduler) {
			super(scheduler);
			_clock = clock;
		}

		@Override
		public void happen() {
			ClockParameters params = (ClockParameters) _clock.getParamameters();
			int state = params.getState();
			schedule(state == Constants.OFF ? params.getTimeUp() : params.getTimeDown());

			_clock.getPin(0).hasChanged();

			params.setState(state == Constants.OFF ? Constants.ON : Constants.OFF);
		}
	}

	public ClockPin(Clock clock, Scheduler scheduler, int pinId) {
		super(clock, scheduler, pinId);
		ClockParameters params = (ClockParameters) clock.getParamameters();
		new ClockEvent(clock, scheduler).schedule(params.getTimeUp());
	}

	@Override
	public void hasChanged() {
		program(((ClockParameters) _gate.getParamameters()).getState(), 0);
	}
}
