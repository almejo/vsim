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
package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;

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

}
