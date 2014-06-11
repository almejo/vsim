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
	private AffineTransform _translationTransformation = new AffineTransform();
	private AffineTransform _computedTransformation = new AffineTransform();
	private AffineTransform _zoomTransformation = new AffineTransform();
	private double _zoom;


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

		Rectangle viewport = new Rectangle(_viewport);
		viewport.setFrame(_viewport.getX(), _viewport.getY(), 1.0/_zoom*_viewport.getWidth(), 1.0/_zoom*_viewport.getHeight());
		_circuit.paint(graphics2D, viewport);

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
		deltaX /= _zoom;
		deltaY /= _zoom;
		_viewport.translate(-deltaX, -deltaY);
		_translationTransformation.translate(deltaX, deltaY);
		updateTransformation();
	}

	private void updateTransformation() {
		_computedTransformation.setToIdentity();
		_computedTransformation.concatenate(_zoomTransformation);
		_computedTransformation.concatenate(_translationTransformation);
	}

	public int toCircuitCoordinatesX(int x) {
		return (int) ((((double) x) / _zoom)  + _viewport.getX());
	}

	public int toCircuitCoordinatesY(int y) {
		return (int) ((((double) y) / _zoom)  + _viewport.getY());
	}

	public void setZoom(double zoom) {
		_zoom = zoom;
		_zoomTransformation.setToIdentity();
		_zoomTransformation.scale(zoom, zoom);
		updateTransformation();
	}

}
