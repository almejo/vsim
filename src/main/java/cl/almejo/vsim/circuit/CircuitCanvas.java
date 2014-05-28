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

package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gui.ColorScheme;

import javax.swing.*;
import java.awt.*;

public class CircuitCanvas extends JPanel {

	private static final long serialVersionUID = 1L;

	private Circuit _circuit;

	public CircuitCanvas(Circuit circuit) {
		_circuit = circuit;
		_circuit.add(this);
	}

	private Rectangle _viewport = new Rectangle();

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.setColor(ColorScheme.getBackground());
		graphics.fillRect(1, 1, (int) getSize().getWidth() - 2, (int) getSize().getHeight() - 2);
		_circuit.paint((Graphics2D) graphics, _viewport);
	}

	public void resizeViewport() {
		_viewport.setSize(getSize());
	}

	public void setCircuit(Circuit circuit) {
		_circuit = circuit;
		_circuit.add(this);
	}
}
