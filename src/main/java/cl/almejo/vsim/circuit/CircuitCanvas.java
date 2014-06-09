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

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gui.ColorScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;

public class CircuitCanvas extends JPanel implements ComponentListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(CircuitCanvas.class);

	private static final long serialVersionUID = 1L;

	private Circuit _circuit;

	private AffineTransform _translation = new AffineTransform();
	private AffineTransform _computedTransformation = new AffineTransform();

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
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setColor(ColorScheme.getBackground());
		graphics2D.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());
		AffineTransform transform = new AffineTransform(_computedTransformation);
		transform.concatenate(graphics2D.getTransform());
		graphics2D.setTransform(transform);
		_circuit.paint(graphics2D, _viewport);
		graphics2D.setTransform(Constants.TRANSFORM_IDENTITY);
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

	public void moveViewport(int deltaX, int deltaY) {
		System.out.println("antes-> " + _viewport);
		_viewport.translate(deltaX, deltaY);
		System.out.println(deltaX + " " + deltaY + " ===> " + _viewport);

		_translation.translate(deltaX, deltaY);
		_computedTransformation.setToIdentity();
		_computedTransformation.concatenate(_translation);
	}
}
