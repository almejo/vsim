package cl.almejo.vsim.gui.actions;

import cl.almejo.vsim.gui.SimWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import cl.almejo.vsim.Messages;
/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class AboutAction extends WindowAction {

	public AboutAction(String text, String description, String icon, KeyStroke keyStroke, SimWindow window) {
		super(text, description, icon, keyStroke, window);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JOptionPane.showMessageDialog(this._window
				, Messages.t("about.message")
				, Messages.t("about.title")
				, JOptionPane.INFORMATION_MESSAGE);
	}

}
