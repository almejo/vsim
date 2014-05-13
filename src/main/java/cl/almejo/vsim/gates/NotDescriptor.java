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


package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;

import java.awt.*;

public class NotDescriptor extends GateDescriptor {
	public NotDescriptor() {
		_pinPosition = new Point[3];
		_pinPosition[0] = new Point(Circuit.gridTrunc(0), Circuit.gridTrunc(16));
		_pinPosition[1] = new Point(Circuit.gridTrunc(32), Circuit.gridTrunc(16));
		_type = GateTypes.NORMAL;
		_pinCount = 2;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(Color.blue);
		int[] pointsX = new int[]{0, 32, 0};
		int[] pointsY = new int[]{0, 16, 32};
		graphics.fillPolygon(pointsX, pointsY, 3);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(32, 16);
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		try {
			return new Not(circuit, params.clone(), this);
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
