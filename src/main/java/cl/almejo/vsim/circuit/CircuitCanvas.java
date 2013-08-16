/**
 *
 * vsim
 *
 * Created on Aug 16, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.circuit;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class CircuitCanvas extends JPanel {

	private static final long serialVersionUID = 1L;

	private final Circuit _circuit;

	public CircuitCanvas(Circuit circuit) {
		_circuit = circuit;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		_circuit.paint((Graphics2D)g);
		repaint();
	}

}
