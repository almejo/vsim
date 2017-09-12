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
class AssociativeGateDescriptor extends GateDescriptor {
	private final int[][] _behavior;
	private final int _associativeType;

	static final int ASSOCIATIVE_TYPE_AND = 0;
	static final int ASSOCIATIVE_TYPE_OR = 1;
	static final int ASSOCIATIVE_TYPE_XOR = 2;

	private final int[] OR_POINTS_X = new int[]{0, 16, 24, 32, 24, 16, 0, 4, 8, 4};
	private final int[] OR_POINTS_Y = new int[]{0, 0, 8, 16, 24, 32, 32, 24, 16, 8};
	private final int[] XOR_POINTS_X = new int[]{8, 16, 24, 32, 24, 16, 8, 12, 12, 12};
	private final int[] XOR_POINTS_Y = new int[]{0, 0, 8, 16, 24, 32, 32, 24, 16, 8};

	private final Stroke DOUBLE_STROKE = new BasicStroke(3f);

	AssociativeGateDescriptor(AssociativeGateParameters parameters, String type) {
		super(parameters, type);
		_behavior = parameters.getBehavior();
		_pinPosition = new Point[parameters.getPinCount() + 1];
		_associativeType = parameters.getAssociativeType();

		int dy = 32 / (parameters.getPinCount() - 1);
		for (int i = 0; i < parameters.getPinCount(); i++) {
			_pinPosition[i] = new Point(Circuit.gridTrunc(0), Circuit.gridTrunc(i * dy));
		}

		_pinPosition[parameters.getPinCount()] = new Point(32, 16);
		_gateType = GateTypes.NORMAL;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(ColorScheme.getGates());
		switch (_associativeType) {
			case ASSOCIATIVE_TYPE_AND:
				graphics.fillArc(-32, 0, 64, 32, -90, 180);
				break;
			case ASSOCIATIVE_TYPE_OR:
				graphics.fillPolygon(OR_POINTS_X, OR_POINTS_Y, 10);
				break;
			case ASSOCIATIVE_TYPE_XOR:
				graphics.fillPolygon(XOR_POINTS_X, XOR_POINTS_Y, 10);
				Stroke oldStroke = graphics.getStroke();
				graphics.setStroke(DOUBLE_STROKE);
				graphics.drawLine(4, 2, 8, 8);
				graphics.drawLine(8, 8, 8, 24);
				graphics.drawLine(8, 24, 4, 30);
				graphics.setStroke(oldStroke);
				break;
		}
	}

	@Override
	public Dimension getSize() {
		return new Dimension(32, 32);
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		return null;
	}

	int computeAssociativeVal(int value1, byte value2) {
		return _behavior[value1 + 1][value2 + 1];
	}

	@Override
	public int getPinCount() {
		return ((AssociativeGateParameters) _originalParameters).getPinCount() + 1;
	}

}
