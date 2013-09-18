package cl.almejo.vsim.gui.actions;
/**
 * Created with IntelliJ IDEA.
 * User: alejo
 * Date: 9/16/13
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */

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
