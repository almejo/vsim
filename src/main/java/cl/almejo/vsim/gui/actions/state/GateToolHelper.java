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

import cl.almejo.vsim.gates.GateFactory;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;

public class GateToolHelper extends ActionToolHelper {
	private final String _gateType;

	public GateToolHelper(String gateType) {
		_gateType = gateType;
	}

	@Override
	public void mouseClicked(SimWindow window, MouseEvent event) {
		super.mouseClicked(window, event);
		if (event.isControlDown()) {
			window.getCircuit().undoableRemoveGate(event.getX(), event.getY());
			return;
		}
		window.getCircuit().undoableAddGate(GateFactory.getInstance(_gateType, window.getCircuit()), event.getX(), event.getY());
	}


}
