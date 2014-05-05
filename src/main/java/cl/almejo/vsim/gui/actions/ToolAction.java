package cl.almejo.vsim.gui.actions;

import cl.almejo.vsim.gui.SimWindow;
import cl.almejo.vsim.gui.actions.state.ActionToolHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alejo
 * Date: 9/18/13
 * Time: 1:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class ToolAction extends WindowAction {
	private ActionToolHelper _toolHelper;

	public ToolAction(String text, String description, String icon, KeyStroke keyStroke, SimWindow window, ActionToolHelper toolHelper) {
		super(text, description, icon, keyStroke, window);
		_toolHelper = toolHelper;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		getWindow().setToolHelper(_toolHelper);
	}
}
