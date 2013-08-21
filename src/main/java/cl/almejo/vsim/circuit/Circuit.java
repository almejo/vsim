package cl.almejo.vsim.circuit;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.simulation.Scheduler;

public class Circuit {

	private static final int GRIDSIZE = 8;

	Scheduler _scheduler;

	List<IconGate> _icons = new LinkedList<IconGate>();

	private Protoboard _protoboard;

	private Rectangle _extent = new Rectangle();

	public Circuit() {
		_protoboard = new Protoboard();
		_scheduler = new Scheduler();
	}

	public Scheduler getScheduler() {
		return _scheduler;
	}

	public void paint(Graphics2D graphics) {
		drawGates(graphics);
		_protoboard.paint(graphics);
	}

	public void add(IconGate icon, int x, int y) {
		_icons.add(icon);

		Dimension size = icon.getSize();

		int _xi = Circuit.gridTrunc(x);// ((int) (x - size.getWidth() / 2)));
		int _yi = Circuit.gridTrunc(y);// ((int) (y - size.getHeight() / 2)));

		icon.setBounds(_xi, _yi, (int) size.getWidth(), (int) size.getHeight());

		icon.moveTo(Circuit.gridTrunc(x), Circuit.gridTrunc(y));

		activate(icon);

		_extent.union(icon);
		System.out.println(_extent);
	}

	public static int gridTrunc(int coord) {
		return coord & (~(GRIDSIZE - 1));
	}

	private void drawGates(Graphics2D graphics) {
		for (IconGate iconGate : _icons) {
			iconGate.paint(graphics);
		}
	}

	public void activate(IconGate icon) {
		for (byte pinId = 0; pinId < icon.getPinsCount(); pinId++) {
			addPin(pinId, icon.getTransformedPinPos(pinId), icon.getGate());
		}
	}

	public void desactivate(IconGate icon) {
		for (byte pinId = 0; pinId < icon.getPinsCount(); pinId++) {
			removePin(pinId, icon.getPinPos(pinId), icon.getGate());
		}
	}

	private void addPin(byte pinId, Point p, Gate gate) {
		_protoboard.addPin(pinId, gate, gridTrunc(p._x), gridTrunc(p._y));
	}

	private void removePin(byte pinId, Point p, Gate gate) {
		_protoboard.removePin(pinId, gate, gridTrunc(p._x), gridTrunc(p._y));
	}

	public void connect(int xi, int yi, int xf, int yf) {
		_protoboard.connect(gridTrunc(xi), gridTrunc(yi), gridTrunc(xf), gridTrunc(yf));
	}

	public void printMatrix() {
		_protoboard.printMatrix();
	}
}
