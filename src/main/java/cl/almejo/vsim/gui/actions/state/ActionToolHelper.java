package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Configurable;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.gui.Draggable;
import cl.almejo.vsim.gui.ImageUtils;
import cl.almejo.vsim.gui.SimWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * vsim
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public abstract class ActionToolHelper {

	public static final ActionToolHelper CURSOR = new CursorToolHelper();
	public static final ActionToolHelper WIRES = new WiresToolHelper();
	public static final ActionToolHelper MOVE_VIEWPORT = new MoveViewPortToolHelper();

	public void mouseDragged(SimWindow window, MouseEvent event) {
	}

	public Object mouseClicked(SimWindow window, MouseEvent event) {
		return null;
	}

	public void mouseDoubleClicked(SimWindow window, MouseEvent event) {
	}

	@SuppressWarnings("UnusedParameters")
	public void mouseMoved(SimWindow window, MouseEvent event) {
	}

	public void mouseDown(SimWindow window, MouseEvent event) {
	}

	public void mouseUp(SimWindow window, MouseEvent event) {
	}

	private void doPopupMenu(Circuit circuit, Configurable configurable, Component component, int x, int y) {
		JPopupMenu menu = new JPopupMenu();
		addDeleteOption(circuit, configurable, menu);
		addRotateOptions(circuit, configurable, menu);
		if (configurable.isConfigurable()) {
			menu.addSeparator();
			addConfigOption(circuit, configurable, menu);
		}
		menu.show(component, x, y);
	}

	private void addDeleteOption(Circuit circuit, Configurable configurable, JPopupMenu menu) {
		JMenuItem menuItem = new JMenuItem(Messages.t("action.delete"));
		menuItem.addActionListener(event -> {
			circuit.undoableRemoveGate((IconGate) configurable);
			circuit.clearSelection();
		});
		menu.add(menuItem);
	}

	private void addConfigOption(Circuit circuit, Configurable configurable, JPopupMenu menu) {
		JMenuItem menuItem = new JMenuItem(Messages.t("action.config"));
		menuItem.addActionListener(event -> new ConfigurationDialog(circuit, configurable).setVisible(true));
		menu.add(menuItem);
	}

	private void addRotateOptions(Circuit circuit, Configurable configurable, JPopupMenu menu) {
		JMenu submenu = new JMenu(Messages.t("action.rotate"));
		JMenuItem menuItem = new JMenuItem(Messages.t("action.rotate.clockwise"), ImageUtils.loadIcon("rotate-right.png"));
		menuItem.addActionListener(event -> circuit.undoableRotateClockWise(configurable));
		submenu.add(menuItem);
		menuItem = new JMenuItem(Messages.t("action.rotate.counter.clockwise"), ImageUtils.loadIcon("rotate-left.png"));
		menuItem.addActionListener(event -> circuit.undoableRotateCounterClockWise(configurable));
		submenu.add(menuItem);
		menu.add(submenu);
	}

	Draggable checkSelection(SimWindow window, MouseEvent event, int x, int y) {
		Circuit circuit = window.getCircuit();
		Draggable draggable = circuit.findDraggable(x, y);
		if (draggable != null) {
			if (event.isShiftDown()) {
				circuit.select(draggable);
			} else if (event.isControlDown()) {
				circuit.deselect(draggable);
			} else {
				circuit.clearSelection();
				circuit.select(draggable);
			}
		} else {
			circuit.clearSelection();
		}
		return draggable;
	}

	public void rightClicked(SimWindow window, MouseEvent event) {
		int x = window.getCanvas().toCircuitCoordinatesX(event.getX());
		int y = window.getCanvas().toCircuitCoordinatesY(event.getY());

		checkSelection(window, event, x, y);

		Configurable configurable = window.getCircuit().findConfigurable(x, y);
		if (configurable != null) {
			doPopupMenu(window.getCircuit(), configurable, window.getCanvas(), event.getX(), event.getY());
		}
	}
}
