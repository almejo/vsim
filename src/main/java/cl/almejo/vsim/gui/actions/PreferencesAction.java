package cl.almejo.vsim.gui.actions;

import cl.almejo.vsim.gui.SimWindow;
import cl.almejo.vsim.gui.actions.preferences.PreferencesDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class PreferencesAction extends WindowAction {

	private JDialog _preferencesDialog;

	public PreferencesAction(String text, String description, String icon, KeyStroke keyStroke, SimWindow window) {
		super(text, description, icon, keyStroke, window);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (_preferencesDialog == null) {
			_preferencesDialog = new PreferencesDialog(_window);
		}
		_preferencesDialog.setVisible(true);
	}
}
