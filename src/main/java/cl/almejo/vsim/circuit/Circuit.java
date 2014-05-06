package cl.almejo.vsim.circuit;

import cl.almejo.vsim.circuit.commands.CommandManager;
import cl.almejo.vsim.circuit.commands.ConnectCommand;
import cl.almejo.vsim.circuit.commands.DisconnectCommand;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.simulation.Scheduler;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Circuit {

	private Simulator _simulatorTask;
	private boolean _simulationIsRunning = false;
	private LinkedList<CircuitStateListner> _stateListeners = new LinkedList<CircuitStateListner>();


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

	class RepaintTask extends TimerTask {

		private final Circuit _circuit;

		public RepaintTask(Circuit circuit) {
			_circuit = circuit;
		}

		@Override
		public void run() {
			_circuit.repaint();
		}
	}

	private static final int GRIDSIZE = 8;

	private CommandManager _commandManager = new CommandManager();

	private Scheduler _scheduler;

	private List<IconGate> _icons = new LinkedList<IconGate>();

	private List<CircuitCanvas> _canvases = new LinkedList<CircuitCanvas>();

	private Protoboard _protoboard;

	private Timer _simulationTimer = new Timer();

	private int _nextGateId = 0;

	public Circuit() {
		_protoboard = new Protoboard();
		_scheduler = new Scheduler();

		new Timer().schedule(new RepaintTask(this), 100, 100);
		startSimulation();
	}

	public void addCircuitEventListener(CircuitStateListner listener) {
		_stateListeners.add(listener);
	}

	public void repaint() {
		for (CircuitCanvas canvas : _canvases) {
			canvas.repaint();
		}
	}

	public Scheduler getScheduler() {
		return _scheduler;
	}

	public void paint(Graphics2D graphics, Rectangle rectangle) {
		if (graphics != null) {
			drawGates(graphics, rectangle);
			_protoboard.paint(graphics, rectangle);
		}
	}

	public void add(IconGate icon, int x, int y) {
		_icons.add(icon);

		Dimension size = icon.getSize();

		int _xi = Circuit.gridTrunc(x);
		int _yi = Circuit.gridTrunc(y);

		icon.setBounds(_xi, _yi, (int) size.getWidth(), (int) size.getHeight());

		icon.moveTo(Circuit.gridTrunc(x), Circuit.gridTrunc(y));

		activate(icon);
		sendChangedEvent();
	}

	public static int gridTrunc(int coord) {
		return coord & (~(GRIDSIZE - 1));
	}

	private void drawGates(Graphics2D graphics, Rectangle rectangle) {
		for (IconGate iconGate : _icons) {
			if (rectangle.intersects(iconGate)) {
				iconGate.paint(graphics);
			}
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

	public void add(CircuitCanvas canvas) {
		_canvases.add(canvas);
	}

	public void remove(CircuitCanvas canvas) {
		_canvases.remove(canvas);
	}

	public List<Connection<Contact>> findToDisconnect(int x, int y) {
		return _protoboard.findToDisconnect(x, y);
	}

	public void disconnectBetween(int xi, int yi, int xf, int yf) {
		_protoboard.disconnectBetween(xi, yi, xf, yf);
	}

	public List<Connection<Contact>> findBeforeConnect(int xi, int yi, int xf, int yf) {
		return _protoboard.findBeforeConnect(xi, yi, xf, yf);
	}


	public void undo() {
		_commandManager.undo();
		sendUndoEvent();
	}

	public void redo() {
		_commandManager.redo();
		sendRedoEvent();
	}

	public void disconnect(int xi, int yi, int xf, int yf) {
		_protoboard.disconnect(xi, yi, xf, yf);
	}

	public void undoableConnect(int xi, int yi, int xf, int yf) {
		xi = gridTrunc(xi);
		yi = gridTrunc(yi);
		xf = gridTrunc(xf);
		yf = gridTrunc(yf);

		if (xi == xf && yi == yf) {
			return;
		}

		ConnectCommand command = new ConnectCommand(this);
		if (yi != yf) {
			command.connect(xi, yi, xi, yf);
		}
		if (xi != xf) {
			command.connect(xi, yf, xf, yf);
		}

		_commandManager.apply(command);
		sendChangedEvent();
	}

	public int getNextGateId() {
		return _nextGateId++;
	}

	public void undoableDisconnect(int x, int y) {
		_commandManager.apply(new DisconnectCommand(this, gridTrunc(x), gridTrunc(y)));
		sendChangedEvent();
	}

	public void setDrawConnectPriew(boolean draw) {
		_protoboard.setDrawConnectPreview(draw);
	}

	public void setConnectPreview(int xi, int yi, int xf, int yf) {
		_protoboard.setConnectPreview(xi, yi, xf, yf);

	}

	public void toggleSimulation() {
		if (_simulationIsRunning) {
			stopSimulation();
		} else {
			startSimulation();
		}
	}

	public boolean isSimulationRunning() {
		return _simulationIsRunning;
	}

	private void startSimulation() {
		_simulatorTask = new Simulator(this);
		_simulationTimer.schedule(_simulatorTask, 1000, 100);
		_simulationIsRunning = true;
		sendResumeEvent();
	}

	private void stopSimulation() {
		_simulatorTask.cancel();
		_simulationTimer.purge();
		_simulationIsRunning = false;
		sendPauseEvent();
	}

	private void sendChangedEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for(CircuitStateListner listner:_stateListeners) {
			listner.onChanged(event);
		}
	}

	private void sendUndoEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for(CircuitStateListner listner:_stateListeners) {
			listner.onUndo(event);
		}
	}

	private void sendRedoEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for(CircuitStateListner listner:_stateListeners) {
			listner.onRedo(event);
		}
	}

	private void sendPauseEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for(CircuitStateListner listner:_stateListeners) {
			listner.onPause(event);
		}
	}

	private void sendResumeEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for(CircuitStateListner listner:_stateListeners) {
			listner.onResume(event);
		}
	}

	public boolean canUndo() {
		return _commandManager.canUndo();
	}

	public boolean canRedo() {
		return _commandManager.canRedo();
	}
}
