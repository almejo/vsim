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

	private static final int NUMBER_WIDTH = 20;
	private static final int NUMBER_HEIGHT = 30;

	public SevenSegmentsDisplayDescriptor(SevenSegmentsDisplayParameters parameters, String type) {
		super(parameters, type);
		_pinPosition = new Point[4];
		_pinPosition[0] = new Point(Circuit.gridTrunc(0), Circuit.gridTrunc(0));
		_pinPosition[1] = new Point(Circuit.gridTrunc(0), Circuit.gridTrunc(16));
		_pinPosition[2] = new Point(Circuit.gridTrunc(0), Circuit.gridTrunc(32));
		_pinPosition[3] = new Point(Circuit.gridTrunc(0), Circuit.gridTrunc(48));
		_gateType = GateTypes.DEBUG;
		_pinCount = 4;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(Color.blue);
		graphics.fillRect(0, 0, Circuit.gridTrunc(32), Circuit.gridTrunc(48));
		drawNumber(graphics, ((SevenSegmentsDisplayParameters) iconGate.getGate().getParamameters()).getNumber(), 6, 6);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(32, 48);
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
