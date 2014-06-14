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


package cl.almejo.vsim.gui;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.CircuitCanvas;
import cl.almejo.vsim.circuit.CircuitEvent;
import cl.almejo.vsim.circuit.CircuitStateListener;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gui.actions.*;
import cl.almejo.vsim.gui.actions.state.ActionToolHelper;
import cl.almejo.vsim.gui.actions.state.GateToolHelper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class SimWindow extends JFrame implements ComponentListener, WindowListener, MouseListener, MouseMotionListener, CircuitStateListener, ChangeListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimWindow.class);

	private static final long serialVersionUID = 1L;
	private final CircuitCanvas _canvas;
	private final JTabbedPane _displaysPane;
	private final JSlider _zoomSlider;
	private Circuit _circuit;

	private ActionToolHelper _toolHelper = ActionToolHelper.CURSOR;

	static final KeyStroke ACCELERATOR_NEW = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
	static final KeyStroke ACCELERATOR_OPEN = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
	static final KeyStroke ACCELERATOR_SAVE = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
	static final KeyStroke ACCELERATOR_SAVE_AS = KeyStroke.getKeyStroke("control shift S");
	static final KeyStroke ACCELERATOR_QUIT = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK);

	static final KeyStroke ACCELERATOR_CUT = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK);
	static final KeyStroke ACCELERATOR_COPY = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
	static final KeyStroke ACCELERATOR_PASTE = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
	static final KeyStroke ACCELERATOR_UNDO = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
	static final KeyStroke ACCELERATOR_REDO = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);
	static final KeyStroke ACCELERATOR_PREFERENCES = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK);


	private final WindowAction NEW_ACTION = new NewAction(Messages.t("action.new"), Messages.t("action.new.description"), "new.png", ACCELERATOR_NEW, this);
	private final WindowAction OPEN_ACTION = new OpenAction(Messages.t("action.open"), Messages.t("action.open.description"), "open.png", ACCELERATOR_OPEN, this);
	private final WindowAction SAVE_ACTION = new SaveAction(Messages.t("action.save"), Messages.t("action.save.description"), "save.png", ACCELERATOR_SAVE, this);
	private final WindowAction SAVE_AS_ACTION = new SaveAsAction(Messages.t("action.saveas"), Messages.t("action.saveas.description"), null, ACCELERATOR_SAVE_AS, this);
	private final WindowAction QUIT_ACTION = new QuitAction(Messages.t("action.quit"), Messages.t("action.quit.description"), null, ACCELERATOR_QUIT, this);

	private final WindowAction CUT_ACTION = new CutAction(Messages.t("action.cut"), Messages.t("action.cut.description"), "cut.png", ACCELERATOR_CUT, this);
	private final WindowAction COPY_ACTION = new CopyAction(Messages.t("action.copy"), Messages.t("action.copy.description"), "copy.png", ACCELERATOR_COPY, this);
	private final WindowAction PASTE_ACTION = new PasteAction(Messages.t("action.paste"), Messages.t("action.paste.description"), "paste.png", ACCELERATOR_PASTE, this);
	private final WindowAction UNDO_ACTION = new UndoAction(Messages.t("action.undo"), Messages.t("action.undo.description"), "undo.png", ACCELERATOR_UNDO, this);
	private final WindowAction REDO_ACTION = new RedoAction(Messages.t("action.redo"), Messages.t("action.redo.description"), "redo.png", ACCELERATOR_REDO, this);
	private final WindowAction PREFERENCES_ACTION = new PreferencesAction(Messages.t("action.preferences"), Messages.t("action.preferences.description"), null, ACCELERATOR_PREFERENCES, this);

	private final WindowAction START_ACTION = new StartStopSimulationAction(Messages.t("action.start"), Messages.t("action.start.description"), "play.png", null, this);
	private final WindowAction PAUSE_ACTION = new StartStopSimulationAction(Messages.t("action.pause"), Messages.t("action.pause.description"), "pause.png", null, this);

	private final WindowAction CLONE_WINDOW_ACTION = new CloneWindowAction(Messages.t("action.window.clone"), Messages.t("action.window.clone.description"), null, null, this);
	private final WindowAction CLOSE_ACTION = new CloseWindowAction(Messages.t("action.window.close"), Messages.t("action.window.close.description"), null, null, this);


	private final WindowAction ONLINE_HELP_ACTION = new OnlineHelpAction(Messages.t("action.help.online"), Messages.t("action.help.online.description"), null, null, this);
	private final WindowAction ABOUT_ACTION = new AboutAction(Messages.t("action.help.about"), Messages.t("action.help.about.description"), null, null, this);

	private final WindowAction CURSOR_TOOL_ACTION = new ToolAction(Messages.t("action.tool.cursor"), Messages.t("action.tool.cursor.description"), "cursor.png", null, this, ActionToolHelper.CURSOR);
	private final WindowAction MOVE_VIEWPORT_TOOL_ACTION = new ToolAction(Messages.t("action.tool.move.viewport"), Messages.t("action.tool.move.viewport.description"), "move-viewport.png", null, this, ActionToolHelper.MOVE_VIEWPORT);
	private final WindowAction WIRES_TOOL_ACTION = new ToolAction(Messages.t("action.tool.wires"), Messages.t("action.tool.wires.description"), "wires.png", null, this, ActionToolHelper.WIRES);
	private final WindowAction AND2_TOOL_ACTION = new ToolAction(Messages.t("action.tool.and2"), Messages.t("action.tool.and2.description"), "and2.png", null, this, new GateToolHelper(Gate.AND2));
	private final WindowAction AND3_TOOL_ACTION = new ToolAction(Messages.t("action.tool.and3"), Messages.t("action.tool.and3.description"), "and3.png", null, this, new GateToolHelper(Gate.AND3));
	private final WindowAction AND4_TOOL_ACTION = new ToolAction(Messages.t("action.tool.and4"), Messages.t("action.tool.and4.description"), "and4.png", null, this, new GateToolHelper(Gate.AND4));
	private final WindowAction OR2_TOOL_ACTION = new ToolAction(Messages.t("action.tool.or2"), Messages.t("action.tool.or2.description"), "or2.png", null, this, new GateToolHelper(Gate.OR2));
	private final WindowAction OR3_TOOL_ACTION = new ToolAction(Messages.t("action.tool.or3"), Messages.t("action.tool.or3.description"), "or3.png", null, this, new GateToolHelper(Gate.OR3));
	private final WindowAction OR4_TOOL_ACTION = new ToolAction(Messages.t("action.tool.or4"), Messages.t("action.tool.or4.description"), "or4.png", null, this, new GateToolHelper(Gate.OR4));
	private final WindowAction NOT_TOOL_ACTION = new ToolAction(Messages.t("action.tool.not"), Messages.t("action.tool.not.description"), "not.png", null, this, new GateToolHelper(Gate.NOT));
	private final WindowAction CLOCK_TOOL_ACTION = new ToolAction(Messages.t("action.tool.clock"), Messages.t("action.tool.clock.description"), "clock.png", null, this, new GateToolHelper(Gate.CLOCK));
	private final WindowAction FLIP_FLOP_DATA_TOOL_ACTION = new ToolAction(Messages.t("action.tool.flip.flop.data"), Messages.t("action.tool.flip.flop.data.description"), "flipflopdata.png", null, this, new GateToolHelper(Gate.FLIP_FLOP_DATA));
	private final WindowAction TRISTATE_TOOL_ACTION = new ToolAction(Messages.t("action.tool.tristate"), Messages.t("action.tool.tristate.description"), "tristate.png", null, this, new GateToolHelper(Gate.TRISTATE));
	private final WindowAction SEVEN_SEGMENTS_DISPLAY_TOOL_ACTION = new ToolAction(Messages.t("action.tool.seven.segments.display"), Messages.t("action.tool.seven.segments.display.description"), "seven-segments-display.png", null, this, new GateToolHelper(Gate.SEVEN_SEGMENTS_DISPLAY));
	private final WindowAction SEVEN_SEGMENTS_DISPLAY_DOUBLE_TOOL_ACTION = new ToolAction(Messages.t("action.tool.seven.segments.display.double"), Messages.t("action.tool.seven.segments.display.double.description"), "seven-segments-display-double.png", null, this, new GateToolHelper(Gate.SEVEN_SEGMENTS_DISPLAY_DOUBLE));
	private final WindowAction LED_TOOL_ACTION = new ToolAction(Messages.t("action.tool.led"), Messages.t("action.tool.led.description"), "led.png", null, this, new GateToolHelper(Gate.LED));
	private final WindowAction TIME_DIAGRAM_TOOL_ACTION = new ToolAction(Messages.t("action.tool.time.diagram"), Messages.t("action.tool.time.diagram.description"), "time-diagram.png", null, this, new GateToolHelper(Gate.TIME_DIAGRAM));
	private final WindowAction SWITCH_TOOL_ACTION = new ToolAction(Messages.t("action.tool.switch"), Messages.t("action.tool.switch.description"), "switch.png", null, this, new GateToolHelper(Gate.SWITCH));

	private static JFileChooser OPEN_FILE_CHOOSER;
	private static JFileChooser SAVE_AS_FILE_CHOOSER;

	static {
		OPEN_FILE_CHOOSER = new JFileChooser();
		OPEN_FILE_CHOOSER.setDialogTitle(Messages.t("file.open"));
		FileNameExtensionFilter vsimFilter = new FileNameExtensionFilter(Messages.t("file.open.filter.description"), "json");
		OPEN_FILE_CHOOSER.addChoosableFileFilter(vsimFilter);
		OPEN_FILE_CHOOSER.setFileFilter(vsimFilter);

		SAVE_AS_FILE_CHOOSER = new JFileChooser();
		OPEN_FILE_CHOOSER.setDialogTitle(Messages.t("file.save"));
		OPEN_FILE_CHOOSER.addChoosableFileFilter(vsimFilter);
		OPEN_FILE_CHOOSER.setFileFilter(vsimFilter);
	}

	public SimWindow(Circuit circuit) {

		setTitle(circuit.getName() + " | " + Messages.t("main.title"));
		_circuit = circuit;
		_circuit.addCircuitEventListener(this);
		_canvas = new CircuitCanvas(_circuit);

		setBounds(100, 100, 700, 700);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		_displaysPane = new JTabbedPane();
		JSplitPane rightSplitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _canvas, _displaysPane);
		rightSplitpane.setOneTouchExpandable(true);
		rightSplitpane.setDividerLocation(500);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JSplitPane(JSplitPane.VERTICAL_SPLIT, getToolsPane(), new JPanel()), rightSplitpane);

		getContentPane().add(splitPane, BorderLayout.CENTER);

		_zoomSlider = createZoomSlider();
		JPanel statusBar = getStatusBar();
		statusBar.add(new JLabel("zoom"));
		statusBar.add(_zoomSlider);
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		setVisible(true);

		addComponentListener(this);
		_canvas.addMouseListener(this);
		_canvas.setZoom(1.0);
		_canvas.addMouseMotionListener(this);
		_canvas.resizeViewport();

		addMenu();
		addMainToolbar();
		updateActionStates();
	}

	private JPanel getStatusBar() {
		JPanel statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return statusBar;
	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = newMenu(Messages.t("menu.file"), KeyEvent.VK_F, menuBar);
		menu.add(newMenuItem(NEW_ACTION, Messages.c("menu.file.new.mnemonic")));
		menu.add(newMenuItem(OPEN_ACTION, Messages.c("menu.file.open.mnemonic")));
		menu.add(newMenuItem(SAVE_ACTION, Messages.c("menu.file.save.mnemonic")));
		menu.add(newMenuItem(SAVE_AS_ACTION, Messages.c("menu.file.saveas.mnemonic")));
		menu.addSeparator();
		menu.add(newMenuItem(QUIT_ACTION, Messages.c("menu.file.quit.mnemonic")));

		menu = newMenu(Messages.t("menu.edit"), KeyEvent.VK_F, menuBar);
		menu.add(newMenuItem(CUT_ACTION, Messages.c("menu.edit.cut.mnemonic")));
		menu.add(newMenuItem(COPY_ACTION, Messages.c("menu.edit.copy.mnemonic")));
		menu.add(newMenuItem(PASTE_ACTION, Messages.c("menu.edit.paste.mnemonic")));
		menu.add(newMenuItem(UNDO_ACTION, Messages.c("menu.edit.undo.mnemonic")));
		menu.add(newMenuItem(REDO_ACTION, Messages.c("menu.edit.redo.mnemonic")));
		menu.add(newMenuItem(PREFERENCES_ACTION, Messages.c("menu.edit.preferences.mnemonic")));

		menu = newMenu(Messages.t("menu.tools"), KeyEvent.VK_H, menuBar);
		menu.add(newMenuItem(CURSOR_TOOL_ACTION, Messages.c("menu.tool.cursor.mnemonic")));
		menu.add(newMenuItem(MOVE_VIEWPORT_TOOL_ACTION, Messages.c("menu.tool.move.viewport.mnemonic")));
		menu.add(newMenuItem(WIRES_TOOL_ACTION, Messages.c("menu.tool.wires.mnemonic")));
		menu.add(newMenuItem(AND2_TOOL_ACTION, Messages.c("menu.tool.and2.mnemonic")));
		menu.add(newMenuItem(AND3_TOOL_ACTION, Messages.c("menu.tool.and3.mnemonic")));
		menu.add(newMenuItem(AND4_TOOL_ACTION, Messages.c("menu.tool.and4.mnemonic")));
		menu.add(newMenuItem(OR2_TOOL_ACTION, Messages.c("menu.tool.or2.mnemonic")));
		menu.add(newMenuItem(OR3_TOOL_ACTION, Messages.c("menu.tool.or3.mnemonic")));
		menu.add(newMenuItem(OR4_TOOL_ACTION, Messages.c("menu.tool.or4.mnemonic")));
		menu.add(newMenuItem(CLOCK_TOOL_ACTION, Messages.c("menu.tool.clock.mnemonic")));
		menu.add(newMenuItem(NOT_TOOL_ACTION, Messages.c("menu.tool.not.mnemonic")));
		menu.add(newMenuItem(FLIP_FLOP_DATA_TOOL_ACTION, Messages.c("menu.tool.flip.flop.data.mnemonic")));
		menu.add(newMenuItem(TRISTATE_TOOL_ACTION, Messages.c("menu.tool.tristate.mnemonic")));
		menu.add(newMenuItem(SEVEN_SEGMENTS_DISPLAY_TOOL_ACTION, Messages.c("menu.tool.seven.segments.display.mnemonic")));
		menu.add(newMenuItem(SEVEN_SEGMENTS_DISPLAY_DOUBLE_TOOL_ACTION, Messages.c("menu.tool.seven.segments.display.double.mnemonic")));
		menu.add(newMenuItem(LED_TOOL_ACTION, Messages.c("menu.tool.led.mnemonic")));
		menu.add(newMenuItem(TIME_DIAGRAM_TOOL_ACTION, Messages.c("menu.tool.time.diagram.mnemonic")));
		menu.add(newMenuItem(SWITCH_TOOL_ACTION, Messages.c("menu.tool.switch.mnemonic")));

		menu = newMenu(Messages.t("menu.simulation"), KeyEvent.VK_F, menuBar);
		menu.add(newMenuItem(START_ACTION, Messages.c("menu.simulation.start.mnemonic")));
		menu.add(newMenuItem(PAUSE_ACTION, Messages.c("menu.simulation.pause.mnemonic")));

		menu = newMenu(Messages.t("menu.window"), KeyEvent.VK_F, menuBar);
		menu.add(newMenuItem(CLONE_WINDOW_ACTION, Messages.c("menu.window.clone.mnemonic")));
		menu.add(newMenuItem(CLOSE_ACTION, Messages.c("menu.window.close.mnemonic")));

		menu = newMenu(Messages.t("menu.help"), KeyEvent.VK_F, menuBar);
		menu.add(newMenuItem(ONLINE_HELP_ACTION, Messages.c("menu.help.online.mnemonic")));
		menu.add(newMenuItem(ABOUT_ACTION, Messages.c("menu.help.about.mnemonic")));


		setJMenuBar(menuBar);
	}

	private JMenuItem newMenuItem(WindowAction action, char mnemonic) {
		JMenuItem menuItem = new JMenuItem(action);
		menuItem.setMnemonic(mnemonic);
		return menuItem;
	}

	private JMenu newMenu(String title, int mnemonic, JMenuBar menuBar) {
		JMenu menu = new JMenu(title);
		menu.setMnemonic(mnemonic);
		menuBar.add(menu);
		return menu;
	}

	private void addMainToolbar() {
		JToolBar toolBar = new JToolBar();
		toolBar.add(newToolbarButton(NEW_ACTION));
		toolBar.add(newToolbarButton(OPEN_ACTION));
		toolBar.add(newToolbarButton(SAVE_ACTION));

		toolBar.addSeparator();

		toolBar.add(newToolbarButton(UNDO_ACTION));
		toolBar.add(newToolbarButton(REDO_ACTION));
		toolBar.add(newToolbarButton(CUT_ACTION));
		toolBar.add(newToolbarButton(COPY_ACTION));
		toolBar.add(newToolbarButton(PASTE_ACTION));

		toolBar.addSeparator();
		ButtonGroup group = new ButtonGroup();

		toolBar.add(newGrouppedButton(START_ACTION, group));
		toolBar.add(newGrouppedButton(PAUSE_ACTION, group));

		getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	private JPanel getToolsPane() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));

		ButtonGroup group = new ButtonGroup();
		JToggleButton button = newGrouppedButton(CURSOR_TOOL_ACTION, group);
		button.setSelected(true);
		panel.add(button);
		panel.add(newGrouppedButton(MOVE_VIEWPORT_TOOL_ACTION, group));
		panel.add(newGrouppedButton(WIRES_TOOL_ACTION, group));

		panel.add(newGrouppedButton(AND2_TOOL_ACTION, group));
		panel.add(newGrouppedButton(AND3_TOOL_ACTION, group));
		panel.add(newGrouppedButton(AND4_TOOL_ACTION, group));

		panel.add(newGrouppedButton(OR2_TOOL_ACTION, group));
		panel.add(newGrouppedButton(OR3_TOOL_ACTION, group));
		panel.add(newGrouppedButton(OR4_TOOL_ACTION, group));

		panel.add(newGrouppedButton(NOT_TOOL_ACTION, group));
		panel.add(newGrouppedButton(CLOCK_TOOL_ACTION, group));
		panel.add(newGrouppedButton(FLIP_FLOP_DATA_TOOL_ACTION, group));
		panel.add(newGrouppedButton(SEVEN_SEGMENTS_DISPLAY_TOOL_ACTION, group));
		panel.add(newGrouppedButton(SEVEN_SEGMENTS_DISPLAY_DOUBLE_TOOL_ACTION, group));
		panel.add(newGrouppedButton(TRISTATE_TOOL_ACTION, group));
		panel.add(newGrouppedButton(LED_TOOL_ACTION, group));
		panel.add(newGrouppedButton(TIME_DIAGRAM_TOOL_ACTION, group));
		panel.add(newGrouppedButton(SWITCH_TOOL_ACTION, group));
		return panel;
	}

	private JButton newToolbarButton(Action action) {
		JButton button = new JButton();
		button.setAction(action);
		button.setText("");
		return button;
	}

	private JToggleButton newGrouppedButton(Action action, ButtonGroup group) {
		JToggleButton button = new JToggleButton();
		button.setAction(action);
		button.setText("");
		group.add(button);
		return button;
	}

	public void undo() {
		_circuit.undo();
	}

	public void redo() {
		_circuit.redo();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		_canvas.resizeViewport();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		_circuit.remove(_canvas);
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getClickCount() >= 2) {
			_toolHelper.mouseDoubleClicked(this, event);
			return;
		}
		_toolHelper.mouseClicked(this, event);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		_toolHelper.mouseDown(this, event);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		_toolHelper.mouseUp(this, event);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		_toolHelper.mouseDragged(this, event);
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		_toolHelper.mouseMoved(this, event);
	}

	public void setToolHelper(ActionToolHelper toolHelper) {
		_toolHelper = toolHelper;
	}

	public void undoableConnect(int xi, int yi, int xf, int yf) {
		_circuit.undoableConnect(xi, yi, xf, yf);
	}

	public Circuit getCircuit() {
		return _circuit;
	}

	public void undoableDisconnect(int x, int y) {
		_circuit.undoableDisconnect(x, y);
	}

	private void updateActionStates() {
		updateStartPause(_circuit);
		updateUndoRedo(_circuit);
		updateFileOperationActions();
		updateCutCopyPaste();
	}

	private void updateCutCopyPaste() {
		CUT_ACTION.setEnabled(false);
		COPY_ACTION.setEnabled(false);
		PASTE_ACTION.setEnabled(false);
	}

	private void updateFileOperationActions() {
		SAVE_ACTION.setEnabled(_circuit.isModified());
	}

	@Override
	public void onPause(CircuitEvent event) {
		updateStartPause(event.getCircuit());
	}

	@Override
	public void onResume(CircuitEvent event) {
		updateStartPause(event.getCircuit());
	}

	@Override
	public void onChanged(CircuitEvent event) {
		SAVE_ACTION.setEnabled(_circuit.isModified());
		updateUndoRedo(event.getCircuit());
		updateTitle();
	}

	public void updateTitle() {
		setTitle(_circuit.getName() + " " + (_circuit.isModified() ? "* " : "") + "| " + Messages.t("main.title"));
	}

	@Override
	public void onUndo(CircuitEvent event) {
		updateUndoRedo(event.getCircuit());
	}

	@Override
	public void onRedo(CircuitEvent event) {
		updateUndoRedo(event.getCircuit());
	}

	@Override
	public void onSaved(CircuitEvent event) {
		updateTitle();
		SAVE_ACTION.setEnabled(_circuit.isModified());
	}

	private void updateUndoRedo(Circuit circuit) {
		UNDO_ACTION.setEnabled(circuit.canUndo());
		REDO_ACTION.setEnabled(circuit.canRedo());
	}

	private void updateStartPause(Circuit circuit) {
		PAUSE_ACTION.setEnabled(circuit.isSimulationRunning());
		START_ACTION.setEnabled(!circuit.isSimulationRunning());
	}

	public void load() {
		if (_circuit != null && _circuit.isModified()) {
			int replace = askSaveNow();
			if (replace == JOptionPane.CANCEL_OPTION) {
				return;
			}
			if (replace == JOptionPane.YES_OPTION) {
				if (saveAs() == JOptionPane.CANCEL_OPTION) {
					LOGGER.info("save cancelled by user.");
				}
			}
		}
		try {
			if (OPEN_FILE_CHOOSER.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = OPEN_FILE_CHOOSER.getSelectedFile();
				setCircuit(Circuit.fromJSon(FileUtils.readFileToString(file), file.getPath()));
				LOGGER.info("Loaded: " + file.getName() + ".");
				return;
			}
			LOGGER.info("load cancelled by user.");
		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}

	public int saveAs() {
		SAVE_AS_FILE_CHOOSER.setSelectedFile(new File(_circuit.getName()));
		if (SAVE_AS_FILE_CHOOSER.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			LOGGER.info("save cancelled by user.");
			return 0;
		}

		File file = SAVE_AS_FILE_CHOOSER.getSelectedFile();
		if (file.exists()) {
			if (askOverwrite() != JOptionPane.YES_OPTION) {
				return JOptionPane.CANCEL_OPTION;
			}
		}

		_circuit.setName(file.getPath());
		updateTitle();
		save(file.getPath());
		LOGGER.info("saved: " + file.getPath());
		return JOptionPane.OK_OPTION;
	}

	public void save(String path) {
		try {
			FileUtils.writeStringToFile(new File(path), _circuit.toJSon());
			_circuit.setSaved();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int askOverwrite() {
		return JOptionPane.showConfirmDialog(this, Messages.t("file.replace.message"), Messages.t("file.replace.title"),
				JOptionPane.YES_NO_OPTION);
	}

	private int askSaveNow() {
		return JOptionPane.showConfirmDialog(this, Messages.t("file.save.now.message"), Messages.t("file.save.now.title"), JOptionPane.YES_NO_CANCEL_OPTION);
	}

	public void setCircuit(Circuit circuit) {
		if (circuit == null) {
			return;
		}
		_circuit.remove(_canvas);
		_circuit.removeCircuitEventListener(this);

		_circuit = circuit;
		_circuit.addCircuitEventListener(this);
		_canvas.setCircuit(_circuit);

		_circuit.setSaved();
		updateTitle();
	}

	public void addDisplayPanel(String idInstance, JPanel displayPanel) {
		_displaysPane.add(idInstance, displayPanel);
	}

	public CircuitCanvas getCanvas() {
		return _canvas;
	}

	private JSlider createZoomSlider() {
		JSlider zoomSlider = new JSlider(1, 8 ,4);
		zoomSlider.setSnapToTicks(true);
		zoomSlider.setMinorTickSpacing(1);
		//zoomSlider.setPaintTicks(true);
		zoomSlider.addChangeListener(this);
		return zoomSlider;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();
		//if (!slider.getValueIsAdjusting()) {
			_canvas.setZoom(0.25 * slider.getValue());
		//D}
	}
}
