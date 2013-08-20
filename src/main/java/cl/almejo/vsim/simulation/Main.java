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
import cl.almejo.vsim.circuit.SimWindow;
import cl.almejo.vsim.gates.And;
import cl.almejo.vsim.gates.AndDescriptor;
import cl.almejo.vsim.gates.AndParams;
import cl.almejo.vsim.gates.Clock;
import cl.almejo.vsim.gates.ClockDescriptor;
import cl.almejo.vsim.gates.ClockParams;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.gates.Not;
import cl.almejo.vsim.gates.NotDescriptor;
import cl.almejo.vsim.gates.NotParams;

class P {
	int getX() {
		return 0;
	}
}

class C extends P {
	int getX() {
		return 5;
	}
}

class M<T extends P> {
	T _t;
	M() {
		
	}
	M(T t) {
		_t= t;
	}
	
	int getX() {
		return _t.getX();
	}
}
public class Main {

	class Simulator extends TimerTask {

		M<C> m = new M<C>();
				
		
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


//
//
//		IconGate iconAnd = new IconGate(new And(circuit, new AndParams(10)));
//		circuit.add(iconClock);

//		iconClock.getGate().getPin(0).connect(iconAnd.getGate().getPin(0));
//		iconClock2.getGate().getPin(0).connect(iconAnd.getGate().getPin(1));
//		
//		iconAnd.getGate().getPin(2).connect(iconNot.getGate().getPin(0));
		
		ClockDescriptor descriptor = new ClockDescriptor();
		IconGate iconClock = new IconGate(new Clock(circuit, new ClockParams(1000, 1000), descriptor));
		circuit.add(iconClock, 100, 80);
		
		IconGate iconClock2 = new IconGate(new Clock(circuit, new ClockParams(3000, 3000), descriptor));
		circuit.add(iconClock2, 100, 118 );
		
		IconGate iconAnd = new IconGate(new And(circuit, new AndParams(1), new AndDescriptor()));
		circuit.add(iconAnd, 300, 96);
		
		IconGate iconNot = new IconGate(new Not(circuit, new NotParams(1), new NotDescriptor()));
		circuit.add(iconNot, 400, 96);

		
//		
//		protoboard.addPin(0, iconClock.getGate(), 0, 100);
//		protoboard.addPin(0, iconClock2.getGate(), 0, 0);
//		
//		protoboard.addPin(0, iconAnd.getGate(), 100, 100);
//		protoboard.addPin(1, iconAnd.getGate(), 100, 0);
//		protoboard.addPin(2, iconAnd.getGate(), 200, 100);
//		
//		protoboard.addPin(0, iconNot.getGate(), 300, 100);
//		
		circuit.connect(112, 96, 300, 96);
		circuit.connect(112, 128, 300, 128);
		circuit.connect(140, 128, 150, 128);
	//	circuit.connect(300, 96, 300, 150);
		circuit.printMatrix();
//		protoboard.connect(0, 0, 100, 0);
//		
//		protoboard.connect(200, 100, 300, 100);
//		
//		protoboard.connect(200, 500, 500, 500);
		//protoboard.connect(0, 0, 100, 0);
		
		
		Timer timer = new Timer();
		timer.schedule(new Simulator(circuit), 1000, 100);
		
		new SimWindow(circuit);
	}

	public static void main(String[] args) {
		new Main();
	}
}
