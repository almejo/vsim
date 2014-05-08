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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;


public abstract class WindowAction extends AbstractAction {
	SimWindow _window;

	public WindowAction() {
	}

	public WindowAction(String text, String description, String icon, KeyStroke keyStroke, SimWindow window) {
		super(text, loadIcon(icon));
		_window = window;
		putValue(AbstractAction.SHORT_DESCRIPTION, description);
		putValue(AbstractAction.ACCELERATOR_KEY, keyStroke);
	}


	protected static ImageIcon loadIcon(String icon) {
		if (icon == null) {
			return null;
		}
		try {
			return new ImageIcon(ImageIO.read(ClassLoader.getSystemResourceAsStream("icons/" + icon)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SimWindow getWindow() {
		return _window;
	}

}
