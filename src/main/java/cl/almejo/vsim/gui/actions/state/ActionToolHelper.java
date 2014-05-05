package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alejo
 * Date: 9/18/13
 * Time: 12:47 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ActionToolHelper {

	public final static ActionToolHelper CURSOR = new CursorToolHelper();
	public final static ActionToolHelper WIRES = new WiresToolHelper();

	public void mouseDragged(SimWindow window, MouseEvent event) {
	}

	public void mouseClicked(SimWindow window, MouseEvent event) {
	}

	public void mouseDoubleClicked(SimWindow window, MouseEvent event) {
	}

	public void mouseMoved(SimWindow window, MouseEvent event) {
	}

	public void mouseDown(SimWindow window, MouseEvent event) {
	}

	public void mouseUp(SimWindow window, MouseEvent event) {
	}

	public void drawPreview(Graphics2D graphics2D, Circuit circuit) {
	}
}
