package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.gates.DisplayInfoGate;
import cl.almejo.vsim.gates.GateFactory;
import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class GateToolHelper extends ActionToolHelper {
	private final String gateType;

	public GateToolHelper(String gateType) {
		this.gateType = gateType;
	}

	@Override
	public IconGate mouseClicked(SimWindow window, MouseEvent event) {
		super.mouseClicked(window, event);

		int x = window.getCanvas().toCircuitCoordinatesX(event.getX());
		int y = window.getCanvas().toCircuitCoordinatesY(event.getY());

		if (event.isControlDown()) {
			window.getCircuit().undoableRemoveGate(window.getCircuit().findIcon(x, y));
			checkSelection(window, event, x, y);
			return null;
		}
		IconGate iconGate = GateFactory.getInstance(gateType, window.getCircuit());
		if (iconGate == null) {
			return null;
		}
		window.getCircuit().undoableAddGate(iconGate, x, y);
		checkSelection(window, event, x, y);
		if (iconGate.getGate() instanceof DisplayInfoGate) {
			window.addDisplayPanel("#" + iconGate.getId(), ((DisplayInfoGate) iconGate.getGate()).getDisplay());
			((DisplayInfoGate) iconGate.getGate()).startDisplay();
		}

		return iconGate;
	}


}
