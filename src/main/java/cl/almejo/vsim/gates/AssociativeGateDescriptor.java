/**
 * vsim
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 */

package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;

import java.awt.*;

public class AssociativeGateDescriptor extends GateDescriptor {
	private final int[][] _behavior;
	private int _associativeType;

	public static final int ASSOCIATIVE_TYPE_AND = 0;
	public static final int ASSOCIATIVE_TYPE_OR = 1;

	public AssociativeGateDescriptor(AssociateveGateParameters parameters) {
		_parameters = parameters;
		_behavior = parameters.getBehavior();
		_pinPosition = new Point[parameters.getPinCount() + 1];
		_associativeType = parameters.getAssociativeType();

		int dy = 32 / (parameters.getPinCount() - 1);
		for (int i = 0; i < parameters.getPinCount(); i++) {
			_pinPosition[i] = new Point(Circuit.gridTrunc(0), Circuit.gridTrunc(i * dy));
		}

		_pinPosition[parameters.getPinCount()] = new Point(32, 16);
		_type = GateTypes.NORMAL;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(Color.blue);
		switch (_associativeType) {
			case ASSOCIATIVE_TYPE_AND:
				graphics.fillArc(-32, 0, 64, 32, -90, 180);
				break;
			case ASSOCIATIVE_TYPE_OR:
				int []pointsX = new int[] {0, 16, 24, 32, 24,  16, 0, 4, 8, 4};
				int []pointsY = new int[] {0 , 0, 8, 16, 24, 32, 32, 24, 16, 8};
				graphics.fillPolygon(pointsX, pointsY, 10);
				break;
		}
	}

	@Override
	public Dimension getSize() {
		return new Dimension(32, 16);
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		return null;
	}

	public int computeAssocVal(int value1, byte value2) {
		return _behavior[value1 + 1][value2 + 1];
	}

	@Override
	public int getPinCount() {
		return ((AssociateveGateParameters) _parameters).getPinCount() + 1;
	}

}
