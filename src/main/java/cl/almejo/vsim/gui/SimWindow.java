/**
 *
 * vsim
 *
 * Created on Aug 16, 2013
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
import cl.almejo.vsim.circuit.CircuitStateListner;
import cl.almejo.vsim.gates.Gate;
import cl.almejo.vsim.gui.actions.*;
import cl.almejo.vsim.gui.actions.state.ActionToolHelper;
import cl.almejo.vsim.gui.actions.state.AddGateToolHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimWindow extends JFrame implements ComponentListener, WindowListener, KeyListener, MouseListener, MouseMotionListener, CircuitStateListner {

	private static final long serialVersionUID = 1L;
	private final CircuitCanvas _canvas;
	private final Circuit _circuit;

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


	private final WindowAction NEW_ACTION = new NewAction(Messages.t("action.new"), Messages.t("action.new.description"), "new.png", ACCELERATOR_NEW, this);
	private final WindowAction OPEN_ACTION = new OpenAction(Messages.t("action.open"), Messages.t("action.open.description"), "open.png", ACCELERATOR_OPEN, this);
	private final WindowAction SAVE_ACTION = new SaveAction(Messages.t("action.save"), Messages.t("action.save.description"), "save.png", ACCELERATOR_SAVE, this);
	private final WindowAction SAVE_AS_ACTION = new SaveAsAction(Messages.t("action.saveas"), Messages.t("action.saveas.description"), null, ACCELERATOR_SAVE_AS, this);
	private final WindowAction QUIT_ACTION = new RedoAction(Messages.t("action.quit"), Messages.t("action.quit.description"), null, ACCELERATOR_QUIT, this);

	private final WindowAction CUT_ACTION = new CutAction(Messages.t("action.cut"), Messages.t("action.cut.description"), "cut.png", ACCELERATOR_CUT, this);
	private final WindowAction COPY_ACTION = new CopyAction(Messages.t("action.copy"), Messages.t("action.copy.description"), "copy.png", ACCELERATOR_COPY, this);
	private final WindowAction PASTE_ACTION = new PasteAction(Messages.t("action.paste"), Messages.t("action.paste.description"), "paste.png", ACCELERATOR_PASTE, this);
	private final WindowAction UNDO_ACTION = new UndoAction(Messages.t("action.undo"), Messages.t("action.undo.description"), "undo.png", ACCELERATOR_UNDO, this);
	private final WindowAction REDO_ACTION = new RedoAction(Messages.t("action.redo"), Messages.t("action.redo.description"), "redo.png", ACCELERATOR_REDO, this);

	private final WindowAction START_ACTION = new StartStopSimulationAction(Messages.t("action.start"), Messages.t("action.start.description"), "play.png", null, this);
	private final WindowAction PAUSE_ACTION = new StartStopSimulationAction(Messages.t("action.pause"), Messages.t("action.pause.description"), "pause.png", null, this);

	private final WindowAction CLONE_WINDOW_ACTION = new CloneWindowAction(Messages.t("action.window.clone"), Messages.t("action.window.clone.description"), null, null, this);
	private final WindowAction CLOSE_ACTION = new CloseWindowAction(Messages.t("action.window.close"), Messages.t("action.window.close.description"), null, null, this);


	private final WindowAction ONLINE_HELP_ACTION = new OnlineHelpAction(Messages.t("action.help.online"), Messages.t("action.help.online.description"), null, null, this);
	private final WindowAction ABOUT_ACTION = new AboutAction(Messages.t("action.help.about"), Messages.t("action.help.about.description"), null, null, this);

	private final WindowAction CURSOR_TOOL_ACTION = new ToolAction(Messages.t("action.tool.cursor"), Messages.t("action.tool.cursor.description"), "cursor.png", null, this, ActionToolHelper.CURSOR);
	private final WindowAction MOVE_VIEWPORT_TOOL_ACTION = new ToolAction(Messages.t("action.tool.move.viewport"), Messages.t("action.tool.move.viewport.description"), "move-viewport.png", null, this, ActionToolHelper.CURSOR);
	private final WindowAction WIRES_TOOL_ACTION = new ToolAction(Messages.t("action.tool.wires"), Messages.t("action.tool.wires.description"), "wires.png", null, this, ActionToolHelper.WIRES);
	private final WindowAction AND2_TOOL_ACTION = new ToolAction(Messages.t("action.tool.and2"), Messages.t("action.tool.and2.description"), "and2.png", null, this, new AddGateToolHelper(Gate.AND2));
	private final WindowAction AND3_TOOL_ACTION = new ToolAction(Messages.t("action.tool.and3"), Messages.t("action.tool.and3.description"), "and3.png", null, this, new AddGateToolHelper(Gate.AND3));
	private final WindowAction NOT_TOOL_ACTION = new ToolAction(Messages.t("action.tool.not"), Messages.t("action.tool.not.description"), "not.png", null, this, new AddGateToolHelper(Gate.NOT));
	private final WindowAction CLOCK_TOOL_ACTION = new ToolAction(Messages.t("action.tool.clock"), Messages.t("action.tool.clock.description"), "clock.png", null, this, new AddGateToolHelper(Gate.CLOCK));


	public SimWindow(Circuit circuit) {

		setTitle(Messages.t("main.title"));
		_circuit = circuit;
		_circuit.addCircuitEventListener(this);
		_canvas = new CircuitCanvas(_circuit);

		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JSplitPane(JSplitPane.VERTICAL_SPLIT, getToolsPane(), new JPanel()), _canvas);

		getContentPane().add(splitPane, BorderLayout.CENTER);
		setVisible(true);

		addComponentListener(this);
		_canvas.addMouseListener(this);
		_canvas.addMouseMotionListener(this);
		_canvas.resizeViewport();

		JTextField text = new JTextField();
		getContentPane().add(text, BorderLayout.SOUTH);
		text.addKeyListener(this);

		addMenu();
		addMainToolbar();
		updateActionStates();
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

		menu = newMenu(Messages.t("menu.tools"), KeyEvent.VK_H, menuBar);
		menu.add(newMenuItem(CURSOR_TOOL_ACTION, Messages.c("menu.tool.cursor.mnemonic")));
		menu.add(newMenuItem(MOVE_VIEWPORT_TOOL_ACTION, Messages.c("menu.tool.move.viewport.mnemonic")));
		menu.add(newMenuItem(WIRES_TOOL_ACTION, Messages.c("menu.tool.wires.mnemonic")));
		menu.add(newMenuItem(AND2_TOOL_ACTION, Messages.c("menu.tool.and2.mnemonic")));
		menu.add(newMenuItem(AND3_TOOL_ACTION, Messages.c("menu.tool.and3.mnemonic")));
		menu.add(newMenuItem(CLOCK_TOOL_ACTION, Messages.c("menu.tool.clock.mnemonic")));
		menu.add(newMenuItem(NOT_TOOL_ACTION, Messages.c("menu.tool.not.mnemonic")));

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
		panel.add(newGrouppedButton(NOT_TOOL_ACTION, group));
		panel.add(newGrouppedButton(CLOCK_TOOL_ACTION, group));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		_circuit.remove(_canvas);
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
//		System.out.println(e.getKeyCode());
//		if (e.getKeyCode() == 10) {
//			JTextField field = (JTextField) e.getSource();
//			String[] points = field.getText().split(",");
//			if (points.length < 4) {
//				return;
//			}
//			System.out.println(Circuit.gridTrunc(Integer.parseInt(points[0].trim())) + ", "
//					+ Circuit.gridTrunc(Integer.parseInt(points[1].trim())) + ", "
//					+ Circuit.gridTrunc(Integer.parseInt(points[2].trim())) + ", "
//					+ Circuit.gridTrunc(Integer.parseInt(points[3].trim())));
//			_circuit.undoableConnect(Integer.parseInt(points[0].trim()), Integer.parseInt(points[1].trim()), Integer.parseInt(points[2].trim()), Integer.parseInt(points[3].trim()));
//			field.setText("");
//		}
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
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

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
		NEW_ACTION.setEnabled(false);
		OPEN_ACTION.setEnabled(false);
		SAVE_ACTION.setEnabled(false);
		SAVE_AS_ACTION.setEnabled(false);
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
		updateUndoRedo(event.getCircuit());
	}

	@Override
	public void onUndo(CircuitEvent event) {
		updateUndoRedo(event.getCircuit());
	}

	@Override
	public void onRedo(CircuitEvent event) {
		updateUndoRedo(event.getCircuit());
	}

	private void updateUndoRedo(Circuit circuit) {
		UNDO_ACTION.setEnabled(circuit.canUndo());
		REDO_ACTION.setEnabled(circuit.canRedo());
	}

	private void updateStartPause(Circuit circuit) {
		PAUSE_ACTION.setEnabled(circuit.isSimulationRunning());
		START_ACTION.setEnabled(!circuit.isSimulationRunning());
	}

}
