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

package cl.almejo.vsim.circuit;

import cl.almejo.vsim.circuit.commands.*;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.GateFactory;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.gui.ColorScheme;
import cl.almejo.vsim.simulation.Scheduler;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Circuit {

	private static final Logger LOGGER = LoggerFactory.getLogger(Circuit.class);

	private Simulator _simulatorTask;
	private boolean _simulationIsRunning = false;
	private LinkedList<CircuitStateListener> _stateListeners = new LinkedList<CircuitStateListener>();

	private String _name;
	private Command _lastSavedCommand = null;

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

	public static final int GRIDSIZE = 8;

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

	public void addCircuitEventListener(CircuitStateListener listener) {
		_stateListeners.add(listener);
	}

	public void removeCircuitEventListener(CircuitStateListener listener) {
		_stateListeners.remove(listener);
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
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			drawGrid(graphics, rectangle);
			drawGates(graphics, rectangle);
			_protoboard.paint(graphics, rectangle);
		}
	}

	private void drawGrid(Graphics2D graphics, Rectangle rectangle) {
		graphics.setColor(ColorScheme.getGrid());
		for (int x = Circuit.gridTrunc(rectangle.x - 10); x < rectangle.getWidth(); x += Circuit.GRIDSIZE * 2) {
			graphics.drawLine(x,  Circuit.gridTrunc(rectangle.y - 10), x,  Circuit.gridTrunc(rectangle.y - 10) + (int) rectangle.getHeight());
		}
		for (int y = Circuit.gridTrunc(rectangle.y - 10); y < rectangle.getHeight(); y += Circuit.GRIDSIZE * 2) {
			graphics.drawLine(0, y, (int) rectangle.getWidth(), y);
		}
	}

	public void add(IconGate icon, int x, int y) {
		_icons.add(icon);

		Dimension size = icon.getSize();

		icon.setBounds(gridTrunc(x), gridTrunc(y), (int) size.getWidth(), (int) size.getHeight());
		icon.moveTo(gridTrunc(x), gridTrunc(y));

		activate(icon);
		sendChangedEvent();
	}

	public void remove(IconGate icon) {
		desactivate(icon);
		_icons.remove(icon);
		sendChangedEvent();
	}

	public void desactivate(IconGate icon) {
		for (byte pinId = 0; pinId < icon.getPinsCount(); pinId++) {
			removePin(pinId, icon.getTransformedPinPos(pinId), icon.getGate());
		}
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

	public IconGate findIcon(int x, int y) {
		int _x = gridTrunc(x);
		int _y = gridTrunc(y);

		for (IconGate iconGate : _icons) {
			if (iconGate.contains(_x, _y))
				return iconGate;
		}
		return null;
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
		sendChangedEvent();
	}

	public void redo() {
		_commandManager.redo();
		sendRedoEvent();
		sendChangedEvent();
	}

	public void undoableAddGate(IconGate iconGate, int x, int y) {
		AddGateCommand command = new AddGateCommand(this, iconGate, gridTrunc(x), gridTrunc(y));
		_commandManager.apply(command);
		sendChangedEvent();
	}

	public void undoableRemoveGate(int x, int y) {
		RemoveGateCommand command = new RemoveGateCommand(this, gridTrunc(x), gridTrunc(y));
		_commandManager.apply(command);
		sendChangedEvent();
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

	public void setDrawConnectPreview(boolean draw) {
		_protoboard.setDrawConnectPreview(draw);
	}

	public void setConnectPreview(int xi, int yi, int xf, int yf) {
		_protoboard.setConnectPreview(xi, yi, gridTrunc(xf), gridTrunc(yf));

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

	private void sendSavedEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for (CircuitStateListener listener : _stateListeners) {
			listener.onSaved(event);
		}
	}

	private void sendChangedEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for (CircuitStateListener listener : _stateListeners) {
			listener.onChanged(event);
		}
	}

	private void sendUndoEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for (CircuitStateListener listener : _stateListeners) {
			listener.onUndo(event);
		}
	}

	private void sendRedoEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for (CircuitStateListener listener : _stateListeners) {
			listener.onRedo(event);
		}
	}

	private void sendPauseEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for (CircuitStateListener listener : _stateListeners) {
			listener.onPause(event);
		}
	}

	private void sendResumeEvent() {
		CircuitEvent event = new CircuitEvent(this);
		for (CircuitStateListener listener : _stateListeners) {
			listener.onResume(event);
		}
	}

	public boolean canUndo() {
		return _commandManager.canUndo();
	}

	public boolean canRedo() {
		return _commandManager.canRedo();
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public boolean isModified() {
		return _lastSavedCommand != _commandManager.getLastApplied();
	}

	public void setSaved() {
		_lastSavedCommand = _commandManager.getLastApplied();
		sendSavedEvent();
	}


	@SuppressWarnings("unchecked")
	public static Circuit fromJSon(String json, String name) {
		Circuit circuit = new Circuit();
		try {

			Map info = new ObjectMapper().readValue(json, Map.class);
			List<Map> gates = (List<Map>) info.get("gates");
			for (Map gate : gates) {

				Map position = (Map) gate.get("position");
				IconGate iconGate = GateFactory.getInstance((String) gate.get("type"), circuit);
				LOGGER.info("Created gate " + iconGate.getId());
				iconGate.getGate().getParamameters().setValues((Map<String, Object>) gate.get("parameters"));
				iconGate.getGate().parametersUpdated();
				circuit.undoableAddGate(iconGate, (Integer) position.get("x"), (Integer) position.get("y"));
			}
			List<Map> connections = (List<Map>) info.get("connections");
			for (Map connection : connections) {
				circuit.undoableConnect((Integer) connection.get("xi"), (Integer) connection.get("yi"), (Integer) connection.get("xf"), (Integer) connection.get("yf"));
			}

			circuit.setName(name);
			circuit.cleanHistory();
			circuit.setSaved();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return circuit;
	}

	public String toJSon() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> gates = new LinkedList<Map>();
		for (IconGate iconGate : _icons) {
			Map<String, Object> gateInfo = new HashMap<String, Object>();
			gateInfo.put("type", iconGate.getGate().getGateDescriptor().getType());
			Map<String, Integer> position = new HashMap<String, Integer>();
			position.put("x", (int) iconGate.getX());
			position.put("y", (int) iconGate.getY());
			gateInfo.put("position", position);

			Map<String, Object> parameters = new HashMap<String, Object>();
			iconGate.getGate().getParamameters().getValues(parameters);
			gateInfo.put("parameters", parameters);
			gates.add(gateInfo);
		}
		map.put("gates", gates);

		List<Map<String, Integer>> allConnections = new LinkedList<Map<String, Integer>>();
		for (Connection<Contact> connection : _protoboard.getAllConnections()) {
			Map<String, Integer> connectionMap = new HashMap<String, Integer>();
			connectionMap.put("xi", connection.getFirst().getX());
			connectionMap.put("yi", connection.getFirst().getY());
			connectionMap.put("xf", connection.getLast().getX());
			connectionMap.put("yf", connection.getLast().getY());
			allConnections.add(connectionMap);
		}
		map.put("connections", allConnections);

		Map<String, String> properties = new HashMap<String, String>();
		map.put("properties", properties);

		ObjectMapper mapper = new ObjectMapper();
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
	}

	private void cleanHistory() {
		_commandManager.cleanHistory();
	}


}
