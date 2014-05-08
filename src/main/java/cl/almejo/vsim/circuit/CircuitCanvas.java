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

import javax.swing.*;
import java.awt.*;

public class CircuitCanvas extends JPanel {

	private static final long serialVersionUID = 1L;

	private final Circuit _circuit;

	public CircuitCanvas(Circuit circuit) {
		_circuit = circuit;
		_circuit.add(this);
	}

	private Rectangle _viewport = new Rectangle();

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(1, 1, (int) getSize().getWidth() - 2, (int) getSize().getHeight() - 2);
		_circuit.paint((Graphics2D) g, _viewport);
	}

	public void resizeViewport() {
		_viewport.setSize(getSize());
	}

}
