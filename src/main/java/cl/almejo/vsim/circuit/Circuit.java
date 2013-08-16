package cl.almejo.vsim.circuit;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import cl.almejo.vsim.simulation.Scheduler;

public class Circuit {

	Scheduler _scheduler;

	List<IconGate> _icons = new LinkedList<IconGate>();

	private Protoboard _protoboard;

	public Circuit() {
		_scheduler = new Scheduler();
	}

	public void add(IconGate iconGate) {
		_icons.add(iconGate);
	}

	public Scheduler getScheduler() {
		return _scheduler;
	}

	public void setProtoboard(Protoboard protoboard) {
		_protoboard = protoboard;
		
	}

	public void paint(Graphics g) {
		_protoboard.paint(g);
	}
}
