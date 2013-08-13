/**
 *
 * vsim
 *
 * Created on Aug 12, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.simulation;

import java.util.Timer;
import java.util.TimerTask;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.IconGate;
import cl.almejo.vsim.gates.Clock;
import cl.almejo.vsim.gates.ClockParams;

public class Main {

	class Simulator extends TimerTask {

		private Scheduler _scheduler;
		private long _lastSimulationTime;
		Simulator(Scheduler scheduler) {
			_scheduler = scheduler;
			_lastSimulationTime = System.currentTimeMillis();
		}
		@Override
		public void run() {
			long currentTime = System.currentTimeMillis();
			long simulationTime = currentTime - _lastSimulationTime;
			_lastSimulationTime = currentTime;
			_scheduler.run(simulationTime);
		}

	}

	public Main() {
		Scheduler scheduler = new Scheduler();

		new Clock(scheduler, new ClockParams(1000, 5000));
		Timer timer = new Timer();
		timer.schedule(new Simulator(scheduler), 1000, 100);
	}

	public static void main(String[] args) {
		Circuit circuit = new Circuit();

		IconGate iconClock = new IconGate(new Clock(circuit.getScheduler(), new ClockParams(1000, 3000)));
		circuit.add(iconClock);
		new Main();
	}
}
