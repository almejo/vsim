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

import cl.almejo.vsim.gates.IconGate;
import cl.almejo.vsim.gates.TimeDiagram;
import cl.almejo.vsim.gui.SimWindow;

import java.awt.event.MouseEvent;

public class TimeDiagramToolHelper extends GateToolHelper {

	public TimeDiagramToolHelper(String gateType) {
		super(gateType);
	}

	@Override
	public IconGate mouseClicked(SimWindow window, MouseEvent event) {
		IconGate iconGate = super.mouseClicked(window, event);
		if (iconGate != null) {
			((TimeDiagram) iconGate.getGate()).setCanvas(window.getTimeDiagramCanvas());
		}
		return iconGate;
	}


}
