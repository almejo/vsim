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
import cl.almejo.vsim.gates.And;
import cl.almejo.vsim.gates.AndParams;
import cl.almejo.vsim.gates.Clock;
import cl.almejo.vsim.gates.ClockParams;
import cl.almejo.vsim.gates.Not;
import cl.almejo.vsim.gates.NotParams;

public class Main {

	class Simulator extends TimerTask {

		private Circuit _circuit;
		private long _lastSimulationTime;
		Simulator(Circuit circuit) {
			_circuit = circuit;
			_lastSimulationTime = System.currentTimeMillis();
		}
		@Override
		public void run() {
			long currentTime = System.currentTimeMillis();
			long simulationTime = currentTime - _lastSimulationTime;
			_lastSimulationTime = currentTime;
			_circuit.getScheduler().run((int) simulationTime);
		}

	}

	public Main() {
		Circuit circuit = new Circuit();

		IconGate iconClock = new IconGate(new Clock(circuit, new ClockParams(10000, 10000)));
		circuit.add(iconClock);

		IconGate iconClock2 = new IconGate(new Clock(circuit, new ClockParams(3000, 3000)));
		circuit.add(iconClock);

		IconGate iconNot = new IconGate(new Not(circuit, new NotParams(1000)));
		circuit.add(iconClock);

		IconGate iconAnd = new IconGate(new And(circuit, new AndParams(10)));
		circuit.add(iconClock);

		iconClock.getGate().getPin(0).connect(iconAnd.getGate().getPin(0));
		iconClock2.getGate().getPin(0).connect(iconAnd.getGate().getPin(1));
		
		iconAnd.getGate().getPin(2).connect(iconNot.getGate().getPin(0));
		
		Timer timer = new Timer();
		timer.schedule(new Simulator(circuit), 1000, 100);
	}

	public static void main(String[] args) {
		new Main();
	}
}
