package cl.almejo.vsim.circuit;

import cl.almejo.vsim.gates.Constants;
import cl.almejo.vsim.gui.ColorScheme;
import cl.almejo.vsim.gui.ViewportListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class CircuitCanvas extends JPanel implements ComponentListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(CircuitCanvas.class);

	private static final long serialVersionUID = 1L;

	private Circuit _circuit;
	private final AffineTransform _translationTransformation = new AffineTransform();
	private final AffineTransform _computedTransformation = new AffineTransform();
	private final AffineTransform _zoomTransformation = new AffineTransform();
	private double _zoom = 1.0;
	private final Rectangle _viewport = new Rectangle();
	private final List<ViewportListener> _viewportListeners = new LinkedList<>();

	public CircuitCanvas(Circuit circuit) {
		_circuit = circuit;
		_circuit.add(this);
		addComponentListener(this);
		setPreferredSize(new Dimension(800, 1800));
		resizeViewport();
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		clean(graphics2D);

		AffineTransform transform = new AffineTransform(_computedTransformation);
		transform.concatenate(graphics2D.getTransform());

		graphics2D.setTransform(transform);
		_circuit.paint(graphics2D, _viewport);
		graphics2D.setTransform(Constants.TRANSFORM_IDENTITY);
	}

	private void clean(Graphics2D graphics2D) {
		graphics2D.setColor(ColorScheme.getBackground());
		graphics2D.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());
	}

	public void resizeViewport() {
		Dimension size = getSize();
		_viewport.setRect(_viewport.getX(), _viewport.getY(), size.getWidth() / _zoom, size.getHeight() / _zoom);
		sendViewportUpdatedEvent();
	}

	public void setCircuit(Circuit circuit) {
		_circuit = circuit;
		_circuit.add(this);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		resizeViewport();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	public void centerViewportTo(int x, int y) {
		moveViewport(Circuit.gridTrunc((int) _viewport.getCenterX() - x), Circuit.gridTrunc((int) _viewport.getCenterY() - y));
	}

	private void translateViewportTo(int x, int y) {
		int dx = x - (int) _viewport.getX();
		int dy = y - (int) _viewport.getY();
		_viewport.translate(dx, dy);
		_translationTransformation.translate(-dx, -dy);
		updateTransformation();
		sendViewportUpdatedEvent();
	}

	public void moveViewport(int deltaX, int deltaY) {
		_viewport.translate(-deltaX, -deltaY);
		_translationTransformation.translate(deltaX, deltaY);
		updateTransformation();
		sendViewportUpdatedEvent();
	}

	private void updateTransformation() {
		_computedTransformation.setToIdentity();
		_computedTransformation.concatenate(_zoomTransformation);
		_computedTransformation.concatenate(_translationTransformation);
	}

	public int toCircuitCoordinatesX(int x) {
		return (int) ((double) x / _zoom + _viewport.getX());
	}

	public int toCircuitCoordinatesY(int y) {
		return (int) ((double) y / _zoom + _viewport.getY());
	}

	public void setZoom(double zoom) {
		double centerX = _viewport.getCenterX();
		double centerY = _viewport.getCenterY();
		_viewport.setFrame(_viewport.getX(), _viewport.getY(), _viewport.getWidth() * _zoom, _viewport.getHeight() * _zoom);
		_viewport.setFrame(_viewport.getX(), _viewport.getY(), _viewport.getWidth() / zoom, _viewport.getHeight() / zoom);
		_zoomTransformation.setToIdentity();
		_zoomTransformation.scale(zoom, zoom);
		int cornerX = (int) (centerX - _viewport.getWidth() / 2);
		int cornerY = (int) (centerY - _viewport.getHeight() / 2);
		translateViewportTo(cornerX, cornerY);
		_zoom = zoom;
	}

	public double getZoom() {
		return _zoom;
	}

	public Rectangle getWorld() {
		Rectangle rectangle = new Rectangle(_viewport);
		if (_circuit.getExtent() != null) {
			rectangle.add(_circuit.getExtent());
		}
		return rectangle;
	}

	public Rectangle getViewport() {
		return new Rectangle(_viewport);
	}

	public void addViewportListener(ViewportListener listener) {
		if (listener != null) {
			_viewportListeners.add(listener);
		}
	}

	private void sendViewportUpdatedEvent() {
		for (ViewportListener listener : _viewportListeners) {
			listener.updated(this);
		}
	}

	public void center() {
		if (_circuit.getExtent() == null) {
			return;
		}
		Rectangle extent = _circuit.getExtent();
		centerViewportTo((int) extent.getCenterX(), (int) extent.getCenterY());
		LOGGER.debug("Viewport centered");
	}
}
