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

	public TemplateDescriptor(GateParameters parameters, String type, Dimension size) {
		super(parameters, type);
		_size = new Dimension(size);
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		Dimension dimension = getSize();
		graphics.fillRect(0,0,dimension.width, dimension.height);
	}

	@Override
	public Dimension getSize() {
		return _size;
	}

	@Override
	public Gate make(Circuit circuit, GateParameters params) {
			return null;
	}
}
