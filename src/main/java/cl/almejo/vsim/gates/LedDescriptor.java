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
class LedDescriptor extends GateDescriptor {
	LedDescriptor(LedParameters parameters, String type) {
		super(parameters, type);
		pinCount = 1;
		pinPosition = new Point[1];
		Dimension dimension = getSize();
		pinPosition[0] = new Point(Circuit.gridTrunc(dimension.width / 2) + Circuit.GRID_SIZE / 2, Circuit.gridTrunc(dimension.height));
		gateType = GateTypes.DEBUG;
	}

	@SuppressWarnings("SuspiciousNameCombination")
	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		Dimension dimension = getSize();

		if (((Led) iconGate.getGate()).getValue() == Constants.ON) {
			graphics.setColor(ColorScheme.getWireOn());
			graphics.fillOval(0, 0, dimension.width, dimension.width);
		} else {
			graphics.setColor(ColorScheme.getOff());
			graphics.drawOval(0, 0, dimension.width, dimension.width);
			int delta = 4;
			graphics.drawLine(dimension.width / 2, dimension.height / 2 + delta, dimension.width / 3, dimension.height / 5 + delta);
			graphics.drawLine(dimension.width / 2, dimension.height / 2 + delta, dimension.width - dimension.width / 3, dimension.height / 5 + delta);
		}
		graphics.setColor(ColorScheme.getGates());
		graphics.fillRect(dimension.width / 3, dimension.width, Circuit.GRID_SIZE, Circuit.GRID_SIZE);
		LedParameters parameters = (LedParameters) iconGate.getGate().getParamameters();
		if (parameters.getText() != null && !parameters.getText().trim().equalsIgnoreCase("")) {
			graphics.setColor(ColorScheme.getLabel());
			graphics.drawString(parameters.getText(), dimension.width + Circuit.GRID_SIZE, dimension.height / 2);
		}
	}

	@Override
	public Dimension getSize() {
		return new Dimension(Circuit.GRID_SIZE * 2, Circuit.GRID_SIZE * 3);
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		try {
			return new Led(circuit, params.clone(), this);
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
