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

import cl.almejo.vsim.gates.DisplayInfoGate;
import cl.almejo.vsim.gates.GateFactory;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;

public class GateToolHelper extends ActionToolHelper {
	private final String _gateType;

	public GateToolHelper(String gateType) {
		_gateType = gateType;
	}

	@Override
	public IconGate mouseClicked(SimWindow window, MouseEvent event) {
		super.mouseClicked(window, event);
		if (event.isControlDown()) {
			window.getCircuit().undoableRemoveGate(event.getX(), event.getY());
			return null;
		}
		IconGate iconGate = GateFactory.getInstance(_gateType, window.getCircuit());
		window.getCircuit().undoableAddGate(iconGate, event.getX(), event.getY());

		if (iconGate.getGate() instanceof DisplayInfoGate) {
			window.addDisplayPanel("#" + iconGate.getId(), ((DisplayInfoGate) iconGate.getGate()).getDisplay());
			((DisplayInfoGate) iconGate.getGate()).startDisplay();
		}

		return iconGate;
	}


}
