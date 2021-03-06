package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;
import cl.almejo.vsim.gui.ColorScheme;

import java.awt.*;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */

class ClockDescriptor extends GateDescriptor {

	ClockDescriptor(ClockParameters parameters, String type) {
		super(parameters, type);
		pinPosition = new Point[1];
		pinPosition[0] = new Point(Circuit.gridTrunc(16), Circuit.gridTrunc(16));
		gateType = GateTypes.NORMAL;
		pinCount = 1;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(ColorScheme.getGates());
		graphics.fillRoundRect(x, y, 16, 16, 3, 3);
		graphics.fillOval(x + 12, y + 12, 9, 9);
		graphics.setColor(ColorScheme.getColor(iconGate.getPin(0)));
		graphics.fillOval(x + 4, y + 4, 8, 8);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(16, 16);
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		try {
			return new Clock(circuit, params.clone(), this);
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
