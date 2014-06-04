/**
 * vsim
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: alejo
 */
package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;
import cl.almejo.vsim.gui.ColorScheme;

import java.awt.*;

public class FlipFlopDataDescriptor extends GateDescriptor {
	public FlipFlopDataDescriptor(FlipFlopDataParameters parameters, String type) {
		super(parameters, type);
		_pinPosition = new Point[3];
		_pinPosition[0] = new Point(0, 16);
		_pinPosition[1] = new Point(32, 16);
		_pinPosition[2] = new Point(16, 32);
		_gateType = GateTypes.NORMAL;
		_pinCount = 3;
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
