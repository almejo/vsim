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
class TristateDescriptor extends GateDescriptor {
	TristateDescriptor(TristateParameters parameters, String type) {
		super(parameters, type);
		_pinPosition = new Point[3];
		_pinPosition[0] = new Point(Circuit.gridTrunc(0), Circuit.gridTrunc(16));
		_pinPosition[1] = new Point(Circuit.gridTrunc(32), Circuit.gridTrunc(16));
		_pinPosition[2] = new Point(Circuit.gridTrunc(16), Circuit.gridTrunc(32));
		_gateType = GateTypes.NORMAL;
		_pinCount = 3;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(ColorScheme.getGates());
		int[] pointsX = new int[]{0, 32, 0};
		int[] pointsY = new int[]{0, 16, 32};
		graphics.fillPolygon(pointsX, pointsY, 3);
		graphics.drawLine(Circuit.gridTrunc(16), Circuit.gridTrunc(16), Circuit.gridTrunc(16), Circuit.gridTrunc(32));
		graphics.fillRect(Circuit.gridTrunc(16) - Circuit.GRID_SIZE / 4, Circuit.gridTrunc(16), Circuit.GRID_SIZE / 2, 16);
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
