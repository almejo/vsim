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
public class FlipFlopDataDescriptor extends GateDescriptor {
	FlipFlopDataDescriptor(FlipFlopDataParameters parameters, String type) {
		super(parameters, type);
		pinPosition = new Point[3];
		pinPosition[0] = new Point(0, 16);
		pinPosition[1] = new Point(32, 16);
		pinPosition[2] = new Point(16, 32);
		gateType = GateTypes.NORMAL;
		pinCount = 3;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(ColorScheme.getGates());
		Dimension dimension = getSize();
		graphics.fillRect(0, 0, dimension.width, dimension.height);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(32, 32);
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		return null;
	}
}
