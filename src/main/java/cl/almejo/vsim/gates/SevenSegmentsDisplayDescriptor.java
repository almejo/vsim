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

public class SevenSegmentsDisplayDescriptor extends GateDescriptor {

	private static final int NUMBER_WIDTH = Circuit.GRIDSIZE * 3;
	private static final int NUMBER_HEIGHT = Circuit.GRIDSIZE * 4;
	private static final int NUMBER_OFFSET_X = Circuit.GRIDSIZE;
	private static final int NUMBER_OFFSET_Y = Circuit.GRIDSIZE;

	public SevenSegmentsDisplayDescriptor(SevenSegmentsDisplayParameters parameters, String type) {
		super(parameters, type);
		_pinCount = parameters.getPinCount();
		_pinPosition = new Point[_pinCount];
		int delta = 64 / _pinCount;
		for (int i = 0; i < _pinCount; i++) {
			_pinPosition[i] = new Point(Circuit.gridTrunc(64 - i * delta), Circuit.gridTrunc(0));
		}
		_gateType = GateTypes.DEBUG;

	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(Color.blue);
		Dimension dimension = getSize();
		graphics.fillRect(0, 0, Circuit.gridTrunc(dimension.width), Circuit.gridTrunc(dimension.height));

		int number = ((SevenSegmentsDisplayParameters) iconGate.getGate().getParamameters()).getNumber();
		for(int i = 0 ; i < (_pinCount / 4); i++) {
			drawNumber(graphics, number & 15, dimension.width - NUMBER_WIDTH - NUMBER_OFFSET_X - (NUMBER_OFFSET_X + NUMBER_WIDTH) * i , NUMBER_OFFSET_Y);
			number >>= 4;
		}
	}

	@Override
	public Dimension getSize() {
		return new Dimension(32 * (_pinCount / 4), 48);
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		return null;
	}

	public void drawNumber(Graphics2D graphics, int number, int dx, int dy) {
		graphics.setColor(Color.red);

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
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2), dx, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 3:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 4:
				graphics.drawLine(dx, dy, dx, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 5:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2), dx, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 6:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
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
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				break;
			case 9:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 10:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 11:
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 12:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 13:
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx + NUMBER_WIDTH, dy, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				break;
			case 14:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx, dy + NUMBER_HEIGHT, dx + NUMBER_WIDTH, dy + NUMBER_HEIGHT);
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				break;
			case 15:
				graphics.drawLine(dx, dy, dx + NUMBER_WIDTH, dy);
				graphics.drawLine(dx, dy + (NUMBER_HEIGHT / 2), dx + NUMBER_WIDTH, dy + (NUMBER_HEIGHT / 2));
				graphics.drawLine(dx, dy, dx, dy + NUMBER_HEIGHT);
				break;
		}
	}
}
