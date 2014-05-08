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

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewAction extends WindowAction {

	public NewAction(String text, String description, String icon, KeyStroke keyStroke, SimWindow window) {
		super(text, description, icon, keyStroke, window);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("new");
	}

}
