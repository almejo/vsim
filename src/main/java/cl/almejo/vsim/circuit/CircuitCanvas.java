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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class CircuitCanvas extends JPanel implements ComponentListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(CircuitCanvas.class);

	private static final long serialVersionUID = 1L;

	private Circuit _circuit;

	public CircuitCanvas(Circuit circuit) {
		_circuit = circuit;
		_circuit.add(this);
		addComponentListener(this);
		setPreferredSize(new Dimension(800, 1800));
		resizeViewport();
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

	@Override
	public void componentResized(ComponentEvent e) {
		LOGGER.info("componentResized");
		resizeViewport();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		LOGGER.info("componentMoved");
	}

	@Override
	public void componentShown(ComponentEvent e) {
		LOGGER.info("componentShown");
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		LOGGER.info("componentHidden");
	}
}
