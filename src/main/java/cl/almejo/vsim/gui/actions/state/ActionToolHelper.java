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
package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gui.Configurable;
import cl.almejo.vsim.gui.SimWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public abstract class ActionToolHelper {

	public final static ActionToolHelper CURSOR = new CursorToolHelper();
	public final static ActionToolHelper WIRES = new WiresToolHelper();
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

	protected void doPopupMenu(Circuit circuit, Configurable configurable, Component component, int x, int y) {
		JPopupMenu menu = new JPopupMenu();
		addRotateOptions(circuit, configurable, menu);
		menu.show(component, x, y);
	}

	private void addRotateOptions(final Circuit circuit, final Configurable configurable, JPopupMenu menu) {
		JMenu submenu = new JMenu(Messages.t("action.rotate"));
		JMenuItem menuItem = new JMenuItem(Messages.t("action.rotate.clockwise"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				circuit.undoableRotateClockWise(configurable);
			}
		});
		submenu.add(menuItem);
		menuItem = new JMenuItem(Messages.t("action.rotate.counter.clockwise"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				circuit.undoableRotateCounterClockWise(configurable);
			}
		});
		submenu.add(menuItem);
		menu.add(submenu);
	}
}
