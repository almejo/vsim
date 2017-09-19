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
public class SevenSegmentsDisplayDescriptor extends GateDescriptor {

	private static final int NUMBER_WIDTH = Circuit.GRID_SIZE * 2;
	private static final int NUMBER_HEIGHT = Circuit.GRID_SIZE * 4;
	private static final int NUMBER_OFFSET_X = 8;
	private static final int NUMBER_OFFSET_Y = Circuit.GRID_SIZE;
	private static final int STROKE_WIDTH = 4;
	private static final Stroke NUMBER_STROKE = new BasicStroke(STROKE_WIDTH);

	SevenSegmentsDisplayDescriptor(SevenSegmentsDisplayParameters parameters, String type) {
		super(parameters, type);
		pinCount = parameters.getPinCount();
		pinPosition = new Point[pinCount];
		Dimension dimension = getSize();
		int delta = (int) dimension.getWidth() / pinCount;
		for (int i = 0; i < pinCount; i++) {
			pinPosition[i] = new Point(Circuit.gridTrunc((int) dimension.getWidth() - i * delta), Circuit.gridTrunc(0));
		}
		gateType = GateTypes.DEBUG;

	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(ColorScheme.getGates());
		Dimension dimension = getSize();
		int unitWidth = dimension.width / (pinCount / 4);
		graphics.fillRoundRect(0, 0, Circuit.gridTrunc(dimension.width), Circuit.gridTrunc(dimension.height), 5, 5);

		int number = ((SevenSegmentsDisplayParameters) iconGate.getGate().getParamameters()).getNumber();
		Stroke oldStroke = graphics.getStroke();
		graphics.setStroke(NUMBER_STROKE);
		for (int i = 0; i < pinCount / 4; i++) {
			graphics.setColor(ColorScheme.getOff());
			int xOffset = dimension.width - unitWidth * (i + 1) + NUMBER_OFFSET_X;
			drawNumber(graphics, 8, xOffset, NUMBER_OFFSET_Y);
			graphics.setColor(ColorScheme.getWireOn());
			drawNumber(graphics, number & 15, xOffset, NUMBER_OFFSET_Y);
			number >>= 4;
		}
		graphics.setStroke(oldStroke);
	}

	@Override
	public Dimension getSize() {
		return new Dimension((NUMBER_WIDTH + NUMBER_OFFSET_X * 2) * (pinCount / 4), 48);
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		return null;
	}

	private void drawNumber(Graphics2D graphics, int number, int dx, int dy) {
		switch (number) {
			case 0:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 1:
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 2:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2, dx, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 3:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 4:
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 5:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2, dx, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 6:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 7:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 8:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				break;
			case 9:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 10:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 11:
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 12:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 13:
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 14:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				break;
			case 15:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT / 2, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT / 2);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				break;
		}
	}
}
