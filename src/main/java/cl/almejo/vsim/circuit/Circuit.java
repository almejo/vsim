package cl.almejo.vsim.circuit;

import cl.almejo.vsim.circuit.commands.*;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.GateFactory;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.gui.ColorScheme;
import cl.almejo.vsim.gui.Draggable;
import cl.almejo.vsim.simulation.Scheduler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */

@Accessors(prefix = "_")
public class Circuit {

	private static final Logger LOGGER = LoggerFactory.getLogger(Circuit.class);

	private Simulator _simulatorTask;
	private boolean _simulationIsRunning;
	private final LinkedList<CircuitStateListener> _stateListeners = new LinkedList<>();

	@Getter
	@Setter
	private String _name;
	private Command _lastSavedCommand;
	private boolean _showGrid = true;

	private final GatesSelection _selection = new GatesSelection();
	private int _dragPreviewY;
	private int _dragPreviewX;
	private BufferedImage _dragPreview;
	private Rectangle _extent = new Rectangle();

	public static final int GRID_SIZE = 8;

	private final CommandManager _commandManager = new CommandManager();

	private final Scheduler _scheduler;

	private final List<IconGate> _icons = new LinkedList<>();

	private final List<CircuitCanvas> _canvases = new LinkedList<>();

	private final Protoboard _protoboard;

	private final Timer _simulationTimer = new Timer();

	private int _nextGateId;

	public void updateSelection() {
		_selection.updateExtent();
	}

	Rectangle getExtent() {
		return _extent;
	}

	private static class GatesSelection implements Selection {
		List<Draggable> _draggables = new LinkedList<>();
		Rectangle _extent;

		@Override
		public void add(Draggable selectable) {
			selectable.select();
			_draggables.add(selectable);
			updateExtent();
		}

		@Override
		public void remove(Draggable selectable) {
			selectable.deselect();
			_draggables.remove(selectable);
		}

		@Override
		public void clear() {
			_draggables.forEach(Draggable::deselect);
			_draggables.clear();
		}

		@Override
		public BufferedImage getImage() {
			if (_draggables.isEmpty()) {
				return null;
			}
			updateExtent();
			BufferedImage bufferedImage = new BufferedImage(_extent.width, _extent.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = bufferedImage.createGraphics();
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			_draggables.forEach(selection -> selection.drawPreview(graphics, _extent.getX(), _extent.getY()));
			return bufferedImage;
		}

		@Override
		public List<Draggable> getDraggables() {
			List<Draggable> draggables = new LinkedList<>();
			draggables.addAll(_draggables);
			return draggables;
		}

		private void updateExtent() {
			if (_draggables.isEmpty()) {
				_extent = new Rectangle();
				return;
			}
			_extent = new Rectangle(_draggables.get(0).getExtent());
			_draggables.forEach(draggable -> _extent.add(draggable.getExtent()));
		}

		@Override
		public int getX() {
			return (int) _extent.getX();
		}

		@Override
		public int getY() {
			return (int) _extent.getY();
		}


		boolean contains(int x, int y) {
			for (Draggable selectable : _draggables) {
				if (selectable.contains(x, y)) {
					return true;
				}
			}
			return false;
		}
	}

	public void toggleGrid() {
		_showGrid = !_showGrid;
	}

	private static class Simulator extends TimerTask {

		private final Circuit _circuit;

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

	private static class RepaintTask extends TimerTask {

		private final Circuit _circuit;

		RepaintTask(Circuit circuit) {
			_circuit = circuit;
		}

		@Override
		public void run() {
			_circuit.repaint();
		}
	}

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

	private void repaint() {
		_canvases.forEach(CircuitCanvas::repaint);
	}

	public Scheduler getScheduler() {
		return _scheduler;
	}

	void paint(Graphics2D graphics, Rectangle rectangle) {
		if (graphics != null) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (_showGrid) {
				drawGrid(graphics, rectangle);
			}
			drawGates(graphics, rectangle);
			_protoboard.paint(graphics, rectangle);
			drawGatesDecorations(graphics, rectangle);
			if (_dragPreview != null) {
				setAlpha(graphics, 0.5f);
				graphics.drawImage(_dragPreview, _dragPreviewX, _dragPreviewY, null);
				setAlpha(graphics, 1.0f);
			}
		}
	}

	private void setAlpha(Graphics2D graphics, float alpha) {
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	}

	private void drawGrid(Graphics2D graphics, Rectangle rectangle) {
		graphics.setColor(ColorScheme.getGrid());

		for (int x = Circuit.gridTrunc(rectangle.x - 10); x < Circuit.gridTrunc((int) (rectangle.x + rectangle.getWidth() + 20)); x += Circuit.GRID_SIZE * 2) {
			graphics.drawLine(x, Circuit.gridTrunc(rectangle.y - 10), x, Circuit.gridTrunc(rectangle.y) + (int) rectangle.getHeight());
		}
		for (int y = Circuit.gridTrunc(rectangle.y - 10); y < Circuit.gridTrunc((int) (rectangle.y + rectangle.getHeight() + 20)); y += Circuit.GRID_SIZE * 2) {
			graphics.drawLine(Circuit.gridTrunc(rectangle.x - 10), y, Circuit.gridTrunc(rectangle.x) + (int) rectangle.getWidth(), y);
		}
	}

	public void add(IconGate icon, int x, int y) {
		_icons.add(icon);

		Dimension size = icon.getSize();

		icon.setBounds(gridTrunc(x), gridTrunc(y), (int) size.getWidth(), (int) size.getHeight());
		icon.moveTo(gridTrunc(x), gridTrunc(y));

		activate(icon);
		updateExtent();
		sendChangedEvent();
	}

	public void remove(IconGate icon) {
		deactivate(icon);
		_icons.remove(icon);
		updateExtent();
		sendChangedEvent();
	}

	public void deactivate(IconGate icon) {
		for (byte pinId = 0; pinId < icon.getPinsCount(); pinId++) {
			removePin(pinId, icon.getTransformedPinPos(pinId), icon.getGate());
		}
	}

	public static int gridTrunc(int coordinated) {
		return coordinated & ~(GRID_SIZE - 1);
	}

	private void drawGates(Graphics2D graphics, Rectangle rectangle) {
		_icons.stream().filter(iconGate -> rectangle.intersects(iconGate.getExtent())).forEach(iconGate -> iconGate.drawIcon(graphics));
	}

	private void drawGatesDecorations(Graphics2D graphics, Rectangle rectangle) {
		_icons.stream().filter(iconGate -> rectangle.intersects(iconGate.getExtent())).forEach(iconGate -> iconGate.drawDecorations(graphics));
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
		updateExtent();
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

		// TODO: Change to findFirst but beware of two gates in the same position
		for (IconGate iconGate : _icons) {
			if (iconGate.contains(_x, _y)) {
				return iconGate;
			}
		}
		return null;
	}

	public List<Connection<Contact>> findToDisconnect(int x, int y) {
		return _protoboard.findToDisconnect(x, y);
	}

	public void disconnectBetween(int xi, int yi, int xf, int yf) {
		_protoboard.disconnectBetween(xi, yi, xf, yf);
		updateExtent();
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

	public void undoableRemoveGate(IconGate iconGate) {
		if (iconGate == null) {
			return;
		}
		RemoveGateCommand command = new RemoveGateCommand(this, iconGate);
		_commandManager.apply(command);
		sendChangedEvent();
	}

	public void disconnect(int xi, int yi, int xf, int yf) {
		_protoboard.disconnect(xi, yi, xf, yf);
		updateExtent();
	}

	public void undoableConnect(int xi, int yi, int xf, int yf) {
		int xiTrunc = gridTrunc(xi);
		int yiTrunc = gridTrunc(yi);
		int xfTrunc = gridTrunc(xf);
		int yfTrunc = gridTrunc(yf);

		if (xiTrunc == xfTrunc && yiTrunc == yfTrunc) {
			return;
		}

		ConnectCommand command = new ConnectCommand(this);
		if (yiTrunc != yfTrunc) {
			command.connect(xiTrunc, yiTrunc, xiTrunc, yfTrunc);
		}
		if (xiTrunc != xfTrunc) {
			command.connect(xiTrunc, yfTrunc, xfTrunc, yfTrunc);
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
		_stateListeners.forEach(listener -> listener.onSaved(event));
	}

	private void sendChangedEvent() {
		CircuitEvent event = new CircuitEvent(this);
		_stateListeners.forEach(listener -> listener.onChanged(event));
	}

	private void sendUndoEvent() {
		CircuitEvent event = new CircuitEvent(this);
		_stateListeners.forEach(listener -> listener.onUndo(event));
	}

	private void sendRedoEvent() {
		CircuitEvent event = new CircuitEvent(this);
		_stateListeners.forEach(listener -> listener.onRedo(event));
	}

	private void sendPauseEvent() {
		CircuitEvent event = new CircuitEvent(this);
		_stateListeners.forEach(listener -> listener.onPause(event));
	}

	private void sendResumeEvent() {
		CircuitEvent event = new CircuitEvent(this);
		_stateListeners.forEach(listener -> listener.onResume(event));
	}

	public boolean canUndo() {
		return _commandManager.canUndo();
	}

	public boolean canRedo() {
		return _commandManager.canRedo();
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
			((List<Map>) info.get("gates")).forEach(gate -> {
				Map position = (Map) gate.get("position");
				IconGate iconGate = GateFactory.getInstance((String) gate.get("type"), circuit);
				if (iconGate != null) {
					LOGGER.info("Created gate " + iconGate.getId());
					iconGate.getGate().getParamameters().setValues((Map<String, Object>) gate.get("parameters"));
					iconGate.getGate().parametersUpdated();
					circuit.undoableAddGate(iconGate, (Integer) position.get("x"), (Integer) position.get("y"));
				}
			});
			((List<Map>) info.get("connections")).forEach(connection -> circuit.undoableConnect((Integer) connection.get("xi")
					, (Integer) connection.get("yi")
					, (Integer) connection.get("xf")
					, (Integer) connection.get("yf")));

			circuit.setName(name);
			circuit.cleanHistory();
			circuit.setSaved();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return circuit;
	}

	public String toJSon() throws IOException {

		Map<String, Object> map = new HashMap<>();
		map.put("gates", _icons.stream().map(iconGate -> {
					Map<String, Object> gateInfo = new HashMap<>();
					gateInfo.put("type", iconGate.getGate().getGateDescriptor().getType());
					Map<String, Integer> position = new HashMap<>();
					position.put("x", (int) iconGate.getX());
					position.put("y", (int) iconGate.getY());
					gateInfo.put("position", position);
					gateInfo.put("parameters", gateParametersToMap(iconGate));
					return gateInfo;
				}
		).collect(Collectors.toList()));

		map.put("connections", _protoboard.getAllConnections().stream().map(connection -> {
			Map<String, Integer> connectionMap = new HashMap<>();
			connectionMap.put("xi", connection.getFirst().getX());
			connectionMap.put("yi", connection.getFirst().getY());
			connectionMap.put("xf", connection.getLast().getX());
			connectionMap.put("yf", connection.getLast().getY());
			return connectionMap;
		}).collect(Collectors.toList()));

		map.put("properties", new HashMap<String, String>());

		return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
	}

	private Map<String, Object> gateParametersToMap(IconGate iconGate) {
		return iconGate.getGate().getParamameters().getValues().stream().collect(Collectors.toMap(ConfigVariable::getName, this::getValue, (a, b) -> b));
	}

	private Object getValue(ConfigVariable variable) {
		return variable.getType() == ConfigValueType.STRING ? variable.getValue() : Integer.parseInt(variable.getValue());
	}

	private void cleanHistory() {
		_commandManager.cleanHistory();
	}

	public void select(Draggable draggable) {
		if (draggable instanceof IconGate) {
			_selection.add(draggable);
		}
	}

	public void clearSelection() {
		_selection.clear();
	}

	public void deselect(Draggable selectable) {
		_selection.remove(selectable);
	}

	public Draggable findDraggable(int x, int y) {
		for (IconGate iconGate : _icons) {
			if (iconGate.contains(x, y)) {
				return iconGate;
			}
		}
		return null;
	}

	public Configurable findConfigurable(int x, int y) {
		for (IconGate iconGate : _icons) {
			if (iconGate.contains(x, y)) {
				return iconGate;
			}
		}
		return null;
	}

	public Selection findSelection(int x, int y) {
		if (_selection.contains(x, y)) {
			return _selection;
		}
		return null;
	}

	public void drawDragPreview(int x, int y, BufferedImage preview) {
		_dragPreviewX = gridTrunc(x);
		_dragPreviewY = gridTrunc(y);
		_dragPreview = preview;
	}

	@SuppressWarnings("AssignmentToNull")
	public void clearDragPreview() {
		_dragPreview = null;
	}


	public void undoableDragSelection(Selection selection, int x, int y) {
		DragSelectionCommand command = new DragSelectionCommand(this, selection, gridTrunc(x), gridTrunc(y));
		_commandManager.apply(command);
		sendChangedEvent();
	}

	public void drag(List<Draggable> draggables, List<Point> destination) {
		int i = 0;
		for (Draggable draggable : draggables) {
			Point point = destination.get(i);
			draggable.drag(gridTrunc(point.getX()), gridTrunc(point.getY()));
			i++;
		}
	}

	public void setSelection(Draggable draggable) {
		_selection.clear();
		_selection.add(draggable);
	}


	public void undoableRotateClockWise(Configurable configurable) {
		RotateClockwiseCommand command = new RotateClockwiseCommand(configurable);
		_commandManager.apply(command);
		sendChangedEvent();
	}

	public void undoableRotateCounterClockWise(Configurable configurable) {
		RotateCounterClockwiseCommand command = new RotateCounterClockwiseCommand(configurable);
		_commandManager.apply(command);
		sendChangedEvent();
	}


	public void undoableConfig(Configurable configurable, Map<String, Object> parameters) {
		ConfigCommand command = new ConfigCommand(configurable, parameters);
		_commandManager.apply(command);
		sendChangedEvent();
	}

	private void updateExtent() {
		if (_icons.isEmpty()) {
			_extent = null;
			return;
		}

		_extent = getIconsExtent();
		if (_extent != null) {
			if (_protoboard.getExtent() != null) {
				_extent.add(_protoboard.getExtent());
			}
		} else {
			_extent = _protoboard.getExtent();
		}
	}

	private Rectangle getIconsExtent() {
		if (_icons.isEmpty()) {
			return null;
		}
		Rectangle rectangle = new Rectangle(_icons.get(0).getExtent());
		_icons.stream().map(IconGate::getExtent).forEach(rectangle::add);
		return rectangle;
	}
}