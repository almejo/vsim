package cl.almejo.vsim.circuit;

import cl.almejo.vsim.circuit.commands.*;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gates.GateFactory;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.gui.ColorScheme;
import cl.almejo.vsim.gui.Selectable;
import cl.almejo.vsim.simulation.Scheduler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
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

public class Circuit {

	private static final Logger LOGGER = LoggerFactory.getLogger(Circuit.class);

	private Simulator simulatorTask;
	private boolean simulationIsRunning;
	private final LinkedList<CircuitStateListener> stateListeners = new LinkedList<>();

	@Getter
	@Setter
	private String name;
	private Command lastSavedCommand;
	private boolean showGrid = true;

	private final SelectedGates selectedGates = new SelectedGates();

	private Selection selectedArea = new Selection();

	private int dragPreviewY;
	private int dragPreviewX;
	private BufferedImage dragPreview;
	private Rectangle extent = new Rectangle();

	public static final int GRID_SIZE = 8;

	private final CommandManager commandManager = new CommandManager();

	private final Scheduler scheduler;

	private final List<IconGate> icons = new LinkedList<>();

	private final List<CircuitCanvas> canvases = new LinkedList<>();

	private final Protoboard protoboard;

	private final Timer simulationTimer = new Timer();

	private int nextGateId;

	public void updateSelection() {
		selectedGates.updateExtent();
	}

	Rectangle getExtent() {
		return extent;
	}

	public Selection findSelectedArea(int x, int y) {
		if (this.selectedArea.contains(x, y)) {
			return this.selectedArea;
		}
		return null;
	}


	public void toggleGrid() {
		showGrid = !showGrid;
	}

	public void startSelectedArea(int x, int y) {
		this.selectedArea.setStart(x, y);
	}

	public void clearSelectedArea() {
		this.selectedArea.clear();
	}

	public void endSelectedArea(int x, int y) {
		this.selectedArea.setEnd(x, y);
	}

	private static class Simulator extends TimerTask {

		private final Circuit circuit;

		private long lastSimulationTime;

		Simulator(Circuit circuit) {
			this.circuit = circuit;
			lastSimulationTime = System.currentTimeMillis();
		}

		@Override
		public void run() {
			long currentTime = System.currentTimeMillis();
			long simulationTime = currentTime - lastSimulationTime;
			lastSimulationTime = currentTime;
			circuit.getScheduler().run((int) simulationTime);
		}

	}

	private static class RepaintTask extends TimerTask {

		private final Circuit circuit;

		RepaintTask(Circuit circuit) {
			this.circuit = circuit;
		}

		@Override
		public void run() {
			circuit.repaint();
		}
	}

	public Circuit() {
		protoboard = new Protoboard();
		scheduler = new Scheduler();

		new Timer().schedule(new RepaintTask(this), 100, 100);
		startSimulation();
	}

	public void addCircuitEventListener(CircuitStateListener listener) {
		stateListeners.add(listener);
	}

	public void removeCircuitEventListener(CircuitStateListener listener) {
		stateListeners.remove(listener);
	}

	private void repaint() {
		canvases.forEach(CircuitCanvas::repaint);
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	void paint(Graphics2D graphics, Rectangle rectangle) {
		if (graphics != null) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (showGrid) {
				drawGrid(graphics, rectangle);
			}
			drawGates(graphics, rectangle);
			protoboard.paint(graphics, rectangle);
			drawGatesDecorations(graphics, rectangle);
			if (!selectedArea.isEmpty()) {
				Color color = graphics.getColor();
				graphics.setColor(Color.RED);
				graphics.drawRect((int) selectedArea.getX()
						, (int) selectedArea.getX()
						, (int) selectedArea.getWidth()
						, (int) selectedArea.getHeight());
				graphics.setColor(color);
			}
			if (dragPreview != null) {
				setAlpha(graphics, 0.5f);
				graphics.drawImage(dragPreview, dragPreviewX, dragPreviewY, null);
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
		icons.add(icon);

		Dimension size = icon.getSize();

		icon.setBounds(gridTrunc(x), gridTrunc(y), (int) size.getWidth(), (int) size.getHeight());
		icon.moveTo(gridTrunc(x), gridTrunc(y));

		activate(icon);
		updateExtent();
		sendChangedEvent();
	}

	public void remove(IconGate icon) {
		deactivate(icon);
		icons.remove(icon);
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
		icons.stream().filter(iconGate -> rectangle.intersects(iconGate.getExtent())).forEach(iconGate -> iconGate.drawIcon(graphics));
	}

	private void drawGatesDecorations(Graphics2D graphics, Rectangle rectangle) {
		icons.stream().filter(iconGate -> rectangle.intersects(iconGate.getExtent())).forEach(iconGate -> iconGate.drawDecorations(graphics));
	}

	public void activate(IconGate icon) {
		for (byte pinId = 0; pinId < icon.getPinsCount(); pinId++) {
			addPin(pinId, icon.getTransformedPinPos(pinId), icon.getGate());
		}
	}

	private void addPin(byte pinId, Point p, Gate gate) {
		protoboard.addPin(pinId, gate, gridTrunc(p.x), gridTrunc(p.y));
	}

	private void removePin(byte pinId, Point p, Gate gate) {
		protoboard.removePin(pinId, gate, gridTrunc(p.x), gridTrunc(p.y));
	}

	public void connect(int xi, int yi, int xf, int yf) {
		protoboard.connect(gridTrunc(xi), gridTrunc(yi), gridTrunc(xf), gridTrunc(yf));
		updateExtent();
	}

	public void add(CircuitCanvas canvas) {
		canvases.add(canvas);
	}

	public void remove(CircuitCanvas canvas) {
		canvases.remove(canvas);
	}

	public IconGate findIcon(int x, int y) {
		int x2grid = gridTrunc(x);
		int y2grid = gridTrunc(y);

		// TODO: Change to findFirst but beware of two gates in the same position
		for (IconGate iconGate : icons) {
			if (iconGate.contains(x2grid, y2grid)) {
				return iconGate;
			}
		}
		return null;
	}

	public List<Connection<Contact>> findToDisconnect(int x, int y) {
		return protoboard.findToDisconnect(x, y);
	}

	public void disconnectBetween(int xi, int yi, int xf, int yf) {
		protoboard.disconnectBetween(xi, yi, xf, yf);
		updateExtent();
	}

	public List<Connection<Contact>> findBeforeConnect(int xi, int yi, int xf, int yf) {
		return protoboard.findBeforeConnect(xi, yi, xf, yf);
	}


	public void undo() {
		commandManager.undo();
		sendUndoEvent();
		sendChangedEvent();
	}

	public void redo() {
		commandManager.redo();
		sendRedoEvent();
		sendChangedEvent();
	}

	public void undoableAddGate(IconGate iconGate, int x, int y) {
		AddGateCommand command = new AddGateCommand(this, iconGate, gridTrunc(x), gridTrunc(y));
		commandManager.apply(command);
		sendChangedEvent();
	}

	public void undoableRemoveGate(IconGate iconGate) {
		if (iconGate == null) {
			return;
		}
		RemoveGateCommand command = new RemoveGateCommand(this, iconGate);
		commandManager.apply(command);
		sendChangedEvent();
	}

	public void disconnect(int xi, int yi, int xf, int yf) {
		protoboard.disconnect(xi, yi, xf, yf);
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

		commandManager.apply(command);
		sendChangedEvent();
	}

	public int getNextGateId() {
		return nextGateId++;
	}

	public void undoableDisconnect(int x, int y) {
		commandManager.apply(new DisconnectCommand(this, gridTrunc(x), gridTrunc(y)));
		sendChangedEvent();
	}

	public void setDrawConnectPreview(boolean draw) {
		protoboard.setDrawConnectPreview(draw);
	}

	public void setConnectPreview(int xi, int yi, int xf, int yf) {
		protoboard.setConnectPreview(xi, yi, gridTrunc(xf), gridTrunc(yf));

	}

	public void toggleSimulation() {
		if (simulationIsRunning) {
			stopSimulation();
		} else {
			startSimulation();
		}
	}

	public boolean isSimulationRunning() {
		return simulationIsRunning;
	}

	private void startSimulation() {
		simulatorTask = new Simulator(this);
		simulationTimer.schedule(simulatorTask, 1000, 100);
		simulationIsRunning = true;
		sendResumeEvent();
	}

	private void stopSimulation() {
		simulatorTask.cancel();
		simulationTimer.purge();
		simulationIsRunning = false;
		sendPauseEvent();
	}

	private void sendSavedEvent() {
		CircuitEvent event = new CircuitEvent(this);
		stateListeners.forEach(listener -> listener.onSaved(event));
	}

	private void sendChangedEvent() {
		CircuitEvent event = new CircuitEvent(this);
		stateListeners.forEach(listener -> listener.onChanged(event));
	}

	private void sendUndoEvent() {
		CircuitEvent event = new CircuitEvent(this);
		stateListeners.forEach(listener -> listener.onUndo(event));
	}

	private void sendRedoEvent() {
		CircuitEvent event = new CircuitEvent(this);
		stateListeners.forEach(listener -> listener.onRedo(event));
	}

	private void sendPauseEvent() {
		CircuitEvent event = new CircuitEvent(this);
		stateListeners.forEach(listener -> listener.onPause(event));
	}

	private void sendResumeEvent() {
		CircuitEvent event = new CircuitEvent(this);
		stateListeners.forEach(listener -> listener.onResume(event));
	}

	public boolean canUndo() {
		return commandManager.canUndo();
	}

	public boolean canRedo() {
		return commandManager.canRedo();
	}

	public boolean isModified() {
		return lastSavedCommand != commandManager.getLastApplied();
	}

	public void setSaved() {
		lastSavedCommand = commandManager.getLastApplied();
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
					iconGate.getGate().getParameters().setValues((Map<String, Object>) gate.get("parameters"));
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
		map.put("gates", icons.stream().map(iconGate -> {
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

		map.put("connections", protoboard.getAllConnections().stream().map(connection -> {
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
		return iconGate.getGate().getParameters().getValues().stream().collect(Collectors.toMap(ConfigVariable::getName, this::getValue, (a, b) -> b));
	}

	private Object getValue(ConfigVariable variable) {
		return variable.getType() == ConfigValueType.STRING ? variable.getValue() : Integer.parseInt(variable.getValue());
	}

	private void cleanHistory() {
		commandManager.cleanHistory();
	}

	public void select(Selectable selectable) {
		selectedGates.add(selectable);
	}

	public void clearSelection() {
		selectedGates.clear();
	}

	public void deselect(Selectable selectable) {
		selectedGates.remove(selectable);
	}

	public Selectable findDraggable(int x, int y) {
		for (IconGate iconGate : icons) {
			if (iconGate.contains(x, y)) {
				return iconGate;
			}
		}
		return null;
	}

	public Configurable findConfigurable(int x, int y) {
		for (IconGate iconGate : icons) {
			if (iconGate.contains(x, y)) {
				return iconGate;
			}
		}
		return null;
	}

	public SelectedGates findSelectedGates(int x, int y) {
		if (selectedGates.contains(x, y)) {
			return selectedGates;
		}
		return null;
	}

	public void drawDragPreview(int x, int y, BufferedImage preview) {
		dragPreviewX = gridTrunc(x);
		dragPreviewY = gridTrunc(y);
		dragPreview = preview;
	}

	@SuppressWarnings("AssignmentToNull")
	public void clearDragPreview() {
		dragPreview = null;
	}


	public void undoableDragSelection(Selection selection, int x, int y) {
		DragSelectionCommand command = new DragSelectionCommand(this, selection, gridTrunc(x), gridTrunc(y));
		commandManager.apply(command);
		sendChangedEvent();
	}

	public void drag(List<Selectable> draggables, List<Point> destination) {
		int i = 0;
		for (Selectable draggable : draggables) {
			Point point = destination.get(i);
			draggable.drag(gridTrunc(point.getX()), gridTrunc(point.getY()));
			i++;
		}
	}

	public void setSelectedGates(Selectable draggable) {
		selectedGates.clear();
		selectedGates.add(draggable);
	}


	public void undoableRotateClockWise(Configurable configurable) {
		RotateClockwiseCommand command = new RotateClockwiseCommand(configurable);
		commandManager.apply(command);
		sendChangedEvent();
	}

	public void undoableRotateCounterClockWise(Configurable configurable) {
		RotateCounterClockwiseCommand command = new RotateCounterClockwiseCommand(configurable);
		commandManager.apply(command);
		sendChangedEvent();
	}


	public void undoableConfig(Configurable configurable, Map<String, Object> parameters) {
		ConfigCommand command = new ConfigCommand(configurable, parameters);
		commandManager.apply(command);
		sendChangedEvent();
	}

	private void updateExtent() {
		if (icons.isEmpty()) {
			extent = null;
			return;
		}

		extent = getIconsExtent();
		if (extent != null) {
			if (protoboard.getExtent() != null) {
				extent.add(protoboard.getExtent());
			}
		} else {
			extent = protoboard.getExtent();
		}
	}

	private Rectangle getIconsExtent() {
		if (icons.isEmpty()) {
			return null;
		}
		Rectangle rectangle = new Rectangle(icons.get(0).getExtent());
		icons.stream().map(IconGate::getExtent).forEach(rectangle::add);
		return rectangle;
	}
}