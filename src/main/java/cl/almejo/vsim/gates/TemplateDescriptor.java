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

public class TemplateDescriptor extends GateDescriptor {
	private final Dimension _size;

	public TemplateDescriptor(GateParameters parameters, String type, Dimension size, Point[] points) {
		super(parameters, type);
		_size = new Dimension(size);
		_pinCount = points.length;
		_pinPosition = points;
		_gateType = GateTypes.TEMPLATE;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(ColorScheme.getGates());
		Dimension dimension = getSize();
		graphics.fillRect(0, 0, dimension.width, dimension.height);
		graphics.setColor(ColorScheme.getLabel());
		int i = 0;
		for (Point point : _pinPosition) {
			int xposition = point.getX() == 0 ? point.getX() + 3 : point.getX() - 30;
			graphics.drawString("#" + i, xposition, point.getY());
			i++;
		}
	}

	@Override
	public Dimension getSize() {
		return _size;
	}

	@Override
	public Gate make(Circuit circuit, GateParameters parameters) {
		return null;
	}
}
