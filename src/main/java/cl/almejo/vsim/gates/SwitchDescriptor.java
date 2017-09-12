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
class SwitchDescriptor extends GateDescriptor {
	SwitchDescriptor(SwitchParameters parameters, String type) {
		super(parameters, type);
		_pinCount = 1;
		_pinPosition = new Point[_pinCount];
		Dimension dimension = getSize();
		_pinPosition[0] = new Point(Circuit.gridTrunc(dimension.width), Circuit.gridTrunc(dimension.height / 2));
		_gateType = GateTypes.DEBUG;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(ColorScheme.getGates());
		Dimension dimension = getSize();
		graphics.fillRect(x, y, dimension.width, dimension.height);
		drawLetter(graphics, Constants.THREE_STATE, iconGate, 1, 12, "T");
		drawLetter(graphics, Constants.ON, iconGate, 11, 12, "1");
		drawLetter(graphics, Constants.OFF, iconGate, 21, 12, "0");
		SwitchParameters parameters = (SwitchParameters) iconGate.getGate().getParamameters();
		if (parameters.getText() != null && !parameters.getText().trim().equalsIgnoreCase("")) {
			graphics.setColor(ColorScheme.getLabel());
			graphics.drawString(parameters.getText(), 0, -dimension.height / 2);
		}
	}

	private void drawLetter(Graphics2D graphics, byte value, IconGate iconGate, int offset, int height, String letter) {
		if (((SwitchParameters) iconGate.getGate().getParamameters()).getValue() == value) {
			graphics.setColor(ColorScheme.getWireOn());
		} else {
			graphics.setColor(ColorScheme.getOff());
		}
		graphics.drawString(letter, offset, height);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(32, 16);
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		return null;
	}

}
