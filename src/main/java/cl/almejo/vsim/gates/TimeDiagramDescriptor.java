/**
 * vsim
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: alejo
 */
package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;
import cl.almejo.vsim.gui.ColorScheme;

import java.awt.*;

public class TimeDiagramDescriptor extends GateDescriptor {

	public static final int PIN_SEPARATION = 16;

	public TimeDiagramDescriptor(TimeDiagramParameters parameters, String type) {
		super(parameters, type);
		_pinCount = 4;
		_pinPosition = new Point[_pinCount];
		for (int i = 0; i < _pinCount; i++) {
			_pinPosition[i] = new Point(0, i * PIN_SEPARATION);
		}
		_gateType = GateTypes.NORMAL;
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(ColorScheme.getGates());
		Dimension dimension = getSize();
		graphics.fillRect(0, 0, dimension.width, dimension.height);
		graphics.setColor(ColorScheme.getLabel());
		graphics.drawString("#" + iconGate.getId(), (int) dimension.getWidth() + 10, (int) dimension.getHeight() / 2);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(32, PIN_SEPARATION * (_pinCount - 1));
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		return null;
	}
}
