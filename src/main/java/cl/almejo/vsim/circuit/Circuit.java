package cl.almejo.vsim.circuit;

import java.util.LinkedList;
import java.util.List;

import cl.almejo.vsim.simulation.Scheduler;

public class Circuit {

	Scheduler _scheduler;

	List<IconGate> _icons = new LinkedList<IconGate>();

	public Circuit() {
		_scheduler = new Scheduler();
	}

	public void add(IconGate iconGate) {
		_icons.add(iconGate);
	}

	public Scheduler getScheduler() {
		return _scheduler;
	}
}
