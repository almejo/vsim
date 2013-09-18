package cl.almejo.vsim.gui.actions;

/**
 * Created with IntelliJ IDEA.
 * User: alejo
 * Date: 9/16/13
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */

import cl.almejo.vsim.gui.SimWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class And2ToolAction extends WindowAction {

	public And2ToolAction(String text, String description, String icon, KeyStroke keyStroke, SimWindow window) {
		super(text, description, icon, keyStroke, window);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("and2 selected");
	}

}
