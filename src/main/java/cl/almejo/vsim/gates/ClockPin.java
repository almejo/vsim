/**
 *
 * vsim
 *
 * Created on Aug 2, 2013
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
			ClockParams params = (ClockParams) _clock.getParams();
			int state = params.getState();
			schedule(state == Constants.OFF ? params.getTimeUp() : params.getTimeDown());

			_clock.getPin(0).hasChanged();

			params.setState((byte) (state == Constants.OFF ? Constants.ON : Constants.OFF));
		}
	}

	private ClockEvent _clockEvent;

	public ClockPin(Clock clock, Scheduler scheduler) {
		super(clock, scheduler);
		ClockParams params = (ClockParams) clock.getParams();
		_clockEvent = new ClockEvent(clock, scheduler);
		_clockEvent.schedule(params.getTimeUp());
	}

	@Override
	public void hasChanged() {
		super.hasChanged();
		program(((ClockParams) _gate.getParams()).getState(), 0);
	}

}
