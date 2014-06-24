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

import cl.almejo.vsim.gui.ImageUtils;
import cl.almejo.vsim.gui.SimWindow;

import javax.swing.*;


public abstract class WindowAction extends AbstractAction {
	SimWindow _window;

	public WindowAction() {
	}

	public WindowAction(String text, String description, String icon, KeyStroke keyStroke, SimWindow window) {
		super(text, ImageUtils.loadIcon(icon));
		_window = window;
		putValue(AbstractAction.SHORT_DESCRIPTION, description);
		putValue(AbstractAction.ACCELERATOR_KEY, keyStroke);
	}


	public SimWindow getWindow() {
		return _window;
	}

}
