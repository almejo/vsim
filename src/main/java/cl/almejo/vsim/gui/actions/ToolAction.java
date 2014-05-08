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

package cl.almejo.vsim.gui.actions;

import cl.almejo.vsim.gui.SimWindow;
import cl.almejo.vsim.gui.actions.state.ActionToolHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;

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
