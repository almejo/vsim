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
import cl.almejo.vsim.gui.ColorScheme;

import java.awt.*;

public class LedDescriptor extends GateDescriptor {
	public LedDescriptor(LedParameters parameters, String type) {
		super(parameters, type);
		_pinCount = 1;
		_pinPosition = new Point[1];
		Dimension dimension = getSize();
		_pinPosition[0] = new Point(Circuit.gridTrunc(dimension.width/2) + Circuit.GRIDSIZE / 2, Circuit.gridTrunc(dimension.height));
		_gateType = GateTypes.DEBUG;
	}

	@SuppressWarnings("SuspiciousNameCombination")
	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(Color.GRAY);
		Dimension dimension = getSize();
		graphics.drawRect(0, 0, dimension.width, dimension.height);
		if (((Led) iconGate.getGate()).getValue() == Constants.ON) {
			graphics.setColor(ColorScheme.getWireOn());
			graphics.fillOval(0, 0, dimension.width, dimension.width);
		} else {
			graphics.setColor(ColorScheme.getOff());
			graphics.drawOval(0, 0, dimension.width, dimension.width);
		}
		graphics.setColor(ColorScheme.getGates());
		graphics.fillRect(dimension.width / 3, dimension.width, Circuit.GRIDSIZE, Circuit.GRIDSIZE);
		LedParameters parameters = (LedParameters) iconGate.getGate().getParamameters();
		if (parameters.getText()!= null && !parameters.getText().trim().equalsIgnoreCase("")) {
			graphics.setColor(ColorScheme.getGates());
			graphics.drawString(parameters.getText(), dimension.width + Circuit.GRIDSIZE, dimension.height / 2);
		}
	}

	@Override
	public Dimension getSize() {
		return new Dimension(Circuit.GRIDSIZE * 2, Circuit.GRIDSIZE * 3);
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
